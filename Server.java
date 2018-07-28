package com.company;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.Scanner;
import java.lang.String;


public class Server {
    public static void main(String[] args) throws Throwable {

        Scanner sc = new Scanner(System.in);

        // Setup the server connection
        // Selector is used to communicate with multiple channels.
        Selector selector = Selector.open();

        // Initialising the server socket channel.
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        //Bind Port
        InetSocketAddress host_address = new InetSocketAddress(4444);
        ssc.bind(host_address);

        //Register channel
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
                    //get client socket channel
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);

                    //register for read and write operations.
                    client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    continue;
                }

                //if readable then server is ready to read.
                if(key.isReadable()){
                    int BUFFER_SIZE = 1024;
                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                    SocketChannel client = (SocketChannel) key.channel();

                    try{
                        client.read(buffer);
                        String test = new String(buffer.array()).trim();
                        if(!test.isEmpty()) {
                            System.out.println("Client: " + test);
                        }
                        buffer.clear();
                    } catch(Exception E) {
                        E.printStackTrace();
                    }
                }

                //if write able then server is ready to write.
                //This will be changed to a method that writes the data to another client.
                 else if(key.isWritable()){
                    int BUFFER_SIZE = 1024;
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
