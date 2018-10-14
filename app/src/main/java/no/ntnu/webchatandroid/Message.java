package no.ntnu.webchatandroid;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable {

    private int id;

    private String message;

    private int roomNumber;

    private String userName;

    private int userNumber;
}
