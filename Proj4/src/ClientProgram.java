//
//  /*ClientProgram.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class ClientProgram extends JFrame implements ActionListener
{

    // GUI items
    JButton sendButton;
    JButton connectButton;
    JTextField machineInfo;
    JTextField portInfo;
    JTextField message;
    JTextArea history;
    JList<String> clientNames;
    DefaultListModel<String> listModel;
    ClientProgramThread thread;
    ArrayList<String> users;

    // Network Items
    boolean connected;
    Socket echoSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    String ID;

    // set up GUI
    public ClientProgram()
    {
        super( "Echo Client" );
        //Random rand = new Random();
        //ID = String.valueOf(rand.nextInt(1000));
        ID = JOptionPane.showInputDialog("Please enter a username");
        if(ID.equals(""))
            ID = String.valueOf((new Random()).nextInt(10000));
        super.setTitle("Client: " + ID);

        users = new ArrayList<String>();

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout (new BorderLayout ());

        // set up the North panel
        JPanel upperPanel = new JPanel ();
        upperPanel.setLayout(new GridLayout(4, 2));
        container.add (upperPanel, BorderLayout.NORTH);

        // create buttons
        connected = false;

        upperPanel.add ( new JLabel ("Message: ", JLabel.RIGHT) );
        message = new JTextField ("");
        message.addActionListener(this);
        upperPanel.add(message);

        sendButton = new JButton( "Send Message" );
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        upperPanel.add(sendButton);

        connectButton = new JButton( "Connect to Server" );
        connectButton.addActionListener(this);
        upperPanel.add(connectButton);

        upperPanel.add ( new JLabel ("Server Address: ", JLabel.RIGHT) );
        machineInfo = new JTextField ("192.168.43.137");
        upperPanel.add( machineInfo );

        upperPanel.add ( new JLabel ("Server Port: ", JLabel.RIGHT) );
        portInfo = new JTextField ("");
        upperPanel.add( portInfo );

        history = new JTextArea ( 10, 40 );
        history.setEditable(false);

        listModel = new DefaultListModel<>();
        clientNames = new JList<>(listModel);
        clientNames.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel lower = new JPanel();
        lower.setLayout(new GridLayout(1,2));
        lower.add(new JScrollPane(clientNames));
        lower.add(new JScrollPane(history));

        container.add(lower);

        setSize(500, 500);
        setVisible( true );

    } // end CountDown constructor

    // handle button event
    public void actionPerformed( ActionEvent event )
    {
        if ( connected &&
                (event.getSource() == sendButton ||
                        event.getSource() == message ) )
        {
            doSendMessage();
        }
        else if (event.getSource() == connectButton)
        {
            doManageConnection();
        }
//        else
//        {
//
//            while(in.readObject())
//        }
    }

    public void doSendMessage()
    {

        try {

            ArrayList<String> usersToSend = new ArrayList<>();

            int[] indices = clientNames.getSelectedIndices();

            for(int i : indices)
            {
                usersToSend.add(listModel.getElementAt(i));
            }
            if(usersToSend.size() == 0)
            {
                ListModel model = clientNames.getModel();
                for(int i = 0; i < model.getSize(); i++)
                {
                    //if(!Objects.equals(model.getElementAt(i).toString(), ID))
                        usersToSend.add(model.getElementAt(i).toString());
                }
            }

            ChatMessage outMessage = new ChatMessage(usersToSend, ID, message.getText());
            //0history.insert ("ME: " + message.getText() + "\n" , 0);
            out.writeObject(outMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doManageConnection()
    {
        if (!connected)
        {
            String machineName = null;
            int portNum = -1;
            try {
                machineName = machineInfo.getText();
                portNum = Integer.parseInt(portInfo.getText());
                echoSocket = new Socket(machineName, portNum );
                out = new ObjectOutputStream(echoSocket.getOutputStream());
                in = new ObjectInputStream(echoSocket.getInputStream());
                out.writeObject(ID);
                thread = new ClientProgramThread(this, ID);

                sendButton.setEnabled(true);
                connected = true;
                connectButton.setText("Disconnect from Server");
            } catch (NumberFormatException e) {
                history.insert ( "Server Port must be an integer\n", 0);
            } catch (UnknownHostException e) {
                history.insert("Don't know about host: " + machineName , 0);
            } catch (IOException e) {
                history.insert ("Couldn't get I/O for "
                        + "the connection to: " + machineName , 0);
            }

        }
        else
        {
            try
            {
                out.close();
                in.close();
                echoSocket.close();
                sendButton.setEnabled(false);
                connected = false;
                connectButton.setText("Connect to Server");
            }
            catch (IOException e)
            {
                history.insert ("Error in closing down Socket ", 0);
            }
        }
    }

}
