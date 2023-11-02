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

public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/board")
    public String boardList(Model model) { //게시글 목록
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
        return "board";
    }

    @GetMapping("/api/board/{b_seq}")
    public String boardDetail(@PathVariable Long b_seq, Model model) {
        Board board = boardService.findById(b_seq);
        model.addAttribute("board", board);
        return "board_view";
    }

    @PostMapping("/api/create")
    public String createBoard(@ModelAttribute Board board) {
        boardService.create(board);
        return "redirect:/board";
    }

    @PutMapping("update/{b_seq}")
    public String updateBoard(@PathVariable Long b_seq, @ModelAttribute Board board) {
        board.setSeq(b_seq);
        boardService.update(board);
        return "redirect:/board";
    }

    @DeleteMapping("delete/{b_seq}")
    public String deleteBoard(@PathVariable Long b_seq) {
        boardService.delete(b_seq);
        return "redirect:/board";
    }

    public String BoardPage(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<Board> boardPage = boardService.findAllPageable(PageRequest.of(page, size));
        List<Board> boards = boardPage.getContent();
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        return "board";
    }

    @GetMapping("/api")
    @ResponseBody
    public Page<BoardResponse> BoardPageList(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Page<Board> boardPage = boardService.findAllPageable(PageRequest.of(page, size));
        return boardPage.map(BoardResponse::new);
    }

    @GetMapping("/api/boardReply")
    public String replyList(Model model) { //댓글 목록
        List<Reply> replys = boardService.findAll();
        model.addAttribute("replys", replys);
        return "board";
    }

}
