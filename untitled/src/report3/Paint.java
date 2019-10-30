package report3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static java.lang.Math.*;

//画板程序
public class Paint extends JFrame {
    int weight = 900;
    int hight = 700;

    MyPanel panel;
    int sam, cla, k;

    double[][][] x;
    int group;
    int len0;
    int xx, yy;

    public Paint(String title, double[][][] x, int group, int len0) {
        super(title);
        this.group = group;
        this.len0 = len0;
        this.x = x;

        sam = cla = 0;
        k = 1;
        xx = yy = -1;

        setSize(weight, hight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //三个下拉菜单、paint按钮
        JMenuBar bar = new JMenuBar();
        JComboBox<String> box1 = new JComboBox<>();
        JComboBox<String> box2x = new JComboBox<>();
        JComboBox<String> box2y = new JComboBox<>();
        JComboBox<String> box3 = new JComboBox<>();

        for (int i = 0; i < x.length; i++) box1.addItem("样本" + (i + 1));
        box1.addItemListener(new MyItemListener());

        box2x.addItem("x值 全0");
        box2y.addItem("y值 全0");
        for (int i = 0; i < group*len0; i++) {
            box2x.addItem("x值 类" + (i / len0 + 1) + " 特征" + (i % len0 + 1));
            box2y.addItem("y值 类" + (i / len0 + 1) + " 特征" + (i % len0 + 1));
        }
        box2x.addItemListener(new MyItemListener());
        box2y.addItemListener(new MyItemListener());

        for (int i = 0; i < x.length; i++) box3.addItem("k值" + (i + 1));
        box3.addItemListener(new MyItemListener());

        JButton button = new JButton("paint");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double[][][] newX = getNewX();
                panel.setKmin(Kapproach1.getP_Kapproach(newX, 1, k));

                panel.flush();
                panel.printCoordinate();
                panel.printX0();
                panel.printKmin();
            }
        });

        bar.add(box1);
        bar.add(box2x);
        bar.add(box2y);
        bar.add(box3);
        bar.add(button);
        add(bar, BorderLayout.NORTH);

        panel = new MyPanel();
        add(panel);

        //获得len
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                for (int l = 0; l < x[0][0].length; l++) {
                    max = max(max, max(abs(x[i][j][0]), abs(x[i][j][1])));
                }
            }
        }
        panel.setLen((int) ceil(max));

        setResizable(false);
        panel.setVisible(true);
        setVisible(true);
    }

    double[][][] getNewX() {
        double[][][] newX = new double[x.length][1][2];
        if (xx == -1) for (int i = 0; i < newX.length; i++) newX[i][0][0] = 0;
        else for (int i = 0; i < newX.length; i++) newX[i][0][0] = x[i][xx / len0][xx % len0];

        if (yy == -1) for (int i = 0; i < newX.length; i++) newX[i][0][1] = 0;
        else for (int i = 0; i < newX.length; i++) newX[i][0][1] = x[i][yy / len0][yy % len0];

        return newX;
    }

    class MyItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String s = e.paramString().split(",")[1];
            int i;
            for (i = s.length() - 1; i >= 0; i--) if (!('0' <= s.charAt(i) && s.charAt(i) <= '9')) break;
            int t = Integer.parseInt(s.substring(i + 1));
            if (s.contains("样")) sam = t - 1;
            else if (s.contains("k值")) k = t;
            else if (s.contains("x值") && s.contains("类")) xx = t - 1;
            else if (s.contains("x值") && s.contains("0")) xx = -1;
            else if (s.contains("y值") && s.contains("类")) yy = t - 1;
            else if (s.contains("y值") && s.contains("0")) yy = -1;
        }
    }

    class MyPanel extends JPanel {
        //最大的x、y，决定坐标轴的边界
        int len;
        ArrayList<double[]>[][] kmin;
        final int r = 4;

        public MyPanel() {
            setDoubleBuffered(true);
        }


        public void setKmin(ArrayList<double[]>[][] kmin) {
            this.kmin = kmin;
        }

        public void setLen(int len) {
            this.len = len;
        }

        //清空画板
        void flush() {
            Graphics g = getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

        }

        //画坐标轴
        void printCoordinate() {
            Graphics g = getGraphics();
            g.setColor(Color.BLACK);

            g.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2);
            g.drawLine(getWidth() / 2, 10, getWidth() / 2, getHeight() - 10);

            g.drawLine(getWidth() / 2, 10, getWidth() / 2 - 5, 10 + 5);
            g.drawLine(getWidth() / 2, 10, getWidth() / 2 + 5, 10 + 5);
            g.drawLine(getWidth() - 10, getHeight() / 2, getWidth() - 10 - 5, getHeight() / 2 - 5);
            g.drawLine(getWidth() - 10, getHeight() / 2, getWidth() - 10 - 5, getHeight() / 2 + 5);

        }

        //用红色打印测试点
        void printX0() {
            Graphics g = getGraphics();
            g.setColor(Color.RED);
            int[] t = trans(x[sam][cla]);
            g.fillOval(t[0] - r, t[1] - r, r * 2, r * 2);
        }

        //用蓝色打印最近k个点
        void printKmin() {
            Graphics g = getGraphics();
            g.setColor(Color.BLUE);
            for (int i = 0; i < kmin[sam][cla].size(); i++) {
                int[] t = trans(kmin[sam][cla].get(i));
                g.fillOval(t[0] - r, t[1] - r, r * 2, r * 2);
            }
        }

        //输入：数据对
        //输出：像素对
        int[] trans(double[] a) {
            int x0 = xx == -1 ? getWidth()/2 : (int) (a[0] * getWidth() / (2 * len) + getWidth() / 2* weight / hight);
            int y0 = yy == -1 ? getHeight() / 2 : (int) (-a[1] * getHeight() / (2 * len) + getHeight() / 2);
            return new int[]{x0, y0};
        }
    }

}
