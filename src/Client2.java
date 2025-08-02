import java.awt.*;
import java.awt.event.*;
import java.math.*;

import java.io.*;
import java.net.*;
import java.util.*;
public class Client2 extends Thread{
    static Socket socket;
    static String server_name;
    static int port;
    DataOutputStream  out_stream;
    String str;
    Scanner sc=new Scanner(System.in);
    static Game_Frame game_frame;
    public Client2(Socket socket){
        try{
            this.socket=socket;
            out_stream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e){
            System.out.println("Exception when connection server in Client class");
        }
    }
    public void run(){
        game_frame = new Game_Frame(socket);
        game_frame.setSize(800, 700);   //設定寬，長
        game_frame.setVisible(true);
        try {
            while (sc.hasNextLine()) {
                str = sc.nextLine();
                out_stream.writeUTF(str);
            }
        }
        catch (Exception e){
            System.out.println("Exception when Typing word in Client class");
        }
    }

    public static void main(String[] args) throws Exception{
        //server_name = "172.20.10.2";
        server_name = "localhost";
        port = 1234;
        Socket socket_to_server=new Socket(InetAddress.getByName(server_name),port);
        Client2 client=new Client2(socket_to_server);
        client.start();
        Input2 input=new Input2(socket_to_server);
        input.start();
    }
}
class Input2 extends Thread{
    DataInputStream  in_stream;
    Socket socket;
    public Input2(Socket socket){
        this.socket=socket;
    }
    public void run(){
        try {
            try {
                Thread.sleep(2000);         //等game_frame new好
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            in_stream = new DataInputStream(socket.getInputStream());
            while(true){
                String message=in_stream.readUTF();
                if(message.length()==1){
                    Integer player_dead = Character.getNumericValue(message.charAt(0));
                    player_dead-=1;
                    Client2.game_frame.whodead(player_dead);
                }
                else if(message.length()==2 && message.contains("人")){
                    Integer player_count = Character.getNumericValue(message.charAt(0));
                    //System.out.println(player_count);
                    Client2.game_frame.update(player_count);
                }
                else{
                    if(message.contains("您是玩家")){
                        Integer player_count = Character.getNumericValue(message.charAt(4));
                        Client2.game_frame.myNum=player_count;
                    }
                    else if(message.contains("第")){
                        //System.out.println("NEW");
                        if(!Client2.game_frame.imdead)Client2.game_frame.new_round();
                    }
                    System.out.println(message);
                    if(message.length()>20 && (message.substring(0,10).equals("＝＝＝＝＝＝＝＝玩家"))){
                        System.out.println("this: "+message.substring(0,12));
                        Client2.game_frame.update_round(1);
                    }
                    else if(message.length()>20 && (message.equals("～～～～～～～～投票環節～～～～～～～～～"))){
                        Client2.game_frame.update_round(2);
                    }
                    Client2.game_frame.message(message);
                }
            }
        }
        catch (Exception e){
            System.out.println("Exception when connection server in Input class");
        }
    }
}
