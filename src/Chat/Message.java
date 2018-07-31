package Chat;

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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();

            byte[] bytes = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.flip();
            sc.write(buffer);
            buffer.clear();
        } catch(Exception E){
            System.out.println("ERROR: Sending from Server  - " + E);
        }

        finally
        {
            try {
                bos.close();
            } catch (Exception E) {
                System.out.println("ERROR: Closing bos - Sending from Server  - " + E);
            }
        }
    }

    public static Message ServerRecieve(SocketChannel client){
        try{
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            client.read(buffer);
        } catch(Exception E) {
            System.out.println("ERROR: Server Receiving read buffer - " + E);
        }

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
        ObjectInput in = null;
        Message msg = null;

        try {
            in = new ObjectInputStream(bis);
            msg = (Message) in.readObject();
        } catch(Exception E) {
            System.out.println("ERROR: Receiving from Server  - " + E);
        }
        finally
        {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception E){
                System.out.println("ERROR: Closing bos - Receiving from Server  - " + E);
            }
        }
        return msg;
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
            buffer.flip();
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
