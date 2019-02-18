package net.web.service.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client")
public class Client {
    @Id
    public String id;

    public String ip;

    public String name;

    public int threshold;

    public Client() {
    }

    public Client(String id, String ip, String name, int threshold) {
        this.id = id;
        this.ip = ip;
        this.name = name;
        this.threshold = threshold;
    }
}
