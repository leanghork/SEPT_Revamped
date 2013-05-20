package model;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.*;
import javax.swing.border.*;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Leanghork, Marcel, Kelvin, Addy
 *
 */
public class DrawingBoard 
	extends JPanel
{
	private static int gID = 1;
	
	private static int newCount = 1;
	private String fileName = null;
	
	private File toRead = null;
	private Dimension size;
	private int zoom = 100;
	
	private int strokeWidth = 2;
	private Color stroke = Color.BLACK;
	private Color fill = Color.BLUE;
	
	private LinkedList<PolyObj> shapes = new LinkedList<PolyObj>();
	private LinkedList<PolyObj> selected = new LinkedList<PolyObj>();
	
	private PolyObj shadow = null;
	private PolyObj group = null;
	
	private int point = 0;
	private PolyObj pointed = null;
		
	/**
	 * Create a clean drawing board
	 */
	public DrawingBoard()
	{
		this.fileName = "New file" + (newCount++);
		
		this.size = new Dimension(700,700);
		this.setSize();
	}
	
	//*************************************************************
	//* Constructor                                               *
	//*************************************************************
	
	/**
	 * Open SVG file
	 * @param toRead
	 */
	public DrawingBoard(File toRead)
	{
		this.toRead = toRead;
		
		this.read(toRead);
		this.defaultSize();
		this.setSize();
	}
		
	/**
	 * Get DrawingBoard
	 * @return Return the drawing board enclosed in a panel
	 */
	public JPanel getPanel()
	{
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(Color.BLACK,2));
		panel.add(this);
		
		return panel;
	}
		
	//*************************************************************
	//* Drawing                                                   *
	//*************************************************************
	
	/**
	 * Get current zoom
	 * @return the current zoom value %
	 */
	public int getZoom()
	{
		return zoom;
	}
	
	/**
	 * Set zoom 
	 * @param zoom
	 */
	public void setZoom(int zoom)
	{
		this.zoom = zoom;
		this.setSize();
		this.refresh();
	}
	
	//*************************************************************
	//* Drawing                                                   *
	//*************************************************************
	
	/**
	 * Add shape to the list of shapes
	 * @param toAdd PolyObj to add to the drawing board
	 */
	public void addShape(PolyObj toAdd)
	{
		this.shapes.add(toAdd);
		
		this.refresh();
	}
	
	/**
	 * Delete the selected shape(s)/group(s)
	 */
	public void remove()
	{		
		removeSelectedDuplicate();
				
		for(int i=selected.size()-1; i>=0 ;i--)
		{
			if(selected.get(i).checkGroup() == 0)
			{
				for(int j=shapes.size()-1; j>=0 ; j--)
				{
					if(selected.get(i).equals(shapes.get(j)))
					{
						shapes.remove(j);
						selected.remove(i);
						break;
					}
				}		
			}
			else
			{
				for(int j=shapes.size()-1; j>=0 ; j--)
				{
					if(!selected.get(i).equals(shapes.get(j)) && selected.get(i).checkGroup() == shapes.get(j).checkGroup())
					{
						shapes.remove(j);
					}
				}
				shapes.remove(selected.get(i));
			}
		}
		
		this.selected.clear();
		
		this.refresh();
	}
	
	/**
	 * Get the current stroke width
	 * @return currently selected strokewidth color
	 */
	public int getStrokeWidth()
	{
		return strokeWidth;
	}
	
	/**
	 * Set the stroke width
	 */
	public void setStrokeWidth()
	{
		String strWidth = JOptionPane.showInputDialog(this, "Enter strokewidth", Integer.toString(strokeWidth));
		
		if(strWidth == null)
			return;
		
		else if(Pattern.matches( "[\\x00-\\x20]*[+]?(\\p{Digit}+)(\\.)?(\\p{Digit}+)?[\\x00-\\x20]*", strWidth))
		{
			strokeWidth = java.lang.Integer.parseInt(strWidth);
		
		
			this.removeSelectedDuplicate();
			for(int i=0;i<selected.size();i++)
			{
				if(selected.get(i).checkGroup() == 0)
					((PolyObj)selected.get(i)).strokeWidth = strokeWidth;
				else
				{
					for(int j=0; j<shapes.size();j++)
					{
						if(selected.get(i).checkGroup() == shapes.get(j).checkGroup())
						{
							shapes.get(j).strokeWidth = strokeWidth;
						}
					}
				}
			}
			this.refresh();
		}
		
		else
			JOptionPane.showMessageDialog(this, "Invalid input detected", "Error", JOptionPane.ERROR_MESSAGE);	
			
		this.refresh();
			
	}
	
	/**
	 * Select the stroke color
	 */
	public void selectStroke()
	{
		Color temp = null;
		temp = JColorChooser.showDialog(this,"Select stroke color",stroke);
		
		if(temp!= null)
		{
			stroke = temp;	

			this.removeSelectedDuplicate();
			for(int i=0;i<selected.size();i++)
			{
				if(selected.get(i).checkGroup() == 0)
					((PolyObj)selected.get(i)).stroke = stroke;
				else
				{
					for(int j=0; j<shapes.size();j++)
					{
						if(selected.get(i).checkGroup() == shapes.get(j).checkGroup())
						{
							shapes.get(j).stroke = stroke;
						}
					}
				}
			}
		
			this.refresh();
		}
	}
	
	/**
	 * Get the current stroke color
	 * @return currently selected stroke color
	 */
	public Color getStroke()
	{
		return stroke;
	}
	
	/**
	 * Select the fill color
	 */
	public void selectFill()
	{
		Color temp = null;
		temp = JColorChooser.showDialog(this,"Select fill color",fill);
		
		if(temp!=null)
		{
			fill = temp;	
		
			this.removeSelectedDuplicate();
			for(int i=0;i<selected.size();i++)
			{
				if(selected.get(i).checkGroup() == 0)
					((PolyObj)selected.get(i)).fill = fill;
				else
				{
					for(int j=0; j<shapes.size();j++)
					{
						if(selected.get(i).checkGroup() == shapes.get(j).checkGroup())
						{
							shapes.get(j).fill = fill;
						}
					}
				}
			}
			
			this.refresh();
		}
	}
	
	/**
	 * Get the current fill color
	 * @return currently selected fill color
	 */
	public Color getFill()
	{
		return fill;
	}
	
	//*************************************************************
	//* Select                                                    *
	//*************************************************************
	
	/**
	 * Select a specific shape/group at the point where mouse click is detected
	 * @param X mouse x-coordinate
	 * @param Y mouse y-coordinate
	 */
	public void select(double X, double Y)
	{
		this.selected.clear();
				
		for(int i = shapes.size()-1; i>=0; i--)
		{
			PolyObj toSelect = shapes.get(i);
			
			Rectangle2D.Double area = new Rectangle2D.Double(X-toSelect.strokeWidth-2,Y-toSelect.strokeWidth-2,toSelect.strokeWidth *2+4, toSelect.strokeWidth*2+4);
			
			if(toSelect.shape.intersects(area))
			{
				selected.add(toSelect);
				break;
			}
		}
		
		removeSelectedDuplicate();
	}
		
	/**
	 * Select multiple shape/group by dragging the mouse cursor on the drawing board
	 * @param startX initial mouse x-coordinate
	 * @param startY initial mouse y-coordinate
	 * @param endX final mouse x-coordinate
	 * @param endY final mouse x-coordinate
	 */
	public void select(double startX, double startY, double endX, double endY)
	{
		this.selected.clear();
		
		double width = Math.abs(startX-endX);
		double height = Math.abs(startY-endY);
		
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
		
		Rectangle2D.Double area = new Rectangle2D.Double(startX,startY,width,height);
		
		for(int i = shapes.size()-1; i>=0; i--)
		{
			PolyObj toSelect = shapes.get(i);
			
			if(toSelect.shape.intersects(area) && !selected.contains(toSelect))
				selected.add(toSelect);
		}
		
		removeSelectedDuplicate();
		
		this.refresh();
	}
	
	
	/**
	 * Select everything on the drawing board
	 */
	public void selectAll()
	{
		selected.clear();
		
		for(int i=0;i<shapes.size();i++)
		{
			selected.add(shapes.get(i));
		}
		
		removeSelectedDuplicate();
		this.refresh();
	}
	
	
	/**
	 * Get the list of selected shapes
	 * @return Number of currently selected shapes
	 */
	public int getSelectedCount()
	{
		return selected.size();
	}
	
	
	/**
	 * Set drag shadow
	 * @param temp 
	 */
	public void setTempPolyObj(PolyObj temp)
	{
		shadow = temp;
		
		this.refresh();
	}
	
	
	/**
	 * Deselect all selected shape(s)/group(s)
	 */
	public void deselect()
	{
		selected.clear();
		refresh();
	}
	
	//*************************************************************
	//* Editting                                                  *
	//*************************************************************
	
	/**
	 * Shift shape(s) or group(s) location
	 * @param startX initial mouse x-coordinate
	 * @param startY initial mouse y-coordinate
	 * @param endX final mouse x-coordinate
	 * @param endY final mouse y-coordinate
	 */
	public void moves(double startX, double startY, double endX, double endY)
	{
		double moveX = endX - startX;
		double moveY = endY - startY;
		
		removeSelectedDuplicate();
		for(int i=0; i<selected.size(); i++)
		{
			PolyObj toMove = selected.get(i);
			Rectangle2D area = new Rectangle2D.Double(startX-toMove.strokeWidth-2,startY-toMove.strokeWidth-2,toMove.strokeWidth *2+4, toMove.strokeWidth*2+4);
			
			if(toMove.checkGroup()==0 && toMove.shape.intersects(area))
			{
				if(toMove.shape instanceof Rectangle2D)
				{
					double x = ((Rectangle2D)toMove.shape).getX() + moveX;
					double y = ((Rectangle2D)toMove.shape).getY() + moveY;
					double w = ((Rectangle2D)toMove.shape).getWidth();
					double h = ((Rectangle2D)toMove.shape).getHeight();
	
					toMove.replaceShape(new Rectangle2D.Double(x,y,w,h));
					
					this.refresh();
				}
				
				if(toMove.shape instanceof Ellipse2D)
				{
					double x = ((Ellipse2D)toMove.shape).getX() + moveX;
					double y = ((Ellipse2D)toMove.shape).getY() + moveY;
					double w = ((Ellipse2D)toMove.shape).getWidth();
					double h = ((Ellipse2D)toMove.shape).getHeight();
	
					toMove.replaceShape(new Ellipse2D.Double(x,y,w,h));
					
					this.refresh();
				}
				
				if(toMove.shape instanceof Line2D)
				{
					double x1 = ((Line2D)toMove.shape).getX1() + moveX;
					double y1 = ((Line2D)toMove.shape).getY1() + moveY;
					double x2 = ((Line2D)toMove.shape).getX2() + moveX;
					double y2 = ((Line2D)toMove.shape).getY2() + moveY;
	
					toMove.replaceShape(new Line2D.Double(x1,y1,x2,y2));
					
					this.refresh();
				}
			}
			
			else if(toMove.checkGroup() != 0)
			{
				MaxMinValue value = this.getMinMax(toMove);
				
				Rectangle2D.Double group = new Rectangle2D.Double(value.minX, value.minY, value.maxX-value.minX, value.maxY-value.minY);
				
				if(group.intersects(area))
				{
					for(int j=0; j<shapes.size();j++)
					{
						if(toMove.checkGroup() == shapes.get(j).checkGroup())
						{
							if(shapes.get(j).shape instanceof Rectangle2D)
							{
								double x = ((Rectangle2D)shapes.get(j).shape).getX() + moveX;
								double y = ((Rectangle2D)shapes.get(j).shape).getY() + moveY;
								double w = ((Rectangle2D)shapes.get(j).shape).getWidth();
								double h = ((Rectangle2D)shapes.get(j).shape).getHeight();
				
								shapes.get(j).replaceShape(new Rectangle2D.Double(x,y,w,h));
								
								this.refresh();
							}
							
							if(shapes.get(j).shape instanceof Ellipse2D)
							{
								double x = ((Ellipse2D)shapes.get(j).shape).getX() + moveX;
								double y = ((Ellipse2D)shapes.get(j).shape).getY() + moveY;
								double w = ((Ellipse2D)shapes.get(j).shape).getWidth();
								double h = ((Ellipse2D)shapes.get(j).shape).getHeight();
				
								shapes.get(j).replaceShape(new Ellipse2D.Double(x,y,w,h));
								
								this.refresh();
							}
							
							if(shapes.get(j).shape instanceof Line2D)
							{
								double x1 = ((Line2D)shapes.get(j).shape).getX1() + moveX;
								double y1 = ((Line2D)shapes.get(j).shape).getY1() + moveY;
								double x2 = ((Line2D)shapes.get(j).shape).getX2() + moveX;
								double y2 = ((Line2D)shapes.get(j).shape).getY2() + moveY;
				
								shapes.get(j).replaceShape(new Line2D.Double(x1,y1,x2,y2));
								
								this.refresh();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Resize the selected object 
	 * @param X current x-coordinate of mouse
	 * @param Y current y-coordinate of mouse
	 */
	public void resizeSelected(double X, double Y)
	{
		this.removeSelectedDuplicate();

		for(int i=0; i<selected.size();i++)
		{
			if(selected.get(i).checkGroup() == 0  && selected.size() == 1)
			{
				switch(point)
				{
					case 1:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = X;
							double minY = Y;
							double maxX = theShape.getMaxX();
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(maxX - width, minY, Math.abs(width), height));
								point = 2;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY + height, width, Math.abs(height)));
								point = 4;
							}
							
							this.refresh();
						}
						
						if(selected.get(i).shape instanceof Ellipse2D)
						{
							Ellipse2D theShape = (Ellipse2D)selected.get(i).shape;
							
							double minX = X;
							double minY = Y;
							double maxX = theShape.getMaxX();
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Ellipse2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(maxX - width, minY, Math.abs(width), height));
								point = 2;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(minX, minY + height, width, Math.abs(height)));
								point = 4;
							}
							
							this.refresh();
						}
						
						if(selected.get(i).shape instanceof Line2D)
						{
							Line2D theShape = (Line2D)selected.get(i).shape;
							
							double x1 = X;
							double y1 = Y;
							
							double x2 = theShape.getX2();
							double y2 = theShape.getY2();
							
							selected.get(i).replaceShape(new Line2D.Double(x1,y1,x2,y2));
							
							this.refresh();
						}
						
					}
						break;
						
					case 2:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = X;
							double maxX = Y;
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX + width, minY, Math.abs(width), height));
								point = 1;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX, maxY - height, width, Math.abs(height)));
								point = 3;
							}
							
							this.refresh();
						}
						
						if(selected.get(i).shape instanceof Ellipse2D)
						{
							Ellipse2D theShape = (Ellipse2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = X;
							double maxX = Y;
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Ellipse2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(minX + width, minY, Math.abs(width), height));
								point = 1;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(minX, maxY - height, width, Math.abs(height)));
								point = 3;
							}
							
							this.refresh();
						}
						
						if(selected.get(i).shape instanceof Line2D)
						{
							Line2D theShape = (Line2D)selected.get(i).shape;
							
							double x2 = X;
							double y2 = Y;
							
							double x1 = theShape.getX1();
							double y1 = theShape.getY1();
							
							selected.get(i).replaceShape(new Line2D.Double(x1,y1,x2,y2));
							
							this.refresh();
						}
					}			
						break;
						
					case 3:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = theShape.getMinY();
							double maxX = X;
							double maxY = Y;
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX + width, minY, Math.abs(width), height));
								point = 4;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX, maxY - height, width, Math.abs(height)));
								point = 2;
							}
							
							this.refresh();
						}
						
						if(selected.get(i).shape instanceof Ellipse2D)
						{
							Ellipse2D theShape = (Ellipse2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = theShape.getMinY();
							double maxX = X;
							double maxY = Y;
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Ellipse2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(minX + width, minY, Math.abs(width), height));
								point = 4;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(minX, maxY - height, width, Math.abs(height)));
								point = 2;
							}
							
							this.refresh();
						}
					}
						break;
						
					case 4:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = X;
							double minY = theShape.getMinY();
							double maxX = theShape.getMaxX();
							double maxY = Y;
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(maxX - width, minY, Math.abs(width), height));
								point = 3;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY + height, width, Math.abs(height)));
								point = 1;
							}
							
							this.refresh();
						}
						
						if(selected.get(i).shape instanceof Ellipse2D)
						{
							Ellipse2D theShape = (Ellipse2D)selected.get(i).shape;
							
							double minX = X;
							double minY = theShape.getMinY();
							double maxX = theShape.getMaxX();
							double maxY = Y;
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Ellipse2D.Double(minX, minY, width, height));
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(maxX - width, minY, Math.abs(width), height));
								point = 3;
							}
							
							if(height < 0)
							{
								selected.get(i).replaceShape(new Ellipse2D.Double(minX, minY + height, width, Math.abs(height)));
								point = 1;
							}
							
							this.refresh();
						}
					}
						break;
						
					case 5:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = Y;
							double maxX = theShape.getMaxX();
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
														
							if(height < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX, maxY - height, width, Math.abs(height)));
								point = 7;
							}
							
							this.refresh();
						}
					}
						break;
						
					case 6:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = theShape.getMinY();
							double maxX = X;
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
								
							if(width < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX + width, minY, Math.abs(width), height));
								point = 8;
							}
														
							this.refresh();
						}
					}
						break;
						
					case 7:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = theShape.getMinX();
							double minY = theShape.getMinY();
							double maxX = theShape.getMaxX();
							double maxY = Y;
							
							double width = maxX - minX;
							double height = maxY - minY;
														
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
														
							if(height < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY + height, width, Math.abs(height)));
								point = 5;
							}
							
							this.refresh();
						}
					}
						break;
						
					case 8:
					{
						if(selected.get(i).shape instanceof Rectangle2D)
						{
							Rectangle2D theShape = (Rectangle2D)selected.get(i).shape;
							
							double minX = X;
							double minY = theShape.getMinY();
							double maxX = theShape.getMaxX();
							double maxY = theShape.getMaxY();
							
							double width = maxX - minX;
							double height = maxY - minY;
							
							selected.get(i).replaceShape(new Rectangle2D.Double(minX, minY, width, height));
									
							
							if(width < 0)
							{
								selected.get(i).replaceShape(new Rectangle2D.Double(minX + width, minY, Math.abs(width), height));
								point = 6;
							}
														
							this.refresh();
						}
					}
						break;
					default:
						break;
				}
			}
		}
	}
	
	/**
	 * Find the anchor of selected shape to be used with resize function
	 * @param X mouse current x-coordinate
	 * @param Y mouse current y-coordinate
	 */
	public void trackMouse(double X, double Y)
	{
		point = 0;
		pointed = null;
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.removeSelectedDuplicate();
		
		Rectangle2D.Double cursor = new Rectangle2D.Double(X - 2.5, Y - 2.5, 5, 5);
		
		for(int i=0;i<selected.size();i++)
		{
			
			
			if(selected.get(i).checkGroup() == 0)
			{
				if(selected.get(i).shape instanceof Line2D)
				{
					Line2D theSelected = (Line2D)selected.get(i).shape;
					
					double x1, x2, y1,y2;
									
					x1 = theSelected.getX1() < theSelected.getX2() ? theSelected.getX1() : theSelected.getX2();
					x2 = theSelected.getX1() > theSelected.getX2() ? theSelected.getX1() : theSelected.getX2();
					
					y1 = theSelected.getX1() < theSelected.getX2() ? theSelected.getY1() : theSelected.getY2();
					y2 = theSelected.getX1() > theSelected.getX2() ? theSelected.getY1() : theSelected.getY2();

					if(cursor.intersects(new Rectangle2D.Double(x1-3.5, y1-3.5, 7, 7)))
					{
						point = 1;
						pointed = selected.get(i);
						
						if(y1 > y2)
							this.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
						else
							this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));

					}
					if(cursor.intersects(new Rectangle2D.Double(x2-3.5, y2-3.5, 7, 7)))
					{			
						point = 2;
						pointed = selected.get(i);
						
						if(y1 > y2)
							this.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
						else
							this.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					}
				}
				
				if(selected.get(i).shape instanceof Rectangle2D)
				{
					Rectangle2D theSelected = (Rectangle2D)selected.get(i).shape;
					
					double x = theSelected.getX() - 5 - selected.get(i).strokeWidth/2;
					double y = theSelected.getY() - 5 - selected.get(i).strokeWidth/2;
					double width = theSelected.getWidth() + 10 + selected.get(i).strokeWidth;
					double height = theSelected.getHeight() + 10 + selected.get(i).strokeWidth;
					
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 ,y-3.5 ,7 ,7)))
					{
						point = 1;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width ,y-3.5 ,7 ,7)))
					{
						point = 2;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height,7 ,7)))
					{
						point = 3;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 ,y-3.5 + height,7 ,7)))
					{
						point = 4;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width/2 ,y-3.5 ,7 ,7)))
					{
						point = 5;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height/2 ,7 ,7)))
					{
						point = 6;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width/2 ,y-3.5 + height,7 ,7)))
					{
						point = 7;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 ,y-3.5 + height/2,7 ,7)))
					{
						point = 8;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					}
				}
				
				if(selected.get(i).shape instanceof Ellipse2D)
				{
					Ellipse2D theSelected = (Ellipse2D)selected.get(i).shape;
					
					double x = theSelected.getX() - 5 - selected.get(i).strokeWidth/2;
					double y = theSelected.getY() - 5 - selected.get(i).strokeWidth/2;
					double width = theSelected.getWidth() + 10 + selected.get(i).strokeWidth;
					double height = theSelected.getHeight() + 10 + selected.get(i).strokeWidth;
					
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 ,y-3.5 ,7 ,7)))
					{
						point = 1;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width ,y-3.5 ,7 ,7)))
					{
						point = 2;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height,7 ,7)))
					{
						point = 3;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					}
					if(cursor.intersects(new Rectangle2D.Double(x-3.5 ,y-3.5 + height,7 ,7)))
					{
						point = 4;
						pointed = selected.get(i);
						
						this.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					}
				}
				
			}
			
		}
	}
	
	/**
	 * Get the resize anchor selected
	 * @return the anchor point of selected shape for resize
	 */
	public int getAnchor()
	{
		return point;
	}
	
	
	//*************************************************************
	//* Group                                                     *
	//*************************************************************
	
	/**
	 * Group shapes/groups together
	 */
	public void group()
	{				
		removeSelectedDuplicate();
		
		for(int i=0; i<selected.size(); i++)
		{
			if(selected.get(i).checkGroup() != 0)
			{
				for(int j=shapes.size()-1; j>=0; j--)
				{
					if( !shapes.get(j).equals(selected.get(i)) && shapes.get(j).checkGroup() == selected.get(i).checkGroup())
						shapes.get(j).group(gID);
				}
				selected.get(i).group(gID);				
			}
			else
			{
				selected.get(i).group(gID);
			}
		}
		
		if(!selected.isEmpty())
		{
			PolyObj temp = selected.getLast();
			selected.clear();
			selected.add(temp);
		}
			
		gID++;
		this.refresh();
	}
	
	/**
	 * Ungroup grouped shapes/groups
	 */
	public void ungroup()
	{
		removeSelectedDuplicate();

		for(int i=selected.size()-1; i>=0 ; i--)
		{
			if(selected.get(i).checkGroup() != 0)
			{
				for(int j=0; j<shapes.size(); j++)
				{
					if(selected.get(i).checkGroup() == shapes.get(j).checkGroup() && !selected.get(i).equals(shapes.get(j)))
					{
						shapes.get(j).ungroup();
						
						if(!selected.contains(shapes.get(j)))
							selected.add(shapes.get(j));
					}
				}
				
				selected.get(i).ungroup();
			}
		}
		
		this.refresh();
	}
	
	/**
	 * Remove shapes of the same group from the selected list
	 */
	private void removeSelectedDuplicate()
	{
		for(int i=0; i<selected.size();i++)
		{			
			for(int j=selected.size()-1; j>i; j--)
			{
				if(selected.get(j).checkGroup()!=0 && selected.get(j).checkGroup() == selected.get(i).checkGroup())
					selected.remove(j);
			}
		}
	}
	
	/**
	 * Get bound for grouped shapes
	 * @param selects selected object
	 * @return maximum value of x and y coordinate of shapes in a group
	 */
	private MaxMinValue getMinMax(PolyObj selects)
	{
		double minX=0, maxX=0, minY=0, maxY=0;
		boolean started = false;
				
		for(int i=shapes.size()-1; i>=0; i--)
		{
			PolyObj toCheck = shapes.get(i);
			
			if(toCheck.checkGroup() == selects.checkGroup())
			{
				
				if(toCheck.shape instanceof Line2D)
				{
					Line2D line = (Line2D)toCheck.shape;
					
					if(!started)
					{							
						minX = line.getX1() < line.getX2() ? line.getX1() : line.getX2();
						maxX = line.getX1() > line.getX2() ? line.getX1() : line.getX2();
						minY = line.getY1() < line.getY2() ? line.getY1() : line.getY2();
						minY = line.getY1() > line.getY2() ? line.getY1() : line.getY2();
						
						started = true;
					}
					else
					{
						minX = minX < line.getX1() ? minX : line.getX1();
						minX = minX < line.getX2() ? minX : line.getX2();
						
						maxX = maxX > line.getX1() ? maxX : line.getX1();
						maxX = maxX > line.getX2() ? maxX : line.getX2();
						
						minY = minY < line.getY1() ? minY : line.getY1();
						minY = minY < line.getY2() ? minY : line.getY2();
						
						maxY = maxY > line.getY1() ? maxY : line.getY1();
						maxY = maxY > line.getY2() ? maxY : line.getY2();
					}
				}
				
				if(toCheck.shape instanceof Rectangle2D)
				{
					Rectangle2D theShape = (Rectangle2D)toCheck.shape;
					
					if(!started)
					{
						minX = theShape.getMinX();
						minY = theShape.getMinY();
						maxX = theShape.getMaxX();
						maxY = theShape.getMaxY();
						
						started = true;
					}
					else
					{
						minX = minX < theShape.getMinX() ? minX : theShape.getMinX();
						minY = minY < theShape.getMinY() ? minY : theShape.getMinY();
						
						maxX = maxX > theShape.getMaxX() ? maxX : theShape.getMaxX();
						maxY = maxY > theShape.getMaxY() ? maxY : theShape.getMaxY();
					}
				}
				
				if(toCheck.shape instanceof Ellipse2D)
				{
					Ellipse2D theShape = (Ellipse2D)toCheck.shape;
					
					if(!started)
					{
						minX = theShape.getMinX();
						minY = theShape.getMinY();
						maxX = theShape.getMaxX();
						maxY = theShape.getMaxY();
						
						started = true;
					}
					else
					{
						minX = minX < theShape.getMinX() ? minX : theShape.getMinX();
						minY = minY < theShape.getMinY() ? minY : theShape.getMinY();
						
						maxX = maxX > theShape.getMaxX() ? maxX : theShape.getMaxX();
						maxY = maxY > theShape.getMaxY() ? maxY : theShape.getMaxY();
					}
				}
				
			}
		}
		
		return new MaxMinValue(maxX,maxY,minX,minY);
	}
	
	/**
	 * Store maximum minimum value of x and y coordinate of shapes in a group
	 * @author Leanghork, Marcel, Kelvin, Addy
	 */
	public class MaxMinValue
	{
		public double maxX;
		public double maxY;
		public double minX;
		public double minY;
		
		public MaxMinValue(double maxX, double maxY, double minX, double minY)
		{
			this.maxX = maxX;
			this.maxY = maxY;
			this.minX = minX;
			this.minY = minY;
		}
	}
	
	//*************************************************************
	//* Paint                                                     *
	//*************************************************************
	
	public void paintComponent(Graphics gg)
	{
		Graphics2D g = (Graphics2D)gg;
		
		g.scale(zoom/100, zoom/100);			
		g.translate(50,50);
		
		this.drawShape(g);		
		this.drawSelected(g);		
		this.drawShadow(g);
		
	}	
	
	/**
	 * Display all shapes drawn
	 * @param g Graphics2D
	 */
	private void drawShape(Graphics2D g)
	{
		for(int i=0; i<shapes.size(); i++)
		{
			PolyObj toDraw = shapes.get(i);
			
			if(toDraw.shape instanceof Line2D)
			{
				g.setStroke(new BasicStroke(toDraw.strokeWidth));
				g.setColor(toDraw.stroke);
				g.draw(toDraw.shape);
			}
			
			if(toDraw.shape instanceof Rectangle2D || toDraw.shape instanceof Ellipse2D)
			{
				g.setStroke(new BasicStroke(toDraw.strokeWidth));
				g.setColor(toDraw.stroke);
				g.draw(toDraw.shape);
				g.setColor(toDraw.fill);
				g.fill(toDraw.shape);
			}
		}
	}

	/**
	 * Draw selected bounds
	 * @param g Graphics2D
	 */
	private void drawSelected(Graphics2D g)
	{
		removeSelectedDuplicate();
		
		for(int i=0;i<selected.size();i++)
		{
			PolyObj selects = selected.get(i);
			
			if(selects.checkGroup() ==0)		
			{
				if(selects.shape instanceof Line2D)
				{
					Line2D theSelected = (Line2D)selects.shape;
					
					double x1, x2, y1,y2;
				
					x1 = theSelected.getX1();
					x2 = theSelected.getX2();
					y1 = theSelected.getY1();
					y2 = theSelected.getY2();
					
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(0));
					
					g.fill(new Rectangle2D.Double(x1-3.5, y1-3.5, 7, 7));
					g.fill(new Rectangle2D.Double(x2-3.5, y2-3.5, 7, 7));
					
				}
				
				if(selects.shape instanceof Rectangle2D)
				{
					Rectangle2D theSelected = (Rectangle2D)selects.shape;
					
					double x = theSelected.getX() - 5 - selects.strokeWidth/2;
					double y = theSelected.getY() - 5 - selects.strokeWidth/2;
					double width = theSelected.getWidth() + 10 + selects.strokeWidth;
					double height = theSelected.getHeight() + 10 + selects.strokeWidth;
			
					Rectangle2D.Double bound = new Rectangle2D.Double(x, y, width, height);
		
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(0));
					
					g.draw(bound);
					g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 ,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 ,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 + height,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 + width/2 ,y-3.5 ,7 ,7));						
					g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height/2 ,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 + width/2 ,y-3.5 + height,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 + height/2,7 ,7));
				}
				
				if(selects.shape instanceof Ellipse2D)
				{
					Ellipse2D theSelected = (Ellipse2D)selects.shape;
					
					double x = theSelected.getX() - 5 - selects.strokeWidth/2;
					double y = theSelected.getY() - 5 - selects.strokeWidth/2;
					double width = theSelected.getWidth() + 10 + selects.strokeWidth;
					double height = theSelected.getHeight() + 10 + selects.strokeWidth;
			
					Rectangle2D.Double bound = new Rectangle2D.Double(x, y, width, height);
		
					g.setColor(Color.BLACK);
					g.setStroke(new BasicStroke(0));
					
					g.draw(bound);
					g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 ,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 ,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height,7 ,7));
					g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 + height,7 ,7));
					//g.fill(new Rectangle2D.Double(x-3.5 + width/2 ,y-3.5 ,7 ,7));						
					//g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height/2 ,7 ,7));
					//g.fill(new Rectangle2D.Double(x-3.5 + width/2 ,y-3.5 + height,7 ,7));
					//g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 + height/2,7 ,7));
				}
			}
			else
			{
				MaxMinValue value = this.getMinMax(selects);			
								
				double maxX = value.maxX;
				double minX = value.minX;
				double maxY = value.maxY;
				double minY = value.minY;
								
				group = new PolyObj(new Rectangle2D.Double(minX, minY, maxX-minX, maxY-minY),0,new Color(0,0,255,50),null);
				
				double x = value.minX - 5;
				double y = value.minY - 5;
				double width = value.maxX - value.minX + 10;
				double height = value.maxY- value.minY + 10;
		
				Rectangle2D.Double bound = new Rectangle2D.Double(x, y, width, height);
	
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(0));
				
				g.draw(bound);
				g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 ,7 ,7));
				g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 ,7 ,7));
				g.fill(new Rectangle2D.Double(x-3.5 + width ,y-3.5 + height,7 ,7));
				g.fill(new Rectangle2D.Double(x-3.5 ,y-3.5 + height,7 ,7));
			}
		}
		
	}
	
	/**
	 * draw drag shadow
	 * @param g Graphics2D
	 */
	private void drawShadow(Graphics2D g)
	{
		if(shadow != null)
		{
		    g.setColor(shadow.fill);
			g.fill(shadow.shape);

			g.setStroke(new BasicStroke(shadow.strokeWidth));
		    g.setColor(shadow.stroke);
	        g.draw(shadow.shape);
		}
	}
	
	
	/**
	 * Revalidate and repaint drawing board
	 */
	private void refresh()
	{
		this.revalidate();
		this.repaint();
	}
	
	//*************************************************************
	//* Document Properties                                       *
	//*************************************************************
	
	/**
	 * Get the file name of current drawing board
	 * @return fileName
	 */
	public String getFileName()
	{
		if(toRead == null)
			return fileName;
		
		return toRead.getName();
	}
	
	/**
	 * Show document properties editting dialog
	 */
	public void document()
	{
		JTextField wField = new JTextField(5);
		JTextField hField = new JTextField(5);
		String width = Integer.toString((int)size.getWidth()), height = Integer.toString((int)size.getHeight());

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Width : "));
		wField.setText(width);
		myPanel.add(wField);
		myPanel.add(Box.createHorizontalStrut(15));
		hField.setText(height);
		myPanel.add(new JLabel("Height : "));
		myPanel.add(hField);
		
		do
		{
			int result = JOptionPane.showConfirmDialog(this, myPanel, "Document Properties", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
			
			if(result == JOptionPane.OK_OPTION)
			{
				width = wField.getText();
				height = hField.getText();
			}
			else if(result == JOptionPane.CANCEL_OPTION)
				break;
			
		}while(! Pattern.matches("[\\x00-\\x20]*[+]?(\\p{Digit}+)(\\.)?(\\p{Digit}+)?[\\x00-\\x20]*",width) || ! Pattern.matches("[\\x00-\\x20]*[+]?(\\p{Digit}+)(\\.)?(\\p{Digit}+)?[\\x00-\\x20]*",width));
		
		size = new Dimension(java.lang.Integer.parseInt(width),java.lang.Integer.parseInt(height));
		setSize();
	}
	
	/**
	 * Set current board size
	 */
	private void setSize()
	{
		Dimension bSize = new Dimension((int)(size.getWidth()*zoom/100),(int)(size.getHeight()*zoom/100));
		
		this.setPreferredSize(bSize);
		this.refresh();
	}
	
	/**
	 * Initialize drawing board to the default size required to display all elements
	 */
	public void defaultSize()
	{
		int maxX = 0;
		int maxY = 0;
		
		for(int i=0;i<shapes.size();i++)
		{			
			if(shapes.get(i).shape instanceof Rectangle2D)
			{
				maxX = (int)((Rectangle2D.Double)shapes.get(i).shape).getMaxX();
				maxY = (int)((Rectangle2D.Double)shapes.get(i).shape).getMaxY();
			}
			
			if(shapes.get(i).shape instanceof Ellipse2D)
			{
				maxX = (int)((Ellipse2D.Double)shapes.get(i).shape).getMaxX();
				maxY = (int)((Ellipse2D.Double)shapes.get(i).shape).getMaxY();
			}
			
			if(shapes.get(i).shape instanceof Line2D)
			{
				maxX = (int)((Line2D.Double)shapes.get(i).shape).getX1();
				
				if(maxX < (int)((Line2D.Double)shapes.get(i).shape).getX2())
					maxX = (int)((Line2D.Double)shapes.get(i).shape).getX2();
				
				maxY = (int)((Line2D.Double)shapes.get(i).shape).getY1();
								
				if(maxY < (int)((Line2D.Double)shapes.get(i).shape).getY2())
					maxY = (int)((Line2D.Double)shapes.get(i).shape).getY2();
			}
			
			if(size.getWidth() > maxX)
				maxX = (int)size.getWidth();
			if(size.getHeight() > maxY)
				maxY = (int)size.getHeight();
			
			size = new Dimension(maxX,maxY);
		}
		
		size = new Dimension(maxX+100,maxY+100);
	}
	
	//*************************************************************
	//* File Handling                                             *
	//*************************************************************

	/**
	 * Save file
	 * @return true if file successfully save
	 */
	public boolean saveas()
	{
		boolean success = false;
		
		JFileChooser sv = new JFileChooser(System.getProperty("user.home"));
		
		sv.setFileFilter
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
		
		if(sv.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File toWrite = sv.getSelectedFile();
			
			if(toWrite.getPath().toLowerCase().endsWith("svg"))
			{
				success = saveToFile(toWrite);
			}
			else
			{
				success = saveToFile(new File(toWrite.getPath()+".svg"));
			}
		}	
		
		return success;
	}
		
	/**
	 * Save file - replace previous file
	 * @return true if file successfully save
	 */
	public boolean save()
	{
		boolean success = false;
		
		if(toRead == null)
			success = saveas();	
		else
			success = saveToFile(toRead);
		
		return success;
	}
	
	/**
	 * Write all shapes and document properties to file
	 * @param toWrite
	 */
	private boolean saveToFile(File toWrite)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document document = parser.newDocument();
			
			Element svgElement = document.createElement("svg");
			svgElement.setAttribute("width", (int)size.getWidth()-100+"px");
			svgElement.setAttribute("height", (int)size.getHeight()-100+"px");
			svgElement.setAttribute("version","1.1");
			svgElement.setAttribute("xmlns","http://www.w3.org/2000/svg");
			
			Node svgNode = svgElement;
			
			document.appendChild(svgNode);
			
			for(int i=0; i<shapes.size(); i++)
			{
				if(shapes.get(i).shape instanceof Rectangle2D)
				{
					Rectangle2D rect = (Rectangle2D) shapes.get(i).shape;
					
					Element rectElement = document.createElement("rect");
					rectElement.setAttribute("x", String.valueOf((int)rect.getX()));
					
					rectElement.setAttribute("y", String.valueOf((int)rect.getY()));
					rectElement.setAttribute("width", (int)rect.getWidth()+"px");
					rectElement.setAttribute("height", (int)rect.getHeight()+"px");
					
					if(shapes.get(i).fill!=null)
					{
						rectElement.setAttribute("fill", String.format("#%02x%02x%02x",shapes.get(i).fill.getRed(),shapes.get(i).fill.getGreen(),shapes.get(i).fill.getBlue()));
					}
					if(shapes.get(i).stroke!=null)
					{
						rectElement.setAttribute("stroke", String.format("#%02x%02x%02x",shapes.get(i).stroke.getRed(),shapes.get(i).stroke.getGreen(),shapes.get(i).stroke.getBlue()));
					}
					if(shapes.get(i).strokeWidth > 0)
					{
						rectElement.setAttribute("stroke-width", shapes.get(i).strokeWidth+"px");
					}
					
					svgNode.appendChild(rectElement);
				}
				
				else if(shapes.get(i).shape instanceof Ellipse2D)
				{
					Ellipse2D circle = (Ellipse2D)shapes.get(i).shape;
					
					int cx = (int)(circle.getX() + circle.getWidth()/2);
					int cy = (int)(circle.getY() + circle.getWidth()/2);
					
					Element circleElement = document.createElement("circle");
					circleElement.setAttribute("cx", String.valueOf((int)cx));
					circleElement.setAttribute("cy", String.valueOf((int)cy));
					circleElement.setAttribute("r", (int)circle.getWidth()/2+"px");
					
					if(shapes.get(i).fill!=null)
					{
						circleElement.setAttribute("fill", String.format("#%02x%02x%02x",shapes.get(i).fill.getRed(),shapes.get(i).fill.getGreen(),shapes.get(i).fill.getBlue()));
					}
					
					if(shapes.get(i).stroke!=null)
					{
						circleElement.setAttribute("stroke", String.format("#%02x%02x%02x",shapes.get(i).stroke.getRed(),shapes.get(i).stroke.getGreen(),shapes.get(i).stroke.getBlue()));
					}
					
					if(shapes.get(i).strokeWidth > 0)
					{
						circleElement.setAttribute("stroke-width", shapes.get(i).strokeWidth+"px");
					}
					
					svgNode.appendChild(circleElement);
				}
				
				else if(shapes.get(i).shape instanceof Line2D)
				{
					Line2D line = (Line2D)shapes.get(i).shape;
					
					Element lineElement = document.createElement("line");
					lineElement.setAttribute("x1", String.valueOf((int)line.getX1()));
					lineElement.setAttribute("x2", String.valueOf((int)line.getX2()));
					lineElement.setAttribute("y1", String.valueOf((int)line.getY1()));
					lineElement.setAttribute("y2", String.valueOf((int)line.getY2()));
					
					if(shapes.get(i).stroke!=null)
					{
						lineElement.setAttribute("stroke", String.format("#%02x%02x%02x",shapes.get(i).stroke.getRed(),shapes.get(i).stroke.getGreen(),shapes.get(i).stroke.getBlue()));
					}
					
					if(shapes.get(i).strokeWidth > 0)
					{
						lineElement.setAttribute("stroke-width", shapes.get(i).strokeWidth+"px");
					}
					svgNode.appendChild(lineElement);
				}
			}
			
			DOMSource source=new DOMSource(document);

			StreamResult result=new StreamResult(toWrite);
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			Transformer transformer=transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.STANDALONE,"no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"-//W3C//DTD SVG 1.1//EN");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");

			transformer.transform(source, result);
			fileName = toWrite.getName();
			toRead = toWrite;			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			return false;			
		}
		
		return true;
	}
	
	/**
	 * Read SVG file
	 * @param f
	 */
	private void read(File f)
	{
		try
		{			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler()
			{
				LinkedList<Integer> gStrokeWidth = new LinkedList<Integer>();
				LinkedList<Color> gStroke = new LinkedList<Color>();
				LinkedList<Color> gFill = new LinkedList<Color>();
				
				public void startElement(String uri,String localName, String qName, Attributes attributes)
				{					
					if(qName.equalsIgnoreCase("svg"))
					{
						int width=0, height=0;
						
						if(attributes.getValue("width")!=null)
							width=unitConvert(attributes.getValue("width"));
						if(attributes.getValue("height")!=null)
							height=unitConvert(attributes.getValue("height"));

						size = new Dimension(width,height);
						System.out.println(size);
					}
					
					if(qName.equalsIgnoreCase("g"))
					{
						int strokeWidth = -1;
						Color fill = null;
						Color stroke = null;
						
						
						if(attributes.getValue("stroke-width")!= null)
							strokeWidth = unitConvert(attributes.getValue("strokeWidth"));
						
						if(attributes.getValue("stroke")!=null)
							stroke = ColorObj.getRGBColor(attributes.getValue("stroke"));
						
						if(attributes.getValue("fill")!=null)
							fill = ColorObj.getRGBColor(attributes.getValue("fill"));
						
						gStrokeWidth.add(strokeWidth);
						gStroke.add(stroke);
						gFill.add(fill);						
					}
					
					if(qName.equalsIgnoreCase("rect"))
					{
						int x=0,y=0,width=0,height=0,strokeWidth=0;
						Color fill=null,stroke=null;
						
						if(attributes.getValue("x")!=null)
							x = unitConvert(attributes.getValue("x"));						
						if(attributes.getValue("y")!=null)
							y = unitConvert(attributes.getValue("y"));
						if(attributes.getValue("width")!=null)
							width=unitConvert(attributes.getValue("width"));
						if(attributes.getValue("height")!=null)
							height=unitConvert(attributes.getValue("height"));
						
						if(attributes.getValue("stroke-width")!=null)
							strokeWidth=unitConvert(attributes.getValue("stroke-width"));
						else if(attributes.getValue("stroke-width")==null)
						{
							for(int i=gStrokeWidth.size()-1;i>=0;i--)
							{
								if(gStrokeWidth.get(i) != -1)
								{
									strokeWidth = gStrokeWidth.get(i);
									break;
								}
							}
						}
						
						if(attributes.getValue("fill")!=null)
							fill = ColorObj.getRGBColor(attributes.getValue("fill"));
						else if(attributes.getValue("fill") == null)
						{
							for(int i=gFill.size()-1;i>=0;i--)
							{
								if(gFill.get(i) != null)
								{
									fill = gFill.get(i);
									break;
								}
							}
						}
						
						if(attributes.getValue("stroke")!=null)
							stroke = ColorObj.getRGBColor(attributes.getValue("stroke"));
						else if(attributes.getValue("stroke") == null)
						{
							for(int i=gStroke.size()-1;i>=0;i--)
							{
								if(gStroke.get(i) != null)
								{
									stroke = gStroke.get(i);
									break;
								}
							}
						}
						
						shapes.add(new PolyObj(new Rectangle2D.Double(x, y, width, height), strokeWidth, fill, stroke));
					}
					
					else if(qName.equalsIgnoreCase("circle"))
					{
						int x=0,y=0,r=0,strokeWidth=0;
						Color fill=null, stroke=null;
						
						if(attributes.getValue("cx")!=null)
							x = unitConvert(attributes.getValue("cx"));
						if(attributes.getValue("cy")!=null)
							y = unitConvert(attributes.getValue("cy"));
						if(attributes.getValue("r")!=null)
							r = unitConvert(attributes.getValue("r"));
						
						if(attributes.getValue("stroke-width")!=null)
							strokeWidth=unitConvert(attributes.getValue("stroke-width"));
						else if(attributes.getValue("stroke-width")==null)
						{
							for(int i=gStrokeWidth.size()-1;i>=0;i--)
							{
								if(gStrokeWidth.get(i) != -1)
								{
									strokeWidth = gStrokeWidth.get(i);
									break;
								}
							}
						}
						
						if(attributes.getValue("fill")!=null)
							fill = ColorObj.getRGBColor(attributes.getValue("fill"));
						else if(attributes.getValue("fill") == null)
						{
							for(int i=gFill.size()-1;i>=0;i--)
							{
								if(gFill.get(i) != null)
								{
									fill = gFill.get(i);
									break;
								}
							}
						}
						
						if(attributes.getValue("stroke")!=null)
							stroke = ColorObj.getRGBColor(attributes.getValue("stroke"));
						else if(attributes.getValue("stroke") == null)
						{
							for(int i=gStroke.size()-1;i>=0;i--)
							{
								if(gStroke.get(i) != null)
								{
									stroke = gStroke.get(i);
									break;
								}
							}
						}
						
						shapes.add(new PolyObj(new Ellipse2D.Double(x-r, y-r, 2*r, 2*r), strokeWidth, fill, stroke));
					}
					
					else if(qName.equalsIgnoreCase("line"))
					{					
						int x1=0,y1=0,x2=0,y2=0,strokeWidth=0;
						Color stroke = null;
						
						if(attributes.getValue("x1")!=null)
							x1 = unitConvert(attributes.getValue("x1"));
						if(attributes.getValue("y1")!=null)
							y1 = unitConvert(attributes.getValue("y1"));
						if(attributes.getValue("x2")!=null)
							x2 = unitConvert(attributes.getValue("x2"));
						if(attributes.getValue("y2")!=null)
							y2 = unitConvert(attributes.getValue("y2"));
						
						if(attributes.getValue("stroke-width")!=null)
							strokeWidth=unitConvert(attributes.getValue("stroke-width"));
						else if(attributes.getValue("stroke-width")==null)
						{
							for(int i=gStrokeWidth.size()-1;i>=0;i--)
							{
								if(gStrokeWidth.get(i) != -1)
								{
									strokeWidth = gStrokeWidth.get(i);
									break;
								}
							}
						}
						
						if(attributes.getValue("stroke")!=null)
						{
							stroke = ColorObj.getRGBColor(attributes.getValue("stroke"));
						}
						else if(attributes.getValue("stroke") == null)
						{
							for(int i=gStroke.size()-1;i>=0;i--)
							{
								if(gStroke.get(i) != null)
								{
									stroke = gStroke.get(i);
									break;
								}
							}
						}
						
						shapes.add(new PolyObj(new Line2D.Double(x1, y1, x2, y2), strokeWidth, null, stroke));
					}
				}
				
				public void endElement(String uri, String localName, String qName)
				{
					if(qName.equalsIgnoreCase("g"))
					{
						gStrokeWidth.removeLast();
						gStroke.removeLast();
						gFill.removeLast();
					}
				}
				
			};
			
			parser.parse(f.getPath(), handler);
		}
		
		
		catch(Exception e)
		{
			
		}
	}
	
	
	/**
	 * Convert the units to integer
	 * @param input
	 * @return the converted value
	 */
	private int unitConvert(String input)
	{
		String part1="", part2="";
		double result=0;
		int i=0;
	
		//Breaking string into 2 parts, to get the value and unit used
		for	(i=0; i<input.length(); i++)
		{
			if	(Character.isDigit(input.charAt(i)))
				part1=part1+input.charAt(i);
	
			else if	(Character.isLetter(input.charAt(i)))
				part2=part2+input.charAt(i);
			
			else
				return 0;
		}
	
		double dpi = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
		
		//Units conversion
		if	((part2.equalsIgnoreCase("px"))||(part2.equals("")))
			result=java.lang.Double.parseDouble(part1);
		
		else if	(part2.equalsIgnoreCase("cm"))
			result=java.lang.Double.parseDouble(part1)*dpi/2.54;
		
		else if	(part2.equalsIgnoreCase("in"))
			result=java.lang.Double.parseDouble(part1)*dpi;
		
		else if	(part2.equalsIgnoreCase("mm"))
			result=java.lang.Double.parseDouble(part1)*dpi/25.4;
		
		else if	(part2.equalsIgnoreCase("pt"))
			result=java.lang.Double.parseDouble(part1)*dpi/72;
		
		else if	(part2.equalsIgnoreCase("pc"))
			result=java.lang.Double.parseDouble(part1)*dpi/6;
				
		return (int)result; 
	}
	
}
