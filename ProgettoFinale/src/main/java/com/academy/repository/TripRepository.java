package com.academy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.academy.entity.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUserUsername(String username);

    @Query("SELECT t FROM Trip t LEFT JOIN FETCH t.tripStops WHERE t.tripId = :tripId")
    Optional<Trip> findByIdWithStops(Long tripId);

    @Query("SELECT t FROM Trip t WHERE t.user.username = :username AND t.tripId = :tripId")
    Optional<Trip> findByUserUsernameAndTripId(String username, Long tripId);
}
