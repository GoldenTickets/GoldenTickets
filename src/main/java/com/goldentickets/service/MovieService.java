package com.goldentickets.service;
import lombok.RequiredArgsConstructor;
import com.goldentickets.domain.Movie;
import com.goldentickets.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

}