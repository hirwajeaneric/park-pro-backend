package com.park.ParkPro.service;

import com.park.ParkPro.dto.DonationDto;
import com.park.ParkPro.dto.DonationResponseDto;
import com.park.ParkPro.exception.ResourceNotFoundException;
import com.park.ParkPro.mapper.DonationMapper;
import com.park.ParkPro.model.Donation;
import com.park.ParkPro.model.DonationStatus;
import com.park.ParkPro.model.Park;
import com.park.ParkPro.repository.DonationRepository;
import com.park.ParkPro.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final ParkRepository parkRepository;
    private final DonationMapper donationMapper;

    @Transactional
    public DonationResponseDto createDonation(DonationDto donationDto) {
        Park park = parkRepository.findById(donationDto.getParkId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Park not found with ID: " + donationDto.getParkId()));

        Donation donation = donationMapper.toEntity(donationDto);
        donation.setPark(park);
        donation.setStatus(DonationStatus.PENDING);

        // In a real application, this would call a payment gateway
        donation.setPaymentReference("PAY-" + UUID.randomUUID().toString());

        Donation savedDonation = donationRepository.save(donation);
        return donationMapper.toDto(savedDonation);
    }

    @Transactional
    public DonationResponseDto completeDonation(UUID donationId, String paymentReference) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donation not found with ID: " + donationId));

        donation.setStatus(DonationStatus.COMPLETED);
        donation.setPaymentReference(paymentReference);

        Donation completedDonation = donationRepository.save(donation);
        return donationMapper.toDto(completedDonation);
    }

    @Transactional(readOnly = true)
    public DonationResponseDto getDonationById(UUID id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donation not found with ID: " + id));
        return donationMapper.toDto(donation);
    }

    @Transactional(readOnly = true)
    public List<DonationResponseDto> getDonationsByPark(UUID parkId) {
        return donationRepository.findAllByParkId(parkId).stream()
                .map(donationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonationResponseDto> getCompletedDonationsByPark(UUID parkId) {
        return donationRepository.findCompletedDonationsByPark(parkId).stream()
                .map(donationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalDonationsByPark(UUID parkId) {
        return donationRepository.findCompletedDonationsByPark(parkId).stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyDonationTrends(UUID parkId) {
        return donationRepository.getMonthlyDonationTrends(parkId);
    }
}
