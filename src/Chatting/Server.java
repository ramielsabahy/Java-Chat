package Chatting;

import java.io.*;
import java.net.*;
import java.util.*;

class Server
{
    static Vector ClientSockets;
    static Vector LoginNames;
    
    Server() throws Exception
    {
        ServerSocket server=new ServerSocket(6060);
        ClientSockets=new Vector();
        LoginNames=new Vector();

        while(true)
        {    
            Socket socket=server.accept();        
            AcceptClient obClient=new AcceptClient(socket);
        }
    }
    public static void main(String args[]) throws Exception
    {
        
        Server ob=new Server();
    }

class AcceptClient extends Thread
{
    Socket ClientSocket;
    DataInputStream din;
    DataOutputStream dout;
    AcceptClient (Socket socket) throws Exception
    {
        ClientSocket=socket;

        din=new DataInputStream(ClientSocket.getInputStream());
        dout=new DataOutputStream(ClientSocket.getOutputStream());
        
        String LoginName=din.readUTF();

        System.out.println("User Logged In :" + LoginName);
        LoginNames.add(LoginName);
        ClientSockets.add(ClientSocket);    
        start();
    }

    public void run()
    {
        while(true)
        {
            
            try
            {
                String msgFromClient=new String();
                msgFromClient=din.readUTF();
                StringTokenizer st=new StringTokenizer(msgFromClient);
                String Sendto=st.nextToken();                
                String MsgType=st.nextToken();
                int iCount=0;
    
                if(MsgType.equals("LOGOUT"))
                {
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.elementAt(iCount).equals(Sendto))
                        {
                            LoginNames.removeElementAt(iCount);
                            ClientSockets.removeElementAt(iCount);
                            System.out.println("User " + Sendto +" Logged Out ...");
                            break;
                        }
                    }
    
                }
                else
                {
                    String msg="";
                    while(st.hasMoreTokens())
                    {
                        msg=msg+" " +st.nextToken();
                    }
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.elementAt(iCount).equals(Sendto))
                        {    
                            Socket tSoc=(Socket)ClientSockets.elementAt(iCount);                            
                            DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                            tdout.writeUTF(msg);                            
                            break;
                        }
                    }
                    if(iCount==LoginNames.size())
                    {
                        dout.writeUTF("I am offline");
                    }
                    else
                    {
                        
                    }
                }
                if(MsgType.equals("LOGOUT"))
                {
                    break;
                }

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            
            
        }        
    }
}
}