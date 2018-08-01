package GUI;

import Chat.Client;
import Chat.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChatGUI {


    public JPanel Main;
    private JTextField textField1;
    private JList UserList;
    private JTextArea ChatArea;
    private JButton sendButton;
    private JTabbedPane tabbedPane1;
    private JScrollBar scrollBar1;
    private JLabel WelcomeLabel;


    public ChatGUI(Client cli) {
        //Receive(cli.getSockChannel());
        WelcomeLabel.setText(WelcomeLabel.getText() + "text u want to append");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);
                ChatArea.append(message + "\n");
                try {
                    Message m = new Message(message, cli.getNickName(), "GLOBAL");
                    m.ClientSend(cli.getSockChannel());
                } catch (Exception E) {
                    E.printStackTrace();
                }
                textField1.setText("");
            }
        });

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);
                ChatArea.append(message + "\n");
                try {
                    Message m = new Message(message, cli.getNickName(), "GLOBAL");
                    m.ClientSend(cli.getSockChannel());
                } catch (Exception E) {
                    E.printStackTrace();
                }
                textField1.setText("");
            }
        });
        Receive(cli.getSockChannel());
    }

    //Threaded method to recieve server posts.
    private void Receive(SocketChannel client){
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while(true){
                    Message m = null;
                     m = Message.ServerRecieve(client);
                    ChatArea.append(m.getMessage() + "\n");
                }

            }
        });
        t1.start();
    }


//    public static void main(String[] args) {
//        JFrame frame = new JFrame("ChatGUI");
//        //frame.setContentPane(new ChatGUI(cli).Main);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
}