// Michael Ly, CS380-p1

import java.util.*;
import java.io.*;
import java.net.*;

public class ChatClient {

    protected Socket socket;

    public ChatClient(Socket s){
            socket = s;
    }

    // runnable for displaying server messages
    Runnable ms = () -> {
        while (true) {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String server = br.readLine();
                while (server != null) {
                    System.out.println(server);
                    System.out.print("Client > ");
                    server = br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args)
    {
        try{
            Socket sock = new Socket("codebank.xyz", 38001);
            OutputStream os = sock.getOutputStream();
            PrintStream ps = new PrintStream(os,true, "UTF-8");

            //First line sent to server is username
            Scanner kb = new Scanner(System.in);
            System.out.print("Client (Me) > ");
            String username = "";
            username = kb.nextLine();
            ps.println(username);

            //starts new thread to display server messages
            Thread test = new Thread(new ChatClient(sock).ms);
            test.start();

            //chat client inputting messages
            System.out.print("Client > ");
            String ctmp = "";

            while(ctmp != null)
            {
                ctmp = kb.nextLine();

                //chat client exiting
                if(ctmp.toLowerCase().equals("exit"))
                {
                    os.close();
                    System.exit(0);
                }
                else {
                    //sending message to server and displaying server tag
                    ps.println(ctmp);
                    System.out.print("Server > ");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}