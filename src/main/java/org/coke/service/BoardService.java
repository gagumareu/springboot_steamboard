package org.coke.service;

import org.coke.dto.BoardDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.dto.PageResultDTO;
import org.coke.entity.Board;
import org.coke.entity.Member;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    BoardDTO getBoard(Long bno);
    void removeWithRepliesAndImages(Long bno);
    void modify(BoardDTO boardDTO);


    default Board dtoToEntity(BoardDTO boardDTO){

        Member member = Member.builder().email(boardDTO.getMemberEmail()).build();

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .member(member)
                .build();

        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .memberEmail(member.getEmail())
                .memberName(member.getName())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }

}
