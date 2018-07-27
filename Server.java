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

  Set<String> nickNames;


  Server() throws Throwable {

  }

  public static void main(String[] args) throws Throwable {

    // Setup the server connection
    //Selector is used to communicate with multiple channels.
    Selector selector = Selector.open();

    //Initialising the server socket channel.
    ServerSocketChannel ssc = ServerSocketChannel.open();
    InetSocketAddress address = new InetSocketAddress(1234);
    ssc.bind(address);
    ssc.configureBlocking(false);
    int ops = ssc.validOps();
    SelectionKey key = ssc.register(selector, ops, null);

    while (true) {

      //This stores available keys in a set.
      //TODO: Set may not be necessary, a different datatype might be possible.
      selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();

      //TODO: Iterate a different way.
      //Iterates through the keys.
      while (iterator.hasNext()) {
        SelectionKey mykey = iterator.next();

        //Connects to socket if possible.
        if (mykey.isAcceptable()) {
          System.out.println("acceptable");
          SocketChannel client = ssc.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_READ);

          // TODO: maybe print 'connect' message


        }
        //Checks whether socket channel is ready to be read.
        else if (mykey.isReadable()) {
          SocketChannel client = (SocketChannel) mykey.channel();
          ByteBuffer buffer = ByteBuffer.allocate(256);
          client.read(buffer);
          String str = new String(buffer.array()).trim();
          System.out.println(str);

          //TODO: Add protocols class.
          if (str.equals("Bye")) {
            client.close();
          }
        }
        //Checks whether socket channel is ready to write.
        else if(mykey.isWritable()){
          SocketChannel client = (SocketChannel) mykey.channel();
          String str = "HIIII";
          byte[] message = str.getBytes();
          ByteBuffer buffer = ByteBuffer.wrap(message);
          buffer.flip();
          client.write(buffer);

          //TODO: What is the point of closing if you are just going to connect again?
          if(str.equals("Bye")) {
            // TODO: print disconnect message
            client.close();
          }
        }



        iterator.remove();
      }

    }
  }

}
