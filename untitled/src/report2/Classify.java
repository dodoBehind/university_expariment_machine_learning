package report2;

import static java.lang.Math.*;
import static report2.Inv.det;
import static report2.Inv.inv;
import static report2.M.*;

public class Classify {

    public static int[] classify(double[][] x0, double[][][] x, double[] p, int group, int len0) {
        double[][][] sigma = getSigma(x, group, len0, x.length);
        double[][] u = getU(x, group, len0);

        int[] clas = new int[x.length];
        double[] t = new double[group];

        for (int i = 0; i < x.length; i++) {
            double min = Integer.MIN_VALUE;
            for (int j = 0; j < group; j++) {
                t[j] = g(x0[i], u[j], sigma[j], p[j]);
                if (t[j] > min) {
                    min = t[j];
                    clas[i] = j + 1;
                }
            }
        }
        return clas;
    }

    //    方程对应《模式分类第二版》P28 （49)
    public static double g(double[] x0, double[] u0, double[][] sigma0, double p) {
        double t1 = -0.5 * mult(mult(new double[][]{sub(x0, u0)}, inv(sigma0)), rowToCol(sub(x0, u0)))[0][0];
        double t2 = -x0.length / 2 * log(2 * PI);
        double t3 = -0.5 * log(det(sigma0));
        double t4 = log(abs(p));
        return t1 + t2 + t3 + t4;
    }

    //    输入：x测试点，u某个类的均值向量，sigma某个类的协方差矩阵，p某个类的先验概率
//    输出：x的判别函数值（方程对应《模式分类第二版》P32 （66）~（69））
    public static double g2(double[] x0, double[] u0, double[][] sigma0, double p) {
        double[][] Wi = mult(-0.5, inv(sigma0));
        double[][] t1 = mult(new double[][]{x0}, Wi);
        double t2 = mult(t1, rowToCol(sub(x0, u0)))[0][0];

        double[][] wi = mult(inv(sigma0), rowToCol(u0));
        double t3 = mult(colToRow(wi), rowToCol(x0))[0][0];

        double[][] t4 = mult(new double[][]{u0}, inv(sigma0));
        double[][] t5 = mult(t4, rowToCol(u0));
        double t6 = mult(-0.5, t5)[0][0];
        t6 = t6 - 0.5 * log(det(sigma0)) + log(p);

        return t2 + t3 + t6;
    }
}
