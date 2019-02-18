package net.web.service.controllers;

import net.web.service.WebServiceApplication;
import net.web.service.models.Gateway;
import net.web.service.repositories.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private GatewayRepository repository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Gateway> all() {
        List<Gateway> gateways = repository.findAll();

        //print debug
        for (Gateway g : gateways) {
            System.out.println(g.toString());
        }

        return gateways;
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public void add(Gateway gateway) {
        repository.insert(gateway);
    }

    @RequestMapping(value = "/threshold", method = RequestMethod.POST)
    public void setThreshold(int threshold) {
        WebServiceApplication.threshold = threshold;
    }
}
