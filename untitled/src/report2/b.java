package report2;

import static report2.M.*;

//2题
public class b {
    //    预处理出四列矩阵
    public static void main(String[] args) {
        int group = 2;
        int len0 = 2;

        double[][] x0 = new double[raw.length][4];
        int[][]x={
                {0,1},
                {0,2},
                {1,2},
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                print2(x0,group,len0,x[i][0],x[i][1],x[j][0],x[j][1]);
            }
        }
    }

     static void print2(double[][] x, int group, int len0, int x1, int x2, int x3, int x4) {
        System.out.println("类1 特征" + (x1 + 1) + (x2 + 1) + " 类2 特征" + (x3 + 1) + (x4 + 1));
        addCols(raw, x1, x, 0);
        addCols(raw, x2, x, 1);
        addCols(raw, x3 + 3, x, 2);
        addCols(raw, x4 + 3, x, 3);
        double[][][] xx = ini(x, group, len0);
        print(getU(xx, group, len0), getSigma(xx, group, len0,x.length));

    }
}
