package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CloseTab 
	extends JPanel
	implements ActionListener
{
	private Model tab = null;
	private String title = null;
	
	JButton close = new closeButton();
	JLabel label = new JLabel();
	
	public CloseTab(Model tab, String title)
	{
		this.tab = tab;
		this.title = title;
		
		close.addActionListener(this);
		
		label.setText(title);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setOpaque(false);
		
		this.add(label);
		this.add(Box.createHorizontalStrut(5));
		this.add(close);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String message  = "Do you want to save SVG before exit";
		String [] option = {"Yes", "No", "Cancel"};
		
		int opt = JOptionPane.showOptionDialog(tab, message, "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, JOptionPane.CANCEL_OPTION);
		
		if(opt == JOptionPane.YES_OPTION)
		{
			if(tab.save())
			{
				tab.closeTab(this);
				tab.refresh();
			}
		}
		else if(opt == JOptionPane.NO_OPTION)
		{
			tab.closeTab(this);
			tab.refresh();
		}
			
	}
}
