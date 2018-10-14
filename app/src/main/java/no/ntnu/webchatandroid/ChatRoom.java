package no.ntnu.webchatandroid;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRoom implements Serializable {
    private int id;

    private String name;

    private String password;

    private boolean removed = false;

    public ChatRoom(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
