package org.coke.repository;

import org.coke.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT b, m, COUNT (r) " +
            "FROM Board b " +
            "LEFT JOIN b.member m " +
            "LEFT JOIN Reply r ON r.board = b " +
            "GROUP BY b",
            countQuery = "SELECT COUNT (b) FROM Board b")
    Page<Object[]> getListWithReplyCount(Pageable pageable);


    @Query("SELECT b, m, COUNT (r) " +
            "FROM Board b " +
            "LEFT OUTER JOIN b.member m " +
            "LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    Object getBoardByBno(Long bno);

    @Query("SELECT b, m " +
            "FROM Board b " +
            "LEFT OUTER JOIN b.member m " +
            "WHERE b.bno = :bno")
    Object getBoardWithMember(@Param("bno") Long bno);

    @Query("SELECT b, r, COUNT (r) " +
            "FROM Board b " +
            "LEFT OUTER JOIN Reply r ON r.board = b " +
            "WHERE b.bno = :bno")
    List<Object[]> getBoardWithReplyCount(@Param("bno") Long bno);

}
