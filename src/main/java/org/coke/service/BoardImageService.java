package org.coke.service;

import org.coke.entity.BoardImage;

import java.util.List;

public interface BoardImageService {

    List<BoardImage> getBoardImageList(Long bno);
}
