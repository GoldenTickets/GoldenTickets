package com.goldentickets.service;

import com.goldentickets.domain.Board;
import com.goldentickets.domain.Movie;
import com.goldentickets.repository.BoardRepository;
import com.goldentickets.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board findById(int seq) {
        return boardRepository.findBySeq(seq)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + seq));


    }
}

