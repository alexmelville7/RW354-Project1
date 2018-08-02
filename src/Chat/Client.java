package Chat;
import GUI.ChatGUI;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {
    private static String nickName;
    private static InetSocketAddress addr;
    private static SocketChannel client;
    private static SocketChannel sockChannel;
    private static SocketAddress address = new InetSocketAddress("localhost", 4444);
    private static int BUFFER_SIZE = 1024;


    //Constructor
    public Client(String nickName)  throws IOException {
        this.nickName = nickName;

        // Initiating the socket Channel
        addr = new InetSocketAddress("localhost",4444);
        sockChannel = SocketChannel.open(addr);
        sockChannel.configureBlocking(false);
    }

    public SocketChannel getSockChannel() {
        return sockChannel;
    }

    //Getters and Setters
    public static String getNickName() {
        return nickName;
    }

    public boolean sendNickName(){
        try{
            Message m = new Message("", nickName, "GLOBAL");
            m.ServerSend(sockChannel);
        } catch(Exception E) {
            System.out.println("Error: Sending nickName  " + E);
        }

        //Receive Data.
        try{
            Message m = null;
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            m = Message.ServerRecieve(sockChannel);

            System.out.println(m.getMessage());

            if(m.getMessage().equals("Successful")){
                System.out.println("Connected to Server.");
                return true;
            } else {
                System.out.println("Unable to connect to Server. Nickname is already in use.");
            }
        } catch(Exception E) {
            System.out.println("Error: Receiving Nickname:  " + E);
        }
        return false;
    }
}
