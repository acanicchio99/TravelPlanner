package com.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.academy.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    @Query("SELECT c FROM City c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<City> findByNomeContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT c FROM City c JOIN c.tags t WHERE t.tagId = :tagId")
    List<City> findByTagId(@Param("tagId") Long tagId);
}
