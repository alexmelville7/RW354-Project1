package Chat;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
  private static String nickName = "Michael";
  private static InetSocketAddress addr;
  private static SocketChannel sockChannel;
  private static int BUFFER_SIZE = 1024;


  public Client(String nickName)  throws IOException {
    this.nickName = nickName;

    // Initiating the socket Channel
    addr = new InetSocketAddress("localhost",4444);
    sockChannel = SocketChannel.open(addr);
    sockChannel.configureBlocking(false);
  }

  public static String getNickName() {
    return nickName;
  }

  public static SocketChannel getSockChannel() {
    return sockChannel;
  }

  /**
  * TODO: finish
  * Function that sends global messages.
  * This function sends a global message publicly to everyone
  * @throws IOException
  * */
  public void sendGlobalMessage(String str) throws IOException {
    //        ByteBuffer buffer = ByteBuffer.allocate(2048);
    /*buffer.put(str.getBytes());
    buffer.flip();
    this.sockChannel.write(buffer);
    buffer.clear();*/

    // Create a new message object
    Message msg = new Message(str, this.nickName, "GLOBAL");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(msg);
      out.flush();
      byte[] bytes = bos.toByteArray();
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      this.sockChannel.write(buffer);
      buffer.clear();
    } finally {
      try {
        bos.close();
      } catch (IOException ex) {

      }
    }
  }

  public boolean sendNickName(){
    try{
      Message m = new Message(nickName, "Michael", "GLOBAL");
      m.ServerSend(sockChannel);
    } catch(Exception E) {
      System.out.println("Error: Sending nickName  " + E);
    }


    //Receive Data.
    try{
      Message m = Message.ServerReceive(sockChannel);
      System.out.println(m.getMessage());
      if(m.getMessage().equals("Successful")){
        System.out.println("Connected to Server.");
        return true;
      } else {
        System.out.println("Unable to connect to Server. Nickname is already in use.");
        return false;
      }
    } catch(Exception E) {
      System.out.println("Error: Receiving Nickname:  " + E);
    }
    return false;
  }

}
