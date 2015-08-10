package brama.com.controllers;

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
        //TODO CHANGE SETTING IN DATABASE
        return new WSResponse("Success","Your notification preference has changed");
    }
}
