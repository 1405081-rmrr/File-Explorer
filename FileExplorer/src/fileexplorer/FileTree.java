/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author remon
 */
class FileTree extends FileDetails{

    private static FileTree obj = null;
    public static FileTable objTable = FileTable.getTableObj();
    public static FileList objList = FileList.getListObj();
    private DefaultMutableTreeNode root;
    private JTree tree;
    /**
     * Tree model for File[].
     */

    private DefaultTreeModel treeModel;
    private TreeSelectionListener treeSelectionListener;
    public JProgressBar progressBar;
    public JScrollPane treeScroll;
    public static FileTree getTreeObj() {
        if (obj == null) {
            obj = new FileTree();
        }
        return obj;
    }

    public void createJTree() {
        // the File tree
        root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);

        treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                DefaultMutableTreeNode node
                        = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
                showChildren(node);
                setFileDetails((File) node.getUserObject());
            }
        };
        // show the file system roots.
    //    File[] roots = FileExplorer.fileSystemView.getRoots();
   /* to show only drive directories */
        File[] roots = File.listRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add(node);
            File[] files = FileExplorer.fileSystemView.getFiles(fileSystemRoot, true);
        
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(treeSelectionListener);
        tree.setCellRenderer(new FileTreeCellRenderer());
        tree.expandRow(0);
        treeScroll = new JScrollPane(tree);
        tree.setVisibleRowCount(15);
        Dimension preferredSize = treeScroll.getPreferredSize();
        Dimension widePreferred = new Dimension(200, (int) preferredSize.getHeight());
        treeScroll.setPreferredSize(widePreferred);
        
        
         JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    treeScroll,
                    FileExplorer.detailView);
            FileExplorer.gui.add(splitPane, BorderLayout.CENTER);

            JPanel simpleOutput = new JPanel(new BorderLayout(3, 3));
            progressBar = new JProgressBar();
            simpleOutput.add(progressBar, BorderLayout.EAST);
            progressBar.setVisible(true);

            FileExplorer.gui.add(simpleOutput, BorderLayout.SOUTH);
    }

    /**
     * Add the files that are contained within the directory of this node.
     */
    private void showChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                File[] files = null;
                if (file.isDirectory()) {
                    files = FileExplorer.fileSystemView.getFiles(file, true);
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if (child.isDirectory() || child.isFile()) {
                                publish(child);
                            }
                        }
                    }
                } else if (file.isFile()) {
                    file = file.getParentFile();
                    files = FileExplorer.fileSystemView.getFiles(file, true);
                }
                objTable.updateTableData(files);
                objList.updateListData(files);
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }

}
