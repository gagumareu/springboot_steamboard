package org.coke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CkEditorDTO {

    private Boolean uploaded;
    private String fileName;
    private String url;

    public CkEditorDTO(boolean uploaded, String fileName, String url) {
        this.uploaded = uploaded;
        this.fileName = fileName;
        this.url = url;
    }
}
