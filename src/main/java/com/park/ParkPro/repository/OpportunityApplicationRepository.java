package com.park.ParkPro.repository;

import com.park.ParkPro.model.ApplicationStatus;
import com.park.ParkPro.model.OpportunityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpportunityApplicationRepository extends JpaRepository<OpportunityApplication, UUID> {

    List<OpportunityApplication> findAllByOpportunityId(UUID opportunityId);

    List<OpportunityApplication> findAllByStatus(ApplicationStatus status);

    boolean existsByOpportunityIdAndEmail(UUID opportunityId, String email);
}
