package view;

import model.*;

import java.awt.*;
import javax.swing.*;

public class View 
	extends JFrame
{
	private Model tab = null;
		
	public View(String [] args)
	{
		if(args.length != 0)
			tab = new Model(args);
		else
			tab = new Model();
		
		
		this.add(tab);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000,700);
		this.setVisible(true);
	}
	
	public static void main(String[]args)
	{
		new View(args);
	}
	
}
