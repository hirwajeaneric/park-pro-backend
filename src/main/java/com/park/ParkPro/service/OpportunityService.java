package com.park.ParkPro.service;

import com.park.ParkPro.dto.OpportunityApplicationDto;
import com.park.ParkPro.dto.OpportunityApplicationResponseDto;
import com.park.ParkPro.dto.OpportunityDto;
import com.park.ParkPro.dto.OpportunityResponseDto;
import com.park.ParkPro.exception.BusinessRuleException;
import com.park.ParkPro.exception.ResourceNotFoundException;
import com.park.ParkPro.mapper.OpportunityApplicationMapper;
import com.park.ParkPro.mapper.OpportunityMapper;
import com.park.ParkPro.model.*;
import com.park.ParkPro.repository.OpportunityApplicationRepository;
import com.park.ParkPro.repository.OpportunityRepository;
import com.park.ParkPro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final OpportunityApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final OpportunityMapper opportunityMapper;
    private final OpportunityApplicationMapper applicationMapper;

    @Transactional
    public OpportunityResponseDto createOpportunity(OpportunityDto opportunityDto, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + creatorId));

        Opportunity opportunity = opportunityMapper.toEntity(opportunityDto);
        opportunity.setCreatedBy(creator);
        opportunity.setStatus(OpportunityStatus.OPEN);

        Opportunity savedOpportunity = opportunityRepository.save(opportunity);
        return opportunityMapper.toDto(savedOpportunity);
    }

    @Transactional(readOnly = true)
    public OpportunityResponseDto getOpportunityById(UUID id) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Opportunity not found with ID: " + id));
        return opportunityMapper.toDto(opportunity);
    }

    @Transactional(readOnly = true)
    public List<OpportunityResponseDto> getAllOpportunities() {
        return opportunityRepository.findAll().stream()
                .map(opportunityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OpportunityResponseDto> getOpenPublicOpportunities() {
        return opportunityRepository.findOpenPublicOpportunities().stream()
                .map(opportunityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OpportunityResponseDto closeOpportunity(UUID opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Opportunity not found with ID: " + opportunityId));

        opportunity.setStatus(OpportunityStatus.CLOSED);
        Opportunity closedOpportunity = opportunityRepository.save(opportunity);
        return opportunityMapper.toDto(closedOpportunity);
    }

    @Transactional
    public OpportunityApplicationResponseDto applyForOpportunity(
            UUID opportunityId,
            OpportunityApplicationDto applicationDto) {

        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Opportunity not found with ID: " + opportunityId));

        if (opportunity.getStatus() != OpportunityStatus.OPEN) {
            throw new BusinessRuleException("This opportunity is not currently open for applications");
        }

        if (applicationRepository.existsByOpportunityIdAndEmail(opportunityId, applicationDto.getEmail())) {
            throw new BusinessRuleException("You have already applied for this opportunity");
        }

        OpportunityApplication application = applicationMapper.toEntity(applicationDto);
        application.setOpportunity(opportunity);
        application.setStatus(ApplicationStatus.SUBMITTED);

        OpportunityApplication savedApplication = applicationRepository.save(application);
        return applicationMapper.toDto(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<OpportunityApplicationResponseDto> getApplicationsForOpportunity(UUID opportunityId) {
        return applicationRepository.findAllByOpportunityId(opportunityId).stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OpportunityApplicationResponseDto updateApplicationStatus(
            UUID applicationId,
            ApplicationStatus status) {

        OpportunityApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with ID: " + applicationId));

        application.setStatus(status);
        OpportunityApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toDto(updatedApplication);
    }

    @Transactional(readOnly = true)
    public List<OpportunityResponseDto> searchOpportunities(String query) {
        return opportunityRepository.searchOpportunities(query).stream()
                .map(opportunityMapper::toDto)
                .collect(Collectors.toList());
    }
}
