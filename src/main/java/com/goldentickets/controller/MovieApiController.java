package com.goldentickets.controller;

import com.goldentickets.domain.Review;
import com.goldentickets.dto.ReviewResponse;
import com.goldentickets.service.ReviewService;
import lombok.RequiredArgsConstructor;
import com.goldentickets.domain.Movie;
import com.goldentickets.dto.MovieResponse;
import com.goldentickets.service.MovieService;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MovieApiController {

    private final MovieService movieService;
    private final ReviewService reviewService;

    @GetMapping("/api/movies/{m_seq}")
    public String findMovies(@PathVariable long m_seq, Model model) {

        Movie movie = movieService.findById(m_seq);
        model.addAttribute("movie", new MovieResponse(movie));

        //List<Review> review = reviewService.findByM_seq_M_seq(m_seq);
        //여기 문제 해결하기


        return "movieInfo";
    }

}