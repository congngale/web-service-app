package net.web.service.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client")
public class Client {
    @Id
    private String id;

    private String ip;

    private String name;

    private int threshold;

    public Client() {
    }

    public Client(String id, String ip, String name, int threshold) {
        this.id = id;
        this.ip = ip;
        this.name = name;
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }
}
