package fr.nantes1900.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * A button with a ? in a circle to show some help. 
 * 
 * When hovering, a quick help * in a tooltip is displayed and
 * when clicked a complete help is displayed in a JOptionPane.
 * @author Camille
 */
public class HelpButton extends JButton implements MouseListener
{
    /**
     * The current color of the text.
     */
    private Color             textColorCurrent;

    /**
     * The normal wanted color for the text.
     */
    private Color             textColor;

    /**
     * The current value for the background.
     */
    private Color             backgroundColorCurrent;

    /**
     * The normal wanted color for the background.
     */
    private Color             backgroundColor;

    /**
     * Indicates if the mouse is on the button area.
     */
    private boolean           in               = false;

    /**
     * Indicates if the left button of the mouse is pressed.
     */
    private boolean           pressed          = false;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Empty constructor.
     */
    public HelpButton()
    {
        textColor = Color.BLUE;
        this.textColorCurrent = textColor;
        backgroundColor = new Color(240, 240, 240);
        this.backgroundColorCurrent = backgroundColor;
        this.setPreferredSize(new Dimension(20, 20));
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // g2d.setBackground(new Color(0, 0, 0, 0));
        // g2d.clearRect(0, 0, 20, 20);
        g2d.setColor(new Color(
                (textColorCurrent.getRed() + backgroundColorCurrent.getRed()) / 2,
                (textColorCurrent.getGreen() + backgroundColorCurrent
                        .getGreen()) / 2,
                (textColorCurrent.getBlue() + backgroundColorCurrent.getBlue()) / 2));
        g2d.fillOval(0, 0, 20, 20);

        g2d.setColor(textColorCurrent);
        g2d.fillOval(1, 1, 18, 18);

        g2d.setColor(new Color(
                (textColorCurrent.getRed() + backgroundColorCurrent.getRed()) / 2,
                (textColorCurrent.getGreen() + backgroundColorCurrent
                        .getGreen()) / 2,
                (textColorCurrent.getBlue() + backgroundColorCurrent.getBlue()) / 2));
        g2d.fillOval(2, 2, 16, 16);

        g2d.setColor(backgroundColorCurrent);
        g2d.fillOval(3, 3, 14, 14);

        g2d.setColor(textColorCurrent);
        g2d.drawString("?", 7, 15);
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        in = true;
        textColorCurrent = lighten(textColor, 50);
        backgroundColorCurrent = lighten(backgroundColor, 25);
        revalidate();
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
        in = false;
        if (!pressed)
        {
            textColorCurrent = textColor;
            backgroundColorCurrent = backgroundColor;
            revalidate();
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
        if (arg0.getButton() == MouseEvent.BUTTON1)
        {
            pressed = true;
            textColorCurrent = darken(textColor, 100);
            backgroundColorCurrent = backgroundColor;
            revalidate();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        if (arg0.getButton() == MouseEvent.BUTTON1)
        {
            pressed = false;

            if (in)
            {
                textColorCurrent = lighten(textColor, 50);
                backgroundColorCurrent = lighten(backgroundColor, 25);
            } else
            {
                textColorCurrent = textColor;
                backgroundColorCurrent = backgroundColor;
            }
            revalidate();
            repaint();
        }
    }

    /**
     * Darkens of the desired amount the given color.
     * 
     * @param color
     *          Color to darken.
     * @param amount
     *          Amont to darken the color of.
     * @return
     *          The darkened color.
     */
    private static Color darken(Color color, int amount)
    {
        int red = (color.getRed() - amount < 0) ? 0 : color.getRed() - amount;
        int green = (color.getGreen() - amount < 0) ? 0 : color.getGreen()
                - amount;
        int blue = (color.getBlue() - amount < 0) ? 0 : color.getBlue()
                - amount;
        Color colorLightened = new Color(red, green, blue);
        return colorLightened;
    }

    /**
     * Lightens of the desired amount the given color.
     * 
     * @param color
     *          Color to lighten.
     * @param amount
     *          Amont to lighten the color of.
     * @return
     *          The lightened color.
     */
    private static Color lighten(Color color, int amount)
    {
        int red = (color.getRed() + amount > 255) ? 255 : color.getRed()
                + amount;
        int green = (color.getGreen() + amount > 255) ? 255 : color.getGreen()
                + amount;
        int blue = (color.getBlue() + amount > 255) ? 255 : color.getBlue()
                + amount;
        Color colorLightened = new Color(red, green, blue);
        return colorLightened;
    }
}
