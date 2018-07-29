package GUI;

import Chat.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatGUI {


    public JPanel Main;
    private JTextField textField1;
    private JList UserList;
    private JTextArea ChatArea;
    private JButton sendButton;
    private JTabbedPane tabbedPane1;
    private JScrollBar scrollBar1;


    public ChatGUI() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);
                try {
                    Client cli = new Client(message);
                    cli.sendGlobalMessage(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ChatGUI");
        frame.setContentPane(new ChatGUI().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
