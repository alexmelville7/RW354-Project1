package GUI;

import Chat.Client;
import Chat.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ChatGUI {

    public JPanel Main;
    private JTextField textField1;
    private JList UserList;
    private JTextArea ChatArea;
    private JButton sendButton;
    private JTabbedPane tabbedPane1;
    private JScrollBar scrollBar1;
    private JLabel WelcomeLabel;
    private Client cli;
    private DefaultListModel listModel;
    public JList lstContacts;
    DateFormat format = new SimpleDateFormat("HH:mm");

    public ChatGUI(Client cli) {
        this.cli = cli;
//
        listModel = new DefaultListModel();
        WelcomeLabel.setText(WelcomeLabel.getText() + cli.getNickName());
        Main.setBackground(Color.lightGray);
        ChatArea.append("You are connected..." + "\n");
        listModel = new DefaultListModel();

        // Response if send button is pressed
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);

                if (!message.isEmpty()) {
                    try {
                        Message m = new Message(message, cli.getNickName(), "GLOBAL");
                        ChatArea.append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                        m.ClientSend(cli.getSockChannel());
                    } catch (Exception E) {
                        E.printStackTrace();
                    }
                    textField1.setText("");
                }
            }
        });

        // Response if Enter key is pressed
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);

                if (!message.isEmpty()) {
                    try {
                        Message m = new Message(message, cli.getNickName(), "GLOBAL");
                        ChatArea.append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                        m.ClientSend(cli.getSockChannel());
                    } catch (Exception E) {
                        E.printStackTrace();
                    }
                    textField1.setText("");
                }
            }
        });
        receive(cli);
    }

    private void receive(Client cli){
        SocketChannel client = cli.getSockChannel();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while(true){
                    Message m = null;
                    m = Message.ServerRecieve(client);

                    if (m.getMessageType().equals("MESSAGE")) {
                        ChatArea.append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                    } else if (m.getMessageType().equals("USER_ADD")) {
                        listModel.addElement(m.getMessage());
                        UserList.setListData(listModel.toArray());
                    } else if (m.getMessageType().equals("USER_ADD_LIST")) {
                        String str = m.getMessage();
                        str = str.substring(1, str.length()-1);
                        String tokens[] = str.split(",");

                        for(int i = 0; i < tokens.length; i++) {
                            listModel.addElement(tokens[i]);
                        }
                        UserList.setListData(listModel.toArray());
                    }
                    else if (m.getMessageType().equals("USER_REM")) {
                        // TODO test
                        listModel.remove(listModel.indexOf(m.getMessage()));
                        UserList.setListData(listModel.toArray());
                    }
                }
            }
        });
        t1.start();
    }

}
