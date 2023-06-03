package org.coke.service;

import jakarta.transaction.Transactional;
import org.coke.dto.BoardDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.dto.PageResultDTO;
import org.coke.entity.Board;
import org.coke.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void insert(){


        BoardDTO boardDTO = BoardDTO.builder()
                        .title("testing service for inserting")
                        .content("testing service for inserting")
                                .MemberEmail("user13@email.com")
                                        .build();

        boardService.register(boardDTO);
    }

    @Test
    public void getListWithReplyCount(){

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }

        System.out.println("----------------");

        result.getDtoList().forEach(i -> {

            System.out.println(i);
        });
    }

    @Test
    public void getBoard(){

        BoardDTO result = boardService.getBoard(310L);

        System.out.println(result);

    }

    @Test
    public void deleteById(){

        boardService.removeWithRepliesAndImages(402L);
    }

    @Test
    public void update(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(401L)
                .title("changed title")
                .content("changed content")
                .build();

        boardService.modify(boardDTO);
    }
}
