package no.ntnu.webchatandroid;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestService {

    private final String api = "http://10.0.2.2:8080/WebChat/api/";
    private ObjectMapper objectMapper;

    public RestService() {
        objectMapper = new ObjectMapper();
    }

    // HTTP GET request
    public JSONArray sendGet(String urlParam) throws Exception {
        String url = api + urlParam;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        StringBuffer response = new StringBuffer();
        int responseCode = con.getResponseCode();
        if(responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        return new JSONArray(response.toString());
    }

    // HTTP POST request
    private void sendPost(String urlParam, JSONObject json) {
        try {
            URL object = new URL(api + urlParam);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if(responseCode == 200) {
                System.out.println("Reached");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewChatRoom(String name, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("password", password);
            sendPost("addNewChatRoom", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getAllMessagesInChatRoom(ChatRoom chatRoom) {
        List<Message> messages = new ArrayList<>();
        JSONArray JSONMessages;
        try {
            JSONMessages = sendGet("getAllMessagesInChatRoom?roomNumber="
                    + chatRoom.getId()
                    + "&password="
                    + chatRoom.getPassword());
            for(int i = 0; i < JSONMessages.length(); i++) {
                messages.add(objectMapper.readValue(JSONMessages.get(i).toString(), Message.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(messages);
        return messages;
    }

    public List<ChatRoom> getAllChatRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        JSONArray JSONChatRooms;
        try {
            JSONChatRooms = sendGet("getAllChatRooms");
            for(int i = 0; i < JSONChatRooms.length(); i++) {
                chatRooms.add(objectMapper.readValue(JSONChatRooms.get(i).toString(), ChatRoom.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(chatRooms);
        return chatRooms;
    }

    public void submitMessage(Message message, ChatRoom chatRoom) {
        JSONObject json = new JSONObject();
        try {
            json.put("message", message.getMessage());
            json.put("roomNumber", message.getRoomNumber());
            json.put("userName", message.getUserName());
            sendPost("addNewMessageToChatRoom", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
