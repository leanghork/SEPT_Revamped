package controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import model.*;

public class MouseControl implements MouseListener, MouseMotionListener 
{
	private Model tab = null;
	private DrawingBoard theBoard = null;
	static double startX, startY, endX, endY, tempX, tempY;
	
	
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
		
		tempX = e.getX()-50;
		tempY = e.getY()-50;
		
		//System.out.println(startX);
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
				theBoard.setTempPolyObj(null);
				if(theBoard.getSelectedList().size()==0){
					theBoard.select(startX, startY,endX, endY);
				}
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

				theBoard.addShape(new PolyObj(shape,theBoard.getStrokeWidth(),theBoard.getFill(),theBoard.getStroke()));
				tab.refresh();
			}	
			break;
			
			case Model.circle:
			{
				double radius = Math.sqrt( (startX-endX)*(startX-endX) + (startY-endY)*(startY-endY) );
				
				Ellipse2D.Double shape = new Ellipse2D.Double(startX-radius, startY-radius, radius*2, radius*2);
				theBoard.addShape(new PolyObj(shape,theBoard.getStrokeWidth(),theBoard.getFill(),theBoard.getStroke()));
				
				tab.refresh();
			}
			break;
				
			case Model.line:
			{
				Line2D.Double shape = new Line2D.Double(startX,startY,endX,endY);
				theBoard.addShape(new PolyObj(shape,theBoard.getStrokeWidth(),null,theBoard.getStroke()));
				
				tab.refresh();
			}
			break;
				
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		switch(tab.currentOption())
		{
			case Model.select:
			{
				theBoard.setTempPolyObj(null);
				
				endX = e.getX()-50;
				endY = e.getY()-50;
				theBoard.moves(tempX,tempY,endX, endY);
				tempX = endX;
				tempY = endY;
				
				// drag shadow
				if(theBoard.getSelectedList().size()==0)
				{
					double tempSX, tempSY, tempEX, tempEY;
					
					tempSX = startX < endX ? startX : endX;
					tempSY = startY < endY ? startY : endY;
					tempEX = startX > endX ? startX : endX;
					tempEY = startY > endY ? startY : endY;
					
					theBoard.setTempPolyObj(new PolyObj(new Rectangle2D.Double(tempSX,tempSY,tempEX-tempSX,tempEY-tempSY),0,new Color(0,0,255,30),null));
				}
				tab.refresh();
			}	
			break;
		}
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
		switch(tab.currentOption())
		{
			case Model.select:
			{
				theBoard.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}	
			break;
			
			default:
			{
				theBoard.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}	
			break;	
			
		}

	}

	

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	

}
