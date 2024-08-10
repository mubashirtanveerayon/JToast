/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toast;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import utils.RoundedButton;

/**
 *
 * @author ayon
 */
public class JToast extends JWindow implements Runnable, ActionListener , MouseListener {
    
    private static JToast notification;




    public boolean finished;
    public boolean sent;



    public enum TYPE{
        SUCCESS,ERROR,INFO
    };
    
    public static final Color INFO_RIBBON_COLOR = new Color(0x1665e9);
    public static final Color SUCCESS_RIBBON_COLOR = new Color(0x2cd339);
    public static final Color ERROR_RIBBON_COLOR = new Color(0xe8171b);
    
    public static final Color BACKGROUND = new Color(0xf3f3f3);
    private Color backgroundColor=BACKGROUND;
    
    private TYPE type=TYPE.INFO;
    private Color ribbonColor=INFO_RIBBON_COLOR;
    
    private float duration = 1f;

    private float animationSpeed = 1f;
    public enum HORIZONTAL_POSITION{
        LEFT,CENTER,RIGHT
    };
    
    public enum VERTICAL_POSITION{
        TOP,CENTER,BOTTOM
    };
    public static final String SUCCESS_TITLE = "Success";
    public static final String FAILURE_TITLE = "Error";
    public static final String INFO_TITLE = "Information";
    
    private String title,body;
    
    private HORIZONTAL_POSITION hPosition=HORIZONTAL_POSITION.RIGHT;
    private VERTICAL_POSITION vPosition=VERTICAL_POSITION.BOTTOM;
    
    private static JFrame mainWindow;

    public boolean mouseExited=true;

    private float transparency = 0.0f;
    
    private JToast(){
        if(mainWindow == null)throw new RuntimeException("Main window not initialized");
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT,5,5);
        setLayout(layout);


        
    }
    
    public static JToast getInstance(){
        if(notification == null)notification = new JToast();
        if(notification.sent && !notification.finished)throw new RuntimeException("Toast is busy");
        return notification;
    }
    
    public static final void setFrame(JFrame frame){
        mainWindow = frame;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setBody(String body){
        this.body = body;
    }
    
    public void setDuration(float durationInSeconds){
        duration = durationInSeconds;
    }
    
    public void setPosition(HORIZONTAL_POSITION hPos,VERTICAL_POSITION vPos){
        hPosition = hPos;
        vPosition = vPos;
    }

    public void setBackground(Color bg){
        backgroundColor = bg;
    }
    
    public void setType(TYPE type){
        this.type=type;
    }

    public void setAnimationSpeed(float speed){
        animationSpeed=speed;
    }
            

    public static void sendToastMessage(TYPE type, String message,HORIZONTAL_POSITION hpos,VERTICAL_POSITION vpos){
        if(mainWindow == null)throw new RuntimeException("Main frame not set");

        JToast toast = JToast.getInstance();
        toast.setBody(message);
        toast.setType(type);
        toast.setPosition(hpos,vpos);
        toast.send();

    }

    public static void sendToastMessage(TYPE type, String title,String message,HORIZONTAL_POSITION hpos,VERTICAL_POSITION vpos){
        if(mainWindow == null)throw new RuntimeException("Main frame not set");

        JToast toast = JToast.getInstance();
        toast.setTitle(title);
        toast.setBody(message);
        toast.setType(type);
        toast.setPosition(hpos,vpos);
        toast.send();

    }


    public static boolean isAvailable(){
        return notification == null || (notification.sent && notification.finished);
    }

    
        @Override
    public void run() {



            try {
                Thread.sleep((int)(duration * 1000));
            } catch (Exception ex) {
                System.out.println(ex);
            }

            //loop
            while (transparency <= 1.0f && !finished) {
                setOpacity(1.0f - transparency);
                if (mouseExited) {
                    transparency += 0.01f;
                }
                try {
                    Thread.sleep((int)(15.0/animationSpeed));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

            if(!finished) dispose();
            finished=true;
            notification=null;


    }
    
    public void send(){
        JLabel ribbon = new JLabel("");
        ribbon.setOpaque(true);
        getContentPane().setBackground(backgroundColor);


        RoundedButton closeButton=new RoundedButton(new Color(0xD0D0D0),new Color(0x959595),new Color(0xBDBDBD),new Color(0xBDBDBD),"/res/close_hover.svg","/res/close.svg",50);
        closeButton.setPreferredSize(new Dimension(30,30));
        closeButton.addActionListener(this);
        JTextPane textPane = new JTextPane();
        textPane.setFont(new Font("SansSerif",Font.PLAIN,12));
        textPane.setBackground(backgroundColor);
        textPane.setForeground(Color.black);

        StyledDocument doc = textPane.getStyledDocument();
        Style titleStyle = doc.addStyle("customStyle",null);

        StyleConstants.setFontSize(titleStyle,20);
        StyleConstants.setBold(titleStyle,true);


        JLabel iconLabel;
        switch(type){
            case TYPE.SUCCESS :{
                ribbonColor = SUCCESS_RIBBON_COLOR;
                StyleConstants.setForeground(titleStyle,new Color(0x377945));
                title = title == null ? SUCCESS_TITLE:title;
                iconLabel = new JLabel(new FlatSVGIcon(getClass().getResource("/res/success.svg")));
                break;
            } case TYPE.ERROR:{
                title = title == null ? FAILURE_TITLE:title;
                ribbonColor = ERROR_RIBBON_COLOR;
                StyleConstants.setForeground(titleStyle,new Color(0x984141));
                iconLabel = new JLabel(new FlatSVGIcon(getClass().getResource("/res/error.svg")));
                break;
            }
            default:{
                iconLabel = new JLabel(new FlatSVGIcon(getClass().getResource("/res/info.svg")));
                title = title == null ? INFO_TITLE:title;
                StyleConstants.setForeground(titleStyle,new Color(0x2A5D8A));
                break;

            }
        }
        body = body == null ? "Hello from JToast":body;


        textPane.setStyledDocument(doc);

        try{
            doc.insertString(0,title+"\n",titleStyle);

            doc.insertString(doc.getLength(),body,null);
        }catch(Exception ex){
            ex.printStackTrace();
        }


        ribbon.setPreferredSize(new Dimension(10,65));
        ribbon.setBackground(ribbonColor);

        addMouseListener(this);
        textPane.addMouseListener(this);
        closeButton.addMouseListener(this);

        add(ribbon);
        add(iconLabel);
        add(textPane);
        add(closeButton);

        pack();

        double x,y;
        switch(hPosition){
            case HORIZONTAL_POSITION.RIGHT:{
                x=mainWindow.getX()+mainWindow.getWidth()-getWidth();
                break;
            }
            case HORIZONTAL_POSITION.CENTER:{
                x=mainWindow.getX()+mainWindow.getWidth()/2.0-getWidth()/2.0;
                break;
            }
            default:{
                x=mainWindow.getX();
            }
        }

        switch(vPosition){
            case VERTICAL_POSITION.TOP:{
                y=mainWindow.getY();
                break;
            }
            case VERTICAL_POSITION.CENTER:{
                y=mainWindow.getY() + mainWindow.getY()/2.0 -getHeight()/2.0;
                break;
            }
            default:{
                y=mainWindow.getY()+mainWindow.getHeight()-getHeight();
            }
        }

        setLocation((int)x,(int)y);
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),25,25));
        setVisible(true);
        sent=true;
        finished=false;
        Thread thread=new Thread(this);
        thread.start();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        finished=true;
        dispose();
        notification=null;
    }


    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

        transparency = 0.0f;
        mouseExited = false;
    }

    @Override
    public void mouseExited(MouseEvent me) {
        mouseExited = true;
    }




}
