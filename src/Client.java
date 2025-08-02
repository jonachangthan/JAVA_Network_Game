import java.awt.*;
import java.awt.event.*;
import java.math.*;

import java.io.*;
import java.net.*;
import java.util.*;
public class Client extends Thread{
    static Socket socket;
    static String server_name;
    static int port;
    DataOutputStream  out_stream;
    String str;
    Scanner sc=new Scanner(System.in);
    static Game_Frame game_frame;
    static winner_Frame winnerFrame ;
    public Client(Socket socket){
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
        server_name = "localhost";
        port = 1234;
        Socket socket_to_server=new Socket(InetAddress.getByName(server_name),port);
        Client client=new Client(socket_to_server);
        client.start();
        Input input=new Input(socket_to_server);
        input.start();
    }
}

class Input extends Thread{
    DataInputStream  in_stream;
    Socket socket;
    public Input(Socket socket){
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
                    Client.game_frame.whodead(player_dead);
                }
                else if(message.length()==2 && message.contains("人")){
                    Integer player_count = Character.getNumericValue(message.charAt(0));
                    //System.out.println(player_count);
                    Client.game_frame.update(player_count);
                }
                else{
                    if(message.contains("您是玩家")){
                        Integer player_count = Character.getNumericValue(message.charAt(4));
                        Client.game_frame.myNum=player_count;
                    }
                    else if(message.contains("第")){
                        //System.out.println("NEW");
                        if(!Client.game_frame.imdead)Client.game_frame.new_round();
                    }
                    if (message.contains("結束spy")){
                        Integer size=Character.getNumericValue(message.charAt(0));
                        ArrayList <Integer> arr=new ArrayList<>();
                        for(int i=0;i<size;i++){
                            arr.add(Character.getNumericValue(message.charAt(6+i)));
                        }
                        System.out.println(message);
                        Client.winnerFrame = new winner_Frame(false, arr, socket);
                        Client.winnerFrame.setVisible(true);
                        continue;
                    }
                    else if (message.contains("結束cli")){
                        Integer size=Character.getNumericValue(message.charAt(0));
                        ArrayList <Integer> arr=new ArrayList<>();
                        for(int i=0;i<size;i++){
                            arr.add(Character.getNumericValue(message.charAt(6+i)));
                        }
                        //System.out.println(message);
                        Client.winnerFrame = new winner_Frame(true, arr, socket);
                        Client.winnerFrame.setVisible(true);
                        continue;
                    }
                    System.out.println(message);
                    if(message.length()>20 && (message.substring(0,10).equals("＝＝＝＝＝＝＝＝玩家"))){
                        System.out.println("this: "+message.substring(0,12));
                        Client.game_frame.update_round(1);
                    }
                    else if(message.length()>20 && (message.equals("～～～～～～～～投票環節～～～～～～～～～"))){
                        Client.game_frame.update_round(2);
                    }
                    Client.game_frame.message(message);
                }
            }
        }
        catch (Exception e){
            System.out.println("Exception when connection server in Input class");
        }
    }
}