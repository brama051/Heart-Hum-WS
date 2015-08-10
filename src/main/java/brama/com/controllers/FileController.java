package brama.com.controllers;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import brama.com.aws.RDS;
import brama.com.aws.S3;
import brama.com.entities.WSResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.StatusLine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.security.ssl.ProtocolVersion;

import javax.xml.ws.Response;

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


    //OVO RADIIII
    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public ResponseEntity<String> fileUpload(@RequestParam("name") String name,
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

                //return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
                //return new ResponseEntity<String>("FileHas uploaded",HttpStatus.OK);
                return new ResponseEntity<String>(HttpStatus.OK);//Entity<String>("true");//new WSResponse("Success", "File path is...");



            } catch (Exception e) {
                return new  ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//HttpEntity<String>("false");//new WSResponse("Fail", "You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//HttpEntity<String>("false");//new WSResponse("Fail", "You failed to upload file because the file was empty.");
        }
    }

    //OVO RADI OLEEEEEEEEEEE
    @RequestMapping(value="/multipleFileUpload", method=RequestMethod.POST )
    public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(new File("C:\\uploaded\\" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +"<br/>";
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }
    /*
    @RequestMapping(value="/multipleFileUpload", method=RequestMethod.POST )
    public ResponseEntity<String> multipleSave(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(new File("C:\\uploaded\\" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +"<br/>";
                } catch (Exception e) {
                    return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//"You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            return new ResponseEntity<String>("true STRING BODY: " + msg,HttpStatus.OK);//msg;
        } else {
            return new ResponseEntity<String>("Unable to upload. File is empty.",HttpStatus.OK);//"Unable to upload. File is empty.";
        }
    }*/
}
