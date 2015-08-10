package brama.com.controllers;

import brama.com.aws.RDS;
import brama.com.entities.WSResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by vedra on 10.08.2015..
 */
@Controller
public class UserController {
    @RequestMapping("/setEmailNotifications")
    public @ResponseBody WSResponse setEmailNotification(@RequestParam("email") String email, @RequestParam("sendresults") String sendresults){

        RDS db = new RDS();
        int userId = db.getUserId(email);
        if(userId<=0){
            System.out.println("Creating new user: " + email);
            db.insertUser(email);
            userId = db.getUserId(email);
        }
        int localSendResults = Integer.parseInt(sendresults);
        db.setSendResults(userId, localSendResults);
        db.close();

        return new WSResponse("Success","Your ("+ email+") notification preference has changed to " + sendresults);
    }
}
