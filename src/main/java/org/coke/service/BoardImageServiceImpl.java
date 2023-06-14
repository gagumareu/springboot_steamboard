package org.coke.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.coke.entity.BoardImage;
import org.coke.repository.BoardImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardImageServiceImpl implements BoardImageService{

    private final BoardImageRepository boardImageRepository;


    @Override
    public List<BoardImage> getBoardImageList(Long bno) {

        List<BoardImage> boardImageList = boardImageRepository.findByBno(bno);

        return boardImageList;
    }
}
