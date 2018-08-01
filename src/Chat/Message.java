package Chat;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import jdk.nashorn.internal.objects.NativeJSON;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.*;
import java.nio.channels.*;

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
    public void ServerSend(SocketChannel sc) {
        Message msg = new Message(this.message, this.sender, "GLOBAL");

        try {
            ObjectOutputStream  oos = new ObjectOutputStream(Channels.newOutputStream(sc));
            oos.writeObject(msg);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static Message ServerReceive(SocketChannel sc) {
        Message m = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(sc));

            m = Message.class.cast(ois.readObject());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return m;
    }
}


//ServerSend
/*
Message msg = new Message(this.message, this.sender, "GLOBAL");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
            out = new ObjectOutputStream(bos);
            out.writeObject(msg);
            out.flush();
            byte[] bytes = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            sc.write(buffer);
            buffer.clear();
            } catch(Exception E){

            }
            finally
            {
            try {
            bos.close();
            } catch (IOException ex) {

            }
            }
*/


//ServerRecieve
/*
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    try{

            client.read(buffer);
            } catch(Exception E) {
            System.out.println("ERROR: Server Receiving read buffer - " + E);
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
            ObjectInput in = null;
            Message msg = null;

            try {
            in = new ObjectInputStream(bis);
            msg = (Message) in.readObject();
            } catch(Exception E){
            System.out.println("ERROR: Receiving from Server  - " + E);

            }finally {
            try {
            if (in != null) {
            in.close();
            }
            } catch (Exception E){
            System.out.println("ERROR: Closing bos - Receiving from Server  - " + E);
            }
            }
            return msg;
*/

//ClientSend
/*

    Message msg = new Message(this.message, this.sender, "GLOBAL");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(msg);
      out.flush();
      byte[] bytes = bos.toByteArray();
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      sc.write(buffer);
      buffer.clear();
    } catch(Exception E) {
      System.out.println("ERROR: sending from client to server  -  " +  E);
    }
    finally {
      try {
        bos.close();
      } catch (IOException ex) {

      }
    }

  }
 */