package org.coke.service;

import org.coke.entity.BoardImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BoardImageServiceTests {

    @Autowired
    private BoardImageService boardImageService;

    @Test
    public void getBoardList(){

        List<BoardImage> boardImageList = boardImageService.getBoardImageList(430L);

        boardImageList.forEach(boardImage -> {
            System.out.println(boardImage);
        });
    }
}
