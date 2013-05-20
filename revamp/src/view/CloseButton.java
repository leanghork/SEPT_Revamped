package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class CloseButton extends JButton
{
	public CloseButton()
	{
		int size = 20;
		setPreferredSize(new Dimension(size, size));
		setMargin(new Insets(0,0,0,0));
		setToolTipText("close this tab");
		setUI(new BasicButtonUI());
		setContentAreaFilled(false);
		setFocusable(false);
		setRolloverEnabled(true);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		if (getModel().isPressed())
		{
			g2.translate(1, 1);
		}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			
		if (getModel().isRollover())
		{
			g2.setColor(Color.RED);
		}
		
		int delta = 6;
		g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
		g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
		g2.dispose();
	}
}