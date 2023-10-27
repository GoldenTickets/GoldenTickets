package com.goldentickets.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_seq", updatable = false)
    private Long m_seq;

    @Column(name = "m_title", nullable = false)
    private String m_title;

    @Column(name = "m_synopsis", nullable = false)
    private String m_synopsis;

    @Column(name = "m_release", nullable = false)
    private String m_release;

    @Column(name = "m_director", nullable = false)
    private String m_director;

    @Column(name = "m_country", nullable = false)
    private String m_country;

    @Column(name = "m_runningtime", nullable = false)
    private String m_runningtime;

    @Column(name = "m_poster", nullable = false)
    private String m_poster;

    @Builder
    public Movie(String m_title, String m_synopsis, String m_release, String m_director, String m_country, String m_runningtime, String m_poster) {
        this.m_title = m_title;
        this.m_synopsis = m_synopsis;
        this.m_release = m_release;
        this.m_director = m_director;
        this.m_country = m_country;
        this.m_runningtime = m_runningtime;
        this.m_poster = m_poster;
    }

    public void update(String m_title, String m_synopsis, String m_release, String m_director, String m_country, String m_runningtime, String m_poster) {
        this.m_title = m_title;
        this.m_synopsis = m_synopsis;
        this.m_release = m_release;
        this.m_director = m_director;
        this.m_country = m_country;
        this.m_runningtime = m_runningtime;
        this.m_poster = m_poster;
    }
}