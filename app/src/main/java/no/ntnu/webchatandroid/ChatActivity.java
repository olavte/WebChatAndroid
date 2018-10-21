package no.ntnu.webchatandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ChatActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private ChatListAdapter mAdapter;
    private ChatRoom chatRoom;
    private User user;
    private ArrayList<Message> messages;
    private RestService restService;
    private Thread listener;
    private EditText chatInput;

    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        restService = new RestService();

        listener = createListener();

        user = new User();
        user.setName(getIntent().getStringExtra("USER"));
        chatInput = findViewById(R.id.chatInput);

        chatRoom = new ChatRoom(
                getIntent().getIntExtra("ID", 0),
                getIntent().getStringExtra("NAME"),
                getIntent().getStringExtra("PASSWORD")
        );

        final RecyclerView chatList = (RecyclerView) findViewById(R.id.chatList);

        mLayoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(mLayoutManager);

        messages = new ArrayList<>();

        mAdapter = new ChatListAdapter(this, messages);
        chatList.setAdapter(mAdapter);

        restService = new RestService();
        messages.addAll(restService.getAllMessagesInChatRoom(chatRoom));

        mAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.submitMessage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chatInput.getText().toString().equals("")) {
                    Message message = new Message();
                    message.setMessage(chatInput.getText().toString());
                    message.setRoomNumber(chatRoom.getId());
                    message.setUserName(user.getName());
                    chatInput.setText("");
                    restService.submitMessage(message, chatRoom);
                }
            }
        });
        chatInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(!chatInput.getText().toString().equals("")) {
                        Message message = new Message();
                        message.setMessage(chatInput.getText().toString());
                        message.setRoomNumber(chatRoom.getId());
                        message.setUserName(user.getName());
                        chatInput.setText("");
                        restService.submitMessage(message, chatRoom);
                    }
                    return true;
                }
                return false;
            }
        });


        listener.start();
    }

    private Thread createListener() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                final Handler handler = new Handler(Looper.getMainLooper());
                while(running) {
                    List<Message> restList = restService.getAllMessagesInChatRoom(chatRoom);
                    if(!messages.containsAll(restList)) {
                        messages.clear();
                        messages.addAll(restList);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
