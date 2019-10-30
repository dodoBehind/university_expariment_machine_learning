package report2;

import static report2.M.*;

//1题
public class a {
    //        预处理二列矩阵
    public static void main(String[] args) {
        int group = 2;
        int len0 = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double[][] x0 = new double[raw.length][2];
                addCols(raw, i, x0, 0);
                addCols(raw, j, x0, 1);
                double[][][] xx = ini(x0, group, len0);

                double[][] u = getU(xx, group, len0);
                double[][][] sigma = getSigma(xx, group, len0, xx.length);

                System.out.println("类1 特征" + (i + 1) + " 类2 特征" + (j + 1));
                print(u, sigma);
            }
        }
    }
}
