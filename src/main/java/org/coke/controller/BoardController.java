package org.coke.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.coke.dto.BoardDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model){

        log.info("board List on controller");

        model.addAttribute("List", boardService.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        log.info("board register on controller");

        log.info("boardDTO: " + boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/read";
    }

    @GetMapping({"/read", "update"})
    public void read(@RequestParam("bno") Long bno,
                     @ModelAttribute("pageRequestDTO")PageRequestDTO pageRequestDTO, Model model){

        log.info("board read on controller");

        BoardDTO boardDTO = boardService.getBoard(bno);

        log.info("boardDTO: " + boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @DeleteMapping("/remove")
    public String delete(Long bno, RedirectAttributes redirectAttributes){

        log.info("board delete on controller");

        boardService.removeWithRepliesAndImages(bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PutMapping("/update")
    public String update(BoardDTO boardDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("board update on controller");
        log.info("boardDTO: " + boardDTO);

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

}
