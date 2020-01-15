package sim.app.trafficsimgeo.view;

import net.miginfocom.swing.MigLayout;
import sim.app.trafficsimgeo.logic.controller.Config;
import sim.app.trafficsimgeo.logic.controller.ConfigFinal;
import sim.app.trafficsimgeo.logic.controller.MainController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

public class NewSimulationVisual {

    private IVisualCommunicator controller;
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu mnSim;
    private JMenu mnData;
    private JMenu mnStats;
    private JMenuItem itNew;
    private JMenuItem itGenerate;
    private JMenuItem itImport;
    private JMenuItem itReport;
    private JLabel lblMap;
    private JLabel lblAverageSpeed;
    private JLabel lblKmh;
    private JLabel lblTimeArrive;
    private JLabel lblCountByArrive;
    private JLabel lblCountOfVehicles;
    private JLabel lblSecondArrive;
    private JLabel lblSecondYellow;
    private JLabel lblSecondGreen;
    private JLabel lblSecondRed;
    private JLabel lblOffenderCollisionProbability;
    private JLabel lblRedPassProbability;
    private JLabel lblProbabilityPassYellow;
    private JLabel lblBeginQuantity;
    private JLabel lblCollisionProbability;
    private JLabel lblRequired;
    private JLabel lblGetFirstProbability;
    private JLabel lblDefaultYellowTime;
    private JLabel lblDefaultGreenTime;
    private JLabel lblDefaultRedTime;
    private JLabel lblRangeCollProb;
    private JLabel lblRangeOffCollProb;
    private JLabel lblRangeRedPassProb;
    private JLabel lblRangePassYellow;
    private JLabel lblRangeGetFirst;
    private JLabel lblInfinite;
    private JComboBox pathOfMaps;
    private JSpinner averageSpeedOfVehiclesSpinner;
    private JSpinner arriveAmountSpinner;
    private JSpinner timeBetweenArrivalSpinner;
    private JSpinner cantMaxVehiclesSpinner;
    private JSpinner vehicleCollisionProbabilitySpinner;
    private JSpinner initialVehicleNumberSpinner;
    private JSpinner defaultGreenTimeSpinner;
    private JSpinner defaultRedTimeSpinner;
    private JSpinner unwiseVehicleCollisionProbabilitySpinner;
    private JSpinner probabilityOfBeingRecklessSpinner;
    private JSpinner getFirstSpinner;
    private JSpinner probabilityPassYellow;
    private JSpinner defaultYellowTimeSpinner;
    private JButton btnExit;
    private JButton btnLoadMason;


    /**
     * Create the application.
     *
     * @wbp.parser.entryPoint
     */
    public NewSimulationVisual() {
        controller = MainController.getInstance();
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH); // But the formatting in English
        EventQueue.invokeLater(() -> {
            try {
                NewSimulationVisual window = new NewSimulationVisual();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        /*frame
                .setIconImage(Toolkit
                        .getDefaultToolkit()
                        .getImage(
                                NewSimulationVisual.class
                                        .getResource("/sim/app/traficsimgeo/view/icon.png")));*/
        frame.setTitle("Simulaci\u00F3n de sem\u00E1foros");
        frame.setBounds(100, 100, 532, 492);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane()
                .setLayout(
                        new MigLayout("", "[98px,grow][][4px][52px][74px]",
                                "[20px][20px][20px][20px][20px][][][][][][][][21.00][][][][][][][][][][][][]"));
        frame.getContentPane().add(getLblMap(),
                "cell 0 0,alignx left,aligny center");
        frame.getContentPane().add(
                getPathOfMaps(), "cell 3 0,growx,aligny top");
        frame.getContentPane().add(getLblRequired(),
                "cell 4 0");
        frame.getContentPane().add(getLblAverageSpeed(),
                "cell 0 1,alignx left,aligny center");
        frame.getContentPane()
                .add(getAverageSpeedOfVehiclesSpinner(),
                        "cell 3 1,growx,aligny top");
        frame.getContentPane().add(getLblKmh(),
                "cell 4 1,alignx left,aligny center");
        frame.getContentPane().add(getLblBeginQuantity(),
                "cell 0 2");
        frame.getContentPane().add(
                getInitialVehicleNumberSpinner(), "cell 3 2,growx");
        frame.getContentPane().add(getLblTimeArrive(),
                "cell 0 3,alignx left,aligny center");
        frame.getContentPane().add(
                getTimeBetweenArrivalSpinner(), "cell 3 3,growx,aligny top");
        frame.getContentPane().add(getLblSecondArrive(),
                "cell 4 3,alignx left,aligny center");
        frame.getContentPane().add(getLblCountByArrive(),
                "cell 0 4,alignx left,aligny center");
        frame.getContentPane().add(getArriveAmountSpinner(),
                "cell 3 4,growx,aligny top");
        frame.getContentPane().add(getLblCountOfVehicles(),
                "cell 0 5,alignx left,aligny center");
        frame.getContentPane().add(
                getCantMaxVehiclesSpinner(), "cell 3 5,growx,aligny top");
        frame.getContentPane().add(getLblInfinite(),
                "cell 4 5");
        frame.getContentPane().add(getLblDefaultGreenTime(),
                "cell 0 6");
        frame.getContentPane().add(
                getDefaultGreenTimeSpinner(), "cell 3 6,growx");
        frame.getContentPane().add(getLblSecondGreen(), "cell 4 6");
        frame.getContentPane().add(getLblYellowTime(),
                "cell 0 7");
        frame.getContentPane().add(
                getDefaultYellowTimeSpinner(), "cell 3 7,growx");
        frame.getContentPane().add(getLblSecondsYellow(), "cell 4 7");

        //rojo
        frame.getContentPane().add(getLblDefaultRedTime(),
                "cell 0 8");
        frame.getContentPane().add(
                getDefaultRedTimeSpinner(), "cell 3 8,growx");
        frame.getContentPane().add(getLblSecondRed(), "cell 4 8");
        //fin rojo

        frame.getContentPane().add(
                getLblCollisionProbability(), "cell 0 9");
        frame.getContentPane().add(
                getVehicleCollisionProbabilitySpinner(), "cell 3 9,growx");
        frame.getContentPane().add(getLblRangeCollProb(), "cell 4 9");
        frame.getContentPane().add(getLblOffenderCollProb(), "cell 0 10");
        frame.getContentPane()
                .add(getUnwiseVehicleCollisionProbabilitySpinner(),
                        "cell 3 10,growx");
        frame.getContentPane().add(getLblRangeOffCollProb(), "cell 4 10");
        frame.getContentPane().add(
                getLblRedPassProbability(), "cell 0 11");
        frame.getContentPane().add(
                getProbabilityOfBeingRecklessSpinner(), "cell 3 11,growx");
        frame.getContentPane().add(getLblRangeRedPassProb(), "cell 4 11");
        frame.getContentPane().add(
                getLblProbabilityPassYellow(), "cell 0 12");
        frame.getContentPane().add(
                getProbabilityPassYellow(), "cell 3 12,growx");
        frame.getContentPane().add(getLblRangePassYellow(), "cell 4 12");
        frame.getContentPane().add(
                getLblGetFirstProbability(), "cell 0 13");
        frame.getContentPane().add(
                getGetFirstSpinner(), "cell 3 13,growx");
        frame.getContentPane().add(getLblRangeGetFirst(), "cell 4 13");
        frame.getContentPane().add(getBtnLoadMason(),
                "cell 3 14,growx");
        frame.getContentPane().add(getBtnExit(),
                "cell 4 14,growx");
        frame.setJMenuBar(getMenuBar());
        frame.setLocationRelativeTo(null);
    }

    private JMenuBar getMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getMnSim());
            menuBar.add(getMnData());
            menuBar.add(getMnStats());
        }
        return menuBar;
    }

    private JMenu getMnSim() {
        if (mnSim == null) {
            mnSim = new JMenu("Simulaci\u00F3n");
            mnSim.add(getItNew());
        }
        return mnSim;
    }

    private JMenuItem getItNew() {
        if (itNew == null) {
            itNew = new JMenuItem("Nueva");
            itNew.addActionListener(e -> cleanAction());
        }
        return itNew;
    }

    private JMenu getMnData() {
        if (mnData == null) {
            mnData = new JMenu("Datos espaciales");
            mnData.add(getItGenerate());
            mnData.add(getItImport());
        }
        return mnData;
    }

    private JMenuItem getItGenerate() {
        if (itGenerate == null) {
            itGenerate = new JMenuItem("Generar");
            itGenerate.addActionListener(e -> actionGenerateDatabaseVisualThread());
        }
        return itGenerate;
    }

    private JMenu getMnStats() {
        if (mnStats == null) {
            mnStats = new JMenu("Estad\u00EDsticos");
            mnStats.add(getItReport());
        }
        return mnStats;
    }

    private JLabel getLblMap() {
        if (lblMap == null) {
            lblMap = new JLabel("Mapa:");
        }
        return lblMap;
    }

    private JComboBox getPathOfMaps() {
        if (pathOfMaps == null) {
            pathOfMaps = new JComboBox();
            pathOfMaps.addItem("Seleccione el mapa");
            List<String> mapList = controller.getMapList();
            for (String map : mapList)
                pathOfMaps.addItem(map);
            //pathOfMaps.addActionListener(e -> System.out.println(Config.pathOfDbSpatiaLite));
        }
        return pathOfMaps;
    }

    private JLabel getLblAverageSpeed() {
        if (lblAverageSpeed == null) {
            lblAverageSpeed = new JLabel("Velocidad promedio:");
        }
        return lblAverageSpeed;
    }

    private JSpinner getAverageSpeedOfVehiclesSpinner() {
        if (averageSpeedOfVehiclesSpinner == null) {
            averageSpeedOfVehiclesSpinner = new JSpinner();
            averageSpeedOfVehiclesSpinner.setModel(new SpinnerNumberModel(
                    Config.averageSpeedOfVehicles, 10, 120, 10));
        }
        return averageSpeedOfVehiclesSpinner;
    }

    private JLabel getLblKmh() {
        if (lblKmh == null) {
            lblKmh = new JLabel("Km/h");
        }
        return lblKmh;
    }

    private JLabel getLblTimeArrive() {
        if (lblTimeArrive == null) {
            lblTimeArrive = new JLabel("Tiempo entre arribo:");
        }
        return lblTimeArrive;
    }

    private JLabel getLblCountByArrive() {
        if (lblCountByArrive == null) {
            lblCountByArrive = new JLabel("Veh\u00EDculos por arribo:");
        }
        return lblCountByArrive;
    }

    private JSpinner getArriveAmountSpinner() {
        if (arriveAmountSpinner == null) {
            arriveAmountSpinner = new JSpinner();
            arriveAmountSpinner.setModel(new SpinnerNumberModel(
                    Config.arriveAmount, 0, 3, 1));
        }
        return arriveAmountSpinner;
    }

    private JSpinner getTimeBetweenArrivalSpinner() {
        if (timeBetweenArrivalSpinner == null) {
            timeBetweenArrivalSpinner = new JSpinner();
            timeBetweenArrivalSpinner.setModel(new SpinnerNumberModel(
                    Config.timeBetweenArrival, 1, 100, 1));
        }
        return timeBetweenArrivalSpinner;
    }

    private JLabel getLblCountOfVehicles() {
        if (lblCountOfVehicles == null) {
            lblCountOfVehicles = new JLabel(
                    "Cantidad de veh\u00EDculos a simular:");
        }
        return lblCountOfVehicles;
    }

    private JSpinner getCantMaxVehiclesSpinner() {
        if (cantMaxVehiclesSpinner == null) {
            cantMaxVehiclesSpinner = new JSpinner();
            cantMaxVehiclesSpinner.setModel(new SpinnerNumberModel(
                    new Integer(Config.cantMaxVehicles),
                    new Integer(0),
                    new Integer(1000),
                    new Integer(1)));
        }
        return cantMaxVehiclesSpinner;
    }

    private JLabel getLblSecondArrive() {
        if (lblSecondArrive == null) {
            lblSecondArrive = new JLabel("segundos");
        }
        return lblSecondArrive;
    }

    private JLabel getLblSecondsYellow() {
        if (lblSecondYellow == null) {
            lblSecondYellow = new JLabel("segundos");
        }
        return lblSecondYellow;
    }

    private JMenuItem getItImport() {
        if (itImport == null) {
            itImport = new JMenuItem("Importar");
            itImport.addActionListener(e -> actionImportVisualThread());
        }
        return itImport;
    }

    private void actionImportVisualThread() {
        new Thread(() -> actionImportVisual()).start();
    }

    /**
     * Import to the project directory the new maps
     */
    private void actionImportVisual() {
        File shapeFile = null;
        File dbfFile = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Seleccione la base de datos");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DATABASE file", "db", "sqlite");
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            shapeFile = new File(selectedFile.getAbsolutePath());
            String path = shapeFile.getPath();
            //dbfFile = new File(path.substring(0, path.length() - 3) + "dbf");
        }
        if (shapeFile != null) {
            File newFile = new File(System.getProperty("user.dir") + "\\maps" + File.separator + shapeFile.getName());
            //File newDBFFile = new File(System.getProperty("user.dir") + File.separator + dbfFile.getName());
            if (shapeFile.getPath().equals(newFile.getPath()))
                JOptionPane.showMessageDialog(null, "El archivo ya est\u00E1 importado", "", JOptionPane.WARNING_MESSAGE);
            else {
                boolean ok = false;
                LoadingVisual.loadingOnOff();
                try {
                    ok = controller.copyFile(shapeFile, newFile);
                    //ok = controller.copyFile(dbfFile, newDBFFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Config.pathOfDbSpatiaLite = newFile.getName();
                if (ok)
                    resetCombo();
                LoadingVisual.loadingOnOff();
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Base de datos importada con \u00E9xito", "OK", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error importando la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void actionGenerateDatabaseVisualThread() {
        new Thread(() -> actionGenerateDatabaseVisual()).start();
    }

    private void actionGenerateDatabaseVisual() {
        File osmFile = null;
//        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "\\maps");
        jfc.setDialogTitle("Seleccione el archivo OSM");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("OSM file", "osm", "osm.pbf");
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            osmFile = new File(selectedFile.getAbsolutePath());
        }
        if (osmFile != null) {
            LoadingVisual.loadingOnOff();
            File osmFileCopy = new File(System.getProperty("user.dir") + "\\maps" + File.separator + osmFile.getName());
            //System.out.println(osmFileCopy.getPath());
            if (!osmFile.getPath().equals(osmFileCopy.getPath())) {

                try {
                    controller.copyFile(osmFile, osmFileCopy);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            boolean ok = false;
            try {
                ok = controller.spatialiteOsmNet(osmFileCopy);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoadingVisual.loadingOnOff();
            if (ok) {
                JOptionPane.showMessageDialog(null, "Base de datos generada con \u00E9xito", "OK", JOptionPane.INFORMATION_MESSAGE);
                cleanAction();
            }
        }
    }

    private boolean copyFile(File source, File dest) {
        boolean canCopy = true;
        if (dest.exists()) {
            String title = "Archivo existente";
            String message = "Ya existe el archivo:\n" + dest.getAbsolutePath() + "\nDesea reemplazarlo?";
            int opt = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                canCopy = dest.delete();
            }
            if (!canCopy)
                JOptionPane.showMessageDialog(null, "Error eliminado el archivo\n" + dest.getPath(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (canCopy) {
            try {
                LoadingVisual.loadingOnOff();
                Files.copy(source.toPath(), dest.toPath());
                LoadingVisual.loadingOnOff();
            } catch (IOException e) {
                canCopy = false;
                JOptionPane.showMessageDialog(null, "No se ha realizado la copia: \nDe: " + dest.getPath() + "\nA: " + source.getPath(), "Archivo no copiado", JOptionPane.WARNING_MESSAGE);
//                e.printStackTrace();
            }
        }
        return canCopy;
    }

    private JButton getBtnExit() {
        if (btnExit == null) {
            btnExit = new JButton("Salir");
            btnExit.addActionListener(e -> frame.dispose());
        }
        return btnExit;
    }

    private JButton getBtnLoadMason() {
        if (btnLoadMason == null) {
            btnLoadMason = new JButton("Cargar MASON");
            btnLoadMason.addActionListener(e -> {
                if (!validateMap()){
                    JOptionPane.showMessageDialog(null,
                            "Faltan datos de entrada ", "Error de datos",
                            JOptionPane.ERROR_MESSAGE);
                    getLblRequired().setVisible(true);
                }
                else if((int)getDefaultYellowTimeSpinner().getValue() > (int)getDefaultRedTimeSpinner().getValue()){
                    JOptionPane.showMessageDialog(null,
                            "El tiempo de luz amarilla debe ser menor que el tiempo de luz roja", "Error de datos",
                            JOptionPane.ERROR_MESSAGE);
                }
                else{
                    updatingConfigValues();
                    controller.actionLoadMason();
                    btnLoadMason.setEnabled(false);
                }
            });
        }
        return btnLoadMason;
    }

    private boolean validateMap(){
        return getPathOfMaps().getSelectedIndex() > 0;
    }

    private void updatingConfigValues() {
        Config.pathOfDbSpatiaLite = "maps\\" + getPathOfMaps()
                .getSelectedItem().toString();
        System.out.println(Config.pathOfDbSpatiaLite);
        Config.averageSpeedOfVehicles = (int) getAverageSpeedOfVehiclesSpinner()
                .getValue();
        Config.initialVehicleNumber = (int) getInitialVehicleNumberSpinner()
                .getValue();
        Config.timeBetweenArrival = (int) getTimeBetweenArrivalSpinner()
                .getValue();
        Config.arriveAmount = (int) getArriveAmountSpinner().getValue();
        Config.cantMaxVehicles = (int) getCantMaxVehiclesSpinner().getValue();
        Config.defaultGreenTime = (int) getDefaultGreenTimeSpinner().getValue();
        Config.defaultYellowTime = (int) getDefaultYellowTimeSpinner().getValue();
        Config.defaultRedTime = (int) getDefaultRedTimeSpinner().getValue();
        Config.vehicleCollisionProbability = (double) getVehicleCollisionProbabilitySpinner()
                .getValue();
        Config.vehicleGetFirstProbability = (double) getGetFirstSpinner()
                .getValue();
        Config.unwiseVehicleCollisionProbability = (double) getUnwiseVehicleCollisionProbabilitySpinner()
                .getValue();
        Config.probabilityOfBeingReckless = (double) getProbabilityOfBeingRecklessSpinner()
                .getValue();
        Config.probabilityPassYellow = (double) getProbabilityPassYellow()
                .getValue();
    }

    private void cleanAction() {
        resetCombo();
        getAverageSpeedOfVehiclesSpinner().setValue(
                ConfigFinal.averageSpeedOfVehicles);
        getInitialVehicleNumberSpinner().setValue(
                ConfigFinal.initialVehicleNumber);
        getTimeBetweenArrivalSpinner().setValue(ConfigFinal.timeBetweenArrival);
        getArriveAmountSpinner().setValue(ConfigFinal.arriveAmount);
        getCantMaxVehiclesSpinner().setValue(ConfigFinal.cantMaxVehicles);
        getDefaultGreenTimeSpinner().setValue(ConfigFinal.defaultGreenTime);
        getDefaultYellowTimeSpinner().setValue(ConfigFinal.defaultYellowTime);
        getDefaultRedTimeSpinner().setValue(ConfigFinal.defaultRedTime);
        getVehicleCollisionProbabilitySpinner().setValue(
                ConfigFinal.vehicleCollisionProbability);
        getGetFirstSpinner().setValue(
                ConfigFinal.vehicleGetFirstProbability);
        getUnwiseVehicleCollisionProbabilitySpinner().setValue(
                ConfigFinal.unwiseVehicleCollisionProbability);
        getProbabilityOfBeingRecklessSpinner().setValue(
                ConfigFinal.probabilityOfBeingReckless);
        getProbabilityPassYellow().setValue(
                ConfigFinal.probabilityPassYellow);
    }

    private JLabel getLblCollisionProbability() {
        if (lblCollisionProbability == null) {
            lblCollisionProbability = new JLabel(
                    "Probabilidad de colisi\u00F3n:");
        }
        return lblCollisionProbability;
    }

    private JLabel getLblGetFirstProbability() {
        if (lblGetFirstProbability == null) {
            lblGetFirstProbability = new JLabel(
                    "Probabilidad de adelantar carril:");
        }
        return lblGetFirstProbability;
    }

    private JLabel getLblProbabilityPassYellow() {
        if (lblProbabilityPassYellow == null) {
            lblProbabilityPassYellow = new JLabel(
                    "Probabilidad de pasar en amarillo:");
        }
        return lblProbabilityPassYellow;
    }

    private JLabel getLblYellowTime() {
        if (lblDefaultYellowTime == null) {
            lblDefaultYellowTime = new JLabel(
                    "Tiempo en amarillo estandar:");
        }
        return lblDefaultYellowTime;
    }

    private JSpinner getVehicleCollisionProbabilitySpinner() {
        if (vehicleCollisionProbabilitySpinner == null) {
            vehicleCollisionProbabilitySpinner = new JSpinner();
            vehicleCollisionProbabilitySpinner.setModel(new SpinnerNumberModel(
                    Config.vehicleCollisionProbability, 0.0, 1.0, 0.1));
        }
        return vehicleCollisionProbabilitySpinner;
    }

    private JSpinner getGetFirstSpinner() {
        if (getFirstSpinner == null) {
            getFirstSpinner = new JSpinner();
            getFirstSpinner.setModel(new SpinnerNumberModel(
                    Config.vehicleGetFirstProbability, 0.0, 1.0, 0.1));
        }
        return getFirstSpinner;
    }

    private JSpinner getProbabilityPassYellow() {
        if (probabilityPassYellow == null) {
            probabilityPassYellow = new JSpinner();
            probabilityPassYellow.setModel(new SpinnerNumberModel(
                    Config.probabilityPassYellow, 0.0, 1.0, 0.1));
        }
        return probabilityPassYellow;
    }

    private JSpinner getDefaultYellowTimeSpinner() {
        if (defaultYellowTimeSpinner == null) {
            defaultYellowTimeSpinner = new JSpinner();
            defaultYellowTimeSpinner.setModel(new SpinnerNumberModel(
                    Config.defaultYellowTime, 1, (int)getDefaultRedTimeSpinner().getValue() - 1, 1));
        }
        return defaultYellowTimeSpinner;
    }

    private JSpinner getDefaultRedTimeSpinner() {
        if (defaultRedTimeSpinner == null) {
            defaultRedTimeSpinner = new JSpinner();
            defaultRedTimeSpinner.setModel(new SpinnerNumberModel(
                    Config.defaultRedTime, (int)getDefaultYellowTimeSpinner().getValue(), 100, 1));
        }
        return defaultRedTimeSpinner;
    }

    private JLabel getLblRangeRedPassProb() {
        if (lblRangeRedPassProb == null) {
            lblRangeRedPassProb = new JLabel("( 0 - 1 )");
        }
        return lblRangeRedPassProb;
    }

    private JLabel getLblRangePassYellow() {
        if (lblRangePassYellow == null) {
            lblRangePassYellow = new JLabel("( 0 - 1 )");
        }
        return lblRangePassYellow;
    }

    private JLabel getLblOffenderCollProb() {
        if (lblOffenderCollisionProbability == null) {
            lblOffenderCollisionProbability = new JLabel(
                    "Probabilidad de colisi\u00F3n para infractores:");
        }
        return lblOffenderCollisionProbability;
    }

    private JLabel getLblRedPassProbability() {
        if (lblRedPassProbability == null) {
            lblRedPassProbability = new JLabel(
                    "Probabilidad de pasar en rojo:");
        }
        return lblRedPassProbability;
    }

    private JLabel getLblRangeCollProb() {
        if (lblRangeCollProb == null) {
            lblRangeCollProb = new JLabel("( 0 - 1 )");
        }
        return lblRangeCollProb;
    }

    private JLabel getLblRangeOffCollProb() {
        if (lblRangeOffCollProb == null) {
            lblRangeOffCollProb = new JLabel("( 0 - 1 )");
        }
        return lblRangeOffCollProb;
    }

    private JLabel getLblRangeGetFirst() {
        if (lblRangeGetFirst == null) {
            lblRangeGetFirst = new JLabel("( 0 - 1 )");
        }
        return lblRangeGetFirst;
    }

    private JLabel getLblBeginQuantity() {
        if (lblBeginQuantity == null) {
            lblBeginQuantity = new JLabel(
                    "Cantidad inicial de veh\u00EDculos:");
        }
        return lblBeginQuantity;
    }

    private JSpinner getInitialVehicleNumberSpinner() {
        if (initialVehicleNumberSpinner == null) {
            initialVehicleNumberSpinner = new JSpinner();
            initialVehicleNumberSpinner.setModel(new SpinnerNumberModel(
                    new Integer(Config.initialVehicleNumber), new Integer(0), new Integer(20), new Integer(1)));
        }
        return initialVehicleNumberSpinner;
    }

    private JLabel getLblInfinite() {
        if (lblInfinite == null) {
            lblInfinite = new JLabel("0 = infinito");
        }
        return lblInfinite;
    }

    private JLabel getLblDefaultGreenTime() {
        if (lblDefaultGreenTime == null) {
            lblDefaultGreenTime = new JLabel("Tiempo de verde est\u00E1ndar:");
        }
        return lblDefaultGreenTime;
    }

    private JLabel getLblSecondGreen() {
        if (lblSecondGreen == null) {
            lblSecondGreen = new JLabel("segundos");
        }
        return lblSecondGreen;
    }

    private JLabel getLblDefaultRedTime() {
        if (lblDefaultRedTime == null) {
            lblDefaultRedTime = new JLabel("Tiempo de rojo est\u00E1ndar:");
        }
        return lblDefaultRedTime;
    }

    private JLabel getLblSecondRed() {
        if (lblSecondRed == null) {
            lblSecondRed = new JLabel("segundos");
        }
        return lblSecondRed;
    }

    private JSpinner getDefaultGreenTimeSpinner() {
        if (defaultGreenTimeSpinner == null) {
            defaultGreenTimeSpinner = new JSpinner();
            defaultGreenTimeSpinner.setModel(new SpinnerNumberModel(
                    new Integer(Config.defaultGreenTime), new Integer(0), new Integer(100), new Integer(1)));
        }
        return defaultGreenTimeSpinner;
    }

    private JSpinner getUnwiseVehicleCollisionProbabilitySpinner() {
        if (unwiseVehicleCollisionProbabilitySpinner == null) {
            unwiseVehicleCollisionProbabilitySpinner = new JSpinner();
            unwiseVehicleCollisionProbabilitySpinner
                    .setModel(new SpinnerNumberModel(
                            Config.unwiseVehicleCollisionProbability, 0.0, 1.0,
                            0.01));
        }
        return unwiseVehicleCollisionProbabilitySpinner;
    }

    private JSpinner getProbabilityOfBeingRecklessSpinner() {
        if (probabilityOfBeingRecklessSpinner == null) {
            probabilityOfBeingRecklessSpinner = new JSpinner();
            probabilityOfBeingRecklessSpinner.setModel(new SpinnerNumberModel(
                    Config.probabilityOfBeingReckless, 0.0, 1.0,
                    0.01));
        }
        return probabilityOfBeingRecklessSpinner;
    }

    private void resetCombo() {
        pathOfMaps.removeAllItems();
        pathOfMaps.addItem("Seleccione el mapa");
        List<String> mapList = controller.getMapList();
        for (String map : mapList)
            pathOfMaps.addItem(map);
    }

    private JMenuItem getItReport() {
        if (itReport == null) {
            itReport = new JMenuItem("Reporte");
            itReport.addActionListener(e -> actionViewReportVisual());
        }
        return itReport;
    }

    private void actionViewReportVisual() {
        StatisticalReportVisual visual = new StatisticalReportVisual();
        visual.setLocationRelativeTo(this.frame);
        visual.setModal(true);
        visual.setVisible(true);
    }

    private JLabel getLblRequired() {
        if (lblRequired == null) {
            lblRequired = new JLabel("* Campo requerido");
            lblRequired.setForeground(Color.RED);
            lblRequired.setVisible(false);
        }
        return lblRequired;
    }

}
