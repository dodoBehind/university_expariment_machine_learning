package report3;

import java.util.ArrayList;
import java.util.Collections;

import static report2.M.ini;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

//题2.1、2.2
//对样本i求临近的k点其他样本，x要根据取原型的哪些列来进行预处理
public class Kapproach1 {

    public static double[][] raw3 = {
            {0.28, 1.31, -6.2, 0.42, -0.087, 0.58, -0.4, 0.58, 0.089, 0.83, 1.6, -0.014},
            {0.07, 0.58, -0.78, -0.2, -3.3, -3.4, -0.31, 0.27, -0.04, 1.1, 1.6, 0.48},
            {1.54, 2.01, -1.63, 1.3, -0.32, 1.7, 0.38, 0.055, -0.035, -0.44, -0.41, 0.32},
            {-0.44, 1.18, -4.32, 0.39, 0.71, 0.23, -0.15, 0.53, 0.011, 0.047, -0.45, 1.4},
            {-0.81, 0.21, 5.73, -1.6, -5.3, -0.15, -0.35, 0.47, 0.034, 0.28, 0.35, 3.1},
            {1.52, 3.16, 2.77, -0.029, 0.89, -4.7, 0.17, 0.69, 0.1, -0.39, -0.48, 0.11},
            {2.20, 2.42, -0.19, -0.23, 1.9, 2.2, -0.011, 0.55, -0.18, 0.34, -0.079, 0.14},
            {0.91, 1.94, 6.21, 0.27, -0.3, -0.87, -0.27, 0.61, 0.12, -0.3, -0.22, 2.2},
            {0.65, 1.93, 4.38, -1.9, 0.76, -2.1, -0.065, 0.49, 0.0012, 1.1, 1.2, -0.46},
            {-0.26, 0.82, -0.96, 0.87, -1.0, -2.6, -0.12, 0.054, -0.063, 0.18, -0.11, -0.49},
    };

    public static void main(String[] args) {
        int group = 4;
        int len0 = 3;
        double[][][]x=ini(raw3,group,len0);
        new Paint("k approach", x, group, len0);
    }

    //对样本i求临近的k点其他样本，x要根据取原型的哪些列来进行预处理，group恒为1，len0恒为2
    public static ArrayList<double[]>[][] getP_Kapproach(double[][][] x, int group, int k) {
        ArrayList<double[]>[][] kmin = new ArrayList[x.length][group];
        for (int i = 0; i < kmin.length; i++) for (int j = 0; j < kmin[0].length; j++) kmin[i][j] = new ArrayList<>();

        ArrayList<P> tem = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < group; j++) {
                double[] x0 = x[i][j];
                tem.clear();

                for (int l = 0; l < x.length; l++) {
                    if (l == i) continue;
                    tem.add(new P(getDis(x0, x[l][j]), l));
                }
                Collections.sort(tem);
                for (int l = 0; l < k; l++) {
                    int to = tem.get(l).to;
                    kmin[i][j].add(x[to][j]);
                }
            }
        }
        return kmin;
    }

    public static double getDis(double[] a, double[] b) {
        double ret = 0;
        for (int i = 0; i < a.length; i++) {
            ret += abs(a[i] - b[i]) * abs(a[i] - b[i]);
        }
        return sqrt(ret);
    }

    static class P implements Comparable<P> {
        double dis;
        int to;

        public P(double dis, int to) {
            this.dis = dis;
            this.to = to;
        }

        @Override
        public int compareTo(P o) {
            return (dis - o.dis) < 0 ? -1 : 1;
        }
    }
}
