package fr.nantes1900.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * A button with a ? in a circle to show some help. When hovering, a quick help
 * * in a tooltip is displayed and when clicked a complete help is displayed in
 * a JOptionPane.
 * @author Camille Bouquet
 */
public class HelpButton extends JButton implements MouseListener {

    /**
     * The current color of the text.
     */
    private Color textColorCurrent;

    /**
     * The normal wanted color for the text.
     */
    private Color textColor = Color.BLUE;

    /**
     * The current value for the background.
     */
    private Color backgroundColorCurrent;

    /**
     * The normal wanted color for the background.
     */
    private Color backgroundColor = new Color(240, 240, 240);

    /**
     * Indicates if the mouse is on the button area.
     */
    private boolean in = false;

    /**
     * Indicates if the left button of the mouse is pressed.
     */
    private boolean pressed = false;

    /**
     * Message to display in the pop-UP.
     */
    private String helpMessage;

    /**
     * Title of the pop-UP.
     */
    private String title;

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Empty constructor.
     */
    public HelpButton() {
        this("");
    }

    /**
     * Creates a new help button with the given tooltip. If no message is set,
     * no pop-UP is showed when the button is clicked.
     * @param tooltip
     *            The tooltip to associate with this button.
     */
    public HelpButton(final String tooltip) {
        this(tooltip, "");
    }

    /**
     * Creates a new help button with the given message and tooltip.
     * @param tooltip
     *            The tooltip to associate with this button.
     * @param helpMessageIn
     *            Message to show in a pop-UP when the button is clicked.
     */
    public HelpButton(final String tooltip, final String helpMessageIn) {
        this(tooltip, helpMessageIn, "");
    }

    /**
     * Creates a new help button with the given message, tooltip and pop-UP
     * title.
     * @param tooltip
     *            The tooltip to associate with this button.
     * @param helpMessageIn
     *            Message to show in a pop-UP when the button is clicked.
     * @param titleIn
     *            The title of the pop-UP.
     */
    public HelpButton(final String tooltip, final String helpMessageIn,
            final String titleIn) {
        this.textColorCurrent = this.textColor;
        this.backgroundColorCurrent = this.backgroundColor;
        this.setMinimumSize(new Dimension(20, 20));
        this.setPreferredSize(new Dimension(20, 20));
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.addMouseListener(this);

        this.title = titleIn;
        this.setToolTipText(tooltip);
        this.helpMessage = helpMessageIn;
    }

    /**
     * Darkens of the desired amount the given color.
     * @param color
     *            Color to darken.
     * @param amount
     *            Amont to darken the color of.
     * @return The darkened color.
     */
    private static Color darken(final Color color, final int amount) {
        int red = (color.getRed() - amount < 0) ? 0 : color.getRed() - amount;
        int green = (color.getGreen() - amount < 0) ? 0 : color.getGreen()
                - amount;
        int blue = (color.getBlue() - amount < 0) ? 0 : color.getBlue()
                - amount;
        return new Color(red, green, blue);
    }

    /**
     * Lightens of the desired amount the given color.
     * @param color
     *            Color to lighten.
     * @param amount
     *            Amont to lighten the color of.
     * @return The lightened color.
     */
    private static Color lighten(final Color color, final int amount) {
        int red = (color.getRed() + amount > 255) ? 255 : color.getRed()
                + amount;
        int green = (color.getGreen() + amount > 255) ? 255 : color.getGreen()
                + amount;
        int blue = (color.getBlue() + amount > 255) ? 255 : color.getBlue()
                + amount;
        return new Color(red, green, blue);
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent arg0) {
        // Not used.
    }

    @Override
    public final void mouseEntered(final MouseEvent arg0) {
        this.in = true;
        this.textColorCurrent = lighten(this.textColor, 50);
        this.backgroundColorCurrent = lighten(this.backgroundColor, 25);
        revalidate();
        repaint();
    }

    @Override
    public final void mouseExited(final MouseEvent arg0) {
        this.in = false;
        if (!this.pressed) {
            this.textColorCurrent = this.textColor;
            this.backgroundColorCurrent = this.backgroundColor;
            revalidate();
            repaint();
        }
    }

    @Override
    public final void mousePressed(final MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON1) {
            this.pressed = true;
            this.textColorCurrent = darken(this.textColor, 100);
            this.backgroundColorCurrent = this.backgroundColor;
            revalidate();
            repaint();
        }
    }

    @Override
    public final void mouseReleased(final MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON1) {
            this.pressed = false;

            if (this.in) {
                this.textColorCurrent = lighten(this.textColor, 50);
                this.backgroundColorCurrent = lighten(this.backgroundColor, 25);
            } else {
                this.textColorCurrent = this.textColor;
                this.backgroundColorCurrent = this.backgroundColor;
            }
            revalidate();
            repaint();

            if (!this.helpMessage.isEmpty()) {
                String titl = (this.title.isEmpty()) ? "Informations complémentaires"
                        : this.title;
                JOptionPane.showMessageDialog(this, this.helpMessage, titl,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public final void paintComponent(final Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(
                (this.textColorCurrent.getRed() + this.backgroundColorCurrent
                        .getRed()) / 2,
                (this.textColorCurrent.getGreen() + this.backgroundColorCurrent
                        .getGreen()) / 2,
                (this.textColorCurrent.getBlue() + this.backgroundColorCurrent
                        .getBlue()) / 2));
        g2d.fillOval(0, 0, 20, 20);

        g2d.setColor(this.textColorCurrent);
        g2d.fillOval(1, 1, 18, 18);

        g2d.setColor(new Color(
                (this.textColorCurrent.getRed() + this.backgroundColorCurrent
                        .getRed()) / 2,
                (this.textColorCurrent.getGreen() + this.backgroundColorCurrent
                        .getGreen()) / 2,
                (this.textColorCurrent.getBlue() + this.backgroundColorCurrent
                        .getBlue()) / 2));
        g2d.fillOval(2, 2, 16, 16);

        g2d.setColor(this.backgroundColorCurrent);
        g2d.fillOval(3, 3, 14, 14);

        g2d.setColor(this.textColorCurrent);
        g2d.drawString("?", 7, 15);
    }

    /**
     * Sets a new help message to show on a pop-UP.
     * @param message
     *            The new help message.
     */
    public final void setHelpMessage(final String message) {
        this.setHelpMessage(message, "");
    }

    /**
     * Sets a new help message to show on a pop-UP with the new title.
     * @param message
     *            The new help message.
     * @param titleIn
     *            The new title.
     */
    public final void
            setHelpMessage(final String message, final String titleIn) {
        this.helpMessage = message;
        this.title = titleIn;
    }

    /**
     * Sets a new tooltip text.
     * @param tooltip
     *            The new text for the tooltip.
     */
    public final void setTooltip(final String tooltip) {
        this.setToolTipText(tooltip);
    }
}
