package model;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D.Double;

import javax.swing.*;
import javax.swing.border.*;

import javax.xml.parsers.*;
//import javax.xml.soap.*;
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

import controller.MouseControl;

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
	private Color stroke = null;
	private Color fill = null;
	
	private LinkedList<PolyObj> shapes = new LinkedList<PolyObj>();
	private LinkedList<PolyObj> selected = new LinkedList<PolyObj>();
	
	private PolyObj tempPolyObj = null;
	
	public DrawingBoard()
	{
		this.fileName = "New file" + (newCount++);
		
		this.size = new Dimension(500,500);
		this.setSize();
	}
	
	public DrawingBoard(File toRead)
	{
		this.toRead = toRead;
		
		this.read(toRead);
		this.defaultSize();
		this.setSize();
	}
	
	public String getFileName()
	{
		if(toRead == null)
			return fileName;
		
		return toRead.getName();
	}
	
	public JPanel getPanel()
	{
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.add(this);
		
		return panel;
	}
	
	
	/**
	 * Shape
	 */
	public void group()
	{
		for(int i=0; i<selected.size(); i++)
		{
			selected.get(i).group(gID);
		}
		
		gID++;
		this.refresh();
	}
	
	public void ungroup()
	{
		for(int i=0; i<selected.size(); i++)
		{
			selected.get(i).ungroup();
		}
		
		this.refresh();
	}
	
	public void selectAll()
	{
		selected.clear();
		
		for(int i=0;i<shapes.size();i++)
		{
			selected.add(shapes.get(i));
		}
		
		this.refresh();
	}
	
	public void select(double startX, double startY)
	{
		this.selected.clear();
				
		for(int i = shapes.size()-1; i>=0; i--)
		{
			PolyObj toSelect = shapes.get(i);
			
			Rectangle2D.Double area = new Rectangle2D.Double(startX-toSelect.strokeWidth-2,startY-toSelect.strokeWidth-2,toSelect.strokeWidth *2+4, toSelect.strokeWidth*2+4);
			
			if(toSelect.shape instanceof Line2D)
			{
				if( ((Line2D.Double)toSelect.shape).intersects(area) )
				{
					if(toSelect.checkGroup() != 0)
					{
						for(int j=0;j<shapes.size();j++)
						{
							if( toSelect.checkGroup() == shapes.get(j).checkGroup() )
								selected.add(shapes.get(j));
						}
					}
					else
						selected.add(toSelect);
					break;
				}
			}
			
			if(toSelect.shape instanceof Rectangle2D)
			{
				if( ((Rectangle2D.Double)toSelect.shape).intersects(area) )
				{
					if(toSelect.checkGroup() != 0)
					{
						for(int j=0;j<shapes.size();j++)
						{
							if( toSelect.checkGroup() == shapes.get(j).checkGroup() )
								selected.add(shapes.get(j));
						}
					}
					else
						selected.add(toSelect);
					
					break;
				}
			}
			
			if(toSelect.shape instanceof Ellipse2D)
			{
				if( ((Ellipse2D.Double)toSelect.shape).intersects(area) )
				{
					if(toSelect.checkGroup() != 0)
					{
						for(int j=0;j<shapes.size();j++)
						{
							if( toSelect.checkGroup() == shapes.get(j).checkGroup() )
								selected.add(shapes.get(j));
						}
					}
					else
						selected.add(toSelect);
					break;
				}
			}
		}
	}
	
	public void select(double startX, double startY, double endX, double endY)
	{
		this.selected.clear();

		System.out.println("sx "+startX+" sy "+startY+" ex "+endX+ " ey "+endY);
		
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
			
			if(toSelect.shape instanceof Line2D)
			{
				if( ((Line2D.Double)toSelect.shape).intersects(area) )
				{
					if(toSelect.checkGroup() != 0)
					{
						for(int j=0;j<shapes.size();j++)
						{
							if( toSelect.checkGroup() == shapes.get(j).checkGroup() && !selected.contains(shapes.get(j)))
								selected.add(shapes.get(j));
						}
					}
					else
						selected.add(toSelect);
				}
			}
			
			if(toSelect.shape instanceof Rectangle2D)
			{
				if( ((Rectangle2D.Double)toSelect.shape).intersects(area) )
				{	
					if(toSelect.checkGroup() != 0)
					{
						for(int j=0;j<shapes.size();j++)
						{
							if( toSelect.checkGroup() == shapes.get(j).checkGroup() && !selected.contains(shapes.get(j)))
								selected.add(shapes.get(j));
						}
					}
					else
						selected.add(toSelect);
				}
			}
			
			if(toSelect.shape instanceof Ellipse2D)
			{
				if( ((Ellipse2D.Double)toSelect.shape).intersects(area) )
				{
					if(toSelect.checkGroup() != 0)
					{
						for(int j=0;j<shapes.size();j++)
						{
							if( toSelect.checkGroup() == shapes.get(j).checkGroup() && !selected.contains(shapes.get(j)))
								selected.add(shapes.get(j));
						}
					}
					else
						selected.add(toSelect);
				}
			}
		}
		
		this.refresh();
		
	}
	
	public void remove()
	{
		
		for(int i=0;i<selected.size();i++)
		{
			this.shapes.remove(selected.get(i));
		}
		
		this.selected.clear();
		this.refresh();
	}
	
	public void deselect()
	{
		selected.clear();
		refresh();
	}
	
	/**
	 * Drawing
	 */
	public int getStrokeWidth()
	{
		return strokeWidth;
	}
	
	public void setStrokeWidth()
	{
		String strWidth = JOptionPane.showInputDialog(this, "Enter strokewidth", "5");
		
		if(Pattern.matches( "[\\x00-\\x20]*[+]?(\\p{Digit}+)(\\.)?(\\p{Digit}+)?[\\x00-\\x20]*", strWidth))
			strokeWidth = java.lang.Integer.parseInt(strWidth);
		else
			JOptionPane.showMessageDialog(this, "Invalid input detected", "Error", JOptionPane.ERROR_MESSAGE);
		
		for(int i=0;i<selected.size();i++)
		{
			((PolyObj)selected.get(i)).strokeWidth = strokeWidth;
		}
		
		this.refresh();
			
	}
	
	public void selectStroke()
	{
		Color temp = null;
		temp = JColorChooser.showDialog(this,"Select stroke color",temp);
		
		if(temp!= null)
		{
			stroke = temp;	

			for(int i=0;i<selected.size();i++)
			{
				((PolyObj)selected.get(i)).stroke = stroke;
			}
		
			this.refresh();
		}
	}
	
	public Color getStroke()
	{
		return stroke;
	}
	
	public void selectFill()
	{
		Color temp = null;
		temp = JColorChooser.showDialog(this,"Select fill color",temp);
		
		if(temp!=null)
		{
			fill = temp;	
		
			for(int i=0;i<selected.size();i++)
			{
				((PolyObj)selected.get(i)).fill = fill;
			}
			
			this.refresh();
		}
	}
	
	public Color getFill()
	{
		return fill;
	}
	
	public void addShape(PolyObj toAdd)
	{
		this.shapes.add(toAdd);
		
		this.refresh();
	}

	public void moves(double startX, double startY, double endX, double endY)
	{
		//System.out.println(startX);
		double moveX = endX - startX;
		double moveY = endY - startY;
		
		for(int i=0; i<selected.size(); i++)
		{
			PolyObj toMove = selected.get(i);
			
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
	}
	
	/**
	 * Size 
	 */
	
	public int getZoom()
	{
		return zoom;
	}
	
	public void setZoom(int zoom)
	{
		this.zoom = zoom;
		this.setSize();
		this.refresh();
	}
	
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

	public void setSize()
	{
		Dimension bSize = new Dimension((int)(size.getWidth()*zoom/100),(int)(size.getHeight()*zoom/100));
		
		this.setPreferredSize(bSize);
		this.refresh();
	}
	
	public void refresh()
	{
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Paint
	 */
	public void setTempPolyObj(PolyObj temp)
	{
		tempPolyObj = temp;
	}
	
	public void paintComponent(Graphics gg)
	{
		Graphics2D g = (Graphics2D)gg;
		
		g.scale(zoom/100, zoom/100);			
		g.translate(50,50);
		
		/**
		 * Draw all shape
		 */
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
		
		this.drawSelected(g);		
		
		if(tempPolyObj != null){
		    g.setColor(tempPolyObj.fill);
			g.fill(tempPolyObj.shape);

			g.setStroke(new BasicStroke(tempPolyObj.strokeWidth));
		    g.setColor(tempPolyObj.stroke);
	        g.draw(tempPolyObj.shape);
		}
		
	}
	
	public void drawSelected(Graphics2D g)
	{
		for(int i=0;i<selected.size();i++)
		{
			PolyObj selects = selected.get(i);
			
			if(selects.checkGroup() ==0)		
			{
				if(selects.shape instanceof Line2D)
				{
					double x1 = ((Line2D.Double)selects.shape).getX1();
					double y1 = ((Line2D.Double)selects.shape).getY1();
					double x2 = ((Line2D.Double)selects.shape).getX2();
					double y2 = ((Line2D.Double)selects.shape).getY2();
					
					g.setStroke(new BasicStroke(0));
					g.setColor(Color.WHITE);
					g.fill(new Rectangle2D.Double(x1-5, y1-5, 10, 10));
					g.fill(new Rectangle2D.Double(x2-5, y2-5, 10, 10));
					
					g.setColor(Color.BLACK);
					g.draw(new Rectangle2D.Double(x1-5, y1-5, 10, 10));
					g.draw(new Rectangle2D.Double(x2-5, y2-5, 10, 10));
				}
				
				if(selects.shape instanceof Rectangle2D)
				{
					double x = ((Rectangle2D.Double)selects.shape).getX() - selects.strokeWidth/2;
					double y = ((Rectangle2D.Double)selects.shape).getY() - selects.strokeWidth/2;
					double width = ((Rectangle2D.Double)selects.shape).getWidth() + selects.strokeWidth;
					double height = ((Rectangle2D.Double)selects.shape).getHeight() + selects.strokeWidth;
					
					g.setStroke(new BasicStroke(0));
					g.setColor(Color.WHITE);
					g.fill(new Rectangle2D.Double(x - 5.0, y - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x + width * 0.5 - 5.0, y - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x + width - 5.0, y - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x - 5.0, y + height * 0.5 - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x + width - 5.0, y + height * 0.5 - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x - 5.0, y + height - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x + width * 0.5 - 5.0, y + height - 5.0, 10.0, 10.0));
					g.fill(new Rectangle2D.Double(x + width - 5.0, y + height - 5.0, 10.0, 10.0));
	
					g.setColor(Color.BLACK);
					g.draw(new Rectangle2D.Double(x - 5.0, y - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x + width * 0.5 - 5.0, y - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x + width - 5.0, y - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x - 5.0, y + height * 0.5 - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x + width - 5.0, y + height * 0.5 - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x - 5.0, y + height - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x + width * 0.5 - 5.0, y + height - 5.0, 10.0, 10.0));
					g.draw(new Rectangle2D.Double(x + width - 5.0, y + height - 5.0, 10.0, 10.0));
				}
				
				if(selects.shape instanceof Ellipse2D)
				{
					double x = ((Ellipse2D.Double)selects.shape).getX();
					double y = ((Ellipse2D.Double)selects.shape).getY();	
					double w = ((Ellipse2D.Double)selects.shape).getWidth();
					double h = ((Ellipse2D.Double)selects.shape).getHeight();
					
					g.setStroke(new BasicStroke(0));
					g.setColor(Color.WHITE);
					g.fill(new Rectangle2D.Double(x - 5.0, y - 5.0, 10.0, 10.0));
			        g.fill(new Rectangle2D.Double(x + w - 5.0, y - 5.0, 10.0, 10.0));
				    g.fill(new Rectangle2D.Double(x - 5.0, y + h - 5.0, 10.0, 10.0));
				    g.fill(new Rectangle2D.Double(x + w - 5.0, y + h - 5.0, 10.0, 10.0));
				    
				    g.setColor(Color.BLACK);
					g.draw(new Rectangle2D.Double(x - 5.0, y - 5.0, 10.0, 10.0));
			        g.draw(new Rectangle2D.Double(x + w - 5.0, y - 5.0, 10.0, 10.0));
				    g.draw(new Rectangle2D.Double(x - 5.0, y + h - 5.0, 10.0, 10.0));
				    g.draw(new Rectangle2D.Double(x + w - 5.0, y + h - 5.0, 10.0, 10.0));
				}
			}
			else
			{
				double minX=0, maxX=0, minY=0, maxY=0;
				
				for(int j=0; j<shapes.size(); j++)
				{
					if(selects.checkGroup() == shapes.get(j).checkGroup())
					{
						if(shapes.get(j).shape instanceof Rectangle2D.Double)
						{
							Rectangle2D shape = (Rectangle2D)shapes.get(j).shape;
							
							if(j==0)
							{
								maxX = shape.getMaxX();
								minX = shape.getMinX();
								maxY = shape.getMaxY();
								minY = shape.getMinY();
							}
							else
							{
								if(maxX < shape.getMaxX())
									maxX = shape.getMaxX();
								
								if(minX > shape.getMinX())
									minX = shape.getMinX();
								
								if(maxY < shape.getMaxY())
									maxY = shape.getMaxY();
								
								if(minY > shape.getMinY())
									minY = shape.getMinY();
							}
						}
						
						if(shapes.get(j).shape instanceof Ellipse2D.Double)
						{
							Ellipse2D shape = (Ellipse2D)shapes.get(j).shape;
							
							if(j==0)
							{
								maxX = shape.getMaxX();
								minX = shape.getMinX();
								maxY = shape.getMaxY();
								minY = shape.getMinY();
							}
							else
							{
								if(maxX < shape.getMaxX())
									maxX = shape.getMaxX();
								
								if(minX > shape.getMinX())
									minX = shape.getMinX();
								
								if(maxY < shape.getMaxY())
									maxY = shape.getMaxY();
								
								if(minY > shape.getMinY())
									minY = shape.getMinY();
							}
						}
						
						if(shapes.get(j).shape instanceof Line2D.Double)
						{
							Line2D shape = (Line2D)shapes.get(j).shape;
							
							if(j==0)
							{
								maxX = shape.getX1() > shape.getX2() ? shape.getX1() : shape.getX2();
								minX = shape.getX1() < shape.getX2() ? shape.getX1() : shape.getX2();
								maxY = shape.getY1() > shape.getY2() ? shape.getY1() : shape.getY2();
								minY = shape.getY1() < shape.getY2() ? shape.getY1() : shape.getY2();	
							}
							else
							{
								if(maxX < shape.getX1())
									maxX = shape.getX1();
								if(maxX < shape.getX2())
									maxX = shape.getX2();
									
								if(minX > shape.getX1())
									minX = shape.getX1();
								if(minX > shape.getX2())
									minX = shape.getX2();
									
								if(maxY < shape.getX1())
									maxY = shape.getX1();
								if(maxY < shape.getX2())
									maxY = shape.getX2();
									
								if(minY > shape.getX1())
									minY = shape.getX1();
								if(minY > shape.getX2())
									minY = shape.getX2();
							}
						}
					}
				}			
				
				g.setStroke(new BasicStroke(0));
				g.setColor(Color.WHITE);
				g.fill(new Rectangle2D.Double(minX - 5.0, maxY + 5.0, 10.0, 10.0));
				g.fill(new Rectangle2D.Double(minX - 5.0, minY - 5.0, 10.0, 10.0));
				g.fill(new Rectangle2D.Double(maxX + 5.0, maxY + 5.0, 10.0, 10.0));
				g.fill(new Rectangle2D.Double(maxX + 5.0, minY - 5.0, 10.0, 10.0));
				
				g.setColor(Color.BLACK);				
				g.draw(new Rectangle2D.Double(minX - 5.0, maxY + 5.0, 10.0, 10.0));
				g.draw(new Rectangle2D.Double(minX - 5.0, minY - 5.0, 10.0, 10.0));
				g.draw(new Rectangle2D.Double(maxX + 5.0, maxY + 5.0, 10.0, 10.0));
				g.draw(new Rectangle2D.Double(maxX + 5.0, minY - 5.0, 10.0, 10.0));
			}
		}
		
	}
	
	/**
	 * File Handling
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
	
	public void saveas()
	{
		File toWrite = null;
		
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
		
		if(sv.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
			
		toWrite = sv.getSelectedFile();
				
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document document = parser.newDocument();
			
			Element svgElement = document.createElement("svg");
			svgElement.setAttribute("width", size.getWidth()+"px");
			svgElement.setAttribute("height", size.getHeight()+"px");
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
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void save()
	{
		File toWrite = null;
		
		if(toRead != null)
			toWrite = toRead;
		else
		{
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
				toWrite = sv.getSelectedFile();
				System.out.println(toWrite.getPath());
			}
			
			
		}
			
			
			
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document document = parser.newDocument();
			
			Element svgElement = document.createElement("svg");
			svgElement.setAttribute("width", size.getWidth()+"px");
			svgElement.setAttribute("height", size.getHeight()+"px");
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
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
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
	
	public LinkedList<PolyObj> getSelectedList(){
		return selected;
	}
	
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
