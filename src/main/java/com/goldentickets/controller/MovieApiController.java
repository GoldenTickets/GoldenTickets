package com.goldentickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "moiveInfo", description = "MI API document")
public class MovieApiController {

    private final MovieService movieService;

    @GetMapping("/api/movies/{m_seq}")
    @Operation(summary = "load", description = "to load MV", tags = {"m_seq"})
    public String findMovies(@PathVariable long m_seq, Model model) {

        Movie movie = movieService.findById(m_seq);
        model.addAttribute("movie", new MovieResponse(movie));

        //List<Review> review = reviewService.findByM_seq_M_seq(m_seq);
        //여기 문제 해결하기

        return "movieInfo";
    }

}