package org.coke.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.coke.dto.CkEditorDTO;
import org.coke.dto.UploadResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.coke.upload.path}")
    private String uploadPath;

    private String makeFolder(){

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        log.info("---------------------uploadAjax controller-----------------");

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles){

            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("this files'type is not image");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") +1);

            log.info("originalName: " + originalName);
            log.info("fileName: " + fileName);

            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            log.info("saveName: " + saveName);

            Path savePath = Paths.get(saveName);

            log.info("savePath: " + savePath);

            try {

                uploadFile.transferTo(savePath);

                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);

                resultDTOList.add(new UploadResultDTO(folderPath,uuid,fileName));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } // for

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @PostMapping("/ckeditor")
    public Map<String, Object> ckeditor(MultipartFile[] upload){

        log.info("---------------------uploadAjax controller-----------------");

        Map<String, Object> fileMap = new HashMap<>();

        for (MultipartFile uploadFile : upload){

//            if(!uploadFile.getContentType().startsWith("image")){
//                log.warn("this files'type is not image");
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }

            String originalFilename = uploadFile.getOriginalFilename();
            String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") +1);

            log.info("originalFilename: " + originalFilename);
            log.info("fileName: " + fileName);

            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            log.info("saveName: " + saveName);

            Path savePath = Paths.get(saveName);
            log.info("savePath: " + savePath);

            String fileURL = URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName);
            log.info("savePath: " + fileURL);

            try {

                uploadFile.transferTo(savePath);

                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);


                fileMap.put("uploaded", 1);
                fileMap.put("fileName", fileName);
                fileMap.put("url", "/display?fileName=" + fileURL);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } // for

        return fileMap;
    }


    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size){

        log.info("----------------- disply on controller ----------------");

        ResponseEntity<byte[]> result = null;

        try {

            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("srcFileName: " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("thumbnail_file: " + file);

            if (size != null && size.equals("1")){
                file = new File(file.getParent(), file.getName().substring(2));
                log.info("not thumbnail_file: " + file);
            }

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);


        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFIle(String fileName){

        log.info("----------removeFile on uploadControler ---------------");

        String srcFileName = null;

        log.info("fileName: " + fileName);

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("srcFileName: " + srcFileName);
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            log.info("result: " + result);

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            log.info("thumbnail: " + thumbnail);
            log.info("thumb result: " + result);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
