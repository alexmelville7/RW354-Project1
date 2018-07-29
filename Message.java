package Chat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private final String message;
    private final Date date;
    private final String sender;
    private final String receiver;

    Message(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getDate() {
        return date;
    }
}
