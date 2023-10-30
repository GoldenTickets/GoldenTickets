package com.goldentickets.dto;

import com.goldentickets.domain.Board;
import com.goldentickets.domain.Movie;
import jakarta.persistence.Column;
import lombok.Getter;

public class BoardResponse {

        private final int b_seq;
        private final String b_title;

        private final String b_writer;

        private final String b_date;

        private final String b_content;

        private final int b_hit;

    public BoardResponse(Board board) {
        this.b_seq = board.getB_seq();
        this.b_title = board.getB_title();
        this.b_writer = board.getB_writer();
        this.b_date = board.getB_date();
        this.b_content = board.getB_content();
        this.b_hit = board.getB_hit();
    }

}
