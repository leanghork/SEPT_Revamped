package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.io.*;
import java.util.*;

public class Model 
	extends JTabbedPane
{
	public static final int rectangle = 0;
	public static final int cirle = 1;
	public static final int line = 2;
	public static final int select = 3;
	public static final int group = 4;
	
	private LinkedList<DrawingBoard> board = new LinkedList<DrawingBoard>();
	
	public Model()
	{
		board.add(new DrawingBoard());
		this.showTab();
	}
	
	public Model(String[]args)
	{
		for(int i=0;i<args.length;i++)
		{
			File toOpen = new File(args[i]);
			
			if( this.checkFile(toOpen) )
			{
				board.add(new DrawingBoard(toOpen));
				this.showTab();
			}
		}
	}
	
	private void showTab()
	{
		DrawingBoard db = board.getLast();
		String title = db.getFileName();
				
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.add(db);
		
		JScrollPane scroll = new JScrollPane();
		JViewport view = scroll.getViewport();
		view.add(panel);
		
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		this.add(scroll);
		this.setTitleAt(this.getTabCount()-1, title);
	}
		
	public boolean checkFile(File f)
	{
		
		if(!f.exists())
		{
			String msg = "File <"+f.getName()+"> does not exists";
			
			System.out.println(msg);
			JOptionPane.showMessageDialog(this,msg,"Unable to open file",JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		if(!f.getName().toLowerCase().endsWith("svg"))
		{
			String msg = "File: <"+f.getName()+"> is not a valid SVG file";
			
			System.out.println(msg);
			JOptionPane.showMessageDialog(this,msg,"Unable to open file",JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}
}
