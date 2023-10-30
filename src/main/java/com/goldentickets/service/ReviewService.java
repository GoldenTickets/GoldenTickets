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

}