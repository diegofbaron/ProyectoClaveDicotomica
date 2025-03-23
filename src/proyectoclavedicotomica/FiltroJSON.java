/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoclavedicotomica;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FiltroJSON extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".json");
    }

    @Override
    public String getDescription() {
        return "Archivos JSON (*.json)";
    }
}