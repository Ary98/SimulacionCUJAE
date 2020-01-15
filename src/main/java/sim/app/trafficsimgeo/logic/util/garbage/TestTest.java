package sim.app.trafficsimgeo.logic.util.garbage;

import javax.swing.*;
import java.io.File;

public class TestTest {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir")+ File.separator);
        int a = JOptionPane.showConfirmDialog(null, "El archivo:\n"+"F"+"\nDesea reemplazarlo?", "reemplazar archivo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        System.out.println(a==JOptionPane.YES_OPTION);
    }
}
