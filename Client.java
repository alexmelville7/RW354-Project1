import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {
  public static void main(String[] args) throws IOException {

    //initiating the socket Channel
    InetSocketAddress addr = new InetSocketAddress("localhost",1234);
    SocketChannel client = SocketChannel.open(addr);


    // TODO: Ask for nickname.
    // TODO: Send to server to test for uniqueness. Cannot proceed to messaging until unique.
    // TODO: Once at the messaging stage we need to take message string and send it a message object.


    // Test that sends "Hello" to server
    String str = "Hello";

    //Creating a buffer
    byte[] message = new String(str).getBytes();
    ByteBuffer buffer = ByteBuffer.wrap(message);

    //Sending buffer to server.
    client.write(buffer);
    buffer.clear();
    buffer = ByteBuffer.allocate(256);
    client.read(buffer);
    System.out.println("SUCCESS");
    client.read(buffer);
    String stri = new String(buffer.array()).trim();
    System.out.print(stri);

    //Reading response from a server.


    //Printing output.

    //client.close();
  }

}
