package com.park.ParkPro.repository;

import com.park.ParkPro.model.Role;
import com.park.ParkPro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findAllByParkId(UUID parkId);
    List<User> findAllByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.park.id = :parkId AND u.role = :role")
    List<User> findUsersByParkAndRole(@Param("parkId") UUID parkId,
                                      @Param("role") Role role);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(concat('%', :name, '%')) "+"OR LOWER(u.lastName) LIKE LOWER(concat('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);
}
