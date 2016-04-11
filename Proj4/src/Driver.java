//
//  /*Driver.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Driver extends JFrame implements ActionListener
{
    private ArrayList<ClientServer> servers;
    private ArrayList<ClientProgram> programs;
    private JButton createServer;
    private JButton createProgram;


    public Driver()
    {
        super("Networked Chat Application");

        servers = new ArrayList<ClientServer>();
        programs = new ArrayList<ClientProgram>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        createServer = new JButton("Open New Server");
        createServer.addActionListener(this);
        createProgram = new JButton("Open New Client");
        createProgram.addActionListener(this);
        panel.add(createServer);
        panel.add(createProgram);

        this.add(panel);

        setSize(250, 250);
        setVisible(true);

    }
    public static void main( String args[] )
    {
        Driver drive = new Driver();

//        ClientServer serverApp = new ClientServer();
//        ClientProgram programApp = new ClientProgram();
//        serverApp.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
//        programApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == createServer)
        {
            servers.add(new ClientServer());
        }
        else if(e.getSource() == createProgram)
        {
            programs.add(new ClientProgram());
        }

    }
}
