package GUI;

import GUI.ChatGUI;
import Chat.Message;
import Chat.Client;
import Chat.Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class ClientGUI {
    private JButton SendButton;
    private JTextField NameInput;
    private JPanel Main;
    private JTextField PortInput;
    private JTextField IPInput;
    private static JFrame frame;

    public ClientGUI() {
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = NameInput.getText();
                String ip = IPInput.getText();
                int port = Integer.parseInt(PortInput.getText());

                if (nickname.contains(",")) {
                    // TODO print error in GUI

                } else {
                    System.out.println(nickname);
                    try {
                        Client cli = new Client(nickname, ip,port);
                        if (cli.sendNickName()) {
                            JFrame frame2 = new JFrame("Chat.ChatGUI");
                            frame2.setContentPane(new ChatGUI(cli).Main);
                            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame2.pack();
                            frame2.setVisible(true);
                            frame.setVisible(false);
                        } else {
                            // TODO print error message in the GUI

                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        NameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = NameInput.getText();
                System.out.println(nickname);
                try {
                    Client cli = new Client(nickname);
                    if (cli.sendNickName()) {
                        JFrame frame2 = new JFrame("Chat.ChatGUI" );
                        frame2.setContentPane(new ChatGUI(cli).Main);
                        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame2.pack();
                        frame2.setVisible(true);
                        frame.setVisible(false);
                    } else {
                        // TODO print error message in the GUI

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
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
