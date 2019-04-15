/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author remon
 */
class FileTable extends FileDetails {

    private File currentFile;
    private static FileTable obj = null;
    private JTable table;
    private ListSelectionListener tableSelectionListener;

    public static JScrollPane tableScroll;
    /**
     * Table model for File[]
     */
    private FileTableModel fileTableModel;

    public static FileTable getTableObj() {
        if (obj == null) {
            obj = new FileTable();
        }
        return obj;
    }

    public void createJTable() {
        //Table
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //show current directory's files
        currentFile = new File(".");
        File[] files = currentFile.listFiles();
        updateTableData(files);
        //selection listener for table
        tableSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                setFileDetails(((FileTableModel) table.getModel()).getFile(row));
                currentFile = ((FileTableModel) table.getModel()).getFile(row);
            }
        };
        //mouse listener for table to open by double mouse click                         
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // Double-click detected
                    if (currentFile.isDirectory()) {
                        File[] files = FileExplorer.fileSystemView.getFiles(currentFile, true);
                        updateTableData(files);
                        FileTree.objList.updateListData(files);
                    } else if (currentFile.isFile()) {
                        try {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(currentFile);
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(tableSelectionListener);
        tableScroll = new JScrollPane(table);
        FileExplorer.detailView.add(tableScroll, BorderLayout.EAST);
    }

    /**
     * Update the table on the EDT
     */
    public void updateTableData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (fileTableModel == null) {
                    fileTableModel = new FileTableModel();
                    table.setModel(fileTableModel);
                }
                table.getSelectionModel().removeListSelectionListener(tableSelectionListener);
                fileTableModel.setFiles(files);
                table.getSelectionModel().addListSelectionListener(tableSelectionListener);
            }
        });
    }
}
