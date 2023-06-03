package org.coke.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.coke.dto.BoardDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.dto.PageResultDTO;
import org.coke.entity.Board;
import org.coke.entity.Member;
import org.coke.repository.BoardImageRepository;
import org.coke.repository.BoardRepository;
import org.coke.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final BoardImageRepository boardImageRepository;

    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = dtoToEntity(boardDTO);

        log.info(board);

        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member) en[1], (Long) en[2]));

        Page<Object[]> result =
                boardRepository.getListWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO getBoard(Long bno) {

        Object result =  boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0], (Member) arr[1], (Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithRepliesAndImages(Long bno) {

        replyRepository.deleteByBno(bno);

        boardImageRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.get();

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        boardRepository.save(board);

    }


}
