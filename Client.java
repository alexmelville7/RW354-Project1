import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class Client {


  private String nickName;
  private InetSocketAddress addr;
  private SocketChannel sockChannel;


  Client(String nickName)  throws IOException {
    this.nickName = nickName;

    // Initiating the socket Channel
    addr = new InetSocketAddress("localhost",1234);
    sockChannel = SocketChannel.open(addr);

  }





  public static void main(String[] args) throws IOException {



    String testnickname = "test"; // TODO: Ask for nickname.
    // TODO: Send to server to test for uniqueness. Cannot proceed to messaging until unique.
    // TODO: Once at the messaging stage we need to take message string and send it a message object.
    // ObjectOutputStream() ?


    // Test that sends "Hello" to server
    String str = "Hello";
    Client client = new Client(testnickname);
    client.sendMessage(str);



    // Reading response from a server.
    ByteBuffer buffer2 = ByteBuffer.allocate(256);
    client.sockChannel.read(buffer2);
    System.out.println("SUCCESS");
    client.sockChannel.read(buffer2);

    String stri = new String(buffer2.array()).trim();
    System.out.print(stri);

    // Printing output.

    client.sockChannel.close();
  }


  private void sendMessage(String str) throws IOException {
    // Creating a buffer
    byte[] message = new String(str).getBytes();
    ByteBuffer buffer = ByteBuffer.wrap(message);

    //Sending buffer to server.
    sockChannel.write(buffer);
    buffer.clear();
  }



}
