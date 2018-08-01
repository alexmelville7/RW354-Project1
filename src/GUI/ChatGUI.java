package GUI;

import Chat.Client;
import Chat.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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


  public ChatGUI(Client cli) {
    client = cli;
    WelcomeLabel.setText(WelcomeLabel.getText() + client.getNickName());
    Main.setBackground(Color.lightGray);
    ChatArea.append("You are connected..." + "\n");

    // Response if send button is pressed
    sendButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String message = textField1.getText();
        System.out.println(message);

        // TODO get messages properly
        //                ChatArea.append(message + "\n");

        try {
          Message m = new Message("test", "Michael", "GLOBAL");
          m.ServerSend(cli.getSockChannel());
        } catch (Exception E) {
          E.printStackTrace();
        }
        textField1.setText("");
      }
    });

    // Response if Enter key is pressed
    textField1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String message = textField1.getText();
        System.out.println(message);

        // TODO get messages properly
        //                ChatArea.append(message + "\n");

        try {
          Message m = new Message("test", "Michael", "GLOBAL");
          m.ServerSend(cli.getSockChannel());
        } catch (Exception E) {
          E.printStackTrace();
        }
        textField1.setText("");
      }
    });
    printMessages();
  }


  private void printMessages() {

    DateFormat format = new SimpleDateFormat("HH:mm");

    // TODO thread this
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        while (true) {
          try {
            Message msg = null; //Message.ServerRecieve(client.getSockChannel());
            if (msg != null) {
              ChatArea.append(msg.getSender() + " (" + format.format(msg.getDate()) + "): " + msg.getMessage() + "\n");
            }

          } catch (Exception E) {
            E.printStackTrace();
          }
        }
      }
    });
    t1.start();


  }

}
