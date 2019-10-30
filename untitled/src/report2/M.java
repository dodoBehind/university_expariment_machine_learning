package report2;

import java.util.Arrays;

public class M {
    public static double[][] raw = {
            {0.011, 1.03, -0.21, 1.36, 2.17, 0.14},
            {1.27, 1.28, 0.08, 1.41, 1.45, -0.38},
            {0.13, 3.12, 0.16, 1.22, 0.99, 0.69},
            {-0.21, 1.23, -0.11, 2.46, 2.19, 1.31},
            {-2.18, 1.39, -0.19, 0.68, 0.79, 0.87},
            {0.34, 1.96, -0.16, 2.51, 3.22, 1.35},
            {-1.38, 0.94, 0.45, 0.60, 2.44, 0.92},
            {-1.02, 0.82, 0.17, 0.64, 0.13, 0.97},
            {-1.44, 2.31, 0.14, 0.85, 0.58, 0.99},
            {0.26, 1.94, 0.08, 0.66, 0.51, 0.88},

    };

    //    输入：x[i][j]第i个样本j类的向量，group类数量，len0维度
    //    输出：sigma[i]第i组的协方差矩阵
    public static double[][][] getSigma(double[][][] x, int group, int len0, int div) {
        double[][] u = getU(x, group, len0);
        double[][][] sigma = new double[group][len0][len0];

        //1/n*sigma（xk-u'）*（xk-u'）.T
        for (int i = 0; i < group; i++) {
            SUB(x, u[i], i);
            double[][] t = new double[len0][len0];
            for (int j = 0; j < x.length; j++) ADD(t, cov0(x[j][i]));
            DIVI(div, t);
            sigma[i] = t;
        }
        return sigma;
    }


    //    输入：向量a
    //    输出：a的方差
    public static double[][] cov0(double[] a) {
        double[][] row = new double[1][a.length];
        row[0] = a;
        double[][] col = new double[a.length][1];
        for (int i = 0; i < a.length; i++) col[i][0] = a[i];

        return mult(col, row);
    }


    //    输入：x[i][j]第i个样本j类的向量，group类数量，len0维度
    //    输出：u[i]第i组的均值向量
    public static double[][] getU(double[][][] x, int group, int len0) {
        double[][] u = new double[group][len0];
        //        1/n*sigma(xk)
        for (int i = 0; i < group; i++) {
            double[] t = new double[len0];
            for (int j = 0; j < x.length; j++) ADD(t, x[j][i]);
            DIVI(x.length, t);
            u[i] = t;
        }
        return u;
    }


    //    输入：样本X，group类总数，len0维度
    //    输出：x[i][j]样本i的j类的向量
    public static double[][][] ini(double[][] X, int group, int len0) {
        double[][][] ret = new double[X.length][group][];
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < group; j++) {
                int s = j * len0, e = s + len0;
                double[] t = new double[len0];
                for (int z = s, p = 0; z < e; z++, p++) t[p] = X[i][z];
                ret[i][j] = t;
            }
        }
        return ret;
    }

//    ------------------------工具方法-----------------------------------


    public static void SUB(double[][][] SUB, double[] b, int col) {
        for (int i = 0; i < SUB.length; i++) {
            for (int j = 0; j < SUB[0][0].length; j++) {
                SUB[i][col][j] -= b[j];
            }
        }
    }

    public static double[] sub(double[] x, double[] u) {
        double[] ret = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            ret[i] = x[i] - u[i];
        }
        return ret;
    }


    public static void ADD(double[] ALL, double[] b) {
        for (int i = 0; i < ALL.length; i++) {
            ALL[i] += b[i];
        }
    }

    public static void ADD(double[][] ALL, double[][] b) {
        for (int i = 0; i < ALL.length; i++) {
            for (int j = 0; j < ALL[0].length; j++) {
                ALL[i][j] += b[i][j];
            }
        }
    }

    //  n*矩阵
    public static double[][] mult(double n, double[][] a) {
        double[][] b = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = a[i][j] * n;
            }
        }
        return b;
    }

    public static double[] mult(double a, double[] b) {
        double[] ret = new double[b.length];
        for (int i = 0; i < b.length; i++) ret[i] = a * b[i];
        return ret;
    }

    //    矩阵乘法
    public static double[][] mult(double[][] a, double[][] b) {
        double[][] c = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    c[i][j] += (a[i][k] * b[k][j]);
                }
            }
        }
        return c;
    }

    //    矩阵/n
    public static void DIVI(double n, double[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] /= n;
        }
    }

    public static void DIVI(double n, double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                a[i][j] /= n;
            }
        }
    }


    public static double[][] rowToCol(double[] x) {
        double[][] ret = new double[x.length][1];
        for (int i = 0; i < x.length; i++) {
            ret[i][0] = x[i];
        }
        return ret;
    }

    public static double[][] colToRow(double[][] wi) {
        double[][] ret = new double[1][wi.length];
        for (int i = 0; i < wi.length; i++) {
            ret[0][i] = wi[i][0];
        }
        return ret;
    }


    //    输入：a样本数据，col a要加的col列，all被加数组，col2 加到all的col2列
    public static void addCols(double[][] a, int col, double[][] all, int col2) {
        for (int i = 0; i < all.length; i++) all[i][col2] = a[i][col];
    }

    public static void addCols(double[][][]a,int col,double[][][]all,int col2){
        for (int i = 0; i < a.length; i++) {
            ADD(all[i][col2],a[i][col]);
        }
    }

    public static void print(double[][] u, double[][][] sigma) {
        System.out.println("\uD835\uDF07̂");
        print(u);
        System.out.println("\uD835\uDF0E ̂2");
        print(sigma);
        System.out.println();
    }

    public static void print(double[][][] a) {
        for (int i = 0; i < a.length; i++) {
            print(a[i]);
            System.out.println();
        }
    }

    public static void print(double[][] a) {
        for (int i = 0; i < a.length; i++) print(a[i]);
    }

    public static void print(double[] a) {
        System.out.println(Arrays.toString(a));
    }

    public static void print(int[] a) { System.out.println(Arrays.toString(a)); }

}
