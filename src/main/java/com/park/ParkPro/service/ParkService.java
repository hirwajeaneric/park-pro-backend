package com.park.ParkPro.service;

import com.park.ParkPro.exception.ResourceNotFoundException;
import com.park.ParkPro.model.Park;
import com.park.ParkPro.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkService {
    private final ParkRepository parkRepository;

    @Transactional
    public Park createPark(Park park) {
        return parkRepository.save(park);
    }

    @Transactional(readOnly = true)
    public List<Park> getAllParks() {
        return parkRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Park getParkById(UUID id) {
        return parkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Park not found width ID: "+id));
    }

    @Transactional
    public Park updatePark(UUID id, Park park) {
        Park existingPark = getParkById(id);
        existingPark.setName(park.getName());
        existingPark.setDescription(park.getDescription());
        existingPark.setLocation(park.getLocation());

        return parkRepository.save(existingPark);
    }
}
