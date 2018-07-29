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
    private JLabel WelcomeLabel;


    public ChatGUI() {
        WelcomeLabel.setText(WelcomeLabel.getText() + "text u want to append");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);
                ChatArea.append(message + "\n");
              /*  try {
                    Client cli = new Client(message);
                    cli.sendGlobalMessage(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }*/
                textField1.setText("");
            }
        });

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                System.out.println(message);
                ChatArea.append(message + "\n");
              /*  try {
                    Client cli = new Client(message);
                    cli.sendGlobalMessage(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }*/
                textField1.setText("");
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
