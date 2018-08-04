package GUI;

import Chat.Client;
import Chat.Message;
import Chat.Protocol;
//import apple.laf.JRSUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JButton disconnectButton;
    private Client cli;
    public JList lstContacts;
    private int chatIndex = 0;
    private DefaultListModel listModel;
    private LinkedList<String> names = new LinkedList<>();
    private LinkedList<String> users = new LinkedList<>();
    private LinkedList<JTextArea> chats = new LinkedList<>();
    private DateFormat format = new SimpleDateFormat("HH:mm");

    public ChatGUI(Client cli, JFrame frame, JFrame frame2) {

        names.add("GLOBAL");
        tabbedPane1.setName("GLOBAL");
        chats.add(ChatArea);
        this.cli = cli;

        ChatArea.append("You are connected..." + "\n");
        listModel = new DefaultListModel();
        WelcomeLabel.setText(WelcomeLabel.getText() + cli.getNickName());
        Main.setBackground(Color.lightGray);
        receive(cli);

        // Response if send button is pressed
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);

                if (!message.isEmpty()) {
                    try {
                        Message m = new Message(message, cli.getNickName(), names.get(tabbedPane1.getSelectedIndex()));
                        if(m.getMessage().length() < 200) {
                            chats.get(chatIndex).append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                            m.ClientSend(cli.getSockChannel(), frame, frame2);
                        } else {
                            chats.get(chatIndex).append("(Client): Message Exceeded max message length. Only below 200 characters please.");
                        }
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
                        Message m = new Message(message, cli.getNickName(), names.get(tabbedPane1.getSelectedIndex()));
                        if(m.getMessage().length() < 200) {
                            chats.get(chatIndex).append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                            m.ClientSend(cli.getSockChannel(), frame, frame2);
                        } else {
                            chats.get(chatIndex).append("(Client): Message Exceeded max message length. Only below 200 characters please.");
                        }
                    } catch (Exception E) {
                        System.out.println("Error Sending message - ChatGUI - " + E);
                        E.printStackTrace();
                    }
                    textField1.setText("");
                }
            }
        });


        tabbedPane1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JTabbedPane list = (JTabbedPane) evt.getSource();
                if (evt.getClickCount() == 1) {
                    // Double-click detected
                    chatIndex = list.getSelectedIndex();
                }
            }
        });

        // TODO make it so typed messages are now sent to whisper chat
        // Response if user in list is double-clicked
        UserList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    JTextArea a = new JTextArea();
                    String name = (String)list.getModel().getElementAt(index);
                    tabbedPane1.addTab(name, a);
                    names.add(name);
                    a.append("Whisper chat with " + list.getModel().getElementAt(index) + "\n");
                    chats.add(a);
                }
            }
        });


        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
//                Message msg = new Message(cli.getNickName(), "DISCONN");
//                msg.ClientSend(cli.getSockChannel());

            }
        });
    }

    private void receive(Client cli){
        SocketChannel client = cli.getSockChannel();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    Message m = null;
                    m = Message.ServerRecieve(client);

                    if (m.getMessageType().equals("MESSAGE")) {
                        if (m.getReceiver().equals("GLOBAL")) {
                            chats.get(tabbedPane1.getSelectedIndex()).append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                        } else if (names.contains(m.getSender())) {
                            chats.get(names.indexOf(m.getSender())).append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                        } else {
                            JTextArea a = new JTextArea();
                            String name = m.getSender();
                            tabbedPane1.addTab(name, a);
                            names.add(name);
                            a.append("Whisper chat with " + m.getSender()+ "\n");
                            a.append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                            chats.add(a);
                        }
                    } else if (m.getMessageType().equals("USER_ADD")) {
                        users.add(m.getMessage());
                        UserList.setListData(users.toArray());
                    } else if (m.getMessageType().equals("USER_ADD_LIST")) {
                        String str = m.getMessage();
                        System.out.println("Userlist Received: " + Protocol.isReceived(str, cli));

                        str = str.substring(1, str.length()-1);
                        String tokens[] = str.split(",");

                        for (int i = 0; i < tokens.length; i++) {
                            if(!cli.getNickName().equals(tokens[i].trim())){
                                users.add(tokens[i].trim());
                            }
                        }
                        UserList.setListData(users.toArray());
                    } else if (m.getMessageType().equals("USER_REM")) {
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
