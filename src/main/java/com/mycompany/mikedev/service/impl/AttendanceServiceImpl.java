package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.Attendance;
import com.mycompany.mikedev.repository.AttendanceRepository;
import com.mycompany.mikedev.service.AttendanceService;
import com.mycompany.mikedev.service.dto.AttendanceDTO;
import com.mycompany.mikedev.service.mapper.AttendanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Attendance}.
 */
@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;

    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public AttendanceDTO save(AttendanceDTO attendanceDTO) {
        log.debug("Request to save Attendance : {}", attendanceDTO);
        Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(attendance);
    }

    @Override
    public AttendanceDTO update(AttendanceDTO attendanceDTO) {
        log.debug("Request to update Attendance : {}", attendanceDTO);
        Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(attendance);
    }

    @Override
    public Optional<AttendanceDTO> partialUpdate(AttendanceDTO attendanceDTO) {
        log.debug("Request to partially update Attendance : {}", attendanceDTO);

        return attendanceRepository
            .findById(attendanceDTO.getId())
            .map(existingAttendance -> {
                attendanceMapper.partialUpdate(existingAttendance, attendanceDTO);

                return existingAttendance;
            })
            .map(attendanceRepository::save)
            .map(attendanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attendances");
        return attendanceRepository.findAll(pageable).map(attendanceMapper::toDto);
    }

    public Page<AttendanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return attendanceRepository.findAllWithEagerRelationships(pageable).map(attendanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceDTO> findOne(Long id) {
        log.debug("Request to get Attendance : {}", id);
        return attendanceRepository.findOneWithEagerRelationships(id).map(attendanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attendance : {}", id);
        attendanceRepository.deleteById(id);
    }
}
