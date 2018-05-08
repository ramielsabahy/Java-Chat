package Chatting;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class MClient extends JFrame implements Runnable
{
    Socket soc;    
    TextField tf;
    TextArea ta;
    Button btnSend,btnClose;
    String sendTo;
    String LoginName;
    Thread t=null;
    DataOutputStream dout;
    DataInputStream din;
    MClient(String LoginName,String chatwith) throws Exception
    {
        super(LoginName);
        this.LoginName=LoginName;
        sendTo=chatwith;
        tf=new TextField(58);
        ta=new TextArea();
        btnSend=new Button("Send");
        btnClose=new Button("Close");
        soc=new Socket("localhost",6060);

        din=new DataInputStream(soc.getInputStream()); 
        dout=new DataOutputStream(soc.getOutputStream());        
        dout.writeUTF(LoginName);

        t=new Thread(this);
        t.start();

    }
    void setup()
    {
        setLayout(null);
        setSize(600,300);
        
        ta.setSize(600, 200);
        add(ta);
        Panel p=new Panel();
        p.setBackground(Color.yellow);
        p.setBounds(0, 200, 600, 100);
        p.add(tf);
        p.add(btnSend);
        p.add(btnClose);
        add(p);
        show();        
    }
    public boolean action(Event e,Object o)
    {
        if(e.arg.equals("Send"))
        {
            try
            {
                dout.writeUTF(sendTo + " "  + "DATA" + " " + tf.getText().toString());            
                ta.append("\n" + LoginName + ": " + tf.getText().toString());    
                tf.setText("");
            }
            catch(Exception ex)
            {
            }    
        }
        else if(e.arg.equals("Close"))
        {
            try
            {
                dout.writeUTF(LoginName + " LOGOUT");
                System.exit(1);
            }
            catch(Exception ex)
            {
            }
            
        }
        
        return super.action(e,o);
    }
    public static void main(String args[]) throws Exception
    {
        String name = JOptionPane.showInputDialog("Enter your name");
        String to = JOptionPane.showInputDialog("Enter name to chat with");
        MClient Client1=new MClient(name,to);
        Client1.setup();                
    }    
    public void run()
    {        
        while(true)
        {
            try
            {
                ta.append( "\n" + sendTo + ": " + din.readUTF());
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}