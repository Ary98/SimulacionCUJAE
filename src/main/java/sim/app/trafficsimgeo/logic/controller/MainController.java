package sim.app.trafficsimgeo.logic.controller;

import sim.app.trafficsimgeo.logic.util.Exporter;
import sim.app.trafficsimgeo.logic.util.FacadeOfTools;
import sim.app.trafficsimgeo.model.dao.StatisticalDAO;
import sim.app.trafficsimgeo.model.dao.StatisticalDAOImp;
import sim.app.trafficsimgeo.view.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainController implements IVisualCommunicator {

    //singleton
    private static MainController singletonInstance;

    public static MainController getInstance() {
        if (singletonInstance == null)
            singletonInstance = new MainController();
        return singletonInstance;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        NewSimulationVisual.main(null);
    }

    @Override
    public boolean spatialiteOsmNet(File osmFile) throws Exception {
        boolean ok = false;
        File dnGenerate = new File(osmFile.getPath() + ".db");
        if (!dnGenerate.exists() || dnGenerate.delete()) {
            String command = "cmd /c spatialite_osm_net -o " +
                    osmFile.getPath() +
                    " -d " +
                    osmFile.getPath() + ".db" +
//                        " -T roads -tf template_file"+
                    " -T roads -tf road_template.cfg";
            Process process = Runtime.getRuntime().exec(command);
            final int exitVal = process.waitFor();
            ok = exitVal == 0;
        }
        return ok;
    }

    @Override
    public boolean copyFile(File source, File dest) throws IOException {
        boolean canCopy = false;
        if (!dest.exists() || dest.delete()) {
            Files.copy(source.toPath(), dest.toPath());
            canCopy = true;
        }
        return canCopy;
    }

    @Override
    public boolean actionExportStatistics(JTable jtable) {
        boolean ok = false;
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Exportar a: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {
                File source = new File(jfc.getSelectedFile().getPath() + File.separator + "stats.xls");
                List tb = new ArrayList();
                List nom = new ArrayList();
                tb.add(jtable);
                nom.add("Report");

                try {
                    Exporter e = new Exporter(source, tb, nom);
                    if (e.export()){
                        return true;
                    }
                }
                catch (Exception e){
                    return false;
                }
            }
        }
        return ok;
    }

    @Override
    public void actionLoadMason() {
        try {
            new TrafficSimGeoWithUI().createController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getMapList() {
        List<String> result = new LinkedList<>();

        // Aquí la carpeta que queremos explorar
        String path = System.getProperty("user.dir") + "\\maps";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles)
            if (file.isFile()) { //and not directory
                String fileName = file.getName();
                if (fileName.endsWith(".db"))
                    result.add(fileName);
            }

        return result;
    }

    @Override
    public boolean actionClean() {
        boolean clean = false;
        // TODO Auto-generated method stub
        String title = "Los reportes se eliminar\u00E1n";
        String message = "Se van a eliminar todos los datos estad\u00EDsticos\n"
                + "Esta operaci\u00F3n no se puede deshacer...\n"
                + "\n\u00BFEst\u00E1 seguro?";
        int opt = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            StatisticalDAO dao = new StatisticalDAOImp();
            try {
                dao.removeAll();
                clean = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error", "Error", JOptionPane.ERROR_MESSAGE);
//                e.printStackTrace();
            }
        }
        return clean;
    }
}
