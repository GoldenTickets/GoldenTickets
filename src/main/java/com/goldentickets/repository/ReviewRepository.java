package com.goldentickets.repository;

import com.goldentickets.domain.Movie;
import com.goldentickets.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
