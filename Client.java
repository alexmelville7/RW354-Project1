import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {

  public static void main(String[] args) throws IOException {

    InetSocketAddress addr = new InetSocketAddress("localhost",1234);
    SocketChannel client = SocketChannel.open(addr);
    
    // Test that sends "Hello" to server
    String str = "Hello";

    byte[] message = new String(str).getBytes();
    ByteBuffer buffer = ByteBuffer.wrap(message);
    client.write(buffer);
    buffer.clear();

    buffer = ByteBuffer.allocate(256);
    client.read(buffer);

    String stri = new String(buffer.array()).trim();

    System.out.println(stri);


    client.close();


  }

}


/*
int number = 42;
Socket s = null;
try {
s = new Socket("127.0.0.1", 1342);
} catch (IOException e) {
e.printStackTrace();
}
Scanner sc = new Scanner(s.getInputStream());
PrintStream p = null;

p = new PrintStream(s.getOutputStream());


p.println(number);
int temp = sc.nextInt();
System.out.println(temp);
*/
