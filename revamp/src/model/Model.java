package model;

import controller.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.io.*;
import java.util.*;

public class Model 
	extends JTabbedPane
{
	public static final int clear = -1;
	public static final int rectangle = 0;
	public static final int circle = 1;
	public static final int line = 2;
	public static final int select = 3;
	public static final int group = 4;
	public static final int ungroup = 5;
	
	
	private int option = select;
	
	private LinkedList<DrawingBoard> board = new LinkedList<DrawingBoard>();
	
	private LinkedList<CloseTab> close = new LinkedList<CloseTab>();
	
	public Model()
	{
		this.openNew();
	}
	
	public Model(String[]args)
	{
		for(int i=0;i<args.length;i++)
		{
			File toOpen = new File(args[i]);
			
			if( this.checkFile(toOpen) )
			{
				board.add(new DrawingBoard(toOpen));
				board.getLast().addMouseListener(new MouseControl(this,board.getLast()));
				board.getLast().addMouseMotionListener(new MouseControl(this,board.getLast()));
				this.showTab();
			}
		}
		
		if(this.getTabCount() != 0)
			this.setSelectedIndex(0);
		else
			this.openNew();
	}
	
	public void setOption(int opt)
	{
		this.option = opt;
	}
	
	public int currentOption()
	{
		return option;
	}
	
	public void zoomIn()
	{
		int index = this.getSelectedIndex();
		
		if(board.get(index).getZoom() <1000)
			board.get(index).setZoom( board.get(index).getZoom()+100 );
		
		this.refresh();
	}
	
	public void zoomOut()
	{
		int index = this.getSelectedIndex();
		
		if(board.get(index).getZoom() > 100)
			board.get(index).setZoom( board.get(index).getZoom()-100 );
		
		this.refresh();
	}
	
	public void delete()
	{
		int index = this.getSelectedIndex();

		board.get(index).remove();
		
		this.refresh();
	}
	
	public void selectAll()
	{
		int index = this.getSelectedIndex();

		board.get(index).selectAll();
		
		this.refresh();
	}
	
	public void group()
	{
		int index = this.getSelectedIndex();

		board.get(index).group();
		
		this.refresh();
	}
	
	public void ungroup()
	{
		int index = this.getSelectedIndex();

		board.get(index).ungroup();
		
		this.refresh();
	}
	
	public void setFill()
	{
		int index = this.getSelectedIndex();

		board.get(index).selectFill();
	}
	
	public void setStroke()
	{
		int index = this.getSelectedIndex();

		board.get(index).selectStroke();
	}
	
	public void setStrokeWidth()
	{
		int index = this.getSelectedIndex();

		board.get(index).setStrokeWidth();
	}
		
	public void deselect()
	{
		int index = this.getSelectedIndex();

		board.get(index).deselect();
	}
	
	public boolean save(CloseTab close)
	{
		return board.get(this.indexOfTabComponent(close)).save();
	}
	
	public boolean save()
	{
		int index = this.getSelectedIndex();
		boolean save = board.get(index).save();
		
		this.setTabComponentAt(index, new CloseTab(this,board.get(index).getFileName()));
		
		return save;
	}
	
	public boolean saveAs()
	{
		int index = this.getSelectedIndex();

		return board.get(index).saveas();
	}
	
	public boolean saveAll()
	{
		boolean check = true;
		
		for(int index = board.size()-1; index>=0;index--)
		{
			this.setSelectedIndex(index);
			boolean status = board.get(index).save();
			check = check && status;
			
			if(status)
			{
				this.remove(index);
				board.remove(index);
			}
		}
		
		return check;
	}
	
	public void document()
	{
		int index = this.getSelectedIndex();

		board.get(index).document();
		
		this.refresh();
	}
	
	public void openFile()
	{
		JFileChooser op = new JFileChooser(System.getProperty("user.home"));
		
		op.setFileFilter
		(
			new javax.swing.filechooser.FileFilter()
			{
				public boolean accept(File f)
				{
					return (f.getName().toLowerCase()).endsWith("svg")||f.isDirectory();
				}
				
				public String getDescription()
				{
					return "SVG file";
				}
			}
		);
		
		if(op.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File toOpen = op.getSelectedFile();
			
			if( this.checkFile(toOpen) )
			{
				board.add(new DrawingBoard(toOpen));
				board.getLast().addMouseListener(new MouseControl(this,board.getLast()));
				board.getLast().addMouseMotionListener(new MouseControl(this,board.getLast()));
				this.showTab();
			}
		}
		
	}
	
	public void openNew()
	{
		board.add(new DrawingBoard());
		board.getLast().addMouseListener(new MouseControl(this,board.getLast()));
		board.getLast().addMouseMotionListener(new MouseControl(this,board.getLast()));
		this.showTab();
	}
	
	private void showTab()
	{
		DrawingBoard db = board.getLast();
		String title = db.getFileName();
					
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(db.getPanel());
		
		JScrollPane scroll = new JScrollPane();
		JViewport view = scroll.getViewport();
		view.add(panel);
		
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		close.add(new CloseTab(this,title));
		
		this.add(scroll);
		this.setTabComponentAt(this.getTabCount()-1, close.getLast());
		this.setSelectedIndex(this.getTabCount()-1);
	}
	
	public void closeTab(CloseTab tab)
	{
		int index = this.indexOfTabComponent(tab);
		this.remove(index);
		board.remove(index);
		
		
		if(this.getTabCount() == 0)
			openNew();
		
		this.refresh();
	}
	
	public LinkedList<DrawingBoard> getBoard()
	{
		return board;
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

	public void refresh()
	{
		this.revalidate();
		this.repaint();
	}
}
