package com.goldentickets.service;

import com.goldentickets.domain.Movie;
import com.goldentickets.domain.Review;
import com.goldentickets.repository.MovieRepository;
import com.goldentickets.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public List<Review> findByM_seq_M_seq(long m_seq) {
        return reviewRepository.findByM_seq_M_seq(m_seq);
    }

}