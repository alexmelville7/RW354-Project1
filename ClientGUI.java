package GUI;

import Chat.Message;
import Chat.Client;
import Chat.Server;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class ClientGUI {
    private JButton SendButton;
    private JTextField NameInput;
    private JPanel Main;


    public ClientGUI() {
        SendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Next step --> send this to the client
                String nickname = NameInput.getText();
                System.out.println(nickname);
                try {
                    Client cli = new Client(nickname);
                    cli.sendGlobalMessage(nickname);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }





            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat.Client" );
        frame.setContentPane(new ClientGUI().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
