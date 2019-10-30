package report2;

//https://blog.csdn.net/qiyu93422/article/details/46921095

public class Inv {
    public static double[][] inv(double[][] data) {
        double[][] newdata = new double[data.length][data[0].length];
        double A = det(data);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if ((i + j) % 2 == 0) {
                    newdata[i][j] = det(getConfactor(data, i + 1, j + 1)) / A;
                } else {
                    newdata[i][j] = -det(getConfactor(data, i + 1, j + 1)) / A;
                }

            }
        }
        newdata = trans(newdata);

        return newdata;
    }

     static double[][] trans(double[][] newdata) {
        double[][] newdata2 = new double[newdata[0].length][newdata.length];
        for (int i = 0; i < newdata.length; i++)
            for (int j = 0; j < newdata[0].length; j++) {
                newdata2[j][i] = newdata[i][j];
            }
        return newdata2;
    }

    /*
     * 计算行列式的值
     */
    public static double det(double[][] data) {
        /*
         * 二维矩阵计算
         */
        if (data.length == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        /*
         * 二维以上的矩阵计算
         */
        double result = 0;
        int num = data.length;
        double[] nums = new double[num];
        for (int i = 0; i < data.length; i++) {
            if (i % 2 == 0) {
                nums[i] = data[0][i] * det(getConfactor(data, 1, i + 1));
            } else {
                nums[i] = -data[0][i] * det(getConfactor(data, 1, i + 1));
            }
        }
        for (int i = 0; i < data.length; i++) {
            result += nums[i];
        }

        return result;
    }

    /*
     * 求(h,v)坐标的位置的余子式
     */
     static double[][] getConfactor(double[][] data, int h, int v) {
        int H = data.length;
        int V = data[0].length;
        double[][] newdata = new double[H - 1][V - 1];
        for (int i = 0; i < newdata.length; i++) {
            if (i < h - 1) {
                for (int j = 0; j < newdata[i].length; j++) {
                    if (j < v - 1) {
                        newdata[i][j] = data[i][j];
                    } else {
                        newdata[i][j] = data[i][j + 1];
                    }
                }
            } else {
                for (int j = 0; j < newdata[i].length; j++) {
                    if (j < v - 1) {
                        newdata[i][j] = data[i + 1][j];
                    } else {
                        newdata[i][j] = data[i + 1][j + 1];
                    }
                }
            }
        }

        return newdata;
    }
}
