package net.web.service.controllers;

import net.web.service.models.Client;
import net.web.service.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientRepository repository;
    
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Client> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody Client client) {
	logger.info("Add client = " + client.toString());
        //check exist
        if (repository.existsById(client.id)) {
            repository.save(client);
        } else {
            repository.insert(client);
        }
    }

    @RequestMapping(value = "/clean")
    public void clean() {
        repository.deleteAll();
    }
}
