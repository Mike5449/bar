package com.mycompany.mikedev.security.jwt;

import com.mycompany.mikedev.domain.Attendance;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.enumeration.Sexe;
import com.mycompany.mikedev.management.SecurityMetersService;
import com.mycompany.mikedev.repository.AttendanceRepository;
import com.mycompany.mikedev.repository.EmployeeRepository;
import com.mycompany.mikedev.service.TokenManager;
import com.mycompany.mikedev.service.dto.TokenPayloadDTO;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import tech.jhipster.config.JHipsterProperties;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    private final SecurityMetersService securityMetersService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public TokenProvider(JHipsterProperties jHipsterProperties, SecurityMetersService securityMetersService) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (!ObjectUtils.isEmpty(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn(
                "Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();

        this.securityMetersService = securityMetersService;
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        Map<String, Object> mapToken = new HashMap<>();
        TokenPayloadDTO tokenPayloadDTO = new TokenPayloadDTO();
        Optional<Employee> employee = employeeRepository.findByUser_Login(authentication.getName());
        // Optional<Attendance> attendance=attendanceRepository.findById(employee.get().getId());

        if(employee.isPresent()){
            tokenPayloadDTO.setLogin(authentication.getName());
            tokenPayloadDTO.setEmployeeId(employee.get().getId());
            tokenPayloadDTO.setFirstName(employee.get().getFirstName());
            tokenPayloadDTO.setLastName(employee.get().getLastName());
            tokenPayloadDTO.setGender(employee.get().getGender());
            // tokenPayloadDTO.setJobId(employee.get().getJob().getId());
            // tokenPayloadDTO.setJobName(employee.get().getJob().getJobName());
            // tokenPayloadDTO.setIdAttendance(attendance.get().getEmployee().getId());

            mapToken.put("login", tokenPayloadDTO.getLogin());
            mapToken.put("employeeId",tokenPayloadDTO.getEmployeeId());
            mapToken.put("firsName",tokenPayloadDTO.getFirstName());
            mapToken.put("lastName",tokenPayloadDTO.getLastName());
            mapToken.put("gender",tokenPayloadDTO.getGender());
            // mapToken.put("jobId", tokenPayloadDTO.getJobId());
            // mapToken.put("jobName",tokenPayloadDTO.getJobName());
            // mapToken.put("attendanceId", tokenPayloadDTO.getIdAttendance());

            
            


        }
        TokenManager.setSubject(tokenPayloadDTO);

        return Jwts
            .builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    private void setTokenPayload(String authToken, Claims claims) {

        TokenManager.setSubject(null);
        TokenPayloadDTO tokenPayloadDTO = new TokenPayloadDTO();

        try {

            tokenPayloadDTO.setLogin(claims.get("sub") != null ? claims.get("sub").toString() : "");
            tokenPayloadDTO.setEmployeeId(Long.parseLong(claims.get("employeeId").toString()));
            tokenPayloadDTO.setFirstName(claims.containsKey("firstName") ? claims.get("firstName").toString() : "");
            tokenPayloadDTO.setLastName(claims.containsKey("lastName") ? claims.get("lastName").toString() : "");
            tokenPayloadDTO.setGender(claims.containsKey("gender") ? Sexe.valueOf(claims.get("gender").toString())
            : Sexe.MASCULIN);
            tokenPayloadDTO.setJobId(Long.parseLong(claims.get("jobId").toString()));
            tokenPayloadDTO.setJobName(claims.containsKey("jobName") ? claims.get("jobName").toString() : "");
            // tokenPayloadDTO.setIdAttendance(Long.parseLong(claims.get("attendanceId").toString()));
            
        } catch (Exception e) {
            // TODO: handle exception
        }

        TokenManager.setSubject(tokenPayloadDTO);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            try {
                Claims claims = jwtParser.parseClaimsJws(authToken).getBody();
                setTokenPayload(authToken, claims);
            } catch (Exception ex) {}
            return true;
        } catch (ExpiredJwtException e) {
            this.securityMetersService.trackTokenExpired();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            this.securityMetersService.trackTokenUnsupported();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (MalformedJwtException e) {
            this.securityMetersService.trackTokenMalformed();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (SignatureException e) {
            this.securityMetersService.trackTokenInvalidSignature();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) { // TODO: should we let it bubble (no catch), to avoid defensive programming and follow the fail-fast principle?
            log.error("Token validation error {}", e.getMessage());
        }

        return false;
    }
}
