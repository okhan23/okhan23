//
//  /*ClientServer.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Objects;

public class ClientServer extends JFrame implements ActionListener
{

    // GUI items
    JButton ssButton;
    JLabel machineInfo;
    JLabel portInfo;
    JTextArea history;
    private boolean running;
    JList<String> clientNames;
    DefaultListModel<String> listModel;
    ConnectionThread connectThread;
//    public ArrayList<CommunicationThread> threads;
//    boolean isMsgPending;
//    ChatMessage pendingMsg;

    // Network Items
    boolean serverContinue;
    ServerSocket serverSocket;

    // Constructor
    public ClientServer()
    {
        super("Server");

        //isMsgPending = false;
        //pendingMsg = new ChatMessage();
//        threads = new ArrayList<CommunicationThread>();

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // create buttons
        running = false;
        ssButton = new JButton( "Start Listening" );
        ssButton.addActionListener( this );
        container.add( ssButton );

        String machineAddress = null;
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            machineAddress = addr.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            machineAddress = "127.0.0.1";
        }
        machineInfo = new JLabel (machineAddress);
        container.add( machineInfo );
        portInfo = new JLabel (" Not Listening ");
        container.add( portInfo );

        history = new JTextArea ( 10, 40 );
        history.setEditable(false);

        listModel = new DefaultListModel<>();
        clientNames = new JList<>(listModel);
        clientNames.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel lower = new JPanel();
        lower.setLayout(new GridLayout(1, 2));
        lower.add(new JScrollPane(clientNames));
        lower.add(new JScrollPane(history));

        container.add(lower);

        setSize( 1000, 250 );
        setVisible( true );

    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Objects.equals(ssButton.getText(), "Start Listening"))
        {
            connectThread = new ConnectionThread (this);
        }
        else
        {
            serverContinue = false;
            ssButton.setText ("Start Listening");
            portInfo.setText (" Not Listening ");
            connectThread.endConnections();
        }
    }
}