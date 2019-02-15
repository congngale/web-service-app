package net.web.service.controllers;

import net.web.service.models.ClientData;
import net.web.service.models.Gateway;
import net.web.service.repositories.ClientDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientDataRepository repository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ClientData> all() {
        return repository.findAll();
    }
}
