package com.park.ParkPro.repository;

import com.park.ParkPro.model.Donation;
import com.park.ParkPro.model.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {
    List<Donation> findAllByParkId(UUID parkId);
    List<Donation> findAllByStatus(DonationStatus status);

    @Query("SELECT d FROM Donation d WHERE d.park.id = :parkId AND d.status = 'COMPLETED' ")
    List<Donation> findCompletedDonationsByPark(@Param("parkId") UUID parkId);

    @Query("SELECT d.park.id, SUM(d.amount) FROM Donation d "+
        "WHERE d.status = 'COMPLETED' "+
        "GROUP BY d.park.id")
    List<Object[]> findTotalDonationsPerPark();

    @Query("SELECT EXTRACT(YEAR FROM d.createdAt) AS year, " +
            "EXTRACT(MONTH FROM d.createdAt) AS month, " +
            "SUM(d.amount) AS total " +
            "FROM Donation d " +
            "WHERE d.park.id = :parkId AND d.status = 'COMPLETED' " +
            "GROUP BY year, month ")
    List<Object[]> getMonthlyDonationTrends(@Param("parkId") UUID parkId);
}
