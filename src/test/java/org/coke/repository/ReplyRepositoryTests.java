package org.coke.repository;

import org.coke.entity.Board;
import org.coke.entity.Member;
import org.coke.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insert(){

        IntStream.rangeClosed(1, 300).forEach(i ->{

            Long bno = (long) ((Math.random() * 99) +305);
            Member member = Member.builder().email("user" +(int)((Math.random() * 99) +1) + "@email.com").build();

            Reply reply = Reply.builder()
                    .text("reply" + i)
                    .member(member)
                    .board(Board.builder().bno(bno).build())
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test
    public void getList(){

        List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(311L).build());

        result.forEach(i -> {
            System.out.println(i);
        });
    }
}
