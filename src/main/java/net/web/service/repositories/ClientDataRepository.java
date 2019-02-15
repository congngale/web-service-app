package net.web.service.repositories;

import net.web.service.models.ClientData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientDataRepository extends MongoRepository<ClientData, String> {
}
