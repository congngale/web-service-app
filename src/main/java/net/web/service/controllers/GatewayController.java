package net.web.service.controllers;

import net.web.service.models.Gateway;
import net.web.service.repositories.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private GatewayRepository repository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Gateway> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public void add(Gateway gateway) {
        repository.insert(gateway);
    }
}
