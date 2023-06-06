package com.mycompany.mikedev.service;

import com.mycompany.mikedev.service.dto.TokenPayloadDTO;

// package com.seventh.borlette.service;

// import com.seventh.borlette.service.dto.TokenPayloadDTO;

public class TokenManager {

    private static final ThreadLocal<TokenPayloadDTO> SUBJECT = new ThreadLocal<TokenPayloadDTO>() {
        protected TokenPayloadDTO initialValue() {
            return null;
        }
    };

    public TokenManager() {}

    public static void setSubject(TokenPayloadDTO subject) {
        SUBJECT.set(subject);
    }

    public static TokenPayloadDTO getSubject() {
        return SUBJECT.get();
    }

    public static void removeSubject() {
        SUBJECT.remove();
    }
}

