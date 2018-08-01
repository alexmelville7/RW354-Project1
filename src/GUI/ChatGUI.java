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

public class ChatGUI {

    public JPanel Main;
    private JTextField textField1;
    private JList UserList;
    private JTextArea ChatArea;
    private JButton sendButton;
    private JTabbedPane tabbedPane1;
    private JScrollBar scrollBar1;
    private JLabel WelcomeLabel;
    private Client client;
    private DefaultListModel listModel;
    DateFormat format = new SimpleDateFormat("HH:mm");

    public ChatGUI(Client cli) {
        //Receive(cli.getSockChannel());
        client = cli;
        WelcomeLabel.setText(WelcomeLabel.getText() + client.getNickName());
        Main.setBackground(Color.lightGray);
        ChatArea.append("You are connected..." + "\n");
        listModel = new DefaultListModel();
        UserList = new JList(listModel);


        // Response if send button is pressed
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);

                if (!message.isEmpty()) {
                    try {
                        Message m = new Message(message, client.getNickName(), "GLOBAL");
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
                        Message m = new Message(message, client.getNickName(), "GLOBAL");
                        ChatArea.append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                        m.ClientSend(cli.getSockChannel());
                    } catch (Exception E) {
                        E.printStackTrace();
                    }
                    textField1.setText("");
                }
            }
        });
        receive(client.getSockChannel());
    }

    /**
     * Threaded method to receive server messages
     * @param client - the socket channel that messages are being received from
     */
    private void receive(SocketChannel client){

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while(true){
                    Message m = null;
                    m = Message.ServerRecieve(client);

                    if (m.getMessageType().equals("MESSAGE")) {
                        ChatArea.append(m.getSender() + " (" + format.format(m.getDate()) + "): " + m.getMessage() + "\n");
                    } else if (m.getMessageType().equals("USER_ADD")) {
                        // TODO test
                        listModel.addElement(m.getMessage());
                    } else if (m.getMessageType().equals("USER_REM")) {
                        // TODO test
                        listModel.remove(listModel.indexOf(m.getMessage()));
                    }

                }

            }
        });
        t1.start();
    }

}
