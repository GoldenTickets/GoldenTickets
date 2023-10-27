package com.goldentickets.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_seq", updatable = false)
    private Long r_seq;

    @ManyToOne
    @JoinColumn(name = "m_seq", referencedColumnName = "m_seq")
    @Column(name = "m_seq", nullable = false)
    private Long m_seq;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "r_rating", nullable = false)
    private String r_rating;

    @Column(name = "r_content", nullable = false)
    private String r_content;

    @Column(name = "r_liked", nullable = false)
    private String r_liked;

    @Column(name = "r_hated", nullable = false)
    private String r_hated;

    @Builder
    public Review(Long m_seq, String email, String r_rating, String r_content, String r_liked, String r_hated) {
        this.m_seq = m_seq;
        this.email = email;
        this.r_rating = r_rating;
        this.r_content = r_content;
        this.r_liked = r_liked;
        this.r_hated = r_hated;
    }

    public void update(Long m_seq, String email, String r_rating, String r_content, String r_liked, String r_hated) {
        this.m_seq = m_seq;
        this.email = email;
        this.r_rating = r_rating;
        this.r_content = r_content;
        this.r_liked = r_liked;
        this.r_hated = r_hated;
    }
}