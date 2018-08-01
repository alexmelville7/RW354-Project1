package Chat;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.lang.String;
import Chat.Client;



public class Server {

    private static Map<String, SocketChannel> nickNames = new HashMap<>();
    private static Queue<Message> messages = new LinkedList<Message>();
    private static int BUFFER_SIZE = 1024;


    Server() throws Throwable {

    }

    public static Map<String, SocketChannel> getNickNames() {
        return nickNames;
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
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    // Get client socket channel
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);

                    // Register for read and write operations.
                    client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                    Message m1 = Message.ServerRecieve(client);
                    System.out.println("Message Name: " + m1.getSender());

                    if(isUniqueName(m1.getSender())) {
                        String msg = "Successful";
                        Message m = new Message(msg, m1.getSender(), "GLOBAL");
                        m.ServerSend(client);

                        nickNames.put(m1.getSender(), client);
                    } else {
                        String msg = "Unsuccessful";
                        Message m = new Message(msg, m1.getSender(), "GLOBAL");
                        m.ServerSend(client);
                    }

                    continue;
                }

                // If readable then server is ready to read.
                if(key.isReadable()){
                    SocketChannel client = (SocketChannel) key.channel();

                    try{
                        Message msg = Message.ServerRecieve(client);
                        messages.add(msg);
                        if(!msg.getMessage().isEmpty()) {
                            System.out.println(msg.getSender()+ ": "+ msg.getMessage());
                        }
                    } catch(Exception E) {
                        System.out.println("ERROR Reading.");
                    }
                }
                // If write able then server is ready to write.
                // This will be changed to a method that writes the data to another client.
                else if(key.isWritable()){
                    if(!messages.isEmpty()){
                        Message msg  = messages.remove();
                        if(msg.getReceiver().equals("GLOBAL")){
                            Iterator iter = nickNames.entrySet().iterator();
                            while(iter.hasNext()){
                                Map.Entry client = (Map.Entry)iter.next();

                                if (!client.getKey().equals(msg.getSender())) {
                                    SocketChannel cli = (SocketChannel) client.getValue();
                                    msg.ServerSend(cli);
                                }
                            }
                        } else {
                            msg.ServerSend(nickNames.get(msg.getReceiver()));
                        }
                    }
                }
            }
        }
    }
}

