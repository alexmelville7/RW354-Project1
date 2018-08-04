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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

import Chat.Client;

import javax.swing.*;


public class Server {

    private static Map<String, SocketChannel> nickNames = new HashMap<>();
    private static Map<String, Lock> nickLocks = new HashMap<>();
    private static Queue<Message> messages = new ConcurrentLinkedQueue<Message>();
    private static int BUFFER_SIZE = 1024;
    private static Selector selector = null;
    private static int count = 0;
    private static Lock l1 = new ReentrantLock();


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

        // Setup the server connection
        // Selector is used to communicate with multiple channels.
        selector = Selector.open();

        // Initialising the server socket channel.
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // Bind Port
        InetSocketAddress host_address = new InetSocketAddress(4444);
        ssc.bind(host_address);

        // Register channel
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(" - Server Running");


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
                count++;
                thread(key);

                while(count != 0){
                    System.out.flush();
                };
            }
        }
    }

    public static void GlobalSend(Message msg){
        Iterator iter = nickNames.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry client = (Map.Entry) iter.next();
            nickLocks.get(client.getKey()).lock();
            if (!client.getKey().equals(msg.getSender())) {
                SocketChannel cli = (SocketChannel) client.getValue();
                msg.ServerSend(cli);
            }
            nickLocks.get(client.getKey()).unlock();
        }
    }

    public static void thread(SelectionKey key){
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                if(key.isAcceptable()) Accept(key);
                // If readable then server is ready to read.
                else if(key.isReadable()) Read(key);
                // If able to write then server is ready to write.
                else if(key.isWritable()) Write();
                l1.lock();
                count--;
                l1.unlock();
                return;
            }
        });
        t1.start();
    }

    public static void Accept(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        // Get client socket channel
        SocketChannel client= null;
        try{
            client = server.accept();
            client.configureBlocking(false);
            // Register for read and write operations.
            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (Exception E) {
            System.out.println("Server could not connect to client - " + E);
        }

        Message m1 = Message.ServerRecieve(client);
        System.out.print(" - User: " + m1.getSender() + " --> Requesting connection to Server --> ");

        if(isUniqueName(m1.getSender())) {
            nickLocks.put(m1.getSender(), new ReentrantLock());
            String msg = "Successful";
            System.out.println(msg);
            Message m = new Message(msg, m1.getSender(), "GLOBAL");
            m.ServerSend(client);
            m = new Message(m1.getSender(), "USER_ADD");
            GlobalSend(m);
            nickNames.put(m1.getSender(), client);
            m = new Message(nickNames.keySet().toString(), "USER_ADD_LIST");
            m.ServerSend(client);


        } else {
            String msg = "Unsuccessful";
            System.out.println(msg);
            Message m = new Message(msg, m1.getSender(), "GLOBAL");
            m.ServerSend(client);
        }
    }

    public static void Read(SelectionKey key){
        SocketChannel client = (SocketChannel) key.channel();

        try{
            if(client.isConnected()){
                Message msg = Message.ServerRecieve(client);
                if(msg.getMessage() == null && msg.getMessageType().equals("USER_ADD_LIST")){
                    Message m = new Message(nickNames.keySet().toString(), "USER_ADD_LIST");
                    m.ServerSend(client);
                }  else if (msg.getMessageType().equals("DISCONN")) {
                    System.out.println(msg.getMessage()+ " disconnected");
                    GlobalSend(msg);
                    key.cancel();
                    key.channel().close();
                    nickNames.remove(msg.getMessage());
                } else {
                    messages.add(msg);
                }
            }
            // TODO disconnect
            /*if(!msg.getMessageType().equals("DISCONN")) {
                System.out.println(msg.getMessage()+ " disconnected");
                key.cancel();
                key.channel().close();
            }*/

        } catch(Exception E) {
            System.out.println("ERROR Reading.");
        }
    }

    public static void Write() {
        if(!messages.isEmpty()){
            Message msg  = messages.remove();
            if(msg.getReceiver().equals("GLOBAL")){
                GlobalSend(msg);
            }
            else {
                msg.ServerSend(nickNames.get(msg.getReceiver()));
            }
        }
    }


}

