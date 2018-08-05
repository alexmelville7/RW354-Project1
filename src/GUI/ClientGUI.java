package GUI;

import Chat.Client;
import Chat.Protocol;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class ClientGUI {
    private JButton SendButton;
    private JTextField NameInput;
    private JPanel Main;
    private JTextField PortInput;
    private JTextField IPInput;
    private JLabel Warnings;
    private static JFrame frame;

    public ClientGUI() {
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = NameInput.getText();
                String ip = IPInput.getText();
                int port = 0;

                // Check that port string is not empty
                if (!PortInput.getText().isEmpty()) {
                    port = Integer.parseInt(PortInput.getText());
                    try {
                        Client cli = new Client(nickname, ip,port);
                        // Check that nickname is unique
                        if (cli.getNickName().equals("GLOBAL")) {
                            Warnings.setText("Invalid Nickname!");
                        } else if (Protocol.isNickName(cli.getNickName(), Warnings) && cli.sendNickName()) {
                            JFrame frame2 = new JFrame("Chat.ChatGUI");
                            frame2.setContentPane(new ChatGUI(cli,frame, frame2).Main);
                            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame2.pack();
                            frame2.setVisible(true);
                            frame.setVisible(false);
                        } else {
                            Warnings.setText("Nickname must be unique.");
                        }
                    } catch (Exception e1) {
                        Warnings.setText("There was a connection error. Make sure IP and Port are correct");

                    }
                } else{
                    Warnings.setText("Port field cannot be empty");
                }

            }
        });

        NameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = NameInput.getText();
                String ip = IPInput.getText();
                int port = 0;

                // Check that port string is not empty
                if (!PortInput.getText().isEmpty()) {
                    port = Integer.parseInt(PortInput.getText());
                    try {
                        Client cli = new Client(nickname, ip,port);
                        // Check that nickname is unique
                        if (cli.getNickName().equals("GLOBAL")) {
                            Warnings.setText("Invalid Nickname!");
                        } else if (Protocol.isNickName(cli.getNickName(), Warnings) && cli.sendNickName()) {
                            JFrame frame2 = new JFrame("Chat.ChatGUI");
                            frame2.setContentPane(new ChatGUI(cli,frame, frame2).Main);
                            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame2.pack();
                            frame2.setVisible(true);
                            frame.setVisible(false);
                            Warnings.setText("");
                        } else {
                            Warnings.setText("Nickname must be unique.");
                        }
                    } catch (Exception e1) {
                        Warnings.setText("There was a connection error. Make sure IP and Port are correct");
                    }
                } else{
                    Warnings.setText("Port field cannot be empty");
                }

            }
        });
    }



    public static void main(String[] args) {
        frame = new JFrame("Chat.Client" );
        frame.setContentPane(new ClientGUI().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
