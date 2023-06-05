package org.coke.service;

import jakarta.transaction.Transactional;
import org.coke.dto.ReplyDTO;
import org.coke.entity.Board;
import org.coke.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Transactional
    @Test
    public void getList(){

        Long bno = 311L;

        List<ReplyDTO> replyDTOList = replyService.getList(bno);

        replyDTOList.forEach( replyDTO -> System.out.println(replyDTO));
    }

    @Test
    public void insert(){

        ReplyDTO replyDTO = ReplyDTO.builder()
                        .text("testing")
                                .bno(416L)
                                        .memberEmail("user3@email.com")
                                                .build();

        replyService.register(replyDTO);

    }
}
