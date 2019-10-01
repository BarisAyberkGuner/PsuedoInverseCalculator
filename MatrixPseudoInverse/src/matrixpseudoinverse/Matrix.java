package matrixpseudoinverse;

import java.util.Random;

public class Matrix {
    private int N,M;
    private double[][] matris;
    private static int toplamToplama = 0;
    private static int toplamCarpma = 0;

    public Matrix() {
        Random rnd = new Random();
        N = rnd.nextInt(4)+1;
        M = rnd.nextInt(4)+1;
        while (M == N){
            M = rnd.nextInt(4)+1;
        }
        matris = new double[N][M];
        randomDoldur();
    }

    public Matrix(int n, int m) {
        N = n;
        M = m;
        matris = new double[N][M];
        randomDoldur();
    }

    public Matrix(double[][] matris) {
        this.matris = matris;
        this.M = matris.length;
        this.N = matris[0].length;
    }


    private void randomDoldur(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matris[i][j] = (int) (Math.random() * 9 + 1);
            }
        }
    }

    public double[][] getInverseMatrix(){
        double Transpoz[][] = new double[N][M];
        double TersiAlinacakMatris[][] = matris;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Transpoz[i][j] = TersiAlinacakMatris[j][i];
            }
        }

        // Matrislerin carpimi
        double zM[][] = MatrisCarpimi(Transpoz, TersiAlinacakMatris);
        double zN[][] = MatrisCarpimi(TersiAlinacakMatris, Transpoz);

        // Matrislerin tersi
        double tersMatrisM[][] = TersMatris(zM);
        double tersMatrisN[][] = TersMatris(zN);

        // Sozde Matris islemi
        double sozdeMatrisM[][] = MatrisCarpimi(tersMatrisM, Transpoz);
        double sozdeMatrisN[][] = MatrisCarpimi(Transpoz, tersMatrisN);

        if(M < N){
            return sozdeMatrisN;
        }else{
            return sozdeMatrisM;
        }
    }

    // matris carpimi
    public static double[][] MatrisCarpimi(double[][] x, double[][] y) {
        double[][] result;
        int xColumns, xRows, yColumns, yRows;

        xRows = x.length;
        xColumns = x[0].length;
        yRows = y.length;
        yColumns = y[0].length;
        result = new double[xRows][yColumns];

        if (xColumns != yRows) {
            throw new IllegalArgumentException("Matrices don't match");
        }

        for (int i = 0; i < xRows; i++) {
            for (int j = 0; j < yColumns; j++) {
                for (int k = 0; k < xColumns; k++) {
                    result[i][j] += (x[i][k] * y[k][j]);
                    toplamToplama++;
                    toplamCarpma++;
                }
            }
        }

        return (result);
    }



    // UstUcgenler
    public static void UstUcgenler(double[][] matris, int[] index) {
        double[] c;
        double c0, c1, pi0, pi1, pj;
        int itmp, k;

        c = new double[matris.length];

        for (int i = 0; i < matris.length; ++i) {
            index[i] = i;
        }

        for (int i = 0; i < matris.length; ++i) {
            c1 = 0;

            for (int j = 0; j < matris.length; ++j) {
                c0 = Math.abs(matris[i][j]);

                if (c0 > c1) {
                    c1 = c0;
                }
            }

            c[i] = c1;
        }

        k = 0;

        for (int j = 0; j < (matris.length - 1); ++j) {
            pi1 = 0;

            for (int i = j; i < matris.length; ++i) {
                pi0 = Math.abs(matris[index[i]][j]);
                pi0 /= c[index[i]];

                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;

            for (int i = (j + 1); i < matris.length; ++i) {
                pj = matris[index[i]][j] / matris[index[j]][j];
                matris[index[i]][j] = pj;

                for (int l = (j + 1); l < matris.length; ++l) {
                    matris[index[i]][l] -= pj * matris[index[j]][l];
                }
            }
        }
    }

    public static double determinant(double a[][], int n) {

        double det = 0;
        int sign = 1, p = 0, q = 0;

        if (n == 1) {
            det = a[0][0];
        } else {
            double b[][] = new double[n - 1][n - 1];
            for (int x = 0; x < n; x++) {
                p = 0;
                q = 0;
                for (int i = 1; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j != x) {
                            b[p][q++] = a[i][j];
                            if (q % (n - 1) == 0) {
                                p++;
                                q = 0;
                            }
                        }
                    }
                }
                det = det + a[0][x] * determinant(b, n - 1) * sign;
                toplamToplama++;
                toplamCarpma++;
                sign = -sign;
            }
        }
        return det;
    }

    // matris tersi
    private static double[][] TersMatris(double[][] matrix) {
        double[][] inverse = new double[matrix.length][matrix[0].length];

        // minors and cofactors
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {

                inverse[i][j] = Math.pow(-1, i + j) * determinant(minor(matrix, i, j), minor(matrix, i, j).length);
                toplamCarpma++;
            }
        }

        // adjugate and determinant
        double det = 1.0 / determinant(matrix, matrix.length);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                toplamCarpma++;
                inverse[j][i] = temp * det;
                toplamCarpma++;
            }
        }

        return inverse;
    }

    private static double[][] minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; i != row && j < matrix[i].length; j++) {
                if (j != column) {
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
                }
            }
        }
        return minor;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getToplamToplama() {
        return toplamToplama;
    }

    public int getToplamCarpma() {
        return toplamCarpma;
    }
}
