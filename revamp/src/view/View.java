package view;

import model.*;
import controller.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class View 
	extends JFrame
{
	private Model tab = null;
	
	/**
	 * Constructor
	 * @param args
	 */
	public View(String [] args)
	{
		if(args.length != 0)
		{
			tab = new Model(args);
		}
		else
		{
			tab = new Model();
		}
				
		this.addToFrame();
		
		this.setTitle("SVG Editor");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(1000,700);
		this.setVisible(true);
	}
		
	/**
	 * Add all components to the JFrame
	 */
	private void addToFrame()
	{
		this.setJMenuBar(this.setMenu());
		
		this.add(this.setToolbar(), BorderLayout.WEST);
		
		this.add(tab,BorderLayout.CENTER);
		
		this.addWindowListener(new ViewWindow(this));
	}
	
	/**
	 * Return a configured JMenuBar
	 * @return configured JMenuBar
	 */
	private JMenuBar setMenu()
	{
		//JMenu declarations
		JMenu file = new JMenu("File");
		JMenu view = new JMenu("View");
		JMenu edit = new JMenu("Edit");
		
		// JMenuItem declaration
		// --> Files
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save As");
		
		// --> View
		JMenuItem zoomIn = new JMenuItem("Zoom In");
		JMenuItem zoomOut = new JMenuItem("Zoom Out");
		
		// --> Edit
		JMenuItem document = new JMenuItem("Document Properties");
		JMenuItem selectAll = new JMenuItem("Select All");
		
		// Action commands
		newFile.setActionCommand("new");
		open.setActionCommand("open");
		save.setActionCommand("save");
		saveAs.setActionCommand("saveas");
		
		zoomIn.setActionCommand("zoomin");
		zoomOut.setActionCommand("zoomOut");
		
		document.setActionCommand("document");
		selectAll.setActionCommand("selectall");
			
		// Adding JMenuItem to JMenu
		file.add(newFile);
		file.add(open);
		file.add(save);
		file.add(saveAs);
				
		view.add(zoomIn);
		view.add(zoomOut);
		
		edit.add(selectAll);
		edit.add(document);
		
		//add action listener
		newFile.addActionListener(new ActionControl(this,tab));
		open.addActionListener(new ActionControl(this,tab));
		save.addActionListener(new ActionControl(this,tab));
		
		zoomIn.addActionListener(new ActionControl(this,tab));
		zoomOut.addActionListener(new ActionControl(this,tab));
		saveAs.addActionListener(new ActionControl(this,tab));
		
		document.addActionListener(new ActionControl(this,tab));
		selectAll.addActionListener(new ActionControl(this,tab));
		
		// set KeyAccelerator
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		
		zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));
		zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));
		
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		document.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
		
		// Adding to JMenuBar
		JMenuBar menu = new JMenuBar();	
		menu.add(file);
		menu.add(view);
		menu.add(edit);
		
		return menu;
	}

	/**
	 * Return a pre-configured JToolBar
	 * @return configured JToolBar
	 */
	private JToolBar setToolbar()
	{		
		JButton select = new JButton(new ImageIcon("src/icon/select.png"));
		select.setToolTipText("Select");
		select.setActionCommand("select");
		select.setFocusable(false);
		select.addActionListener(new ActionControl(this,tab));
		
		JButton circle = new JButton(new ImageIcon("src/icon/draw-ellipse-icon.png"));
		circle.setToolTipText("Draw Circle");
		circle.setActionCommand("circle");
		circle.setFocusable(false);
		circle.addActionListener(new ActionControl(this,tab));
		
		JButton rect = new JButton(new ImageIcon("src/icon/rect.png"));
		rect.setToolTipText("Draw Rectangle");
		rect.setActionCommand("rect");
		rect.setFocusable(false);
		rect.addActionListener(new ActionControl(this,tab));
		
		JButton line = new JButton(new ImageIcon("src/icon/line.png"));
		line.setToolTipText("Draw Line");
		line.setActionCommand("line");
		line.setFocusable(false);
		line.addActionListener(new ActionControl(this,tab));
		
		JButton group = new JButton(new ImageIcon("src/icon/group.png"));
		group.setToolTipText("Group");
		group.setActionCommand("group");
		group.setFocusable(false);
		group.addActionListener(new ActionControl(this,tab));
		
		JButton ungroup = new JButton(new ImageIcon("src/icon/un.png"));
		ungroup.setToolTipText("ungroup");
		ungroup.setActionCommand("ungroup");
		ungroup.setFocusable(false);
		ungroup.addActionListener(new ActionControl(this,tab));
		
		JButton delete = new JButton(new ImageIcon("src/icon/delete.png"));
		delete.setToolTipText("Delete");
		delete.setActionCommand("delete");
		delete.setFocusable(false);
		delete.addActionListener(new ActionControl(this,tab));
		
		JButton stroke = new JButton(new ImageIcon("src/icon/strokeC.png"));
		stroke.setToolTipText("Stroke Color");
		stroke.setActionCommand("stroke");
		stroke.setFocusable(false);
		stroke.addActionListener(new ActionControl(this,tab));
		
		JButton strokeWidth = new JButton(new ImageIcon("src/icon/strokeW.png"));
		strokeWidth.setToolTipText("Stroke Width");
		strokeWidth.setActionCommand("strokeWidth");
		strokeWidth.setFocusable(false);
		strokeWidth.addActionListener(new ActionControl(this,tab));
		
		JButton fill = new JButton(new ImageIcon("src/icon/fill.png"));
		fill.setToolTipText("Fill");
		fill.setActionCommand("fill");
		fill.setFocusable(false);
		fill.addActionListener(new ActionControl(this,tab));
		
		JToolBar toolB = new JToolBar("Tools", SwingConstants.VERTICAL);
		
		toolB.add(select);
		toolB.add(circle);
		toolB.add(rect);
		toolB.add(line);
		toolB.add(group);
		toolB.add(ungroup);
		toolB.add(delete);
		toolB.add(strokeWidth);
		toolB.add(stroke);
		toolB.add(fill);

		return toolB;
	}
	
	
	/**
	 * Used to revalidate and repaint JFrame
	 */
	private void refresh()
	{
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Exit the system
	 */
	public void close()
	{
		if(tab.getTabCount() > 0)
		{
			String message  = "Do you want to save SVG before exit";
			String [] option = {"Yes", "No", "Cancel"};
			
			int opt = JOptionPane.showOptionDialog(this, message, "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, option, JOptionPane.CANCEL_OPTION);
			
			if(opt == JOptionPane.YES_OPTION)
			{
				if(tab.saveAll())
					System.exit(0);
			}
			else if(opt == JOptionPane.NO_OPTION)
				System.exit(0);
			else
				return;
		}
		else
			System.exit(0);
	}
	
	public static void main(String[]args)
	{
		new View(args);
	}
	
}
