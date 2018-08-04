package Chat;

import org.omg.CORBA.portable.InputStream;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

    private final String message;
    private final String messageType;
    private final Date date;
    private final String sender;
    private final String receiver;
    private static int BUFFER_SIZE = 1024;


    /******************************************** Constructors ********************************************/

    public Message(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date();
        this.messageType = "MESSAGE";
    }

    // Used to update list of old users.
    public Message(String user, String type) {
        this.message = user;
        this.receiver = "";
        this.sender = "";
        this.date = new Date();
        this.messageType = type;
    }

    /******************************************** Communication Functions ********************************************/

    /**
     * Function that is used by the server to send a message
     * @param sc - The socket channel that message is sent on
     * */
    public void ServerSend(SocketChannel sc){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try{
            oos = new ObjectOutputStream(baos);
        } catch (Exception E) {
            System.out.println("Error - Server Send - Could not create output stream - " + E);
        }

        buffer.clear();

        try{
            oos.writeObject(this);
            buffer = ByteBuffer.wrap(baos.toByteArray());
            sc.write(buffer);
            oos.flush();
            baos.flush();
        } catch(Exception E) {
            System.out.println("Error - Server Send - could not parse object - " + E);
        }

        buffer.clear();
    }

    /**
     * Function that is used by the server to receive a message
     * @param sc - The socket channel that message is sent on
     * */
    public static Message ServerRecieve(SocketChannel sc){
        Message m = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        while(m == null) {
            int bytesRead = 0;
            try {
                bytesRead = sc.read(buffer);
            } catch (Exception E) {
                System.out.println("Error - Server Receive - could not read from socketchannel - " + E);
            }

            try {
                if (bytesRead > 0) {
                    buffer.flip();
                    ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    m = (Message) ois.readObject();
                    ois.close();
                }
            } catch (Exception E) {
                System.out.println("Error - Server Receive - could not read from output stream - " + E);
            }
        }
        //buffer.clear();
        return m;
    }


    /**
     * Function that is used by the client to send a message
     * @param sc - The socket channel that message is sent on
     * */
    public void ClientSend(SocketChannel sc, JFrame frame, JFrame frame2){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();

            byte[] bytes = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            sc.write(buffer);
            buffer.clear();
        } catch(Exception E) {
            System.out.println("ERROR: Sending Client side - " + E);
            /*frame.setVisible(true);
            frame2.setVisible(false);
            frame2.dispose();*/
        }
        finally
        {
            try {
                bos.close();
            } catch (IOException ex) {
                System.out.println("ERROR: Sending Message from Client");
            }
        }
    }


    /******************************************** Getters and Setters ********************************************/

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getDate() {
        return date;
    }

    public String getMessageType() {
        return messageType;
    }

}
