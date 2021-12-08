package hk.edu.cuhk.iems5722.a2_1155164941;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private int totalPages;
    private int currentPage;
    ArrayList<Message> messagesArray=new ArrayList<>();

    void asyncGetPage(OkHttpClient httpClient, Request request, MessageAdapter messageAdapter){
        // make http request asynchronously
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            // parse response
            // update UI components in UI thread
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("OK")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray messages = data.getJSONArray("messages");
                        totalPages=data.getInt("total_pages");
                        currentPage=data.getInt("current_page");
//                        if (currentPage==1){
//                            messagesArray.clear();
//                        }
                        for (int i = 0; i <messages.length(); i++) {
                            JSONObject message = messages.getJSONObject(i);
                            String messageContent = message.getString("message");
                            String messageTime = message.getString("message_time");
                            String userName = message.getString("name");
                            String userId=message.getString("user_id");
                            Message messageObject = new Message(userName, messageContent, messageTime);
                            if (userId.equals("1155164941")){
                                messageObject.setType(0);
                            }else {
                                messageObject.setType(1);
                            }
                            messagesArray.add(0,messageObject);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                            messageAdapter=new MessageAdapter(ChatActivity.this,messagesArray);
                            messageAdapter.notifyDataSetChanged();
//                        listView.setAdapter(messageAdapter);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //get chatroom meta data
        Bundle extras = this.getIntent().getExtras();
        String roomId = extras.getString("roomId");
        String roomName = extras.getString("roomName");



//      switch to main activity
        Toolbar toolbar1 = findViewById(R.id.tbRoom);
        toolbar1.setTitle(roomName);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // initiate listview related components
        ListView messagesView = findViewById(R.id.lvMsg);
        MessageAdapter messageAdapter=new MessageAdapter(this,messagesArray);
        messagesView.setAdapter(messageAdapter);
        //initiate http client and request
        OkHttpClient client = new OkHttpClient();
        Request getFirstPage = new Request.Builder()
                .url("http://18.217.125.61/api/a3/get_messages?chatroom_id=" + roomId + "&page=1")
                .get().build();
        Request getSecondPage = new Request.Builder()
                .url("http://18.217.125.61/api/a3/get_messages?chatroom_id=" + roomId + "&page=2")
                .get().build();
        //get the first page
        asyncGetPage(client,getFirstPage,messageAdapter);
        //get the second page
//        asyncGetPage(client,getSecondPage,messageAdapter);
        // scroll and refresh
        messagesView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean top=false;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState==SCROLL_STATE_IDLE){
                    if (top==true){
                        if (currentPage <totalPages){
                            Request getHistoryPage=new Request.Builder()
                                    .url("http://18.217.125.61/api/a3/get_messages?chatroom_id=" + roomId + "&page="+(currentPage +1))
                                    .get().build();
                            asyncGetPage(client,getHistoryPage,messageAdapter);
                        }
                        top=false;
                    }
//                    if (bottom==true){
//                        if (currentPage>1){
//                            Request getHistoryPage=new Request.Builder()
//                                    .url("http://18.217.125.61/api/a3/get_messages?chatroom_id=" + roomId + "&page="+(currentPage -1))
//                                    .get().build();
//                            asyncGetPage(client,getHistoryPage,messageAdapter);
//                        }
//                        bottom=false;
//                    }
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((visibleItemCount>0)&&(firstVisibleItem==0)){
                    // scrolled to the top of listview
                    Log.d("messagesView","scrolled to the top");
                    top=true;
                }
//                if (absListView.getLastVisiblePosition()==totalItemCount-1 && totalItemCount>0){
//                    // scrolled to the bottom of listview
//                    Log.d("messagesView","scrolled to the bottom");
//                    bottom=true;
//                }
            }
        });



//      click button_send and send message
        Button buttonSend = findViewById(R.id.btnSend);
        EditText inputText=findViewById(R.id.textInput);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=inputText.getText().toString();
                if(!msg.trim().isEmpty()){
                    inputText.getText().clear();

                    FormBody formBody=new FormBody.Builder()
                            .add("chatroom_id",roomId)
                            .add("user_id","1155164941")
                            .add("name","Alice")
                            .add("message",msg)
                            .build();

                    Request postMessages=new Request.Builder()
                            .url("http://18.217.125.61/api/a3/send_message")
                            .post(formBody)
                            .build();

                    client.newCall(postMessages).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String s = response.body().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String status = jsonObject.getString("status");
                                if (status.equals("ERROR")){
                                    Log.d("postMessageStatus",status);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        // click refresh and refresh message
        ImageButton buttonRefresh = findViewById(R.id.refreshButton);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesArray.clear();
                asyncGetPage(client,getFirstPage,messageAdapter);
//                asyncGetPage(client,getSecondPage,messageAdapter);
            }
        });
    }
}
