package net.web.service.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client_data")
public class ClientData {

    public int data;

    public String client_id;

    public  ClientData() {
    }

    public ClientData(String id, int data) {
        this.data = data;
        this.client_id = id;
    }
}
