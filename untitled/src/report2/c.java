package report2;

import static report2.M.*;

//3题
public class c {
//    六列不处理
    public static void main(String[] args) {
        double[][]X=raw;
        int group = 2;
        int len0 = 3;
        double[][][] x = ini(X, group, len0);
        double[][] u = getU(x, group, len0);
        double[][][] sigma = getSigma(x, group, len0,x.length);

        System.out.println("\uD835\uDF07̂");
        print(u);
        System.out.println();
        System.out.println("\uD835\uDF0E ̂2");
        print(sigma);
    }
}
