package report3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static report2.M.ini;
import static report2.M.print;
import static java.lang.Math.*;

//题2.3
//对测试数据x0在样本数据x中求临近k点
public class Kapproach2 {
    public static double[][] x0 = {
            {0.14, 0.72, 4.1},
            {-0.81, 0.61, -0.38},
            {0.31, 1.51, -0.50},
    };

    public static void main(String[] args) {
        int group = 4;
        int len0 = 3;
        int dimension = 3;
        double[][][] x = ini(Kapproach1.raw3, group, len0);
        System.out.println("k：n");
        double[][]p=getP_Kapproach2(x0,x,group,x.length-1, dimension);
        System.out.println("p[i][j]: 第i个测试点类j的概率");
        print(p);
    }
    //对测试数据x0在样本数据x中求临近k点
    public static double[][] getP_Kapproach2(double[][]x0,double[][][] x, int group, int k,int dimension) {
        double[][]p=new double[x0.length][group];
        ArrayList<double[]>[][] kmin = new ArrayList[x0.length][group];
        for (int i = 0; i < kmin.length; i++) for (int j = 0; j < kmin[0].length; j++) kmin[i][j] = new ArrayList<>();

        ArrayList<Kapproach1.P> tem = new ArrayList<>();
        for (int i = 0; i < x0.length; i++) {
            for (int j = 0; j < group; j++) {
                tem.clear();

                for (int l = 0; l < x.length; l++) {
                    if (l == i) continue;
                    tem.add(new Kapproach1.P(getDis(x0[i], x[l][j]), l));
                }
                Collections.sort(tem);
                for (int l = 0; l < k; l++) {
                    int to = tem.get(l).to;
                    kmin[i][j].add(x[to][j]);
                }
                p[i][j]=1.0*kmin[i][j].size()/kmin.length/getV(x0[i],kmin[i][j],dimension);
            }
        }
        return p;
    }

    public static double getV(double[] x0, ArrayList<double[]> al, int dimension) {
        double r = getR(x0, al);
        if (dimension == 1) return r * 2;
        if (dimension == 2) return PI * r * r;
        return 4.0 / 3 * PI * r * r * r;
    }

    public static double getR(double[] x0, ArrayList<double[]> al) {
        double[] dis = new double[al.size()];
        for (int i = 0; i < al.size(); i++) dis[i] = getDis(x0, al.get(i));
        Arrays.sort(dis);
        return dis[al.size() - 1];
    }

    public static double getDis(double[] x0, double[] x1) {
        double sum = 0;
        for (int i = 0; i < x0.length; i++) sum += abs(x0[i] - x1[i]) * abs(x0[i] - x1[i]);
        return sqrt(sum);
    }

}
