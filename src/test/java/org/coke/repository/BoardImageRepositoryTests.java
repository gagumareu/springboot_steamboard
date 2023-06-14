package org.coke.repository;

import org.coke.entity.BoardImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BoardImageRepositoryTests {

    @Autowired
    private BoardImageRepository boardImageRepository;

    @Test
    public void getImageList(){

        List<BoardImage> boardImageList = boardImageRepository.findByBno(430L);

        boardImageList.forEach(boardImage -> {
            System.out.println(boardImage);
        });
    }
}
