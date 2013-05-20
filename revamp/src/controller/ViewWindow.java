package controller;

import view.*;

import java.awt.event.*;

import javax.swing.JOptionPane;

public class ViewWindow 
	implements WindowListener
{
	private View view = null;
	
	public ViewWindow(View view)
	{
		this.view = view;
	}
	
	public void windowClosed(WindowEvent e)
	{
		
	}
	
	public void windowClosing(WindowEvent e)
	{
		view.close();
	}
	
	public void windowOpened(WindowEvent e)
	{
		
	}

	public void windowActivated(WindowEvent e)
	{
		
	}
	
	public void windowDeactivated(WindowEvent e)
	{
		
	}
	
	public void windowIconified(WindowEvent e)
	{
	}
	
	public void windowDeiconified(WindowEvent e)
	{
		
	}
}
