package hk.edu.cuhk.iems5722.a2_1155164941;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter {

    public MessageAdapter(@NonNull Context context, @NonNull ArrayList<Message> messages) {
        super(context, 0,messages);
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = (Message) getItem(position);
        return msg.getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message msg=(Message) getItem(position);
//        if(convertView==null){
//            if (getItemViewType(position)==0){
//                convertView= LayoutInflater.from(getContext()).inflate(R.layout.chat_right,parent,false);
//                TextView tvMessage=(TextView) convertView.findViewById(R.id.messageSend);
//                TextView tvTime=(TextView) convertView.findViewById(R.id.timeSend);
//                TextView tvUserName = (TextView)convertView.findViewById(R.id.userSend);
//                tvMessage.setText(msg.getMessage());
//                tvTime.setText(msg.getTime());
//                tvUserName.setText(msg.getUserName());
//            }else {
//                convertView= LayoutInflater.from(getContext()).inflate(R.layout.chat_left,parent,false);
//                TextView tvMessage=(TextView) convertView.findViewById(R.id.messageReceive);
//                TextView tvTime=(TextView) convertView.findViewById(R.id.timeReceive);
//                TextView tvUserName = (TextView)convertView.findViewById(R.id.userReceive);
//                tvMessage.setText(msg.getMessage());
//                tvTime.setText(msg.getTime());
//                tvUserName.setText(msg.getUserName());
//            }
//        }
        if (getItemViewType(position)==0){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.chat_right,parent,false);
            TextView tvMessage=(TextView) convertView.findViewById(R.id.messageSend);
            TextView tvTime=(TextView) convertView.findViewById(R.id.timeSend);
            TextView tvUserName = (TextView)convertView.findViewById(R.id.userSend);
            tvMessage.setText(msg.getMessage());
            tvTime.setText(msg.getTime());
            tvUserName.setText(msg.getUserName());
        }else {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.chat_left,parent,false);
            TextView tvMessage=(TextView) convertView.findViewById(R.id.messageReceive);
            TextView tvTime=(TextView) convertView.findViewById(R.id.timeReceive);
            TextView tvUserName = (TextView)convertView.findViewById(R.id.userReceive);
            tvMessage.setText(msg.getMessage());
            tvTime.setText(msg.getTime());
            tvUserName.setText(msg.getUserName());
        }
        return convertView;
    }

}
