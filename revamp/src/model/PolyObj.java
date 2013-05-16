package model;


import java.awt.*;
import java.awt.geom.*;

public class PolyObj 
{
	public Shape shape;
	public int strokeWidth;
	public Color fill, stroke;
	private boolean select;
	
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
