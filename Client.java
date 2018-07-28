package Chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private String nickName;
    private InetSocketAddress addr;
    private SocketChannel sockChannel;


    public Client(String nickName)  throws IOException {
        this.nickName = nickName;

        // Initiating the socket Channel
        addr = new InetSocketAddress("localhost",4444);
        sockChannel = SocketChannel.open(addr);
        System.out.println("Connected!");

    }


    /**
     * TODO: finish
     * Function that receives global messages.
     * This function receives a global message publicly from everyone
     * @throws IOException
     * */
    public void receiveGlobalMessages() throws IOException, ClassNotFoundException {


    }

    /**
     * TODO: finish
     * Function that sends global messages.
     * This function sends a global message publicly to everyone
     * @throws IOException
     * */
    public void sendGlobalMessage(String str) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.put(str.getBytes());
        buffer.flip();
        this.sockChannel.write(buffer);
        buffer.clear();
    }


    // TODO: Ask for nickname.
    // TODO: Send to server to test for uniqueness. Cannot proceed to messaging until unique.
    // TODO: Once at the messaging stage we need to take message string and send it a message object.
    public static void main(String[] args) throws IOException {

        SocketAddress address = new InetSocketAddress("localhost", 4444);
        SocketChannel client = SocketChannel.open(address);

        ByteBuffer buffer = ByteBuffer.allocate(2048);
        Scanner sc = new Scanner(System.in);


        while(true){
            //buffer.flip();
            client.read(buffer);
            System.out.println(new String(buffer.array()).trim());
            buffer.clear();

            String msg = sc.nextLine();
            buffer.put(msg.getBytes());
            buffer.flip();
            client.write(buffer);
            buffer.clear();

        }

    }
}
