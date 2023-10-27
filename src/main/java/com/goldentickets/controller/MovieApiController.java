package com.goldentickets.controller;

import lombok.RequiredArgsConstructor;
import com.goldentickets.domain.Movie;
import com.goldentickets.dto.MovieResponse;
import com.goldentickets.service.MovieService;
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

    @GetMapping("/api/movies")
    public ResponseEntity<List<MovieResponse>> findAllMovies() {
        List<MovieResponse> movies = movieService.findAll()
                .stream()
                .map(MovieResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(movies);
    }
    @GetMapping("/api/movies/{m_seq}")
    public String findMovies(@PathVariable long m_seq, Model model) {
        Movie movie = movieService.findById(m_seq);
        model.addAttribute("movie", new MovieResponse(movie));

        return "movieInfo";
    }

    @GetMapping("/api/movie/{m_seq}")
    public String fidMovies(@PathVariable long m_seq, Model model) {
        Movie movie = movieService.findById(m_seq);
        model.addAttribute("movie", new MovieResponse(movie));

        return "main";
    }

}