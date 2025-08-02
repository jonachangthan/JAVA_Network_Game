import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

public class First_Frame extends JFrame {

    private boolean start_game = false;
    static Socket socket;
    private JButton start_btn;
    private JButton game_rule_btn;
    private JButton exit_btn;
    //private JLabel title_image;
    static public String user_name = null;

    public boolean get_start_game() {return start_game;}
    public void set_start_game(){start_game = true;}


    public First_Frame(Socket sockets){
        super("誰是臥底");   //建立標題名稱
        super.setLayout(null);

        super.setSize(800, 600);   //設定寬，長
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //設定預設的關閉視窗
        super.getContentPane().setBackground(new Color(43,64,91));
        super.setLocationRelativeTo(null);
        socket = sockets;

        start_btn = new JButton();
        start_btn.setBorderPainted(false);
        ImageIcon start = new ImageIcon("src/game_start.png");
        ImageIcon start_hover = new ImageIcon("src/game_start_hover.png");
        Image img3 = start.getImage();
        Image imgh3 = start_hover.getImage();
        start_btn.setBounds(270, 250, 250,80 );
        Image image3 = img3.getScaledInstance(start_btn.getWidth(),start_btn.getHeight(),Image.SCALE_SMOOTH);
        Image imageh3 = imgh3.getScaledInstance(start_btn.getWidth(),start_btn.getHeight(),Image.SCALE_SMOOTH);
        start_btn.setIcon(new ImageIcon(image3));
        add(start_btn);
        start_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                start_btn.setIcon(new ImageIcon(imageh3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                start_btn.setIcon(new ImageIcon(image3));
            }
        });
        start_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                set_start_game();
                Client.game_frame = new Game_Frame(socket);
                Client.game_frame.setSize(800, 700);   //設定寬，長
                Client.game_frame.setVisible(true);
                Client.game_frame.setLocationRelativeTo(null);
                First_Frame.super.setVisible(false);
            }
        });

        game_rule_btn= new JButton();
        game_rule_btn.setBorderPainted(false);
        ImageIcon rule = new ImageIcon("src/game_rule.png");
        ImageIcon rule_hover = new ImageIcon("src/game_rule_hover.png");
        Image img2 = rule.getImage();
        Image imgh2 = rule_hover.getImage();
        game_rule_btn.setBounds(270,350 , 250,80 );
        Image image2 = img2.getScaledInstance(game_rule_btn.getWidth(),game_rule_btn.getHeight(),Image.SCALE_SMOOTH);
        Image imageh2 = imgh2.getScaledInstance(game_rule_btn.getWidth(),game_rule_btn.getHeight(),Image.SCALE_SMOOTH);
        game_rule_btn.setIcon(new ImageIcon(image2));
        add(game_rule_btn);
        game_rule_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                game_rule_btn.setIcon(new ImageIcon(imageh2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                game_rule_btn.setIcon(new ImageIcon(image2));
            }
        });
        game_rule_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game_rule_Frame gr = new game_rule_Frame();
                gr.setVisible(true);
            }
        });

        exit_btn = new JButton();
        exit_btn.setBorderPainted(false);
        ImageIcon end = new ImageIcon("src/game_end.png");
        ImageIcon end_hover = new ImageIcon("src/game_end_hover.png");
        Image img1 = end.getImage();
        Image imgh1 = end_hover.getImage();
        exit_btn.setBounds(270, 450,250,80);
        Image image1 = img1.getScaledInstance(exit_btn.getWidth(),exit_btn.getHeight(),Image.SCALE_SMOOTH);
        Image imageh1 = imgh1.getScaledInstance(exit_btn.getWidth(),exit_btn.getHeight(),Image.SCALE_SMOOTH);
        exit_btn.setIcon(new ImageIcon(image1));
        add(exit_btn);
        exit_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exit_btn.setIcon(new ImageIcon(imageh1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                exit_btn.setIcon(new ImageIcon(image1));
            }
        });
        exit_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JLabel p1img = new JLabel();
        ImageIcon imgThisImg = new ImageIcon("src/game_first.png");
        Image image = imgThisImg.getImage();
        p1img.setBounds(0,0,800,600);
        Image img = image.getScaledInstance(super.getWidth(),super.getHeight(),Image.SCALE_SMOOTH);
        p1img.setIcon(new ImageIcon(img));
        add(p1img);
    }
}


