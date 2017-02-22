package nu.annat.simplesqlexample;

public class Message {
    private final int id;
    private final long timestamp;
    private final String user;
    private final String text;

    public Message(int id, long timestamp, String user, String text) {
        this.text = text;
        this.user = user;
        this.timestamp = timestamp;
        this.id = id;
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
