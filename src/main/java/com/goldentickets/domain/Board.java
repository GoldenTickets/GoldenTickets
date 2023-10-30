package com.goldentickets.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long b_seq;

    @Column
    private String b_title;

    @Column
    private String b_writer;

    @Column
    private String b_date;

    @Column
    private String b_content;

    @Column
    private int b_hit;

}
