package sim.app.trafficsimgeo.logic.util.garbage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestCopy {
    public static void main(String[] args) {
        try {
            File h = new File("D:\\t\\hola.txt");

            System.out.println(h.getCanonicalFile());
            System.out.println(h.getName());
            copyFileUsingJava7Files(h,new File("D:\\t\\hola1.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
}
