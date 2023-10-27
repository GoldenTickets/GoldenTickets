package com.goldentickets.dto;

import com.goldentickets.domain.Movie;
import com.goldentickets.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private final String m_seq;
    private final String email;
    private final String r_rating;
    private final String r_content;
    private final String r_liked;
    private final String r_hated;

    public ReviewResponse(Review review) {
        this.m_seq = review.getM_seq();
        this.email = review.getEmail();
        this.r_rating = review.getR_rating();
        this.r_content = review.getR_content();
        this.r_liked = review.getR_liked();
        this.r_hated = review.getR_hated();
    }
}