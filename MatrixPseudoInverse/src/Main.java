import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private double[][] matris;
    private Matrix matrix;

    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    void run(){

        JFrame frame1 = new JFrame();
        JPanel jPanel = new JPanel();
        jPanel.setBounds(10,50,630,600);
        jPanel.setBackground(Color.GRAY);
        jPanel.setVisible(true);

        JButton btnRandomGenerate = new JButton("Random Oluştur");
        btnRandomGenerate.setBounds(10,10,200,40);

        btnRandomGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matrix = new Matrix();
            }
        });

        JButton btnManualGenerate = new JButton("Manual Oluştur");
        btnManualGenerate.setBounds(220,10,200,40);

        btnManualGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog(frame1,"Matris Girişi",true);
                d.setLayout(new FlowLayout());
                JTextField matrixDimensions = new JTextField("Matrix boyutunu giriniz (N,M)");
                matrixDimensions.setBounds(10,10,30,40);
                d.add(matrixDimensions);

                JButton dimOk = new JButton("OK");
                d.add(dimOk);

                JButton btnOk = new JButton("OK");
                btnOk.setVisible(false);


                dimOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dimOk.setVisible(false);
                        matrixDimensions.setVisible(false);
                        int N = Integer.parseInt(matrixDimensions.getText().split(",")[0]);
                        int M = Integer.parseInt(matrixDimensions.getText().split(",")[1]);

                        String data[][] = new String[N][M];
                        String cols[] = new String[M];

                        for (int i = 0; i < N; i++) {
                            for (int j = 0; j < M; j++) {
                                data[i][j] = "0";
                            }
                        }

                        for (int i = 0; i < M; i++) {
                            cols[i] = String.valueOf(i);
                        }

                        JTable jt = new JTable(data,cols);
                        jt.setBounds(50,50,400,400);
                        JScrollPane sp = new JScrollPane(jt);

                        d.add(sp);
                        btnOk.setVisible(true);
                    }
                });

                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JScrollPane scrollPane =  (JScrollPane) d.getContentPane().getComponent(3);
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponents()[0];
                        double[][] mat = new double[table.getModel().getRowCount()][table.getModel().getColumnCount()];
                        for (int i = 0; i < table.getModel().getRowCount(); i++) {
                            for (int j = 0; j < table.getModel().getColumnCount(); j++) {
                                mat[i][j] = Double.parseDouble(table.getModel().getValueAt(i,j).toString());
                            }
                        }

                        matris = mat;
                        d.setVisible(false);
                    }
                });

                d.add(btnOk);

                d.setSize(500,500);
                d.setVisible(true);
            }
        });

        JButton btnCalculate = new JButton("Hesapla");
        btnCalculate.setBounds(430,10,200,40);

        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(matrix == null){
                    matrix = new Matrix(matris);
                }
                double[][] tersMatris = matrix.getInverseMatrix();
                String data[][] = new String[tersMatris.length][tersMatris[0].length];

                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[0].length; j++) {
                        data[i][j] = String.valueOf(tersMatris[i][j]);
                    }
                }

                String[] cols = new String[data[0].length];
                for (int i = 0; i < cols.length; i++) {
                    cols[i] = String.valueOf(i);
                }

                JTable jTable = new JTable(data,cols);
                jTable.setBounds(10,50,630,400);
                JScrollPane sp = new JScrollPane(jTable);
                sp.setVisible(true);
                jPanel.add(sp);

                JLabel jLabel = new JLabel("Toplam Toplama : " + matrix.getToplamToplama() + " | Toplam Çarpma : " + matrix.getToplamCarpma());
                jLabel.setBounds(10,500,500,40);
                jPanel.add(jLabel);

                jPanel.revalidate();
                jPanel.repaint();
            }
        });

        frame1.add(btnRandomGenerate);
        frame1.add(btnManualGenerate);
        frame1.add(btnCalculate);
        frame1.add(jPanel);

        frame1.setSize(650,800);
        frame1.setLayout(null);
        frame1.setVisible(true);
    }
}
