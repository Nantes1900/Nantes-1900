package fr.nantes1900.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class HelpButton extends JButton implements MouseListener
{
    private Color             textColorActual;
    private Color             textColor;

    private Color             backgroundColorActual;
    private Color             backgroundColor;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public HelpButton()
    {
        textColor = Color.BLUE;
        this.textColorActual = textColor;
        backgroundColor = new Color(240, 240, 240);
        this.backgroundColorActual = backgroundColor;
        this.setPreferredSize(new Dimension(20, 20));
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.addMouseListener(this);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

//        g2d.setBackground(new Color(0, 0, 0, 0));
//        g2d.clearRect(0, 0, 20, 20);
        g2d.setColor(new Color(
                (textColorActual.getRed() + backgroundColorActual.getRed()) / 2,
                (textColorActual.getGreen() + backgroundColorActual.getGreen()) / 2,
                (textColorActual.getBlue() + backgroundColorActual.getBlue()) / 2));
        g2d.fillOval(0, 0, 20, 20);

        g2d.setColor(textColorActual);
        g2d.fillOval(1, 1, 18, 18);

        g2d.setColor(new Color(
                (textColorActual.getRed() + backgroundColorActual.getRed()) / 2,
                (textColorActual.getGreen() + backgroundColorActual.getGreen()) / 2,
                (textColorActual.getBlue() + backgroundColorActual.getBlue()) / 2));
        g2d.fillOval(2, 2, 16, 16);

        g2d.setColor(backgroundColorActual);
        g2d.fillOval(3, 3, 14, 14);

        g2d.setColor(textColorActual);
        g2d.drawString("?", 7, 15);
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        textColorActual = backgroundColor;
        backgroundColorActual = textColor;
        revalidate();
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
        textColorActual = textColor;
        backgroundColorActual = backgroundColor;
        revalidate();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }
}
