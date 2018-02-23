package ru.mail.coffee.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.system.service.DaoFactory;
import ru.mail.coffee.model.Coffee;

import java.util.List;

@RestController
public class CoffeController {

    private static final Logger logger = Logger.getLogger(CoffeController.class);

    private final DaoFactory daoFactory;

    @Autowired
    public CoffeController(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * getting coffees from DB and send response to jsp
     *
     * @return response entity with list of coffees
     */

    @RequestMapping(value = {"/api/v1/coffees"}, method = RequestMethod.GET)
    public ResponseEntity<Object> coffes() {

        logger.debug("getting list of all coffees");

        List<Coffee> coffees = daoFactory.getCoffeeDao().list();

        return new ResponseEntity<Object>(coffees, HttpStatus.OK);
    }
}
