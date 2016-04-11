import java.io.Serializable;
import java.util.ArrayList;

//
//  /*ChatMessage.java*/
//
//  Name: Omaid Khan & Jesus Solorzano
//  Course: CS 342 SP16 - Troy UIC
//  Assignment: Networked Chat Application
//
public class ChatMessage implements Serializable
{
    public ArrayList<String> to;
    public String from;
    public String message;

    public ChatMessage()
    {

    }

    public ChatMessage(ArrayList<String> t,String f, String m)
    {
        to = new ArrayList<String>();
        to.addAll(t);
        from = f;
        message = m;
    }


}
