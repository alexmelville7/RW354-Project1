package GUI;

import Chat.Client;
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

                // Check that port string is not empty
                if (!PortInput.getText().isEmpty()) {
                    int port = Integer.parseInt(PortInput.getText());
                } else{
                    // TODO print error message
                    Warnings.setText("Port field cannot be empty");
                }

                if (nickname.contains(",")) {
                    // TODO print error in GUI
                    Warnings.setText("Nickname cannot have ',' in it.");
                } else {
                    try {
                        Client cli = new Client(nickname/*, ip,port*/);
                        if (cli.sendNickName()) {
                            JFrame frame2 = new JFrame("Chat.ChatGUI");
                            frame2.setContentPane(new ChatGUI(cli,frame2).Main);
                            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame2.pack();
                            frame2.setVisible(true);
                            frame.setVisible(false);
                        } else {
                            // TODO print error message in the GUI
                            Warnings.setText("Nickname must be unique.");
                        }
                    } catch (IOException e1) {
                        // TODO print error message in GUI
                        Warnings.setText("There was a connection error. Make sure IP and Port are correct");
                        e1.printStackTrace();

                    }
                }
            }
        });

        NameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = NameInput.getText();
                String ip = IPInput.getText();

                // Check that port string is not empty
                if (!PortInput.getText().isEmpty()) {
                    int port = Integer.parseInt(PortInput.getText());
                } else{
                    // TODO print error message
                }



                if (nickname.contains(",")) {
                    // TODO print error in GUI
                    System.out.println("Nickname can't have ,");
                } else {
                    System.out.println(nickname);
                    try {
                        Client cli = new Client(nickname/*, ip,port*/);
                        if (cli.sendNickName()) {
                            JFrame frame2 = new JFrame("Chat.ChatGUI");
                            frame2.setContentPane(new ChatGUI(cli,frame2).Main);
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
    }




    public static void main(String[] args) {
        frame = new JFrame("Chat.Client" );
        frame.setContentPane(new ClientGUI().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
