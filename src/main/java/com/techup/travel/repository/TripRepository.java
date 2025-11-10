package com.techup.travel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techup.travel.entity.Trips;

public interface TripRepository extends JpaRepository<Trips, Long> {
    List<Trips> findByAuthorId(Long authorId);
    List<Trips> findByTagsContaining(String tag);
}