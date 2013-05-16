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
		switch(e.getActionCommand())
		{
			case "new":
				tab.openNew();
			break;
			
			case "open":
				tab.openFile();
			break;
			
			case "save":
			break;
			
			case "zoomin":
				tab.zoomIn();
			break;
			
			case "zoomOut":
				tab.zoomOut();
			break;
			
			case "select":
				tab.setOption(Model.select);
			break;
			
			case "circle":
				tab.setOption(Model.circle);
			break;
			
			case "rect":
				tab.setOption(Model.rectangle);
			break;
			
			case "line":
				tab.setOption(Model.line);
			break;
			
			case "group":
				tab.setOption(Model.group);
			break;
			
			case "ungroup":
				tab.setOption(Model.ungroup);
			break;
			
			case "delete":
			break;
			
			case "stroke":
			break;
			
			case "strokeWidth":
			break;
			
			case "fill":
			break;
				
			default:
			break;			
		}
	}
}
