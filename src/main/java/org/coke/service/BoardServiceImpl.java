package org.coke.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.coke.dto.BoardDTO;
import org.coke.dto.BoardImageDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.dto.PageResultDTO;
import org.coke.entity.Board;
import org.coke.entity.BoardImage;
import org.coke.entity.Member;
import org.coke.repository.BoardImageRepository;
import org.coke.repository.BoardRepository;
import org.coke.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final BoardImageRepository boardImageRepository;

    @Transactional
    @Override
    public Long register(BoardDTO boardDTO) {

        Map<String, Object> entityMap = dtoToEntity(boardDTO);
        Board board = (Board) entityMap.get("board");
        List<BoardImage> boardImageList = (List<BoardImage>) entityMap.get("imageList");

        log.info(board);

        boardRepository.save(board);

        if(boardImageList != null && boardImageList.size() > 0){
            boardImageList.forEach(boardImage -> {
                boardImageRepository.save(boardImage);
            });
        }

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

//        Page<Object[]> result =
//                boardRepository.getListWithMemberImageAndReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Function<Object[], BoardDTO> fn = (arr -> entityToDTO(
                (Board) arr[0],
                (List<BoardImage>) (Arrays.asList((BoardImage)arr[1])),
                (Member) arr[2],
                (Long) arr[3]
        ));

        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO getBoard(Long bno) {

        List<Object[]> result =  boardRepository.getBoardByBnoWithImageMemberAndReplyCount(bno);

        Board board = (Board) result.get(0)[0];

        List<BoardImage> boardImageList = new ArrayList<>();

        result.forEach(arr ->{
            BoardImage boardImage = (BoardImage) arr[1];
            boardImageList.add(boardImage);
        });

        Member member = (Member) result.get(0)[2];

        Long replyCount = (Long) result.get(0)[3];

        return entityToDTO(board, boardImageList, member, replyCount);
    }

    @Transactional
    @Override
    public void removeWithRepliesAndImages(Long bno) {

        replyRepository.deleteByBno(bno);

        boardImageRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        boardImageRepository.deleteByBno(boardDTO.getBno());

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.get();

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        boardRepository.save(board);

        List<BoardImage> boardImageList = boardImageDtoToEntity(boardDTO);

        if(boardImageList.size() > 0 && boardImageList != null){
            boardImageList.forEach(boardImage -> {
                boardImageRepository.save(boardImage);
            });
        }
    }





}
