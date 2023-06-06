package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.LeaveRequest;
import com.mycompany.mikedev.repository.LeaveRequestRepository;
import com.mycompany.mikedev.service.LeaveRequestService;
import com.mycompany.mikedev.service.dto.LeaveRequestDTO;
import com.mycompany.mikedev.service.mapper.LeaveRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaveRequest}.
 */
@Service
@Transactional
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    private final LeaveRequestRepository leaveRequestRepository;

    private final LeaveRequestMapper leaveRequestMapper;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, LeaveRequestMapper leaveRequestMapper) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRequestMapper = leaveRequestMapper;
    }

    @Override
    public LeaveRequestDTO save(LeaveRequestDTO leaveRequestDTO) {
        log.debug("Request to save LeaveRequest : {}", leaveRequestDTO);
        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(leaveRequestDTO);
        leaveRequest = leaveRequestRepository.save(leaveRequest);
        return leaveRequestMapper.toDto(leaveRequest);
    }

    @Override
    public LeaveRequestDTO update(LeaveRequestDTO leaveRequestDTO) {
        log.debug("Request to update LeaveRequest : {}", leaveRequestDTO);
        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(leaveRequestDTO);
        leaveRequest = leaveRequestRepository.save(leaveRequest);
        return leaveRequestMapper.toDto(leaveRequest);
    }

    @Override
    public Optional<LeaveRequestDTO> partialUpdate(LeaveRequestDTO leaveRequestDTO) {
        log.debug("Request to partially update LeaveRequest : {}", leaveRequestDTO);

        return leaveRequestRepository
            .findById(leaveRequestDTO.getId())
            .map(existingLeaveRequest -> {
                leaveRequestMapper.partialUpdate(existingLeaveRequest, leaveRequestDTO);

                return existingLeaveRequest;
            })
            .map(leaveRequestRepository::save)
            .map(leaveRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveRequests");
        return leaveRequestRepository.findAll(pageable).map(leaveRequestMapper::toDto);
    }

    public Page<LeaveRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return leaveRequestRepository.findAllWithEagerRelationships(pageable).map(leaveRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveRequestDTO> findOne(Long id) {
        log.debug("Request to get LeaveRequest : {}", id);
        return leaveRequestRepository.findOneWithEagerRelationships(id).map(leaveRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaveRequest : {}", id);
        leaveRequestRepository.deleteById(id);
    }
}
