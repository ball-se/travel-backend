package com.techup.travel.service;

import com.techup.travel.dto.TripResponse;
import com.techup.travel.entity.Trip;
import com.techup.travel.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.techup.travel.dto.TripRequest;
import com.techup.travel.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.ArrayList;

/**
 * Service สำหรับอ่านข้อมูลทริปเท่านั้น
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TripService {

    private final TripRepository tripRepository;
    private final AuthService authService;

    /** READ ALL: Entity -> Response DTO (list) */
    public List<TripResponse> getAll() {
        return tripRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /** READ ONE: not found -> 404 */
    public TripResponse getById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        return toResponse(trip);
    }

    public List<TripResponse> search(String keyword) {
        return tripRepository.searchNative(keyword.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TripResponse> getMyTrips() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = authService.findByEmailOrThrow(email);
        
        return tripRepository.findByAuthorId(currentUser.getId())
                .stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt())) // เรียงจากใหม่ไปเก่า
                .map(this::toResponse)
                .toList();
    }

    public TripResponse update(Long id, TripRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = authService.findByEmailOrThrow(email);
        
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        
        // ตรวจสอบว่าเป็นเจ้าของ trip หรือไม่
        if (!trip.getAuthor().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own trips");
        }
        
        // อัปเดตข้อมูล
        trip.setTitle(request.getTitle());
        trip.setDescription(request.getDescription());
        trip.setPhotos(request.getPhotos());
        trip.setTags(request.getTags());
        trip.setLatitude(request.getLatitude());
        trip.setLongitude(request.getLongitude());
        
        Trip saved = tripRepository.save(trip);
        return toResponse(saved);
    }
    
    public void delete(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = authService.findByEmailOrThrow(email);
        
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        
        // ตรวจสอบว่าเป็นเจ้าของ trip หรือไม่
        if (!trip.getAuthor().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own trips");
        }
        
        tripRepository.delete(trip);
    }

    public TripResponse create(TripRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = authService.findByEmailOrThrow(email); 
    
        Trip trip = Trip.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .photos(request.getPhotos() != null ? request.getPhotos() : new ArrayList<>())
                .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .author(author)
                .build();
    
        Trip saved = tripRepository.save(trip);
        return toResponse(saved);
    }

    public TripResponse attachFileUrl(Long id, String url) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
  
        List<String> photos = new ArrayList<>(trip.getPhotos() != null ? trip.getPhotos() : new ArrayList<>());
        photos.add(url);
        trip.setPhotos(photos);
  
        Trip saved = tripRepository.save(trip);
        return toResponse(saved);
    }
      

    // ---------- mapper ----------
    private TripResponse toResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .description(trip.getDescription())
                .photos(trip.getPhotos())
                .tags(trip.getTags())
                .latitude(trip.getLatitude())
                .longitude(trip.getLongitude())
                .authorId(trip.getAuthor() != null ? trip.getAuthor().getId() : null)
                .authorDisplayName(trip.getAuthor() != null ? trip.getAuthor().getDisplayName() : null)
                .createdAt(trip.getCreatedAt())
                .updatedAt(trip.getUpdatedAt())
                .build();
    }
}