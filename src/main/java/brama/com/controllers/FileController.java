package brama.com.controllers;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import brama.com.aws.RDS;
import brama.com.aws.S3;
import brama.com.entities.FileUploadResponse;
import brama.com.entities.MultipleFileUploadResponse;
import brama.com.entities.MultiplePositiveResultResponse;
import brama.com.entities.PositiveResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by vedra on 09.08.2015..
 */
@Controller
public class FileController {
    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();

                S3 upldr = new S3();
                upldr.uploadFile();

                RDS conn = new RDS();
                conn.close();


                return "You successfully uploaded " + name + "!";



            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }



    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public @ResponseBody
    FileUploadResponse fileUpload(@RequestParam("email") String email,
                                 @RequestParam("file") MultipartFile file){

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("new file name")));
                stream.write(bytes);
                stream.close();

                S3 upldr = new S3();
                upldr.uploadFile();

                RDS conn = new RDS();
                conn.close();


                return new FileUploadResponse("primjerFilePATH","primjerFileURL",email);
            } catch (Exception e) {
                e.printStackTrace();
                return null;//new  ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//HttpEntity<String>("false");//new WSResponse("Fail", "You failed to upload " + name + " => " + e.getMessage());
            }
        } else {

            return null; //new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//HttpEntity<String>("false");//new WSResponse("Fail", "You failed to upload file because the file was empty.");
        }
    }

    @RequestMapping(value="/multipleFileUpload", method=RequestMethod.POST )
    public @ResponseBody MultipleFileUploadResponse multipleSave(@RequestParam("file") MultipartFile[] files, @RequestParam("email") String email){
        String fileName = null;
        String msg = "";
        List<FileUploadResponse> listFileUploadResponse= new ArrayList<FileUploadResponse>();
        MultipleFileUploadResponse multipleFileUploadResponse = new MultipleFileUploadResponse();
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(new File("C:\\uploaded\\" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();

                    listFileUploadResponse.add(new FileUploadResponse(fileName, null, email));
                    //msg += "You have successfully uploaded " + fileName +"<br/>";
                } catch (Exception e) {
                    e.printStackTrace();
                    //return //new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//"You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            multipleFileUploadResponse.setResponseList(listFileUploadResponse);
            return  multipleFileUploadResponse;
        } else {
            return null;
        }
    }

    @RequestMapping(value="/getPositiveResults", method= RequestMethod.POST)
    public @ResponseBody
    MultiplePositiveResultResponse getPositiveResults(@RequestParam("email") String email) {
        //TODO GET POSITIVE RESULTS FROM DATABASE AND ADD THEM INTO ARRAY LIST AND PUBLISH
        List<PositiveResultResponse> responseList = new ArrayList<PositiveResultResponse>();
        responseList.add(new PositiveResultResponse("awsurl"));
        responseList.add(new PositiveResultResponse("awsurl2"));
        responseList.add(new PositiveResultResponse("awsurl3"));
        MultiplePositiveResultResponse response = new MultiplePositiveResultResponse(responseList);
        return response;
    }

}
