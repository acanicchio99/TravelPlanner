package com.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academy.entity.TripStop;

@Repository
public interface TripStopRepository extends JpaRepository<TripStop, Long> {

    List<TripStop> findByTripTripIdOrderByOrderInTripAsc(Long tripId);

    void deleteByTripTripId(Long tripId);
}
