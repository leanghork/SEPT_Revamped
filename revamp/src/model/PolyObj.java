package model;


import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class PolyObj 
{
	public Shape shape;
	public int strokeWidth;
	public Color fill, stroke;
	private boolean select;
	private LinkedList<Integer> groupID = new LinkedList<Integer>();
	
	public PolyObj(Shape shape, int strokeWidth, Color fill, Color stroke)
	{
		this.shape = shape;
		this.strokeWidth = strokeWidth;
		this.fill = fill;
		this.stroke = stroke;
	}
	
	public void setSelected()
	{
		
	}
	
	public int checkGroup()
	{
		if(groupID.isEmpty())
			return 0;
		
		return groupID.getLast();
	}
	
	public void group(int id)
	{
		groupID.add(id);
	}
	
	public void ungroup()
	{
		if(groupID.isEmpty())
			return;
		
		groupID.removeLast();
	}
	
	public void replaceShape(Shape shape)
	{
		this.shape = shape;
	}
	
	public PolyObj clone()
	{
		Shape cloneShape = null;
		
		if(shape instanceof Rectangle2D)
		{
			cloneShape = (Rectangle2D)(((Rectangle2D)shape).clone());
		}
		else if(shape instanceof Ellipse2D)
		{
			cloneShape = (Ellipse2D)(((Ellipse2D)shape).clone());
		}
		else if(shape instanceof Line2D)
		{
			cloneShape = (Line2D)(((Line2D)shape).clone());
		}
		
		return (
				new PolyObj(
					cloneShape,
					new Integer(strokeWidth), 
					new Color(fill.getRed(),fill.getGreen(),fill.getBlue(),fill.getAlpha()),
					new Color(stroke.getRed(),stroke.getGreen(),stroke.getBlue(),stroke.getAlpha())
				));
	}
}
