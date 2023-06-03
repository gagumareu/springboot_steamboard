package org.coke.repository;

import org.coke.entity.Board;
import org.coke.entity.BoardImage;
import org.coke.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @Test
    public void insertBoardWithImages(){

        IntStream.rangeClosed(1, 100).forEach( i -> {

            Member member = Member.builder().email("user" +(int)((Math.random() * 99) +1) + "@email.com").build();

            Board board = Board.builder()
                    .title("title " + i)
                    .content("board content" + i)
                    .member(member).build();

            boardRepository.save(board);

            int count = (int) ((Math.random() * 5) + 1);

            for (int j = 0; j < count; j++){

                BoardImage boardImage = BoardImage.builder()
                        .board(board)
                        .uuid(UUID.randomUUID().toString())
                        .fileName("board image file (board no: " + i + ", image no: " + j + ".jpg")
                        .build();

                boardImageRepository.save(boardImage);
            }
        });
    }


    @Test
    public void test(){

        IntStream.rangeClosed(1, 300).forEach(i ->{
            int count = (int) ((Math.random() * 99) + 1);
            System.out.println("count NO("+i+"): " + count);
        });

    }

    @Test
    public void getListWithReplyCount(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getListWithReplyCount(pageable);

        result.get().forEach(arr -> {
            Object[] list = arr;

            System.out.println(Arrays.toString(list));
        });

    }

    @Test
    public void getBoardByBno(){

        Object result = boardRepository.getBoardByBno(310L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));

    }

    @Test
    public void getBoardWithMember(){

        Object result = boardRepository.getBoardWithMember(310L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void getBoardWithReplyCount(){

        List<Object[]> result = boardRepository.getBoardWithReplyCount(310L);

        result.forEach(arr -> {
            System.out.println(Arrays.toString(arr));
        });
    }

}
