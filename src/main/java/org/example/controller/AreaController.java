package org.example.controller;
import org.example.model.Area;
import org.example.repository.AreaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@CrossOrigin(origins = "*") // Allows your frontend website to call this API without CORS errors
public class AreaController{

    private final AreaRepository areaRepository;

    // Spring automatically injects our AreaRepository here (Dependency Injection)
    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    // 1. Endpoint to get all Bangalore areas: GET http://localhost:8080/api/v1/areas
    @GetMapping
    public List<Area> getAllAreas() {
        return areaRepository.findAll();
    }

    // 2. Endpoint for Search Bar autocomplete: GET http://localhost:8080/api/v1/areas/search?query=hsr
    @GetMapping("/search")
    public List<Area> searchAreas(@RequestParam String query) {
        return areaRepository.findByNameContainingIgnoreCase(query);
    }

    // 3. Endpoint to fetch one area by slug: GET http://localhost:8080/api/v1/areas/hsr-layout
    @GetMapping("/{slug}")
    public ResponseEntity<Area> getAreaBySlug(@PathVariable String slug) {
        return areaRepository.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Returns 404 if slug doesn't exist
    }

    //4. Admin Endpoint: Add a new Bangalore Area
    @PostMapping
    public ResponseEntity<Area> createArea(@RequestBody Area area) {
        if (area.getSlug() == null || area.getSlug().isBlank()) {
            area.setSlug(area.getName().toLowerCase().trim().replace(" ", "-"));
        }
        Area savedArea = areaRepository.save(area);
        return ResponseEntity.status(201).body(savedArea);
    }
}