package com.goldentickets.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_seq", nullable = false)
    private Long r_seq;

    @Column(name = "r_writer", nullable = false)
    private String r_writer;

    @Column(name = "r_date", nullable = false)
    private String r_date;

    @Column(name = "r_content", nullable = false)
    private String r_content;

    @Builder
    public Board(String r_writer,String r_date,String r_content){
        this.r_writer=r_writer;
        this.r_date=r_date;
        this.r_content=r_content;
    }
}

