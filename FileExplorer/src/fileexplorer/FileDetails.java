/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer;

import static fileexplorer.FileExplorer.f;
import static fileexplorer.FileExplorer.fileName;
import static fileexplorer.FileExplorer.fileSystemView;
import static fileexplorer.FileExplorer.gui;
import static fileexplorer.FileExplorer.path;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author remon
 */
class FileDetails {

    public static JPanel detailView;
    /*
      currently selected File.
     */
    private static File currentFile;


    /**
     * Update the File details view with the details of this File.
     */
    public void setFileDetails(File file) {
        currentFile = file;
        Icon icon = fileSystemView.getSystemIcon(file);
        fileName.setIcon(icon);
        fileName.setText(fileSystemView.getSystemDisplayName(file));
        path.setText(file.getPath());
        f = (JFrame) gui.getTopLevelAncestor();
        if (f != null) {
            f.setTitle("FILE_EXPLORER");
        }
        gui.repaint();
    }

}
