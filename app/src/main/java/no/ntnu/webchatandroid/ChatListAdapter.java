package no.ntnu.webchatandroid;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<Message> mDataset;
    private AppCompatActivity activity;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        MyViewHolder(View v) {
            super(v);
            mTextView = itemView.findViewById(R.id.name);
        }
    }

    public ChatListAdapter(AppCompatActivity activity, ArrayList<Message> myDataset) {
        this.activity = activity;
        mDataset = myDataset;
    }

    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Message message = mDataset.get(position);
        holder.mTextView.setText(message.getUserName() + ": \n" + message.getMessage());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}