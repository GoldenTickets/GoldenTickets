package com.goldentickets.service;

import com.goldentickets.domain.Board;
import com.goldentickets.domain.Movie;
import com.goldentickets.domain.Reply;
import com.goldentickets.repository.BoardRepository;
import com.goldentickets.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findAll() { //게시글 목록
        return boardRepository.findAll();
    }

    public Board findById(Long seq) { //게시글 상세보기
        return boardRepository.findBySeq(seq)
                .orElseThrow(() -> new IllegalArgumentException("Not found: " + seq));
    }

    public Board create(Board board) { //게시글 작성
        return boardRepository.save(board);
    }

    public Board update(Board board) { //게시글 수정
            throw new IllegalArgumentException("Not found: " + board.getSeq());

    }

    public void delete(Long seq) { //게시글 삭제
            boardRepository.deleteById(seq);
    }

    public List<Reply> findAll() { //댓글 목록
        return boardRepository.findAll();
    }

}

}

