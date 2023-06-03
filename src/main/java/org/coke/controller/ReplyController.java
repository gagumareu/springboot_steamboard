package org.coke.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.coke.dto.ReplyDTO;
import org.coke.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){

        log.info("replyDTO: " + replyDTO);

        replyService.register(replyDTO);

        return new ResponseEntity<>(replyDTO.getRno(), HttpStatus.OK);
    }

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getList(@PathVariable("bno") Long bno){

        log.info("bno: " + bno);

        List<ReplyDTO> dtoList = replyService.getList(bno);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){

        log.info("rno: " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> update(@RequestBody ReplyDTO replyDTO){

        log.info("replyDTO: " + replyDTO);

        replyService.update(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


}
