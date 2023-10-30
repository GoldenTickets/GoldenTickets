package com.goldentickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import com.goldentickets.domain.Movie;

@Getter
@Schema(description = "MV dto")
public class MovieResponse {

    @Schema(description = "m_title")
    private final String m_title;
    @Schema(description = "m_synopsis")
    private final String m_synopsis;
    @Schema(description = "m_release")
    private final String m_release;
    @Schema(description = "m_director")
    private final String m_director;
    @Schema(description = "m_country")
    private final String m_country;
    @Schema(description = "m_runningtime")
    private final String m_runningtime;
    @Schema(description = "m_poster")
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