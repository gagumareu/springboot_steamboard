package org.coke.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class BoardImage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private String uuid;

    private String path;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
