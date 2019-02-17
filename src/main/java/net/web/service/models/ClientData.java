package net.web.service.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "data")
public class ClientData {

    @Id
    private ObjectId id;

    public int data;

    public String client_id;

    public  ClientData() {
    }

    public ClientData(String id, int data) {
        this.data = data;
        this.client_id = id;
    }
}