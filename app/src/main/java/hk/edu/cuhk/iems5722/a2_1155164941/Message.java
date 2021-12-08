package hk.edu.cuhk.iems5722.a2_1155164941;

public class Message {
    private String userName;
    private String message;
    private String time;
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() { return type; }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getUserName() { return userName;}


    public Message(String userName,String message, String time){
        this.userName=userName;
        this.message=message;
        this.time=time;
    }
}
