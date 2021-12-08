package hk.edu.cuhk.iems5722.a2_1155164941;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ArrayList<ChatRoom> chatRooms=new ArrayList<ChatRoom>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.lvChatrooms);
        ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(this, chatRooms);
        listView.setAdapter(chatRoomAdapter);
        OkHttpClient client = new OkHttpClient();
        Request getChatRooms=new Request.Builder()
                .url("http://18.217.125.61/api/a3/get_chatrooms")
                .get().build();
        client.newCall(getChatRooms).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");

                    if (status.equals("OK")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            String roomName=data.getJSONObject(i).getString("name");
                            String roomId=data.getJSONObject(i).getString("id");
                            ChatRoom chatRoom = new ChatRoom(roomName,roomId);
                            chatRooms.add(chatRoom);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatRoomAdapter.notifyDataSetChanged();
//                        chatRoomAdapter.addAll(chatRooms);
                    }
                });
            }
        });

        // switch to the corresponding chatroom
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ChatRoom chatRoom = (ChatRoom)chatRoomAdapter.getItem(position);
                String roomId = chatRoom.getRoomId();
                String roomName=chatRoom.getRoomName();
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("roomId",roomId);
                bundle.putString("roomName",roomName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}