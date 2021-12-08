package hk.edu.cuhk.iems5722.a2_1155164941;

public class ChatRoom {
    private String roomName;
    private String roomId;

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String rooomName) {
        this.roomName = roomName;
    }

    public ChatRoom(String roomName,String roomId) {
        this.roomName = roomName;
        this.roomId=roomId;
    }
}
