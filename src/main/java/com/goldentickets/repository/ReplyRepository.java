package com.goldentickets.repository;

import com.goldentickets.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
}
