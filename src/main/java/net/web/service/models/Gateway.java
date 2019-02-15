package net.web.service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gateways")
public class Gateway {
    @Id
    private String id;

    private String ip;

    private String name;

    public Gateway(String id, String name, String ip) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }
}
