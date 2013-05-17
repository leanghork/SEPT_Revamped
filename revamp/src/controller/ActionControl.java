package controller;

import model.*;
import view.*;
import java.awt.event.*;

public class ActionControl 
	implements ActionListener
{
	Model tab = null;
	View UI = null;
	
	public ActionControl(View UI, Model tab)
	{
		this.UI = UI;
		this.tab = tab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		tab.refresh();
		
		switch(e.getActionCommand())
		{
			case "document":
				tab.document();
			break;
			
			case "new":
				tab.openNew();
			break;
			
			case "open":
				tab.openFile();
			break;
			
			case "save":
				tab.save();
			break;
			
			case "saveas":
				System.out.println("test");
				tab.saveAs();
			break;
			
			case "zoomin":
				tab.zoomIn();
			break;
			
			case "zoomOut":
				tab.zoomOut();
			break;
			
			case "selectall":
				tab.selectAll();
			break;
			
			case "select":
				tab.setOption(Model.select);	
			break;
			
			case "circle":
				tab.setOption(Model.circle);
				tab.deselect();
			break;
			
			case "rect":
				tab.setOption(Model.rectangle);
				tab.deselect();
			break;
			
			case "line":
				tab.setOption(Model.line);
				tab.deselect();
			break;
			
			case "group":
				tab.group();
			break;
			
			case "ungroup":
				tab.ungroup();
			break;
			
			case "delete":
				tab.delete();
			break;
			
			case "stroke":
				tab.setStroke();
			break;
			
			case "strokeWidth":
				tab.setStrokeWidth();
			break;
			
			case "fill":
				tab.setFill();
			break;
				
			default:
				System.err.println("<SVG editor> ActionListener detected invalid action in ActionControl.java...");
			break;			
		}
	}
}
