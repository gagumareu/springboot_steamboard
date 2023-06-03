package org.coke.repository;

import org.coke.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Modifying
    @Query("DELETE FROM BoardImage bi WHERE bi.board.bno = :bno")
    void deleteByBno(Long bno);
}
