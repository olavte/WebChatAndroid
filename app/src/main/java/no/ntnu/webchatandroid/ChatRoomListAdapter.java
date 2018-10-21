package no.ntnu.webchatandroid;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.MyViewHolder> {

    private ArrayList<ChatRoom> mDataset;
    private MainActivity activity;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        MyViewHolder(View v) {
            super(v);
            mTextView = itemView.findViewById(R.id.name);
        }
    }

    public ChatRoomListAdapter(MainActivity activity, ArrayList<ChatRoom> myDataset) {
        this.activity = activity;
        mDataset = myDataset;
    }

    @Override
    public ChatRoomListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room, parent, false);
        return new ChatRoomListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ChatRoom chatRoom = mDataset.get(position);
        holder.mTextView.setText(chatRoom.getName());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ChatActivity.class);
                myIntent.putExtra("ID", chatRoom.getId());
                myIntent.putExtra("NAME", chatRoom.getName());
                myIntent.putExtra("PASSWORD", chatRoom.getPassword());
                myIntent.putExtra("USER", activity.getUser().getName());
                activity.startActivity(myIntent);
            }
        });
        holder.mTextView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent myIntent = new Intent(activity, ChatActivity.class);
                    myIntent.putExtra("ID", chatRoom.getId());
                    myIntent.putExtra("NAME", chatRoom.getName());
                    myIntent.putExtra("PASSWORD", chatRoom.getPassword());
                    myIntent.putExtra("USER", activity.getUser().getName());
                    activity.startActivity(myIntent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}