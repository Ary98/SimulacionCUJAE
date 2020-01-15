package sim.app.trafficsimgeo.view;

import sim.app.trafficsimgeo.model.dao.StatisticalDAO;
import sim.app.trafficsimgeo.model.dao.StatisticalDAOImp;
import sim.app.trafficsimgeo.model.entity.Statistical;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import sim.app.trafficsimgeo.logic.controller.MainController;

/**
 * * @Author * @Software * @Company
 */
public class StatisticalReportVisual extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JPanel jPanelHeader = null;
    private JPanel jPanelDatos = null;
    private JPanel jPanelAction = null;
    private JPanel jPanelNorth = null;
    private JPanel jPanelCenter = null;
    // private JPanel jPanelError = null;
    private JScrollPane jScrollPane_Stats_table = null;
    private JTable jTable_Stats_table = null;
    DefaultTableModel DefaultTableModelStats_table = null;
    private JLabel jlabeltitle = null;
    private Statistical statisticalReport = null;
    private List<Statistical> listaStatisticalReport = null;
    private JButton btnSalir;
    private JButton btnExportar;
    private IVisualCommunicator controller;
    private JButton btnLimpiar;

    public StatisticalReportVisual() {
        super();
        listaStatisticalReport = new LinkedList<>();
        initialize();
        controller = MainController.getInstance();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(854, 679);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setContentPane(getJContentPane());
        RefreshStats_table();
    }

    /**
     * This method initializes jScrollPane_Stats_table
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane_Stats_table() {
        if (jScrollPane_Stats_table == null) {
            jScrollPane_Stats_table = new JScrollPane();
            jScrollPane_Stats_table.setBorder(BorderFactory.createLineBorder(
                    Color.black, 1));
            jScrollPane_Stats_table.setViewportView(getJTable_Stats_table());
            jScrollPane_Stats_table.setName("jScrollPaneStats_table");
        }
        return jScrollPane_Stats_table;
    }

    /**
     * This method initializes DefaultTableModelStats_table
     *
     * @return javax.swing.table.DefaultTableModel
     */
    private DefaultTableModel getDefaultTableModelStats_table() {
        if (DefaultTableModelStats_table == null) {
            DefaultTableModelStats_table = new DefaultTableModel() {
                /**
                 *

                 */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    // TODO Auto-generated method stub
                    if (column == 0) {
                        return true;
                    }
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    // TODO Auto-generated method stub

                    if (columnIndex == 0 || columnIndex == 11) {
                        return Boolean.class;
                    }
                    return super.getColumnClass(columnIndex);
                }
            };
            DefaultTableModelStats_table.addColumn("");
            DefaultTableModelStats_table.addColumn("Entrada");
            DefaultTableModelStats_table.addColumn("Salida");
            DefaultTableModelStats_table
                    .addColumn("Accidentes (infracci\u00F3n)");
            DefaultTableModelStats_table
                    .addColumn("Accidentes (imprudencia)");
            DefaultTableModelStats_table.addColumn("Infracciones");
            DefaultTableModelStats_table.addColumn("Infractores");
            DefaultTableModelStats_table.addColumn("Velocidad promedio");
            DefaultTableModelStats_table.addColumn("Tiempo promedio en el sistema");
            DefaultTableModelStats_table.addColumn("Tiempo de espera promedio");
            DefaultTableModelStats_table.addColumn("Tiempo total de simulaci\u00F3n");
            DefaultTableModelStats_table.addColumn("Se\u00F1alizada");
            DefaultTableModelStats_table.addColumn("Instante");
        }
        return DefaultTableModelStats_table;
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanelHeader(), BorderLayout.NORTH);
            jContentPane.add(getJPanelAction(), BorderLayout.SOUTH);
            jContentPane.add(getJPanelDatos(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanelNorth
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelNorth() {
        if (jPanelNorth == null) {
            jPanelNorth = new JPanel();
            jPanelNorth.setName("jPanelNorth");
            jPanelNorth.setLayout(new CardLayout());
        }
        return jPanelNorth;
    }

    /**
     * This method initializes jPanelCenter
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelCenter() {
        if (jPanelCenter == null) {
            jPanelCenter = new JPanel();
            jPanelCenter.setLayout(new CardLayout());
            jPanelCenter.setName("jPanelCenter");
            jPanelCenter.add(getJScrollPane_Stats_table(),
                    getJScrollPane_Stats_table().getName());
        }
        return jPanelCenter;
    }

    /**
     * This method initializes jpanelheader *
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelHeader() {
        if (jPanelHeader == null) {
            FlowLayout flowLayout3 = new FlowLayout();
            flowLayout3.setAlignment(FlowLayout.CENTER);
            jlabeltitle = new JLabel();
            jlabeltitle.setFont(new Font("Tahoma", Font.BOLD, 24));
            jlabeltitle.setText("Reportes estad\u00EDsticos");
            jPanelHeader = new JPanel();
            jPanelHeader.setLayout(flowLayout3);
            jPanelHeader.setName("jpanelheader");
            jPanelHeader.add(jlabeltitle, null);
        }
        return jPanelHeader;
    }

    /**
     * This method initializes jpanelAction
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelAction() {
        if (jPanelAction == null) {
            FlowLayout flowLayout1 = new FlowLayout();
            flowLayout1.setAlignment(FlowLayout.RIGHT);
            jPanelAction = new JPanel();
            jPanelAction.setLayout(flowLayout1);
            jPanelAction.setName("jPanelAction");
            jPanelAction.add(getBtnLimpiar());
            jPanelAction.add(getBtnExportar());
            jPanelAction.add(getBtnSalir());
            // jPanelAction.add(getJButtonInsertar(), null);
            // jPanelAction.add(getJButtonModificar(), null);
            // jPanelAction.add(getJButtonEliminar(), null);
            // jPanelAction.add(getJButtonCancelar(), null);
        }
        return jPanelAction;
    }

    /**
     * This method initializes jPanelDatos
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelDatos() {
        if (jPanelDatos == null) {
            jPanelDatos = new JPanel();
            jPanelDatos.setName("jPanelDatos");
            jPanelDatos.setLayout(new BorderLayout());
            jPanelDatos.add(getJPanelNorth(), BorderLayout.NORTH);
            // jPanelDatos.add(getJPanelError(), BorderLayout.CENTER);
            jPanelDatos.add(getJPanelCenter(), BorderLayout.CENTER);
        }
        return jPanelDatos;
    }

    /**
     * Este metodo es para refrescar la tabla *
     *
     * @return void
     */
    public void RefreshStats_table() {

        try {
            getDefaultTableModelStats_table().setRowCount(0);
            StatisticalDAO dao = new StatisticalDAOImp();
            listaStatisticalReport = dao.findAll();
            for (Statistical statisticalReport : listaStatisticalReport) {
                getDefaultTableModelStats_table().addRow(
                        new Object[]{
                                false,
                                statisticalReport.getVehicleNumberInput(),
                                statisticalReport.getVehicleNumberOutput(),
                                statisticalReport
                                        .getNumberOfAccidentsForInfringement(),
                                statisticalReport
                                        .getNumberOfAccidentsForImprudence(),
                                statisticalReport.getNumberOfInfractions(),
                                statisticalReport.getNumberOfOffenders(),
                                statisticalReport.getAverageSpeedOfVehicles(),
                                statisticalReport.getAverageSystemTime(),
                                statisticalReport.getAverageTimeOnHold(),
                                statisticalReport.getTotalSimulationTime(),
                                statisticalReport.isSignalized(),
                                statisticalReport.getMoment()});
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listaStatisticalReport.isEmpty()) {
            getBtnExportar().setEnabled(false);
            getBtnLimpiar().setEnabled(false);
        }

    }

    private JButton getBtnSalir() {
        if (btnSalir == null) {
            btnSalir = new JButton("Salir");
            btnSalir.addActionListener(e -> dispose());
        }
        return btnSalir;
    }

    private JButton getBtnExportar() {
        if (btnExportar == null) {
            btnExportar = new JButton("Exportar");
            btnExportar.addActionListener(e -> actionExportStatisticsVisualThread());
        }
        return btnExportar;
    }

    private void actionExportStatisticsVisualThread() {
        new Thread(() -> actionExportStatisticsVisual()).start();
    }

    private void actionExportStatisticsVisual() {
        if(getJTable_Stats_table().getRowCount() > 0) {
            boolean a = controller.actionExportStatistics(this.getJTable_Stats_table());
            if (a) {
                JOptionPane.showMessageDialog(null, "Los datos fueron exportados con exito", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton getBtnLimpiar() {
        if (btnLimpiar == null) {
            btnLimpiar = new JButton("Limpiar");
            btnLimpiar.addActionListener(e -> actionCleanInVisual());
        }
        return btnLimpiar;
    }

    /**
     * This method initializes jTable_Stats_table
     *
     * @return javax.swing.JTable
     */
    private JTable getJTable_Stats_table() {
        if (jTable_Stats_table == null) {
            jTable_Stats_table = new JTable(getDefaultTableModelStats_table());
            jTable_Stats_table.setShowGrid(true);
            jTable_Stats_table
                    .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTable_Stats_table.getColumn("").setMaxWidth(25);
            jTable_Stats_table.getColumn("").setWidth(25);
            jTable_Stats_table.getColumn("").setMinWidth(25);
            jTable_Stats_table.removeColumn(jTable_Stats_table.getColumn(""));
            jTable_Stats_table.removeColumn(jTable_Stats_table.getColumn("Instante"));

        }
        return jTable_Stats_table;
    }

    public void actionCleanInVisual() {
        boolean clean = controller.actionClean();
        if (clean) {
            RefreshStats_table();
        }
    }
}
