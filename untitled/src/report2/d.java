package report2;

import java.util.Arrays;

import static java.lang.Math.abs;
import static report2.Classify.g;
import static report2.M.*;

//4题
public class d {
    public static void main(String[] args) {
        System.out.println("n: "+raw.length);
        System.out.println(getSigmaDiv(ini(raw, 2, 3), new double[]{1 / 2, 1 / 2}, 2, 3));
    }

    static int getSigmaDiv(double[][][] x, double[] p, int group, int len0) {
        double[][][] sigma1 = getSigma(x, group, len0, x.length);
        double[][][] sigma2 = getSigma(x, group, len0, x.length - 1);
        double[][] u = getU(x, group, len0);

        double[][] x0 = new double[1][u[0].length];
        Arrays.fill(x0[0], 1);

        double[] t1 = new double[group];
        double[] t2 = new double[group];

        for (int j = 0; j < group; j++) {
            t1[j] = g(x0[0], u[j], sigma1[j], p[j]);
            t2[j] = g(x0[0], u[j], sigma2[j], p[j]);
        }
        Arrays.sort(t1);
        Arrays.sort(t2);

        return abs(t1[t1.length - 1] - t1[t1.length - 2]) > abs(t2[t2.length - 1] - t2[t2.length - 2]) ?
                x.length : x.length - 1;
    }
}
