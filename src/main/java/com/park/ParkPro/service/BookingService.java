package com.park.ParkPro.service;

import com.park.ParkPro.dto.BookingDto;
import com.park.ParkPro.dto.BookingResponseDto;
import com.park.ParkPro.exception.ResourceNotFoundException;
import com.park.ParkPro.mapper.BookingMapper;
import com.park.ParkPro.model.Booking;
import com.park.ParkPro.model.BookingStatus;
import com.park.ParkPro.model.Park;
import com.park.ParkPro.model.User;
import com.park.ParkPro.repository.BookingRepository;
import com.park.ParkPro.repository.ParkRepository;
import com.park.ParkPro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkRepository parkRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDto createBooking(BookingDto bookingDto) {
        User visitor = userRepository.findById(bookingDto.getVisitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found with ID: " + bookingDto.getVisitorId()));

        Park park = parkRepository.findById(bookingDto.getParkId())
                .orElseThrow(() -> new ResourceNotFoundException("Park not found with ID: " + bookingDto.getParkId()));

        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setVisitor(visitor);
        booking.setPark(park);
        booking.setStatus(BookingStatus.PENDING);

        // In a real application, this would call a payment gateway.
        booking.setPaymentReference("PAY-"+ UUID.randomUUID().toString());

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Transactional
    public BookingResponseDto confirmBooking(UUID bookingId, String paymentReference) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with ID: " + bookingId));

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentReference(paymentReference);

        Booking confirmedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(confirmedBooking);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with ID: " + id));
        return bookingMapper.toDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByPark(UUID parkId) {
        return bookingRepository.findAllByParkId(parkId).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByVisitor(UUID visitorId) {
        return bookingRepository.findAllByVisitorId(visitorId).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByParkAndDate(UUID parkId, LocalDate date) {
        return bookingRepository.findByParkAndDate(parkId, date).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueByPark(UUID parkId) {
        return bookingRepository.findAllByParkId(parkId).stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyBookingStats(UUID parkId) {
        return bookingRepository.getDailyBookingStats(parkId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getActivityStats(UUID parkId) {
        return bookingRepository.getActivityStats(parkId);
    }
}
