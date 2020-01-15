package sim.app.trafficsimgeo.logic.util;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class Exporter {

    private File file;
    private List table;
    private List nom_files;

    public Exporter(File file, List table, List nom_files) throws Exception{
        this.file = file;
        this.table = table;
        this.nom_files = nom_files;
        if(nom_files.size() != table.size()){
            throw new Exception("Error");
        }
    }

    public boolean export(){
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            WritableWorkbook w = Workbook.createWorkbook(out);
            for (int index = 0; index < table.size(); index++){
                JTable tabla = (JTable) table.get(index);
                WritableSheet s = w.createSheet(String.valueOf(nom_files.get(index)), 0);
                for (int i = 0; i < tabla.getColumnCount(); i++){
                    for (int j = 0; j < tabla.getRowCount(); j++){
                        Object object = tabla.getValueAt(j, i);
                        s.addCell(new Label(i, j, String.valueOf(object)));
                    }
                }
            }
            w.write();
            w.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}
