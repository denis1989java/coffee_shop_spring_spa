package ru.mail.system.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomController {
    private static final Logger logger = Logger.getLogger(WelcomController.class);

    /**
     * open coffees.jsp
     *
     * @return coffees.jsp
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String showCoffes() {
        return "coffees";
    }

}
