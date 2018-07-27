import java.util.Date;

public class Message {

  private final String message;
  private final String nickname;
  private final Date date;

  Message(String message, String username) {
    this.message = message;
    this.nickname = username;
    this.date = new Date();
  }

  public String getMessage() {
    return message;
  }

  public String getNickname() {
    return nickname;
  }

  public Date getDate() {
    return date;
  }
}

/*
Plagiarism declaration:

https://crunchify.com/java-nio-non-blocking-io-with-server-client-example-java-nio-bytebuffer-and-channels-selector-java-nio-vs-io/
*/
