package model;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.border.*;

import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class DrawingBoard 
	extends JPanel
{
	private static int newCount = 1;
	private String fileName = null;
	
	private File toRead = null;
	private Dimension size;
	private int zoom = 100;
	
	private LinkedList<PolyObj> shapes = new LinkedList<PolyObj>();
	private LinkedList<PolyObj> selected = new LinkedList<PolyObj>();
	
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
					selected.add(toSelect);
					break;
				}
			}
			
			if(toSelect.shape instanceof Rectangle2D)
			{
				if( ((Rectangle2D.Double)toSelect.shape).intersects(area) )
				{
					selected.add(toSelect);
					break;
				}
			}
			
			if(toSelect.shape instanceof Ellipse2D)
			{
				if( ((Ellipse2D.Double)toSelect.shape).intersects(area) )
				{
					selected.add(toSelect);
					break;
				}
			}
		}
	}
	
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
			
			if(toSelect.shape instanceof Line2D)
			{
				if( ((Line2D.Double)toSelect.shape).intersects(area) )
					selected.add(toSelect);
			}
			
			if(toSelect.shape instanceof Rectangle2D)
			{
				if( ((Rectangle2D.Double)toSelect.shape).intersects(area) )
					selected.add(toSelect);
			}
			
			if(toSelect.shape instanceof Ellipse2D)
			{
				if( ((Ellipse2D.Double)toSelect.shape).intersects(area) )
					selected.add(toSelect);
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
	
	/**
	 * Drawing
	 */
	public void addShape(PolyObj toAdd)
	{
		this.shapes.add(toAdd);
		
		this.refresh();
	}

	public void moves(double x, double y)
	{
		for(int i=0;i<selected.size();i++)
		{
			PolyObj select = selected.get(i);
			
			for(int j=0;j<shapes.size();j++)
			{
				if(select.equals(shapes.get(j)))
				{
					if(select.shape instanceof Rectangle2D)
					{
						double width = ((Rectangle2D)select.shape).getWidth();
						double height = ((Rectangle2D)select.shape).getHeight();
						
						Rectangle2D newShape = new Rectangle2D.Double(x,y,width,height);
						System.out.println(shapes.get(j).shape);
						shapes.get(j).replaceShape(newShape);
						System.out.println(shapes.get(j).shape);
					}
				}
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
		
		/**
		 * Draw selected bounds
		 */
		for(int i=0;i<selected.size();i++)
		{
			PolyObj selects = selected.get(i);
			
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
	}
	
	/**
	 * File Handling
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
					/*if(qName.equalsIgnoreCase("g"))
					{
						gStrokeWidth.removeLast();
						gStroke.removeLast();
						gFill.removeLast();
					}*/
				}
				
			};
			
			parser.parse(f.getPath(), handler);
		}
		
		
		catch(Exception e)
		{
			
		}
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
			result=Double.parseDouble(part1);
		
		else if	(part2.equalsIgnoreCase("cm"))
			result=Double.parseDouble(part1)*dpi/2.54;
		
		else if	(part2.equalsIgnoreCase("in"))
			result=Double.parseDouble(part1)*dpi;
		
		else if	(part2.equalsIgnoreCase("mm"))
			result=Double.parseDouble(part1)*dpi/25.4;
		
		else if	(part2.equalsIgnoreCase("pt"))
			result=Double.parseDouble(part1)*dpi/72;
		
		else if	(part2.equalsIgnoreCase("pc"))
			result=Double.parseDouble(part1)*dpi/6;
				
		return (int)result; 
	}
}
