import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Server extends Thread {
    private static ServerSocket SSocket;
    private static int port;
    public Hashtable ht = new Hashtable();      //  <Socket,Dataoutputstream>
    public Hashtable ht_name = new Hashtable(); //  <Socket,Integer>
    Socket socket;
    static Integer player_count,player_num;;
    static boolean[] player=new boolean[10];
    public static boolean game_start;
    static Server_Frame server_frame;

    public void run() {
        player_count=0;
        for(int i=0;i<10;i++)player[i]=false;
        try {
            //打開Frame
            server_frame = new Server_Frame(ht,ht_name);
            server_frame.setSize(800,600);
            server_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            server_frame.setLocationRelativeTo(null);
            server_frame.setVisible(true);

            SSocket = new ServerSocket(port);
            System.out.println("Server created.");
            System.out.println("waiting for client to connect...");

            while (!game_start) {
                socket = SSocket.accept();
                System.out.println("connected from Client " +
                        socket.getInetAddress().getHostAddress());

                player_count++;
                for(int i=0;i<10;i++){
                    if(player[i]==false){
                        player[i]=true;
                        player_num=i+1;
                        break;
                    }
                }

                String player_name="玩家"+ player_num;
                DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
                outstream.writeUTF("您是"+player_name);
                ht.put(socket, outstream);
                ht_name.put(socket,player_num);


                Thread thread = new Thread(new ServerThread(socket, ht, ht_name));
                thread.start();

                server_frame.update();  //目前功能:更新人數
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        port = 1234;
        game_start=false;
        String player_str,spy_str;

        Server ServerStart=new Server();
        ServerStart.start();

    }
}

class ServerThread extends Thread implements Runnable {
    private Socket socket;
    private Hashtable ht;
    private Hashtable ht_name;
    DataInputStream in_stream;

    public ServerThread(Socket socket, Hashtable ht,Hashtable ht_name) {
        this.socket = socket;
        this.ht = ht;
        this.ht_name=ht_name;
    }

    public void run() {
        try {
            in_stream = new DataInputStream(socket.getInputStream());
            while (true) {
                String message = in_stream.readUTF();
                if(message.length()==1) {
                    Integer num= Integer.valueOf(message);
                    Server.server_frame.vote(num);
                    continue;
                }
                else{
                    Server.server_frame.message("玩家" + ht_name.get(socket) + " : " + message);
                }
                synchronized(ht) {
                    for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                        DataOutputStream outstream = (DataOutputStream)e.nextElement();
                        try {
                            outstream.writeUTF("玩家"+ht_name.get(socket)+" : "+message);
                        }
                        catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        //斷線
        catch (IOException ex) {
            System.out.println("Exception when connection server in ServerThread class");
        }
        finally {
            synchronized(ht) {
                System.out.println("Remove connection: " + socket);

                Integer player_quit= (Integer) ht_name.get(socket);
                Server.player[player_quit-1]=false;

                ht.remove(socket);
                ht_name.remove(socket);
                Server.server_frame.update();
                Server.player_count--;
                try {
                    socket.close();
                }
                catch (IOException ex) {
                }
            }
        }
    }
}

