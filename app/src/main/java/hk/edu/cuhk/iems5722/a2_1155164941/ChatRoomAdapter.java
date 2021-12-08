package hk.edu.cuhk.iems5722.a2_1155164941;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class ChatRoomAdapter extends ArrayAdapter {
    public ChatRoomAdapter(@NonNull Context context, @NonNull ArrayList<ChatRoom> chatRooms) {
        super(context,0,chatRooms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatRoom chatRoom= (ChatRoom) getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.chatrooms_item,parent,false);
        }
        TextView tvChatroom = (TextView) convertView.findViewById(R.id.tvChatroom);
        tvChatroom.setText(chatRoom.getRoomName());
        return convertView;
    }
}
