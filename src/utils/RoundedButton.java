/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

    
public class RoundedButton extends JButton implements MouseListener{
    
    Color hoverColor;
    public Color defaultColor;
    Color pressedColor;
    Color hoverTextColor;
    Color textColor;
    public Color borderColor;
    int radius=2;
    public ImageIcon normalIcon,hoverIcon;

    
        public RoundedButton(Color hover,Color pressed,Color normal,Color border,Color hoverText,Color textColor,String text,String hoverImagePath,String normalImagePath,boolean regularFont,int textSize, int radius){
        super(text);
        this.radius=radius;
        normalIcon = normalImagePath.endsWith(".svg")?new FlatSVGIcon(getClass().getResource(normalImagePath)): new ImageIcon(normalImagePath);
        hoverIcon = hoverImagePath.endsWith(".svg")?new FlatSVGIcon(getClass().getResource(hoverImagePath)): new ImageIcon(hoverImagePath);
        setIcon(normalIcon);

        borderColor=border;

        hoverColor=hover;
        pressedColor=pressed;
        defaultColor=normal;
        hoverTextColor=hoverText;
        this.textColor=textColor;
        setForeground(textColor);




        if (regularFont) setFont(new Font("SansSerif",textSize,Font.PLAIN));
            else setFont(new Font("SansSerif",textSize,Font.BOLD));
        configure();

    }

    public RoundedButton(Color hover,Color pressed,Color normal,Color border,Color hoverText,Color textColor,String text,boolean regularFont,int textSize, int radius){
        super(text);

            borderColor = border;
            this.radius = radius;
            hoverColor = hover;
            hoverTextColor = hoverText;
            pressedColor = pressed;
            defaultColor = normal;
            this.textColor = textColor;
            setHorizontalTextPosition(JLabel.CENTER);
            setForeground(textColor);


//        setFont(AutoLoad.BOLD_FONT);


            if (regularFont) setFont(new Font("SansSerif",textSize,Font.PLAIN));
            else setFont(new Font("SansSerif",textSize,Font.BOLD));

            configure();



    }



    public RoundedButton(Color hoverColor, Color pressedColor, Color defaultColor, Color borderColor, String hoverImagePath, String normalImagePath, int radius) {
        this.hoverColor = hoverColor;
        this.defaultColor = defaultColor;
        this.pressedColor = pressedColor;
        this.borderColor = borderColor;
        this.radius = radius;
        normalIcon = normalImagePath.endsWith(".svg")?new FlatSVGIcon(getClass().getResource(normalImagePath)): new ImageIcon(normalImagePath);
        hoverIcon = hoverImagePath.endsWith(".svg")?new FlatSVGIcon(getClass().getResource(hoverImagePath)): new ImageIcon(hoverImagePath);
        setIcon(this.normalIcon);
        configure();

    }

    
    private void configure(){
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setBackground(defaultColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setBorder(new EmptyBorder(0,0,0,0));
        addMouseListener(this);
        

    }
    
    @Override
    public void paintComponent(Graphics g){

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(borderColor);
        g2d.fillRoundRect(0,0, getWidth(),getHeight(),radius,radius );
        g2d.setColor(getBackground());
        g2d.fillRoundRect( 2,2,getWidth()-4,getHeight()-4,radius,radius);
        super.paintComponent(g);

    }

    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        setBackground(pressedColor);

    }

    @Override
    public void mouseReleased(MouseEvent e) {


        setBackground(hoverColor);

    }

    @Override
    public void mouseEntered(MouseEvent e) {


        setBackground(hoverColor);
        setForeground(hoverTextColor);
        if(hoverIcon != null)setIcon(hoverIcon);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(defaultColor);
        setForeground(textColor);
        if(normalIcon != null)setIcon(normalIcon);
    }

}
