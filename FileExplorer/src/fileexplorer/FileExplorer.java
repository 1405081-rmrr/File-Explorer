package fileexplorer;

import static fileexplorer.FileDetails.detailView;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;

class FileExplorer {

    private FileTree objTree = FileTree.getTreeObj();

    /* memubar & items */
    private JMenuBar menuBar;
    private JMenu options;
    private JMenuItem tableView;
    private JMenuItem listView;
            /**
     * frame for all components
     */
    public static JFrame f;
         /* File details for Tree,Table & List */
    public static JLabel fileName;
    public static JTextField path;

    /**
     * Provides nice icons and names for files.
     */
    public static FileSystemView fileSystemView;



    /**
     * Main GUI container
     */
    public static JPanel gui;
    public static JPanel detailView;

    /**
     * Directory listing
     */
    /*Jlist for list view */


    public void createJMenu() {
        menuBar = new JMenuBar();
        f.setJMenuBar(menuBar);
        options = new JMenu("Options");
        menuBar.add(options);
    }

    public void displayTableView() {
        tableView = new JMenuItem("List View");
        tableView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                FileTable.tableScroll.setVisible(true);
                FileList.listScroll.setVisible(false);
                detailView.add(FileTable.tableScroll, BorderLayout.NORTH);
            }
        }
        );
        options.add(tableView);
    }

    public void displayListView() {
        listView = new JMenuItem("Tile View");
        listView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                FileList.listScroll.setVisible(true);
                FileTable.tableScroll.setVisible(false);
                detailView.add(FileList.listScroll, BorderLayout.CENTER);

            }
        });
        options.add(listView);
    }

    public void selectTableOrListView() {
        /**
         * selection between list view and tile view
         */
        createJMenu();
        displayTableView();
        displayListView();
    }



    public Container fileGUI() throws IOException {
        if (gui == null) {
            gui = new JPanel(new BorderLayout(3, 3));
            gui.setBorder(new EmptyBorder(5, 5, 5, 5));
            fileSystemView = FileSystemView.getFileSystemView();
//panel for table and list
            detailView = new JPanel(new BorderLayout(3, 3));
            detailView.setVisible(true);

            FileTree.objTable.createJTable();

            FileTree.objList.createJList();

            selectTableOrListView();

            objTree.createJTree();

            showFileDetails();

        }

        return gui;
    }

        public static void showFileDetails() {
        // details for a File( File + Path/name )
        JPanel fileMainDetails = new JPanel(new BorderLayout(2, 2));
        fileMainDetails.setBorder(new EmptyBorder(0, 6, 0, 6));
        JPanel fileDetailsLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        fileMainDetails.add(fileDetailsLabels, BorderLayout.WEST);
        JPanel fileDetailsValues = new JPanel(new GridLayout(0, 1, 2, 2));
        fileMainDetails.add(fileDetailsValues, BorderLayout.CENTER);
        fileDetailsLabels.add(new JLabel("File", JLabel.TRAILING));
        fileName = new JLabel();
        fileDetailsValues.add(fileName);
        fileDetailsLabels.add(new JLabel("File Path", JLabel.TRAILING));
        path = new JTextField(5);
        path.setEditable(false);
        fileDetailsValues.add(path);
        JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
        int count = fileDetailsLabels.getComponentCount();
        for (int ii = 0; ii < count; ii++) {
            fileDetailsLabels.getComponent(ii).setEnabled(false);
        }
        count = flags.getComponentCount();
        for (int ii = 0; ii < count; ii++) {
            flags.getComponent(ii).setEnabled(false);
        }
        JPanel fileView = new JPanel(new BorderLayout(3, 3));
        fileView.add(fileMainDetails, BorderLayout.CENTER);
        detailView.add(fileView, BorderLayout.PAGE_START);
    }

    /*
    public void showRootFile() {
        // ensure the main files are displayed
        tree.setSelectionInterval(0, 0);
    }
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                     //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                f = new JFrame("FILE_EXPLORER");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                FileExplorer FileBrowser = new FileExplorer();
                try {
                    f.setContentPane(FileBrowser.fileGUI());
                } catch (IOException ex) {
                }
                f.pack();
                f.setLocationByPlatform(true);
                f.setMinimumSize(f.getSize());
                f.setVisible(true);

                //    FileBrowser.showRootFile();
            }
        });
    }

}
