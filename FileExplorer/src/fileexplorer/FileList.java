package fileexplorer;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import static javax.swing.JList.HORIZONTAL_WRAP;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author remon
 */
class FileList extends FileDetails {

    private File currentFile;
    private static FileList obj = null;
    private JList displayList;
    private ListSelectionListener listSelectionListener;
    public static JScrollPane listScroll;
    /**
     * List model for File[];
     */
    private DefaultListModel fileListModel;

    public static FileList getListObj() {
        if (obj == null) {
            obj = new FileList();
        }
        return obj;
    }

    public void createJList() {
        //List    
        displayList = new JList();
        displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        displayList.setCellRenderer(new FileListCellRenderer());
        displayList.setLayoutOrientation(HORIZONTAL_WRAP);
        displayList.setVisibleRowCount(-1);
        //show current directory's files
        currentFile = new File(".");
        File[] files = currentFile.listFiles();
        updateListData(files);
        //selection listener for List
        listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = displayList.getSelectionModel().getLeadSelectionIndex();
                setFileDetails((File) displayList.getModel().getElementAt(row));
                currentFile = (File) displayList.getModel().getElementAt(row);
            }
        };

        //mouse listener for table to open by double mouse click                         
        displayList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // Double-click detected
                    if (currentFile.isDirectory()) {
                        File[] files = FileExplorer.fileSystemView.getFiles(currentFile, true);
                        updateListData(files);
                        FileTree.objTable.updateTableData(files);
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
        displayList.getSelectionModel().addListSelectionListener(listSelectionListener);
        listScroll = new JScrollPane(displayList);
        FileExplorer.detailView.add(listScroll, BorderLayout.CENTER);
        //    FileList.listScroll.setVisible(false);                             
    }

    /**
     * Update the list on the EDT
     */
    public void updateListData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (fileListModel == null) {
                    fileListModel = new DefaultListModel();
                    displayList.setModel(fileListModel);
                }
                displayList.getSelectionModel().removeListSelectionListener(listSelectionListener);
                fileListModel.removeAllElements();
                for (File file : files) {
                    fileListModel.addElement(file);
                }
                displayList.getSelectionModel().addListSelectionListener(listSelectionListener);
            }
        });
    }
}
