package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.Scanner;
import java.lang.String;


public class Server {

  Set<String> nickNames;
  Queue<Message> messages;


  Server() throws Throwable {

  }

  /**
  * TODO: finish
  * Function that receivess global messages.
  * This function receives a global message publicly from a user
  * @param sc: this is the socketChannel that the message is passed to.
  * @throws IOException If something goes wrong with I/O
  * */
  private static void receiveGlobalMessage(SocketChannel sc) throws IOException, ClassNotFoundException {

  }

  /**
  * TODO: finish
  * Function that sends global messages.
  * This function sends a global message publicly to everyone
  * @param sc: this is the socketChannel that the message is passed to.
  * @throws IOException If something goes wrong with I/O
  * */
  private void sendGlobalMessages(SocketChannel sc) throws IOException {


  }


  /**
  * TODO: finish
  * Function that receives whispered messages.
  * This function receives a whispered message privately from one person
  * @param sc: this is the socketChannel that the message is passed from.
  * @throws IOException If something goes wrong with I/O
  * */
  private void receiveWhisperMessage(SocketChannel sc) throws IOException, ClassNotFoundException {

  }

  /**
  * TODO: finish
  * Function that sends whispered messages.
  * This function sends a whispered message privately to one person
  * @param sc: this is the socketChannel that the message is passed to.
  * @param msg: the message to be passed.
  * @throws IOException If something goes wrong with I/O
  * */
  private void sendWhisperMessage(SocketChannel sc, Message msg) throws IOException {


  }

  /**
  * Function that tests the uniqueness of a name.
  * @param name: The name to be tested.
  * @return true if name is not already in use on the server.
  * */
  private Boolean isUniqueName(String name) {
    return !this.nickNames.contains(name);
  }


  public static void main(String[] args) throws Throwable {

    Scanner sc = new Scanner(System.in);

    // Setup the server connection
    // Selector is used to communicate with multiple channels.
    Selector selector = Selector.open();

    // Initialising the server socket channel.
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.configureBlocking(false);

    // Bind Port
    InetSocketAddress host_address = new InetSocketAddress(4444);
    ssc.bind(host_address);

    // Register channel
    ssc.register(selector, SelectionKey.OP_ACCEPT);


    while (true) {
      int ready_count = selector.select();
      if(ready_count == 0){
        continue;
      }

      Set<SelectionKey> ready_keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = ready_keys.iterator();
      while (iterator.hasNext()){
        SelectionKey key = iterator.next();
        iterator.remove();

        if(key.isAcceptable()){
          ServerSocketChannel server = (ServerSocketChannel) key.channel();
          // Get client socket channel
          SocketChannel client = server.accept();
          client.configureBlocking(false);

          // Register for read and write operations.
          client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
          continue;
        }
        
        // If readable then server is ready to read.
        if(key.isReadable()){
          SocketChannel client = (SocketChannel) key.channel();

          // Read byte coming from the client.
          int BUFFER_SIZE = 1024;
          ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
          try{
            client.read(buffer);
            String test = new String(buffer.array()).trim();
            if(!test.isEmpty()) {
              System.out.println(test);
              buffer.clear();
            }
          } catch(Exception E) {
            E.printStackTrace();
          }
          continue;
        }

        // If writeable then server is ready to write.
        if(key.isWritable()){
          int BUFFER_SIZE = 2048;
          System.out.println("TEST");
          SocketChannel client = (SocketChannel) key.channel();
          String test = sc.nextLine();
          ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
          buffer.put(test.getBytes());
          buffer.flip();
          client.write(buffer);
          buffer.clear();
          continue;
        }
      }
    }
  }
}
