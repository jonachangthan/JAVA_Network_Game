import com.sun.jdi.request.StepRequest;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.ImageGraphicAttribute;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Game_Frame extends JFrame {
    Integer myNum=0;
    boolean deadplayer[] = new boolean[10];
    boolean imdead=false;
    private final JLabel player[] = new JLabel[10];
    private final JLabel deadbtn[] = new JLabel[10];
    private final JButton vote_btn;
    private final JButton vote[] = new JButton[10];
    private Image hover_picture[] = new Image[10];
    private Image button_picture[] = new Image[10];

    private Image dark_picture[] = new Image[10];
    private Image vote_icons;
    private Image vote_hicons;
    private Image vote_bicons;
    private int vote_player = 0;
    private static String textcontent;
    private final TextField textField;
    private static JTextArea textlabel;
    private final JLabel countlabel = new JLabel();
    //private int countdownVal = 10;
    private int playernum = 10;
    static Timer timer;
    int delay = 1000;
    int btn_push = -1;
    DataOutputStream outstream;


    static final int countdown=5,countdown_vote=10;
    static int countdownVal = countdown,countdownvote=countdown_vote;
    static Timer timer_round,timer_vote;

    /*
    ActionListener taskPerformer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (countdownVal >= 0) {
                countlabel.setText(Integer.toString(countdownVal--));
            } else {
                timer.stop();
            }
        }
    };
    */
    ActionListener taskPerformer_7s = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (countdownVal > 0) {
                countlabel.setText(Integer.toString(countdownVal--));
            } else {
                countlabel.setText(Integer.toString(countdownVal--));
                timer_round.stop();
                countdownVal = countdown ;
            }
        }
    };
    ActionListener taskPerformer_12s = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (countdownvote > 0) {
                countlabel.setText(Integer.toString(countdownvote--));
            } else {
                countlabel.setText(Integer.toString(countdownvote--));
                timer_vote.stop();
                countdownvote = countdown_vote ;
            }
        }
    };

    public void update_round(Integer num){
        if(num==1){
            timer_round.start();
        }
        else{
            timer_vote.start();
        }
    }

    public void update(Integer num){
        //更新人數
        playernum=num;
        //更新圖像與玩家
        for(int i=0;i<10;i++){
            if(i<playernum){
                player[i].setVisible(true);
                vote[i].setVisible(true);
            }
            else {
                player[i].setVisible(false);
                vote[i].setVisible(false);
            }
        }
    }
    public static void message(String str){
        textcontent+=str+'\n';
        textlabel.setText(textcontent);
    }

    public void load_picture() {
        ImageIcon icon[] = new ImageIcon[10];
        ImageIcon hicon[] = new ImageIcon[10];
        ImageIcon dicon[] = new ImageIcon[10];
        for (int i = 0; i < 10; i++) {
            icon[i] = new ImageIcon("res/p" + (i + 1) + ".png");
            hicon[i] = new ImageIcon("res/p" + (i + 1) + "_hover.png");
            dicon[i] = new ImageIcon("res/p" + (i + 1) + "_ban.png");
        }
        for (int i = 0; i < 10; i++) {
            button_picture[i] = icon[i].getImage();
            hover_picture[i] = hicon[i].getImage();
            dark_picture[i] = dicon[i].getImage();
        }
    }

    public void new_round() {
        vote_btn.setVisible(true);
        for (int i = 0; i < playernum; i++) {
            if (!deadplayer[i]) {
                vote[i].setVisible(true);
                vote[i].setIcon(new ImageIcon(button_picture[i]));
            }
        }
        btn_push = -1;
        //textcontent = textcontent + "新的回合\n";
        //textlabel.setText(textcontent);
    }
    public void whodead(int num) {
        deadplayer[num] = true;
        if(myNum==num+1){
            imdead=true;    //目前沒用
            textField.setEnabled(false);
            for(int i=0;i<10;i++)vote[i].setVisible(false);
            vote_btn.setVisible(false);
        }
        player[num].setText("<html><s>玩家" + (num + 1) + "</s></html>");
        player[num].setForeground(Color.red);
        message("玩家"+(num+1)+"遭處決!");
    }

    public Game_Frame(Socket socket) {
        super("遊戲視窗");
        super.setLayout(null);
        load_picture();
        getContentPane().setBackground(new Color(43, 64, 91));
        for (int i = 0; i < playernum; i++) {
            deadplayer[i] = false;
        }
        //計時器
        /*
        timer = new Timer(delay, taskPerformer);
        //timer.start();
        countlabel.setBounds(625, 40, 50, 50);
        countlabel.setForeground(Color.WHITE);
        countlabel.setFont(new Font(null, Font.PLAIN, 30));
        add(countlabel);
        */


        timer_round = new Timer(delay, taskPerformer_7s);
        countlabel.setBounds(630,50,50,50);
        countlabel.setForeground(Color.WHITE);
        countlabel.setFont(new Font(null,Font.PLAIN,30));
        add(countlabel);

        timer_vote = new Timer(delay, taskPerformer_12s);

        String playername1 = "玩家一";
        //投票區
        //投票背景
        JLabel vote_background = new JLabel();
        vote_background.setBackground(Color.black);
        vote_background.setOpaque(true);
        vote_background.setBounds(500, 0, 285, 565);
        ImageIcon votebackicon = new ImageIcon("res/game_vote.png");
        Image voteimage = votebackicon.getImage();
        voteimage = voteimage.getScaledInstance(vote_background.getWidth(), vote_background.getHeight(), Image.SCALE_SMOOTH);
        vote_background.setIcon(new ImageIcon(voteimage));
        //投票按鈕

        for (int i = 0; i < playernum; i++) {
            vote[i] = new JButton();
            if (i % 2 == 0) {
                vote[i].setBounds(535, 75 * (i / 2) + 100, 100, 50);
            } else {
                vote[i].setBounds(650, 75 * (i / 2) + 100, 100, 50);
            }
        }

        for (int i = 0; i < playernum; i++) {
            button_picture[i] = button_picture[i].getScaledInstance(vote[i].getWidth(), vote[i].getHeight(), Image.SCALE_SMOOTH);
            hover_picture[i] = hover_picture[i].getScaledInstance(vote[i].getWidth(), vote[i].getHeight(), Image.SCALE_SMOOTH);
            dark_picture[i] = dark_picture[i].getScaledInstance(vote[i].getWidth(), vote[i].getHeight(), Image.SCALE_SMOOTH);
            vote[i].setIcon(new ImageIcon(button_picture[i]));
            vote[i].setFocusPainted(false);
            vote[i].setBorderPainted(false);
            int finalI = i;
            vote[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (btn_push != -1) {
                        btn_push = -1;
                        for (int j = 0; j < playernum; j++) {
                            vote[j].setIcon(new ImageIcon(button_picture[j]));
                        }
                    } else {
                        btn_push = finalI;
                        System.out.println(btn_push);////
                        for (int j = 0; j < playernum; j++) {
                            vote[j].setIcon(new ImageIcon(dark_picture[j]));
                        }
                    }
                    vote[finalI].setIcon(new ImageIcon(button_picture[finalI]));
                }
            });
            vote[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (btn_push == -1) vote[finalI].setIcon(new ImageIcon(hover_picture[finalI]));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (btn_push == -1) vote[finalI].setIcon(new ImageIcon(button_picture[finalI]));
                }
            });
            add(vote[i]);
        }

        for (int i = 0; i < playernum; i++) {
            deadbtn[i] = new JLabel(new ImageIcon(dark_picture[i]));
            if (i % 2 == 0) {
                deadbtn[i].setBounds(535, 75 * (i / 2) + 100, 100, 50);
            } else {
                deadbtn[i].setBounds(650, 75 * (i / 2) + 100, 100, 50);
            }
            add(deadbtn[i]);
        }

        vote_btn = new JButton();
        ImageIcon vote_icon = new ImageIcon("res/vote_icon.png");
        vote_icons = vote_icon.getImage();
        ImageIcon vote_hicon = new ImageIcon("res/vote_hover.png");
        vote_hicons = vote_hicon.getImage();
        ImageIcon vote_bicon = new ImageIcon("res/vote_ban.png");
        vote_bicons = vote_bicon.getImage();
        vote_btn.setBounds(560, 480, 160, 45);
        vote_icons = vote_icons.getScaledInstance(vote_btn.getWidth(), vote_btn.getHeight(), Image.SCALE_SMOOTH);
        vote_hicons = vote_hicons.getScaledInstance(vote_btn.getWidth(), vote_btn.getHeight(), Image.SCALE_SMOOTH);
        vote_bicons = vote_bicons.getScaledInstance(vote_btn.getWidth(), vote_btn.getHeight(), Image.SCALE_SMOOTH);
        vote_btn.setIcon(new ImageIcon(vote_icons));
        vote_btn.setBorderPainted(false);
        vote_btn.setFocusPainted(false);
        vote_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn_push != -1) {
                    try {
                        outstream = new DataOutputStream(socket.getOutputStream());
                        outstream.writeUTF(Integer.toString(btn_push));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    vote_btn.setIcon(new ImageIcon(vote_bicons));
                    for (int i = 0; i < playernum; i++) {
                        vote[i].setVisible(false);
                    }
                    vote_btn.setVisible(false);
                }
            }
        });
        vote_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                vote_btn.setIcon(new ImageIcon(vote_hicons));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                vote_btn.setIcon(new ImageIcon(vote_icons));
            }
        });
        add(vote_btn);
        JLabel dead_vote = new JLabel();
        dead_vote.setIcon(new ImageIcon(vote_bicons));
        dead_vote.setBounds(560, 480, 160, 45);
        add(dead_vote);
        //投票按鈕end

        //玩家名稱
        JLabel p1img = new JLabel();
        ImageIcon imgThisImg = new ImageIcon("res\\spy.jpg");
        Image image = imgThisImg.getImage();
        p1img.setBounds(0, 0, 50, 50);
        Image img = image.getScaledInstance(p1img.getWidth(), p1img.getHeight(), Image.SCALE_SMOOTH);
        for (int i = 0; i < playernum; i++) {
            player[i] = new JLabel("玩家" + (i + 1));
            player[i].setBounds(0, 50 * i, 100, 50);
            player[i].setBackground(new Color(43, 64, 91));
            player[i].setForeground(Color.white);
            player[i].setIcon(new ImageIcon(img));
            player[i].setOpaque(true);
            add(player[i]);
        }


        //聊天室窗
        //文字輸入區
        textField = new TextField();
        textField.setBounds(100, 535, 325, 30);
        add(textField);
        //聊天室
        //內容
        textcontent = "";
        textlabel = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textlabel.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textlabel.setLineWrap(true);
        //textlabel.setBackground(new Color(172,232,250));
        textlabel.setForeground(Color.WHITE);
        textlabel.setFont(new Font(null, Font.PLAIN, 18));
        textlabel.setOpaque(false);
        textlabel.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(textlabel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds(100, 0, 400, 535);
        jScrollPane.setBorder(null);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setOpaque(false);
        add(jScrollPane);
        //背景
        ImageIcon imageIcon = new ImageIcon("res\\game_background.png");
        Image image1 = imageIcon.getImage();
        image1 = image1.getScaledInstance(jScrollPane.getWidth(), jScrollPane.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image1);
        JLabel chat_background = new JLabel();
        chat_background.setBounds(100, 0, jScrollPane.getWidth(), jScrollPane.getHeight());
        chat_background.setIcon(icon);
        add(chat_background);
        //送出文字按鈕
        JButton sent_btn = new JButton();
        ImageIcon send_icon = new ImageIcon("res\\send_icon.png");
        ImageIcon send_hover = new ImageIcon("res\\send_hover.png");
        Image si = send_icon.getImage();
        Image si_hover = send_hover.getImage();
        sent_btn.setBounds(425, 530, 75, 30);
        Image si_s = si.getScaledInstance(sent_btn.getWidth(), sent_btn.getHeight(), Image.SCALE_SMOOTH);
        Image si_s_hover = si_hover.getScaledInstance(sent_btn.getWidth(), sent_btn.getHeight(), Image.SCALE_SMOOTH);
        sent_btn.setIcon(new ImageIcon(si_s));
        sent_btn.setBorderPainted(false);
        sent_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sent_btn.setIcon(new ImageIcon(si_s_hover));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sent_btn.setIcon(new ImageIcon(si_s));
            }
        });
        sent_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    outstream = new DataOutputStream(socket.getOutputStream());
                    outstream.writeUTF(textField.getText());
                    textField.setText("");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(sent_btn);
        add(vote_background);
        //test
        JButton test_btn = new JButton("Test");
        test_btn.setBounds(0, 600, 100, 50);
        test_btn.setVisible(true);
        add(test_btn);
        test_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new_round();
            }
        });
    }

    public static void main(String[] args) {
        Socket socket = new Socket();
        Game_Frame gameFrame = new Game_Frame(socket);
        gameFrame.setSize(800, 600);
        gameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
}