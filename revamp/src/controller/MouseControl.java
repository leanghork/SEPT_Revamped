package controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import model.*;

public class MouseControl implements MouseListener, MouseMotionListener 
{
	private Model tab = null;
	private DrawingBoard theBoard = null;
	private double startX, startY, endX, endY;
	
	public MouseControl(Model tab, DrawingBoard theBoard)
	{
		this.tab = tab;
		this.theBoard = theBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		startX = e.getX()-50;
		startY = e.getY()-50;
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		endX = e.getX()-50;
		endY = e.getY()-50;
		
		switch(tab.currentOption())
		{
			case Model.select:
			{
				theBoard.select(startX, startY,endX, endY);
				
				tab.refresh();
			}	
			break;
			
			case Model.rectangle:
			{	
				double width = Math.abs(startX - endX);
				double height = Math.abs(startY - endY);
				
				if(startX>endX)
				{
					double temp = startX;
					startX = endX;
					endX = temp;
				}
				
				if(startY>endY)
				{
					double temp = startY;
					startY = endY;
					endY = temp;
				}
								
				Rectangle2D.Double shape = new Rectangle2D.Double(startX,startY,width,height);

				theBoard.addShape(new PolyObj(shape,2,Color.LIGHT_GRAY,Color.BLACK));
				tab.refresh();
			}	
			break;
			
			case Model.circle:
			{
				double radius = Math.sqrt( (startX-endX)*(startX-endX) + (startY-endY)*(startY-endY) );
				
				Ellipse2D.Double shape = new Ellipse2D.Double(startX-radius, startY-radius, radius*2, radius*2);
				theBoard.addShape(new PolyObj(shape,2,Color.LIGHT_GRAY,Color.BLACK));
				
				tab.refresh();
			}
			break;
				
			case Model.line:
			{
				Line2D.Double shape = new Line2D.Double(startX,startY,endX,endY);
				theBoard.addShape(new PolyObj(shape,2,null,Color.BLACK));
				
				tab.refresh();
			}
			break;
				
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{

	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		switch(tab.currentOption())
		{
			case Model.select:
			{
				theBoard.select(e.getX()-50, e.getY()-50);
				
				tab.refresh();
			}	
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	

}
