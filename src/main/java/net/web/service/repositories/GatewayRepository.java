package net.web.service.repositories;

import net.web.service.models.Gateway;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GatewayRepository extends MongoRepository<Gateway, String> {
}
