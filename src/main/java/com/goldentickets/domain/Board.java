package com.goldentickets.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_seq", nullable = false)
    private Long b_seq;

    @Column(name = "b_title", nullable = false)
    private String b_title;

    @Column(name = "b_writer", nullable = false)
    private String b_writer;

    @Column(name = "b_date", nullable = false)
    private String b_date;

    @Column(name = "b_content", nullable = false)
    private String b_content;

    @Column(name = "b_hit", nullable = false)
    private int b_hit;

    @Builder
    public Board(String b_title,String b_writer,String b_date,String b_content,int b_hit){
        this.b_title=b_title;
        this.b_writer=b_writer;
        this.b_date=b_date;
        this.b_content=b_content;
        this.b_hit=b_hit;
    }
}
