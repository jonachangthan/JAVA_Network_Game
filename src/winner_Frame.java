import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class winner_Frame extends JFrame {
    static Socket socket;
    private boolean decision; // false = 臥底贏 true = 平民贏
    private ArrayList<Integer> person;
    private String output = "";

    public winner_Frame(boolean d , ArrayList<Integer> p, Socket sockets){
        super("勝利");   //建立標題名稱
        super.setLayout(null);
        super.setSize(600, 450);   //設定寬，長
        super.setLocationRelativeTo(null);
        decision = d;
        person = p;
        socket = sockets;

//        Border redline = BorderFactory.createLineBorder(Color.RED);
        for(int i =0;i<person.size();i++)
        {
            if(person.get(i) != 0)
                output = output + " " + Integer.toString(person.get(i));
        }
        JLabel winner_who = new JLabel("玩家"+output, JLabel.CENTER);
        winner_who.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        winner_who.setForeground(Color.WHITE);
        winner_who.setBounds(80,245,420,50);
//        winner_who.setBorder(redline);
        add(winner_who);


        JButton exit_btn= new JButton();
        exit_btn.setBorderPainted(false);
        ImageIcon ext = new ImageIcon("src/exit_icon.png");
        ImageIcon ext_hover = new ImageIcon("src/exit_hover.png");
        Image ext_img = ext.getImage();
        Image ext_imgh = ext_hover.getImage();
        exit_btn.setBounds(170,300 , 250,80);
        Image ext_imgs = ext_img.getScaledInstance(exit_btn.getWidth(),exit_btn.getHeight(),Image.SCALE_SMOOTH);
        Image ext_imghs = ext_imgh.getScaledInstance(exit_btn.getWidth(),exit_btn.getHeight(),Image.SCALE_SMOOTH);
        exit_btn.setIcon(new ImageIcon(ext_imgs));
        add(exit_btn);
        exit_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exit_btn.setIcon(new ImageIcon(ext_imghs));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                exit_btn.setIcon(new ImageIcon(ext_imgs));
            }
        });
        exit_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JLabel winner_page = new JLabel();
        ImageIcon image = new ImageIcon("src/spy_win.png");
        ImageIcon image2 = new ImageIcon("src/cv_win.png");
        Image img = image.getImage();
        Image img2 = image2.getImage();
        winner_page.setBounds(-10,0,600,450);
        Image imgs = img.getScaledInstance(winner_page.getWidth(),winner_page.getHeight(),Image.SCALE_SMOOTH);
        Image imgs2 = img2.getScaledInstance(winner_page.getWidth(),winner_page.getHeight(),Image.SCALE_SMOOTH);
        if(decision) {
            winner_page.setIcon(new ImageIcon(imgs2));
            add(winner_page);
        }
        else{
            winner_page.setIcon(new ImageIcon(imgs));
            add(winner_page);
        }
    }
}
