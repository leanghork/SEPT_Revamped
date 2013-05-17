package controller;

import view.*;
import java.awt.event.*;

import model.Model;

public class KeyController 
	implements KeyListener
{
	View view;
	Model tab;
	boolean ctrlHold = false;
	
	public KeyController(View view, Model tab)
	{
		this.view=view;
		this.tab = tab;
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_CONTROL)
			ctrlHold = true;
		
		if(ctrlHold && (e.getKeyCode()==KeyEvent.VK_EQUALS || e.getKeyCode()==KeyEvent.VK_ADD))
			tab.zoomIn();
		
		if(ctrlHold && (e.getKeyCode()==KeyEvent.VK_MINUS || e.getKeyCode()==KeyEvent.VK_SUBTRACT))
			tab.zoomOut();
		
		if(ctrlHold && e.getKeyCode()==KeyEvent.VK_N)
			tab.openNew();
		
		if(ctrlHold && e.getKeyCode()==KeyEvent.VK_O)
			tab.openFile();
		
		if(ctrlHold && e.getKeyCode()==KeyEvent.VK_S)
			tab.save();
		
		if(ctrlHold && e.getKeyCode()==KeyEvent.VK_A)
			tab.selectAll();

	}
	
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_CONTROL)
			ctrlHold = false;
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
}
