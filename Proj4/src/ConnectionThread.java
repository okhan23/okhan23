//
//  /*ConnectionThread.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionThread extends Thread implements Runnable
{
    public ClientServer gui;
    public ArrayList<CommunicationThread> threads;
    public ArrayList<ObjectOutputStream> outputs;
    public ArrayList<String> usersOnServer;

    public ConnectionThread(ClientServer serv)
    {
        gui = serv;
        threads = new ArrayList<CommunicationThread>();
        outputs = new ArrayList<ObjectOutputStream>();
        usersOnServer = new ArrayList<String>();
        this.setDaemon(true);
        start();
    }

    public void run()
    {
        gui.serverContinue = true;

        try
        {
            gui.serverSocket = new ServerSocket(0);
            gui.portInfo.setText("Listening on Port: " + gui.serverSocket.getLocalPort());
            System.out.println ("Connection Socket Created");
            try {
                while (gui.serverContinue)
                {
                    System.out.println("Waiting for Connection");
                    gui.ssButton.setText("Stop Listening");
                    Socket clientSocket = gui.serverSocket.accept();
                    ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    outputs.add(outStream);
                    threads.add(new CommunicationThread(this, clientSocket, inStream, outStream, gui));
//                    gui.listModel.addElement(threads.get(threads.size() - 1).ID);
                    //usersOnServer.add(threads.get(threads.size() - 1).ID);

//                    for(CommunicationThread thr : threads)
//                        thr.writeClientList(usersOnServer);
                    //threads.get(threads.size() - 1).writeClientList();
                    //writeClientList();
                    threads.get(threads.size()-1).setDaemon(true);
                    threads.get(threads.size()-1).start();
                }

            }
            catch (IOException e)
            {
                System.err.println("Accept failed.");
                for(CommunicationThread thr : threads)
                    thr.clientSocket.close();
                //System.exit(1);
            }

        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        }
        finally
        {
            try
            {
                gui.serverSocket.close();
            }
            catch (IOException e)
            {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }

    public void endConnections()
    {
        try
        {
//            for(CommunicationThread thr : threads)
//                thr.clientSocket.close();
            gui.serverSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Could not close port: 10008.");
            System.exit(1);
        }
    }

}
