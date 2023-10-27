package com.goldentickets.dto;

import lombok.Getter;
import com.goldentickets.domain.Movie;

@Getter
public class MovieResponse {

    private final String m_title;
    private final String m_synopsis;
    private final String m_release;
    private final String m_director;
    private final String m_country;
    private final String m_runningtime;
    private final String m_poster;

    public MovieResponse(Movie movie) {
        this.m_title = movie.getM_title();
        this.m_synopsis = movie.getM_synopsis();
        this.m_release = movie.getM_release();
        this.m_director = movie.getM_director();
        this.m_country = movie.getM_country();
        this.m_runningtime = movie.getM_runningtime();
        this.m_poster = movie.getM_poster();
    }
}