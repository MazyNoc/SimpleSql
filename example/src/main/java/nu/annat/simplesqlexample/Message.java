package nu.annat.simplesqlexample;

public class Message {
    private  int id;
    private  long timestamp;
    private  String user;
    private  String text;

    public Message() {
    }

    public Message(int id, long timestamp, String user, String text) {
        this.text = text;
        this.user = user;
        this.timestamp = timestamp;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", text='" + text + '\'' +
                '}';
    }


}
