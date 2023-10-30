package com.goldentickets.controller;

import com.goldentickets.domain.Board;
import com.goldentickets.domain.Movie;
import com.goldentickets.dto.BoardResponse;
import com.goldentickets.dto.MovieResponse;
import com.goldentickets.service.BoardService;
import com.goldentickets.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/board")
    public ResponseEntity<List<BoardResponse>> findAllBoard() {
        List<BoardResponse> board = boardService.findAll()
                .stream()
                .map(BoardResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(board);
    }
    @GetMapping("/api/board/{b_seq}")
    public String findBoard(@PathVariable long b_seq, Model model) {
        Board board = boardService.findBySeq(b_seq);
        model.addAttribute("board", new BoardResponse(board));

        return "boardDetail";
    }

    @GetMapping("/api/boardDetail/{b_seq}")
    public String findBoardDetail(@PathVariable long b_seq, Model model) {
        Board board = boardService.findBySeq(b_seq);
        model.addAttribute("board", new BoardResponse(board));

        return "main";
    }
}
