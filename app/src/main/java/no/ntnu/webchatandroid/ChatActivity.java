package no.ntnu.webchatandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ChatActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private ChatListAdapter mAdapter;
    private ChatRoom chatRoom;
    private ArrayList<Message> messages;
    private RestService restService;
    private Thread listener;

    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        restService = new RestService();

        listener = createListener();


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        listener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }

    private Thread createListener() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                final Handler handler = new Handler(Looper.getMainLooper());
                while(running) {
                    messages.clear();
                    messages.addAll(restService.getAllMessagesInChatRoom(chatRoom));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
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
