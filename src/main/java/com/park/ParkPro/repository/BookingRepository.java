package com.park.ParkPro.repository;

import com.park.ParkPro.model.Booking;
import com.park.ParkPro.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findAllByParkId(UUID parkId);
    List<Booking> findAllByVisitorId(UUID visitorId);
    List<Booking> findAllByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.park.id = :parkId AND b.visitDate = :date")
    List<Booking> findByParkAndDate(
            @Param("parkId") UUID parkId,
            @Param("date") LocalDate date);

    @Query("SELECT b.visitDate, COUNT(b), SUM(b.amount) FROM Booking b "+
        "WHERE b.park.id = :parkId AND b.status = 'CONFIRMED' " +
            "GROUP BY b.visitDate " +
            "ORDER BY b.visitDate")
    List<Object[]> getDailyBookingStats(@Param("parkId") UUID parkId);

    @Query("SELECT b.activity, COUNT(b), SUM(b.amount) FROM Booking b "+
        "WHERE b.park.id = :parkId AND b.status = 'CONFIRMED' "+
            "GROUP BY b.activity")
    List<Object[]> getActivityStats(@Param("parkId") UUID parkId);
}
