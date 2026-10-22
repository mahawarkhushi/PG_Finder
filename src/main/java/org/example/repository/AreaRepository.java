package org.example.repository;

import org.example.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    // 1. Used by the Search Bar autocomplete (matches letters typed by the user)
    List<Area> findByNameContainingIgnoreCase(String query);

    // 2. Used when someone opens a clean URL like /areas/hsr-layout
    Optional<Area> findBySlug(String slug);
}