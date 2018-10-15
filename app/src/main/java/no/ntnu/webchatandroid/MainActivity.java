package no.ntnu.webchatandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private ChatRoomListAdapter mAdapter;
    private ArrayList<ChatRoom> chatRooms;
    private RestService restService;
    private Thread listener;

    private User user = null;

    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listener = createListener();
        user = new User();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addChatRoomBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChatRoomDialog();
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

        showLogin();
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

    private void showLogin() {
        // get two_input_dialog.xmlialog.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.two_input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set two_input_dialog.xmlialog.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText loginUsername = (EditText) promptsView
                .findViewById(R.id.inputOne);
        final EditText loginPassword = (EditText) promptsView
                .findViewById(R.id.inputTwo);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                user.setName(loginUsername.getText().toString());
                                user.setPassword(loginPassword.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void addChatRoomDialog() {
        // get two_input_dialog.xmlialog.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.two_input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set two_input_dialog.xmlialog.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText chatRoomName = (EditText) promptsView
                .findViewById(R.id.inputOne);
        final EditText chatRoomPassword = (EditText) promptsView
                .findViewById(R.id.inputTwo);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                restService.addNewChatRoom(
                                        chatRoomName.getText().toString(),
                                        chatRoomPassword.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public User getUser() {
        return user;
    }
}
