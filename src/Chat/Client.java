package Chat;
import GUI.ChatGUI;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {
    private static String nickName = "Michael";
    private static InetSocketAddress addr;
    private static SocketChannel client;
    private static SocketChannel sockChannel;
    private static SocketAddress address = new InetSocketAddress("localhost", 4444);
    private static int BUFFER_SIZE = 1024;


    //Main Method
    // TODO: Ask for nickname.
    // TODO: Send to server to test for uniqueness. Cannot proceed to messaging until unique.
    // TODO: Once at the messaging stage we need to take message string and send it a message object.
    public static void main(String[] args) throws IOException {

        //Send(client);
//        Receive(client);
        //If you close the client now, the client will instantly close because it's a thread.
        //client.close();
    }

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

    /**
     * TODO: finish
     * Function that receives global messages.
     * This function receives a global message publicly from everyone
     * @throws IOException
     * */
    private Message receiveGlobalMessage(SocketChannel sc, byte[] bytes) throws IOException, ClassNotFoundException {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = null;
            Message msg = null;

            try {
                in = new ObjectInputStream(bis);
                msg = (Message) in.readObject();
            }

            catch(Exception E){
                System.out.println("Error receiving message: " + E);
            }

            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception E){
                    System.out.println("Error receiving message: NESTED: " + E);
                }
            }
            return msg;
    }

    public boolean sendNickName(){
        try{
            Message m = new Message(nickName, "Michael", "GLOBAL");
        } catch(Exception E) {
            System.out.println("Error: Sending nickName  " + E);
        }

        //Receive Data.
        try{
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            Message m = Message.ServerRecieve(sockChannel);

            if(m.getMessage().equals("Successful")){
                System.out.println("Connected to Server.");
                return true;
            } else {
                System.out.println("Unable to connect to Server. Nickname is already in use.");
                return false;
            }
        } catch(Exception E) {
            System.out.println("Error: Receiving Nickname:  " + E);
        }
        return false;
    }
}
