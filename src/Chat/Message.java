package Chat;

import org.omg.CORBA.portable.InputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class Message implements Serializable {

    private final String message;
    private final Date date;
    private final String sender;
    private final String receiver;
    private static int BUFFER_SIZE = 1024;

    public Message(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date();
    }

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



    //Communication Functions
    public void ServerSend(SocketChannel sc){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try{
            oos = new ObjectOutputStream(baos);
        } catch (Exception E) {
            System.out.println("Error - Server Send - Could not create output stream - " + E);
            return;
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
            return;
        }
    }

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
        buffer.clear();
        return m;
    }

    public void ClientSend(SocketChannel sc){

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

}
