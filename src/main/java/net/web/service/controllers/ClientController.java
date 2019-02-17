package net.web.service.controllers;

import net.web.service.models.Client;
import net.web.service.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientRepository repository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Client> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public void add(Client client) {
        repository.insert(client);
    }
}
