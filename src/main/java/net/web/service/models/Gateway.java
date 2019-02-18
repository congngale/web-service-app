package net.web.service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gateway")
public class Gateway {
    @Id
    public String id;

    public String ip;

    public String name;

    public Gateway() {

    }

    public Gateway(String id, String name, String ip) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Gateway{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}