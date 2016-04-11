//
//  /*CommunicationThread.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class CommunicationThread extends Thread implements Runnable
{
    //private boolean serverContinue = true;
    public ConnectionThread conThread;
    public Socket clientSocket;
    public ClientServer gui;
    public String ID;

    public ObjectInputStream inStream;
    public ObjectOutputStream outStream;



    public CommunicationThread (ConnectionThread t, Socket clientSoc, ObjectInputStream in , ObjectOutputStream out , ClientServer ec3)
    {
        conThread = t;
        clientSocket = clientSoc;
        gui = ec3;
        try {
            //inStream = new ObjectInputStream(clientSocket.getInputStream());
            //outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = in;
            outStream = out;
            ID = (String)inStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.setDaemon(true);
//        try {
//            outStream.writeObject(conThread.usersOnServer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //start();
    }

    public void run()
    {
        System.out.println("New Communication Thread Started");

        try {
            conThread.usersOnServer.add(ID);
            gui.listModel.addElement(ID);
            writeClientList();

            Object input;


            while ((input = inStream.readObject()) != null && gui.serverContinue)
            {

                if(input.getClass().equals(ChatMessage.class))
                {

                    gui.history.insert("From: " + ((ChatMessage) input).from + "\nTo: " + ((ChatMessage) input).to + "\nMessage: " + ((ChatMessage) input).message + "\n", 0);

                    for(CommunicationThread con : conThread.threads)
                    {
                        for(String to : ((ChatMessage) input).to)
                        {
                            String formatedStringTo = to
                                    .replace(",", "")  //remove the commas
                                    .replace("[", "")  //remove the right bracket
                                    .replace("]", "")  //remove the left bracket
                                    .trim();
                            String formatedStringFrom = ((ChatMessage) input).from
                                    .replace(",", "")  //remove the commas
                                    .replace("[", "")  //remove the right bracket
                                    .replace("]", "")  //remove the left bracket
                                    .trim();
                            if(formatedStringTo.equals(con.ID) )//|| formatedStringFrom.equals(ID))
                                con.outStream.writeObject((ChatMessage)input);
                        }
                    }


                    if (((ChatMessage)input).message.equals("End Server."))
                        gui.serverContinue = false;
                }


            }

        }
        catch (IOException e)
        {
            try
            {
                System.err.println("Problem with Communication Server");
                for(int i = 0; i < conThread.threads.size(); i++)
                {
                    if(conThread.threads.get(i).equals(this))
                    {
                        conThread.threads.remove(i);
                    }

                }
                for(int i = 0; i <gui.listModel.getSize(); i++)
                {
                    if(gui.listModel.get(i).equals(ID))
                        gui.listModel.remove(i);
                    if(conThread.usersOnServer.get(i).equals(ID))
                        conThread.usersOnServer.remove(i);
                }
                gui.serverContinue = false;

                writeClientList();


                outStream.close();
                inStream.close();
                clientSocket.close();

                System.err.println("Finished the Closing of everything");

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void writeClientList()
    {
        for(CommunicationThread thr : conThread.threads)
        {

            try
            {
                // Clears the clients list before updating online list.
                Integer t = 1;
                thr.outStream.writeObject(t);

                // remove all the bullshit and send online clients names one by one.
                for(int i = 0; i < gui.listModel.size(); i++)
                {
                    String name = gui.listModel.getElementAt(i)
                            .replace(",", "")  //remove the commas
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();

                    thr.outStream.writeObject(name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
