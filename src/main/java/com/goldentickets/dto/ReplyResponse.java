package com.goldentickets.dto;

import com.goldentickets.domain.Board;

public class ReplyResponse {
    private final long r_seq;
    private final String r_writer;

    private final String r_date;

    private final String r_content;

    public BoardResponse(Board board) {
        this.r_seq = board.getR_seq();
        this.r_writer = board.getR_writer();
        this.r_date = board.getR_date();
        this.r_content = board.getR_content();
    }
}
