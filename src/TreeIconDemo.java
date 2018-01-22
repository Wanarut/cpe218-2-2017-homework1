/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

/**
 * A 1.4 application that requires the following additional files:
 *   TreeDemoHelp.html
 *    arnold.html
 *    bloch.html
 *    chan.html
 *    jls.html
 *    swingtutorial.html
 *    tutorial.html
 *    tutorialcont.html
 *    vm.html
 */

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.ImageIcon;

import java.net.URL;
import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TreeIconDemo extends JPanel implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private JTree tree;
    DefaultMutableTreeNode top;
    DefaultMutableTreeNode current;
    
    public TreeIconDemo(Node n) {
		// TODO Auto-generated constructor stub
    	super(new GridLayout(1,0));

        //Create the nodes.
        top = new DefaultMutableTreeNode(n.getNodeName());
        createNodes(n, top);

        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Set the icon for leaf nodes.
        ImageIcon leafIcon = createImageIcon("images/middle.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setClosedIcon(leafIcon);
            renderer.setOpenIcon(leafIcon);
            tree.setCellRenderer(renderer);
        } else {
            System.err.println("Leaf icon missing; using default.");
        }

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
//        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); //XXX: ignored in some releases
                                           //of Swing. bug 4101306
        //workaround for bug 4101306:
        //treeView.setPreferredSize(new Dimension(100, 100)); 

        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);
	}
    
    public String inorder(DefaultMutableTreeNode n) {
		if (n == null) return "";
		if(n == current && !n.isLeaf()) {
			return 			inorder(n.getNextNode()) + n.toString() + inorder(n.getNextNode().getNextSibling());
		}else if(Homework1.isOperate(n.toString().charAt(0)) && n != top) {
			return "(" + 	inorder(n.getNextNode()) + n.toString() + inorder(n.getNextNode().getNextSibling()) + ")";
		}else {
			return 			n.toString();
		}
	}
    
    public int calculate(DefaultMutableTreeNode n) {
    	if(n.isLeaf()) return Integer.parseInt(n.toString());
    	int first_value = calculate(n.getNextNode());
    	int second_value = calculate(n.getNextNode().getNextSibling());
    	int result = 0;
    	switch(n.toString()) {
    		case "+" :{
    			result = first_value + second_value;
    			break;
    		}
    		case "-" :{
    			result = first_value - second_value;
    			break;
    		}
    		case "*" :{
    			result = first_value * second_value;
    			break;
    		}
    		case "/" :{
    			result = first_value / second_value;
    			break;
    		}
    	}
    	return result;
    }

	/** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        current = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

        if (current == null) return;
        
//        Object nodeInfo = node.getUserObject();
        htmlPane.setFont(new Font("Angsana New", 1, 50));
        String text = inorder(current);
        if(!current.isLeaf()) text += "=" + calculate(current);
        htmlPane.setText(text);
    }
    
    private void createNodes(Node n, DefaultMutableTreeNode m) {
    	DefaultMutableTreeNode first_child = null;
        DefaultMutableTreeNode second_child = null;
        
        NodeList list = n.getChildNodes();
    	if(list.item(0) == null || list.item(1) == null) return;
        first_child = new DefaultMutableTreeNode(list.item(1).getNodeName());
        createNodes(list.item(1), first_child);
        m.add(first_child);
        
        second_child = new DefaultMutableTreeNode(list.item(0).getNodeName());
        createNodes(list.item(0), second_child);
        m.add(second_child);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TreeIconDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(Node n) {
        //Create and set up the window.
        JFrame frame = new JFrame("Binary Tree Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TreeIconDemo newContentPane = new TreeIconDemo(n);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(Node n) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(n);
            }
        });
    }
}