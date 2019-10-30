package report3;

import static java.lang.Math.PI;
import static report2.Classify.g;
import static report2.M.*;

//题1
public class Parzen {
    public static double[][] raw2 = {
            {0.28, 1.31, -6.2, 0.42, -0.087, 0.58, -0.4, 0.58, 0.089, 0.83, 1.6, -0.014},
            {0.07, 0.58, -0.78, -0.2, -3.3, -3.4, -0.31, 0.27, -0.04, 1.1, 1.6, 0.48},
            {1.54, 2.01, -1.63, 1.3, -0.32, 1.7, 0.38, 0.055, -0.035, -0.44, -0.41, 0.32},
            {-0.44, 1.18, -4.32, 0.39, 0.71, 0.23, -0.15, 0.53, 0.011, 0.047, -0.45, 1.4},
            {-0.81, 0.21, 5.73, -1.6, -5.3, -0.15, -0.35, 0.47, 0.034, 0.28, 0.35, 3.1},
            {1.52, 3.16, 2.77, -0.029, 0.89, -4.7, 0.17, 0.69, 0.1, -0.39, -0.48, 0.11},
            {2.20, 2.42, -0.19, -0.23, 1.9, 2.2, -0.01, 0.55, -0.18, 0.34, -0.079, 0.14},
            {0.91, 1.94, 6.21, 0.27, -0.3, -0.87, -0.27, 0.61, 0.12, -0.3, -0.22, 2.2},
            {0.65, 1.93, 4.38, -1.9, 0.76, -2.1, -0.065, 0.49, 0.0012, 1.1, 1.2, -0.46},
            {-0.26, 0.82, -0.96, 0.87, -1.0, -2.6, -0.12, 0.054, -0.063, 0.18, -0.11, -0.49},
    };
    public static double[][] x0 = {
            {0.5, 1.0, 0.0},
            {0.41, 0.82, 0.88},
            {0.3, 0.44, -0.1},
    };

    public static void main(String[] args) {
        int group = 4;
        int len0 = 3;
        int h = 1;
        double[][][] x = ini(raw2, group, len0);
        double[][] p = getP_Parzen(x0, x, group, h);
        System.out.println("多维概率密度函数");
        print(p);
        int[] clas = classify2(x0, x, p, group, len0);
        System.out.println("\n分类结果");
        print(clas);
    }

    static double[][] getP_Parzen(double[][] x0, double[][][] x, int group, double h) {
        double v = getV(h/2);
        double[][] p = new double[x0.length][group];
        for (int k = 0; k < x0.length; k++) {

            for (int j = 0; j < group; j++) {
                for (int i = 0; i < x.length; i++) {
                    double[][] t1 = new double[][]{sub(mult(-1, x0[k]), x[i][j])};
                    double[][] t2 = rowToCol(sub(x0[k], x[i][j]));
                    double t3 = mult(t1, t2)[0][0];
                    p[k][j] += t3 / (2 * h * h * v);
                }
//                p[i][j]<0时令p[i][j]=0。
//                式（12）要求窗函数的值必须大于0 ，出现了<0的情况，
//                参考（9）之后，我打算令p[i][j]=0
                if (p[k][j] < 0) p[k][j] = 0;
                p[k][j] /= x.length;
            }
        }
        return p;
    }

    static double getV(double h){
        return 4.0/3*PI*h*h*h;
    }

    public static int[] classify2(double[][] x0, double[][][] x, double[][] p, int group, int len0) {
        double[][][] sigma = getSigma(x, group, len0, x.length);
        double[][] u = getU(x, group, len0);

        int[] clas = new int[x0.length];
        double[] t = new double[group];

        for (int i = 0; i < x0.length; i++) {
            double min = Integer.MIN_VALUE;
            for (int j = 0; j < group; j++) {
                t[j] = g(x0[i], u[j], sigma[j], p[i][j]);
                if (t[j] > min) {
                    min = t[j];
                    clas[i] = j + 1;
                }
            }

        }
        return clas;
    }
}
