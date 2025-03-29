package com.park.ParkPro.repository;

import com.park.ParkPro.model.Opportunity;
import com.park.ParkPro.model.OpportunityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OpportunityRepository extends JpaRepository<Opportunity, UUID> {

    List<Opportunity> findAllByStatus(OpportunityStatus status);

    List<Opportunity> findAllByVisibility(String visibility);

    @Query("SELECT o FROM Opportunity o WHERE o.status = 'OPEN' AND o.visibility = 'PUBLIC'")
    List<Opportunity> findOpenPublicOpportunities();

    @Query("SELECT o FROM Opportunity o WHERE LOWER(o.title) LIKE LOWER(concat('%', :query, '%')) " +
            "OR LOWER(o.description) LIKE LOWER(concat('%', :query, '%'))")
    List<Opportunity> searchOpportunities(@Param("query") String query);
}
