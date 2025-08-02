import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class game_rule_Frame extends JFrame {

    private final JLabel rule_title;

    public game_rule_Frame(){
        super("遊戲規則");   //建立標題名稱
        super.setLayout(null);
        super.getContentPane().setBackground(new Color(43,64,91));
        super.setSize(400, 600);   //設定寬，長
        super.setLocationRelativeTo(null);


        rule_title = new JLabel("<html><h3 style='text-align:center;font-size:30px;color:white;'>遊戲規則</h3></html>");
        rule_title.setBounds(110, 20, 250,50);
        add(rule_title);


        JLabel rule_text = new JLabel("<html><h3 style='text-align:left;color:white;font-size:15px'>" +
                "1.身分共有「臥底」、「平民」。" +
                "<br>2.每回合，每個人輪流描述一句你所看到的詞彙，不可直接說出，也不可說謊。" +
                "<br>3.只有臥底的詞彙與他人不同，且每輪會進行投票，得票最高者會出局。" +
                "<br>4.當臥底被票選出局，平民勝利。</html>");
        rule_text.setBounds(20,0,350 ,450 );
        add(rule_text);

        JLabel p1img = new JLabel();
        ImageIcon imgThisImg = new ImageIcon("src/game_background.png");
        Image image = imgThisImg.getImage();
        p1img.setBounds(0,0,400,600);
        Image img = image.getScaledInstance(super.getWidth(),super.getHeight(),Image.SCALE_SMOOTH);
        p1img.setIcon(new ImageIcon(img));
        add(p1img);
    }
}
