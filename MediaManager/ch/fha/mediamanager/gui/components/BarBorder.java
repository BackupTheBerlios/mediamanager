package ch.fha.mediamanager.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import javax.swing.border.Border;

/**
 * This implementation of <code>Border<code> consists of a simple border and a bar
 * on the top, which can contains a title.
 * 
 * <p>Several properties are made available and can be setted by invoking the appropriate
 * set-methods:
 * <ul>
 *   <li>Title font
 *   <li>Bar thickness
 *   <li>Bar color
 *   <li>Text color
 *   <li>Text shadow
 * </ul>
 * 
 * @author ia02vond
 * @version $Id: BarBorder.java,v 1.1 2004/06/18 12:30:14 ia02vond Exp $
 */
public class BarBorder implements Border {

    // --------------------------------
    // ATTRIBUTES
    // --------------------------------

	/** The title. */
	private String title;
	
	/** The title font. */
	private Font font;
	
	/** The bar thickness. */
	private int thickness;
	
	/** Color definitions. */
	private Color textColor, textShadowColor, barColor;
	
	/** If true the title will have a shadow. */
	private boolean textShadow;
	
	/** The border offset. */
	private final static int OFFSET = 3;

    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------

	/**
	 * Creates a <code>BarBorder</code> with the given
	 * title. The properties will be set on the default
	 * values.
	 * @param title    the border title
	 */
	public BarBorder(String title) {
		this.title = title;
		font = new Font("Sans-Serif", Font.PLAIN, 12);
		thickness = 30;
		textColor = Color.white;
		textShadowColor = Color.black;
		barColor = new Color(41, 72, 131);
		textShadow = true;
	}
    
    // --------------------------------
    // MUTATORS
    // --------------------------------

	/**
	 * Sets the title font.
	 * @param font    the title font
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Sets the title thickness on the given value.
	 * @param thickness    the title thickness
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	
	/**
	 * Sets the title color.
	 * @param color    the title color
	 */
	public void setTextColor(Color color) {
		textColor = color;
	}
	
	/**
	 * Sets the bar color. The bar contains a horizontal changement
	 * from the given color to the background color of component
	 * which contains this border.
	 * @param color    the bar color
	 */
	public void setBarColor(Color color) {
		barColor = color;
	}
	
	/**
	 * Sets whether the title should have a shadow.
	 * @param textShadow    if true a title shadow will be painted.
	 */
	public void setTextShadow(boolean textShadow) {
		this.textShadow = textShadow;
	}

	/**
	 * Returns always <code>false</code>.
	 * @return false
	 * @see javax.swing.border.Border#isBorderOpaque()
	 */
	public boolean isBorderOpaque() {
		return false;
	}

	/**
	 * Paints this border by using the defined properties.
	 * @param c        the component which contains this border
	 * @param g        the graphics object to paint on
	 * @param x        the horizontal coordinate of the point upper left
	 * @param y        the vertical coordinate of the point upper left
	 * @param width    the width of the component
	 * @param height   the height of the component
	 */
	public void paintBorder(Component c, Graphics g, 
        int x, int y, int width, int height) {
		
        if (width > 2*OFFSET && height > 2*OFFSET) {
			Color bgColor = c.getBackground();
			
			g.setColor(Color.DARK_GRAY);
			g.drawRect(x+OFFSET, y+OFFSET, width-2-2*OFFSET, height-2-2*OFFSET);
			g.drawLine(x+OFFSET, y+thickness+1+OFFSET, x+width-2-OFFSET, 
                y+thickness+1+OFFSET);
			
			g.setColor(bgColor.darker());
			g.drawLine(x+width-1-OFFSET, y+1+OFFSET, x+width-1-OFFSET, y+height-1-OFFSET);
			g.drawLine(x+OFFSET+1, y+height-1-OFFSET, x+width-1-OFFSET, y+height-1-OFFSET);
			bgColor.brighter();
			
			g.setColor(Color.WHITE);
			g.drawLine(x+1+OFFSET, y+thickness+OFFSET, x+1+OFFSET, y+1+OFFSET);
			g.drawLine(x+1+OFFSET, y+1+OFFSET, x+width-3-OFFSET, y+1+OFFSET);
			
			Graphics2D g2d = (Graphics2D)g;
			Rectangle2D rect = new Rectangle2D.Double(x+2+OFFSET, y+2+OFFSET, 
                width-4-2*OFFSET, thickness-1);
                                
			GradientPaint gp = new GradientPaint(x+2+OFFSET,y+2+OFFSET, barColor,
                x+width-2-2*OFFSET, y+thickness+OFFSET, bgColor, false);
			g2d.setPaint( gp );
			g2d.fill( rect );
			
			// center text
			int ascent = g.getFontMetrics().getAscent();
			int dY = (thickness-1+ascent)/2;
			
			g.setFont(font);
			if (textShadow) {
				g.setColor(textShadowColor);
				g.drawString(title, x+6+OFFSET, y+dY+OFFSET+1);
			}
			g.setColor(textColor);
			g.drawString(title, x+5+OFFSET, y+dY+OFFSET);
		} 
	}

	/**
	 * Returns the border OFFSETs.
	 * @param c    the component which contains this border
	 * @return the OFFSETs
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(thickness+7+OFFSET, 5+OFFSET, 5+OFFSET, 5+OFFSET);
	}

}