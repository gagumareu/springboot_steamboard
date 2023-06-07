package org.coke.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;

    private String memberEmail;
    private String memberName;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private int replyCount;

    @Builder.Default
    private List<BoardImageDTO> imageDTOList = new ArrayList<>();

}
