package org.coke.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.coke.dto.BoardDTO;
import org.coke.dto.BoardImageDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.dto.PageResultDTO;
import org.coke.entity.Board;
import org.coke.entity.BoardImage;
import org.coke.service.BoardImageService;
import org.coke.service.BoardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final BoardImageService boardImageService;

    @Value("${org.coke.upload.path}")
    private String uploadPath;

    private void deleteFile(List<BoardImage> boardImageList){

        log.info("**************** removing images on controller ******************");

        log.info("boardImageList : " + boardImageList);
        log.info("boardImageList_size : " + boardImageList.size());

        boardImageList.forEach(boardImage -> {

            String path = boardImage.getPath();
            String uuid = boardImage.getUuid();
            String fileName = boardImage.getFileName();


            File file = new File(uploadPath + File.separator + path + File.separator + uuid + "_" + fileName);
            log.info("file: " + file);
            file.delete();

            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            log.info("thumbnail: " + thumbnail);
            thumbnail.delete();
        });
    }

    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model){

        log.info("************ board List on controller ************");

        model.addAttribute("boardList", boardService.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        log.info("************ board register on controller ************");

        log.info("boardDTO: " + boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("msg", bno);

        redirectAttributes.addAttribute("bno", bno);

        return "redirect:/board/read";
    }

    @GetMapping({"/read", "update"})
    public void read(@RequestParam("bno") Long bno,
                     @ModelAttribute("pageRequestDTO")PageRequestDTO pageRequestDTO, Model model){

        log.info("************ board read on controller ************");
        log.info("pageRequestDTO: " + pageRequestDTO);

        BoardDTO boardDTO = boardService.getBoard(bno);

        log.info("boardDTO: " + boardDTO);

        System.out.println("list : " + boardDTO.getImageDTOList());
        System.out.println("list size: " + boardDTO.getImageDTOList().size());
        System.out.println("list folderPath: " + boardDTO.getImageDTOList().get(0));

        model.addAttribute("dto", boardDTO);
    }

    @Transactional
    @PostMapping("/remove")
    public String delete(Long bno, RedirectAttributes redirectAttributes){

        log.info("************ board delete on controller ************");

        log.info("[1] delete files");
        List<BoardImage> boardImageList = boardImageService.getBoardImageList(bno);
        log.info("boardImageList: " + boardImageList);

        if (boardImageList != null && boardImageList.size() > 0){
            deleteFile(boardImageList);
        }

        log.info("[2] deleteDB");
        boardService.removeWithRepliesAndImages(bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("************ board update on controller ************");
        log.info("boardDTO: " + boardDTO);
        log.info("pageRequestDTO: " + pageRequestDTO);

        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
//        redirectAttributes.addAttribute("page", pageRequestDTO.getType());
//        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());

        return "redirect:/board/read";
    }


}
