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
  private static String nickName;
  private static InetSocketAddress addr;
  private static SocketChannel sockChannel;
  private static SocketAddress address = new InetSocketAddress("localhost", 4444);


  public Client(String nickName)  throws IOException {
    this.nickName = nickName;

    // Initiating the socket Channel
    addr = new InetSocketAddress("localhost",4444);
    sockChannel = SocketChannel.open(addr);
    System.out.println("Connected!");
  }

  private void sendMessage(String str) throws IOException {
    // Creating a buffer
    byte[] message = str.getBytes();
    ByteBuffer buffer = ByteBuffer.wrap(message);
  }
  /**
  * TODO: finish
  * Function that receives global messages.
  * This function receives a global message publicly from everyone
  * @throws IOException
  * */
  public void receiveGlobalMessages() throws IOException, ClassNotFoundException {


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


  // TODO: Ask for nickname.
  // TODO: Send to server to test for uniqueness. Cannot proceed to messaging until unique.
  // TODO: Once at the messaging stage we need to take message string and send it a message object.
  public static void main(String[] args) throws IOException {

    SocketAddress address = new InetSocketAddress("localhost", 4444);
    SocketChannel client = SocketChannel.open(address);
    client.configureBlocking(false);
    Send(client);
    Receive(client);
    //If you close the client now, the client will instantly close because it's a thread.
    //client.close();
  }

  //This is a threaded send method.
  private static void Send(SocketChannel client){
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        Scanner sc = new Scanner(System.in);
        while(true){
          //Sending
          int BUFFER_SIZE = 1024;
          ByteBuffer buffer1 = ByteBuffer.allocate(BUFFER_SIZE);
          buffer1.clear();
          System.out.print("Client: ");
          String msg = sc.nextLine();
          buffer1.put(msg.getBytes());
          buffer1.flip();
          try {
            client.write(buffer1);
          } catch(Exception E){
            System.out.println("Error: " + E);
          }

          if(msg.equals("bye")) break;
          buffer1.clear();
        }
      }
    });
    t1.start();
  }

  //This is a threaded Recieve method.
  private static void Receive(SocketChannel client){
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        Scanner sc = new Scanner(System.in);
        while(true) {
          //Receiving
          int BUFFER_SIZE = 1024;
          ByteBuffer buffer2 = ByteBuffer.allocate(BUFFER_SIZE);
          try{
            client.read(buffer2);
          }
          catch(Exception E){
            System.out.println("ERROR: " + E);
          }

          String test = new String(buffer2.array()).trim();
          if(!test.isEmpty()) {
            System.out.println("Server: "+ test);
          }
          buffer2.clear();
          if(test.equals("bye")) break;
        }
      }
    });
    t1.start();
  }
}
