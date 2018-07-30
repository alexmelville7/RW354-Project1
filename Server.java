package Chat;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.lang.String;



public class Server {

  private static Map<String, SocketAddress> nickNames = new HashMap<>();
  private static Queue<Message> messages;
  private static int BUFFER_SIZE = 1024;


  Server() throws Throwable {

  }

  public static Map<String, SocketAddress> getNickNames() {
    return nickNames;
  }

  /**
  * TODO: finish
  * Function that receivess global messages.
  * This function receives a global message publicly from a user
  * @param bytes byte array
  * @throws IOException If something goes wrong with I/O
  * */
  private static Message receiveGlobalMessage(byte[] bytes) throws IOException, ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    ObjectInput in = null;
    Message msg = null;

    try {
      in = new ObjectInputStream(bis);
      msg = (Message) in.readObject();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException ex){

      }
    }
    return msg;
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
  private static Boolean isUniqueName(String name) {
    //        System.out.println(name);
    return (nickNames.get(name) == null);
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
          // Get client socket channel
          ServerSocketChannel server = (ServerSocketChannel) key.channel();
          SocketChannel client = server.accept();
          client.configureBlocking(false);

          // Register for read and write operations.
          client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

          // Checking NickName
          ByteBuffer buffer1 = ByteBuffer.allocate(BUFFER_SIZE);
          ByteBuffer buffer2 = ByteBuffer.allocate(BUFFER_SIZE);
          client.read(buffer1);
          String name = new String(buffer1.array()).trim();

          // TODO for testing, you can remove it when the map is populated
          nickNames.put("test", client.getLocalAddress());

          if(isUniqueName(name)) {
            String msg = "Successful";
            buffer2.put(msg.getBytes());
            buffer2.flip();
            client.write(buffer2);
            buffer2.clear();

            //TODO: Test when have multiple clients.
            nickNames.put(name, client.getLocalAddress());
          } else {
            String msg = "Unsuccessful";
            buffer2.put(msg.getBytes());
            buffer2.flip();
            client.write(buffer2);
            buffer2.clear();
            client.close();
          }
          continue;
        }

        // If readable then server is ready to read.
        if(key.isReadable()){
          ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
          SocketChannel client = (SocketChannel) key.channel();

          try{
            client.read(buffer);
            //String test = new String(buffer.array()).trim();
            Message msg = receiveGlobalMessage(buffer.array());
            if(!msg.getMessage().isEmpty()) {
              System.out.println(msg.getSender()+": " + msg.getMessage());
            }
            buffer.clear();
          } catch(Exception E) {
            E.printStackTrace();
          }
        }
        // If write able then server is ready to write.
        // This will be changed to a method that writes the data to another client.
        else if(key.isWritable()){
          ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
          System.out.print("Server: ");
          SocketChannel client = (SocketChannel) key.channel();
          String test = sc.nextLine();
          buffer.put(test.getBytes());
          buffer.flip();
          client.write(buffer);
          buffer.clear();
        }
      }
    }
  }
}
