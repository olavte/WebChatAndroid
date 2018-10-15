package no.ntnu.webchatandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.MyViewHolder> {

    private ArrayList<ChatRoom> mDataset;
    private MainActivity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public ChatRoomListAdapter(MainActivity activity, ArrayList<ChatRoom> myDataset) {
        this.activity = activity;
        mDataset = myDataset;
    }

    @Override
    public ChatRoomListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
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
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}