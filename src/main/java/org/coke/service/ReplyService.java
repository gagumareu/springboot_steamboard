package org.coke.service;

import org.coke.dto.ReplyDTO;
import org.coke.entity.Board;
import org.coke.entity.Member;
import org.coke.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);
    List<ReplyDTO> getList(Long bno);
    void remove(Long rno);
    void update(ReplyDTO replyDTO);

    default Reply dtoToEntity(ReplyDTO replyDTO){

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .member(Member.builder().email(replyDTO.getMemberEmail()).build())
                .board(Board.builder().bno(replyDTO.getBno()).build())
                .build();

        return reply;
    }

    default ReplyDTO entityToDto(Reply reply){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .bno(reply.getBoard().getBno())
                .memberEmail(reply.getMember().getEmail())
                .memberName(reply.getMember().getName())
                .build();

        return replyDTO;

    }

}
