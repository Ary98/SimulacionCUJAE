package sim.app.trafficsimgeo.view;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IVisualCommunicator {

    boolean actionExportStatistics(JTable table);

//	boolean actionImportDatabase(File spatiaLiteFile, File newDbFile);

    void actionLoadMason();

    List<String> getMapList();

    boolean actionClean();

    boolean spatialiteOsmNet(File osmFile) throws Exception;

    boolean copyFile(File source, File dest) throws IOException;

}
