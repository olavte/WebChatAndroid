package no.ntnu.webchatandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private ChatRoomListAdapter mAdapter;
    private ArrayList<ChatRoom> chatRooms;
    private RestService restService;
    private Thread listener;

    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listener = createListener();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addChatRoomBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final RecyclerView chatRoomList = (RecyclerView) findViewById(R.id.chatRoomList);

        mLayoutManager = new LinearLayoutManager(this);
        chatRoomList.setLayoutManager(mLayoutManager);

        chatRooms = new ArrayList<>();

        mAdapter = new ChatRoomListAdapter(this, chatRooms);
        chatRoomList.setAdapter(mAdapter);

        restService = new RestService();
        chatRooms.addAll(restService.getAllChatRooms());

        mAdapter.notifyDataSetChanged();
        listener.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!running) {
            listener.start();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Thread createListener() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                final Handler handler = new Handler(Looper.getMainLooper());
                while(running) {
                    chatRooms.clear();
                    chatRooms.addAll(restService.getAllChatRooms());
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
