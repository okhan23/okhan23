//
//  /*ClientProgramThread.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//

import java.io.IOException;
import java.util.Objects;

public class ClientProgramThread extends Thread implements Runnable
{
    private ClientProgram client;
    private String ID;

    public ClientProgramThread(ClientProgram c, String i)
    {
        this.client = c;
        this.ID = i;
        this.setDaemon(true);
        this.start();
    }

    public void run()
    {
        try
        {
            Object input;
            while((input = client.in.readObject()) != null)
            {
                //if(input.getClass().equals(ArrayList.class))
                if(input.getClass().equals(Integer.class))
                {
                    client.listModel.clear();
                }
                else if(input.getClass().equals(String.class))
                {
//                    ArrayList<String> usrName = (ArrayList<String>)input;
//                    String[] userName = new String[usrName.size()];
//                    userName = usrName.toArray(userName);
//                    client.clientNames = new JList(userName);
                    //client.listModel.clear();
//                    boolean present = false;
//                    for(String n : client.users)
//                        if(Objects.equals(n, (String) input))
//                            present = true;

//                    if(!present)
//                    {
                        String username = (String) input;
//                        client.users.add(usernames);
                        client.listModel.addElement(username);
//                    }
//                    ArrayList<String> usernames = (ArrayList<String>)input;
//                    for(String names : usernames)
//                        client.listModel.addElement(names);
                }
                else if(input.getClass().equals(ChatMessage.class))
                {
                    ChatMessage message = (ChatMessage)input;
                    if(Objects.equals(message.from, client.ID))
                        client.history.insert("Me: " + message.message + "\n", 0);
                    else
                        client.history.insert(message.from + ": " + message.message + "\n",0);
                }
            }
        } catch (IOException e)
        {
            try {
                System.err.println("Problem with Program Client");
                client.listModel.clear();
                client.out.close();
                client.in.close();
                client.echoSocket.close();
                client.sendButton.setEnabled(false);
                client.connected = false;
                client.sendButton.setEnabled(false);
                client.connectButton.setText("Connect to Server");
            } catch (IOException e1) {
                client.history.insert ("Error in closing down Socket ", 0);
                e1.printStackTrace();
            }

            return;
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
