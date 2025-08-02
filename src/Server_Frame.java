import javax.print.attribute.IntegerSyntax;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;


public class Server_Frame extends JFrame {
    public static JLabel p1n,text1,text2,text3,count_label=new JLabel();
    public static JLabel imglabels[],textlabels[];
    public static String textcontent,player_str,spy_str;
    private final TextField textField,player_ans,spy_ans;
    private static JTextArea textlabel;
    Font font = new Font("SansSerif", Font.BOLD, 20);
    public  Hashtable ht = new Hashtable();
    public  Hashtable ht_name = new Hashtable();
    static Integer player_count = 0,spy_num,live_count;   //目前遊玩人數
    static Integer[] vote_player=new Integer[10];
    static final int countdown=5,countdown_vote=10;
    static int delay=1000,countdownVal = countdown,countdownvote=countdown_vote;
    static Timer timer_round,timer_vote;
    static boolean deadplayer[] = new boolean[10],gameend=false;
    ActionListener taskPerformer_7s = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (countdownVal > 0) {
                count_label.setText(Integer.toString(countdownVal--));
            } else {
                count_label.setText(Integer.toString(countdownVal--));
                timer_round.stop();
                countdownVal = countdown ;
            }
        }
    };
    ActionListener taskPerformer_12s = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (countdownvote > 0) {
                count_label.setText(Integer.toString(countdownvote--));
            } else {
                count_label.setText(Integer.toString(countdownvote--));
                timer_vote.stop();
                countdownvote = countdown_vote ;
            }
        }
    };
    public void update(){
        //更新人數
        player_count=ht.size();
        text3.setText("遊玩人數: "+player_count);
        text3.update(this.getGraphics());
        //更新圖像與玩家
        for(int i=0;i<10;i++){
            imglabels[i].setVisible(false);
            textlabels[i].setVisible(false);
        }
        Iterator<Socket> iterator=ht.keySet().iterator();
        while(iterator.hasNext()){
            Socket key=iterator.next();
            imglabels[(Integer)ht_name.get(key)-1].setVisible(true);
            textlabels[(Integer)ht_name.get(key)-1].setVisible(true);
        }
    }

    public void vote(Integer num) {
        vote_player[num]++;
    }
    public static int who_win(Integer vote_num,ArrayList<Integer> spy){
        if (spy.contains(vote_num)){
            live_count--;
            --spy_num;
        }
        else live_count--;
        if (live_count/2 <= spy_num){
            Server_Frame.message("臥底勝利");
            return 2;
        }else if (spy_num == 0){
            Server_Frame.message("平民勝利");
            return 1;
        }
        else return 0;
    }
    public static Integer end_vote(){
        Integer max=0;
        Integer num=-2;
        for(int i=0;i<10;i++){
            if(max<vote_player[i]){
                max=vote_player[i];
                num=i;
            }
        }
        Comparator<Integer> cmp= new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        };
        Arrays.sort(vote_player,cmp);
        if(vote_player[0]==vote_player[1]){
            for(int i=0;i<10;i++)vote_player[i]=0;
            return -1;
        }
        else{
            for(int i=0;i<10;i++)vote_player[i]=0;
            return num+1;
        }
    }
    public static void message(String str){
        textcontent+=str+'\n';
        textlabel.setText(textcontent);
    }
    public Server_Frame(Hashtable ht,Hashtable ht_name) {
        super("遊戲視窗");
        super.setLayout(null);
        this.ht=ht;
        this.ht_name=ht_name;
        for(int i=0;i<10;i++){
            vote_player[i]=0;
            deadplayer[i]=false;
        }
        //getContentPane().setBackground(new Color(43, 64, 91));
        getContentPane().setBackground(new Color(43, 64, 91));

        Border blackline = BorderFactory.createLineBorder(Color.gray);

        //計時器
        timer_round = new Timer(delay, taskPerformer_7s);
        //timer.start();
        count_label.setBounds(200,0,50,50);
        count_label.setForeground(Color.WHITE);
        count_label.setFont(new Font(null,Font.PLAIN,30));
        add(count_label);

        timer_vote = new Timer(delay, taskPerformer_12s);

        //Label

        //server圖示  改動
        p1n = new JLabel();
        ImageIcon host = new ImageIcon("src/host_icon.png");
        Image ht_icon = host.getImage();
        p1n.setBounds(30, 0, 150, 45);
        Image ht_icons = ht_icon.getScaledInstance(p1n.getWidth(), p1n.getHeight(), Image.SCALE_SMOOTH);
//        p1n.setBackground(new Color(43, 64, 91));
//        p1n.setForeground(new Color(252, 252, 252));
//        p1n.setFont(new Font(null,Font.PLAIN,25));
//        p1n.setOpaque(true);
        p1n.setIcon(new ImageIcon(ht_icons));
        add(p1n);

        //玩家題目圖示 改動
        text1=new JLabel();
        ImageIcon cv_t = new ImageIcon("src/cv_thing.png");
        Image cv_th = cv_t.getImage();
        text1.setBounds(40, 55, 125, 50);
        Image cv_ths = cv_th.getScaledInstance(text1.getWidth(), text1.getHeight(), Image.SCALE_SMOOTH);
        text1.setIcon(new ImageIcon(cv_ths));
        add(text1);

        //臥底題目圖是 改動
        text2=new JLabel();
        ImageIcon sy_t = new ImageIcon("src/spy_thing.png");
        Image sy_th = sy_t.getImage();
        text2.setBounds(40, 150, 125, 50);
        Image sy_ths = sy_th.getScaledInstance(text2.getWidth(), text2.getHeight(), Image.SCALE_SMOOTH);
        text2.setIcon(new ImageIcon(sy_ths));
//        text2.setBackground(new Color(43, 64, 91));
//        text2.setForeground(new Color(252, 252, 252));
//        text2.setFont(new Font(null,Font.PLAIN,20));
//        text2.setOpaque(true);
        add(text2);

        player_count=ht.size();
        text3=new JLabel("遊玩人數: "+player_count);
        text3.setBounds(10, 260, 200, 25);
        text3.setBackground(new Color(43, 64, 91));
        text3.setForeground(new Color(252, 252, 252));
        text3.setFont(new Font(null,Font.PLAIN,20));
        text3.setOpaque(true);
        add(text3);

        imglabels = new JLabel[10];
        for(int i=0; i<imglabels.length; i++) {
            imglabels[i] = new JLabel();
        }
        for(int i=0; i<10; i++){
            ImageIcon player1_imgIcon = new ImageIcon("src/spy.jpg");
            Image image1 = player1_imgIcon.getImage();
            imglabels[i].setBounds(0+(i%2)*120, 310+(i/2)*50, 50, 50);
            imglabels[i].setBorder(blackline);
            Image img1 = image1.getScaledInstance(imglabels[i].getWidth(), imglabels[i].getHeight(), Image.SCALE_SMOOTH);
            imglabels[i].setIcon(new ImageIcon(img1));
            imglabels[i].setVisible(false);
            add(imglabels[i]);
        }

        textlabels = new JLabel[10];
        for(int i=0; i<textlabels.length; i++) {
            textlabels[i] = new JLabel("玩家"+(i+1));
        }
        for(int i=0; i<10; i++){
            textlabels[i].setBounds(55+(i%2)*120, 320+(i/2)*50, 70, 25);
            textlabels[i].setBackground(new Color(43, 64, 91));
            textlabels[i].setForeground(new Color(252, 252, 252));
            textlabels[i].setFont(new Font(null,Font.PLAIN,20));
            textlabels[i].setOpaque(true);
            textlabels[i].setVisible(false);
            add(textlabels[i]);
        }
        //Label End

        //Field
        textField = new TextField();
        textField.setFont(new Font(null,Font.PLAIN,30));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    textcontent = textcontent+"Server: "+textField.getText()+"\n";
                    textlabel.setText(textcontent);
                    textField.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        textField.setBounds(285, 529, 425, 30);
        add(textField);

        //玩家題目輸入
        player_ans = new TextField();
        player_ans.setFont((new Font(null,Font.PLAIN,30)));
        player_ans.setBounds(40, 105, 150, 30);
        add(player_ans);

        //臥底題目輸入
        spy_ans = new TextField();
        spy_ans.setFont((new Font(null,Font.PLAIN,30)));
        spy_ans.setBounds(40, 200, 150, 30);
        add(spy_ans);
        //Field End

        //開始Button
        JLabel btn_start_bk = new JLabel();
        ImageIcon st_ban = new ImageIcon("src/start_ban.png");
        Image st_bans = st_ban.getImage();
        btn_start_bk.setBounds(205, 130, 70, 70);
        Image st_ban_image = st_bans.getScaledInstance(btn_start_bk.getWidth(), btn_start_bk.getHeight(), Image.SCALE_SMOOTH);
        btn_start_bk.setIcon(new ImageIcon(st_ban_image));
        btn_start_bk.setVisible(false);
        add(btn_start_bk);

        JButton btn_start=new JButton();
        btn_start.setBorderPainted(false);
        ImageIcon st_icon = new ImageIcon("src/start_icon.png");
        ImageIcon st_hover = new ImageIcon("src/start_hover.png");
        Image st_icons = st_icon.getImage();
        Image st_hovers = st_hover.getImage();
        btn_start.setBounds(205, 130, 70, 70);
        Image st_image = st_icons.getScaledInstance(btn_start.getWidth(), btn_start.getHeight(), Image.SCALE_SMOOTH);
        Image st_hover_image = st_hovers.getScaledInstance(btn_start.getWidth(), btn_start.getHeight(), Image.SCALE_SMOOTH);
        btn_start.setIcon(new ImageIcon(st_image));

        //hover button
        btn_start.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn_start.setIcon(new ImageIcon(st_hover_image));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn_start.setIcon(new ImageIcon(st_image));
            }
        });
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player_str=player_ans.getText();
                spy_str=spy_ans.getText();
                Game game=new Game(ht,ht_name);
                game.start();
                btn_start.setVisible(false);
                btn_start_bk.setVisible(true);
            }
        });
        add(btn_start);
        //送出文字按鈕
        JButton rent_btn = new JButton("送出");
        rent_btn.setBounds(710,529,75,30);
        rent_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textField.getText().isEmpty()){
                    textcontent = textcontent+"Server: "+textField.getText()+"\n";
                    textlabel.setText(textcontent);
                    textField.setText("");
                }
            }
        });
        add(rent_btn);

        //Button End

        //聊天室
        textcontent = new String();
        textlabel = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textlabel.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textlabel.setLineWrap(true);
        textlabel.setBackground(new Color(172,232,250));
        textlabel.setBounds(285, 0, 500, 550);
        textlabel.setFont(new Font(null,Font.PLAIN,18));
        textlabel.setForeground(Color.WHITE);
        textlabel.setOpaque(false);
        textlabel.setEnabled(false);
        JScrollPane jScrollPane = new JScrollPane(textlabel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds(285,0,500,550);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setOpaque(false);
        jScrollPane.setBorder(null);
        add(jScrollPane);

        //聊天室背景
        JLabel text_background = new JLabel();
        ImageIcon txt_background = new ImageIcon("src/text_background.png");
        Image txt_th = txt_background.getImage();
        text_background.setBounds(285, 0, 500, 550);
        Image txt_ths = txt_th.getScaledInstance(text_background.getWidth(), text_background.getHeight(), Image.SCALE_SMOOTH);
        text_background.setIcon(new ImageIcon(txt_ths));
        add(text_background);
        //聊天室End
    }
}

class Game extends Thread {
    int spy_count;
    ArrayList<Integer> spy=new ArrayList<>();   //放的不屬於index
    public  Hashtable ht;
    public  Hashtable ht_name;
    public Integer all_round=0;

    public Game(Hashtable ht,Hashtable ht_name){
        this.ht=ht;
        this.ht_name=ht_name;

        Server_Frame.live_count=ht.size();
        spy_count=ht.size()/3;
        if(spy_count==0)spy_count=1;
        //隨機選出間諜
        for(int i=0;i<spy_count;i++){
            while(true){
                Server_Frame.spy_num=(int)(Math.random() * ht.size())+1;
                if(!spy.contains(Server_Frame.spy_num)){
                    spy.add(Server_Frame.spy_num);
                    break;
                }
            }
        }
        Server_Frame.spy_num = spy.size();
    }

    public void run(){
        synchronized(ht) {
            Iterator<Socket> iterator=ht.keySet().iterator();
            while(iterator.hasNext()){
                Socket key=iterator.next();
                DataOutputStream outstream=(DataOutputStream) ht.get(key);
                Integer player_num=(Integer)ht_name.get(key);
                try {
                    outstream.writeUTF(Server_Frame.player_count.toString()+"人");   //1.輸出總玩家人數 長度2
                    if(!spy.contains(player_num)){ //這是平民
                        outstream.writeUTF("你的題目是: "+Server_Frame.player_str);  //2.輸出題目 長度不定
                    }
                    else outstream.writeUTF("你的題目是: "+Server_Frame.spy_str);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                //imglabels[(Integer)ht_name.get(key)-1].setVisible(true);
            }

        }
        while(!Server_Frame.gameend){
            all_round++;
            //輸出第幾輪
            synchronized (ht){
                for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                    DataOutputStream outstream = (DataOutputStream)e.nextElement();
                    try {
                        outstream.writeUTF("第"+all_round+"輪");   //1.輸出總玩家人數 長度2
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            Server_Frame.message("第"+all_round+"輪");

            for(Integer i=1;i<=10;i++){
                if(ht_name.contains(i) && !Server_Frame.deadplayer[i-1]){   //判斷是否包含玩家i 且 玩家i沒死
                    //輸出玩家回合
                    synchronized (ht){
                        for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                            DataOutputStream outstream = (DataOutputStream)e.nextElement();
                            try {
                                outstream.writeUTF("＝＝＝＝＝＝＝＝玩家"+i+"回合＝＝＝＝＝＝＝＝");   //1.輸出總玩家人數 長度2
                            }
                            catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    Server_Frame.message("＝＝＝＝＝＝＝＝玩家"+i+"回合＝＝＝＝＝＝＝＝");
                    Server_Frame.timer_round.start();
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //輸出投票環節
            synchronized (ht){
                for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                    DataOutputStream outstream = (DataOutputStream)e.nextElement();
                    try {
                        outstream.writeUTF("～～～～～～～～投票環節～～～～～～～～～");   //1.輸出總玩家人數 長度2
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            Server_Frame.message("～～～～～～～～投票環節～～～～～～～～～");
            Server_Frame.timer_vote.start();
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //結束投票
            Integer vote_out=Server_Frame.end_vote();
            if(vote_out!=-1) {
                Server_Frame.deadplayer[vote_out-1]=true;
                Server_Frame.textlabels[vote_out-1].setText("<html><s>玩家" +vote_out+ "</s></html>");
                Server_Frame.textlabels[vote_out-1].setForeground(Color.red);
                Integer whowin=Server_Frame.who_win(vote_out,spy);
                if (whowin!=0){
                    synchronized (ht) {
                        for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                            DataOutputStream outstream = (DataOutputStream) e.nextElement();
                            try {
                                if (whowin==1) {
                                    Server_Frame.gameend=true;
                                    String str="結束cli";
                                    for(Integer i=1;i<=Server_Frame.player_count;i++){
                                        if(!spy.contains(i)){
                                            str+=i.toString();
                                        }
                                    }
                                    outstream.writeUTF((Server_Frame.player_count-spy.size())+str);
                                }
                                else if(whowin==2){
                                    Server_Frame.gameend=true;
                                    String str="結束spy";
                                    for(int i=0;i<spy.size();i++){
                                        str+=spy.get(i).toString();
                                    }
                                    outstream.writeUTF(spy.size()+str);
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    synchronized (ht) {
                        for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                            DataOutputStream outstream = (DataOutputStream) e.nextElement();
                            try {
                                outstream.writeUTF(vote_out.toString());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
            else{
                synchronized (ht) {
                    for (Enumeration e = ht.elements(); e.hasMoreElements(); ) {
                        DataOutputStream outstream = (DataOutputStream) e.nextElement();
                        try {
                            outstream.writeUTF("平票進行下一輪!");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            if(vote_out==-1){
                Server_Frame.message("平票進行下一輪!");
            }
            else {
                Server_Frame.message("被票出的玩家: " + vote_out.toString());
            }
        }
        //while(true){ ; }
    }
}

