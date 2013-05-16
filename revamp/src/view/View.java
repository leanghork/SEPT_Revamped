package view;

import model.*;
import controller.*;

import java.awt.*;
import javax.swing.*;

public class View 
	extends JFrame
{
	private Model tab = null;
	
	
		
	public View(String [] args)
	{
		if(args.length != 0)
			tab = new Model(args);
		else
			tab = new Model();
				
		this.addToFrame();
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(1000,700);
		this.setVisible(true);
	}
		
	private void addToFrame()
	{
		this.setJMenuBar(this.setMenu());
		
		this.add(this.setToolbar(), BorderLayout.WEST);
		
		this.add(tab,BorderLayout.CENTER);
		
		this.addWindowListener(new ViewWindow(this));
	}
	
	private JMenuBar setMenu()
	{
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu zoom = new JMenu("Zoom");
		
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem zoomIn = new JMenuItem("Zoom In");
		JMenuItem zoomOut = new JMenuItem("Zoom Out");
		
		newFile.setActionCommand("new");
		open.setActionCommand("open");
		save.setActionCommand("save");
		zoomIn.setActionCommand("zoomin");
		zoomOut.setActionCommand("zoomOut");
			
		file.add(newFile);
		file.add(open);
		file.add(save);
		zoom.add(zoomIn);
		zoom.add(zoomOut);
		
		//add action listener
		newFile.addActionListener(new ActionControl(this,tab));
		open.addActionListener(new ActionControl(this,tab));
		save.addActionListener(new ActionControl(this,tab));
		zoomIn.addActionListener(new ActionControl(this,tab));
		zoomOut.addActionListener(new ActionControl(this,tab));
		
		
		menu.add(file);
		menu.add(zoom);
		
		return menu;
	}

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
	
	public void refresh()
	{
		this.revalidate();
		this.repaint();
	}
	
	public void close()
	{
		System.exit(0);
	}
	
	public static void main(String[]args)
	{
		new View(args);
	}
	
}
