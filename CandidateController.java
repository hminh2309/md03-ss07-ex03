package org.example.session07.controller;

import org.example.session07.dto.ApiResponse;
import org.example.session07.dto.CandidateCreateDTO;
import org.example.session07.dto.CandidateUpdateDTO;
import org.example.session07.entity.Candidate;
import org.example.session07.repository.CandidateRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateController(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Candidate>> createCandidate(@Valid @RequestBody CandidateCreateDTO dto) {
        Candidate candidate = new Candidate(
                dto.getFullName(),
                dto.getEmail(),
                dto.getAge(),
                dto.getYearsOfExperience()
        );

        Candidate savedCandidate = candidateRepository.save(candidate);

        ApiResponse<Candidate> response = new ApiResponse<>(
                "201 CREATED",
                "Thêm mới ứng viên thành công",
                savedCandidate
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint cập nhật Form Data
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCandidate(
            @PathVariable Long id,
            @Valid @ModelAttribute CandidateUpdateDTO dto) {

        return candidateRepository.findById(id).map(candidate -> {
            candidate.setAddress(dto.getAddress());
            candidate.setBio(dto.getBio());

            Candidate updatedCandidate = candidateRepository.save(candidate);
            return ResponseEntity.ok(updatedCandidate);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}