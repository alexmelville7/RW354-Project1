import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class Server {



  Server() throws Throwable {

  }



  public static void main(String[] args) throws Throwable {


    // Setup the server connection

    Selector selector = Selector.open();

    ServerSocketChannel ssc = ServerSocketChannel.open();
    InetSocketAddress address = new InetSocketAddress(1234);

    ssc.bind(address);
    ssc.configureBlocking(false);

    int ops = ssc.validOps();
    SelectionKey key = ssc.register(selector, ops ,null);

    while (true) {
      selector.select();


      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();

      while (iterator.hasNext()) {
        SelectionKey mykey = iterator.next();

        if (mykey.isAcceptable()) {
          SocketChannel client = ssc.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_READ);

        } else if (mykey.isReadable()) {
          SocketChannel client = (SocketChannel) mykey.channel();
          ByteBuffer buffer = ByteBuffer.allocate(256);
          client.read(buffer);

          String stri = new String(buffer.array()).trim();

          if (stri.equals("Hello")) {
            System.out.println(stri+" World!");
            byte[] message = new String("abcd").getBytes();
            ByteBuffer buffer2 = ByteBuffer.wrap(message);
            client.write(buffer2);
            client.close();
          }

        }
        iterator.remove();
      }

    }
  }

}

/*
* int number;
ServerSocket s1 = new ServerSocket(1342);
Socket ss = s1.accept();
Scanner sc = new Scanner(ss.getInputStream());

number = sc.nextInt();

number = number *2;
PrintStream p = new PrintStream(ss.getOutputStream());
p.println(number);
* */
