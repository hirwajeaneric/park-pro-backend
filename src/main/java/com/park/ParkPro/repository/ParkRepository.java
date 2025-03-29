package com.park.ParkPro.repository;

import com.park.ParkPro.model.Park;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParkRepository extends JpaRepository<Park, UUID> {

}
