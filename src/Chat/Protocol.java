package Chat;


import javax.swing.*;
import java.nio.channels.SocketChannel;

public class Protocol {
    public static boolean isNickName(String name, JLabel label){
        if(name.equals("")){
            label.setText("You cannot have an empty nickname as your nickname.");
            return false;
        } else if(name.contains(",")){
            label.setText("Comma's in a nickname is not allowed.");
            return false;
        } else if(name.length() > 20){
            label.setText("Name must be shorter than 20 characters in length.");
            return false;
        }
        return true;
    }
    //This is to check whether the userList is received. This will be moved soon.
    public static boolean isReceived(String names, Client client){
        if(names.equals("")){
            Message m = new Message(client.getNickName(), "USER__ADD_LIST");
            return false;
        }
        return true;
    }
}
