package com.techup.travel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.techup.travel.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByAuthorId(Long authorId);

    @Query(
        value = """
            SELECT *
            FROM trips
            WHERE jsonb_exists(tags, :tag)
            ORDER BY created_at DESC
            """,
        nativeQuery = true
    )
    List<Trip> findByTagNative(@Param("tag") String tag);

    @Query(
        value = """
            SELECT *
            FROM trips
            WHERE lower(title) LIKE lower(concat('%', :keyword, '%'))
            OR lower(description) LIKE lower(concat('%', :keyword, '%'))
            OR lower(coalesce(tags::text, '')) LIKE lower(concat('%', :keyword, '%'))
            ORDER BY created_at DESC
            """,
        nativeQuery = true
    )
    List<Trip> searchNative(@Param("keyword") String keyword);
}