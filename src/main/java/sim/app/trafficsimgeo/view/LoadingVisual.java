package sim.app.trafficsimgeo.view;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class LoadingVisual extends JDialog {
    private JProgressBar progressBar;
    private JLabel lblPorFavorEspere;

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(() -> {
//            try {
//                LoadingVisual dialog = new LoadingVisual();
//                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
////                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//                dialog.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

    /**
     * Create the dialog.
     */
    public LoadingVisual() {
        setTitle("Cargando");
        setBounds(100, 100, 292, 123);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(getProgressBar(), GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(getLblPorFavorEspere()))
                                .addContainerGap(33, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(getLblPorFavorEspere())
                                .addGap(18)
                                .addComponent(getProgressBar(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(33))
        );
        getContentPane().setLayout(groupLayout);
        initialize();
    }

    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
        }
        return progressBar;
    }

    private JLabel getLblPorFavorEspere() {
        if (lblPorFavorEspere == null) {
            lblPorFavorEspere = new JLabel("Por favor, espere...");
        }
        return lblPorFavorEspere;
    }


    private void initialize() {
        //no click out
        setModal(true);
        //center
        setLocationRelativeTo(null);
        //no close button
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //no change size
        setResizable(false);
    }

    private static LoadingVisual loadingVisual;

    public static void loadingOnOff() {
        new Thread(() -> {
            if (create()) {
                loadingVisual.setVisible(true);
            } else {
                try {
                    Thread.sleep(128);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadingVisual.dispose();
                loadingVisual = null;
            }
        }).start();

    }

    private static synchronized boolean create() {
        boolean notIsCreated = loadingVisual == null;
        if (notIsCreated) {
            loadingVisual = new LoadingVisual();
        }
        return notIsCreated;
    }
}
