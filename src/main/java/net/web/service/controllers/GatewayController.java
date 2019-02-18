package net.web.service.controllers;

import net.web.service.WebServiceApplication;
import net.web.service.models.Gateway;
import net.web.service.repositories.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private GatewayRepository repository;
    
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Gateway> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody Gateway gateway) {
	logger.info("Add gateway = " + gateway.toString());
        //check exist
        if (repository.existsById(gateway.id)) {
            //update gateway
            repository.save(gateway);
        } else {
            //add new gateway
            repository.insert(gateway);
        }
    }

    @RequestMapping(value = "/threshold/{level}", method = RequestMethod.POST)
    public void setThreshold(@PathVariable int level) {
        WebServiceApplication.threshold = level;
    }

    @RequestMapping(value = "/clean")
    public void clean() {
        repository.deleteAll();
    }
}
