import toast.JToast;

import javax.swing.*;

public class Test extends JFrame {

    public Test(){
        setBounds(200,100,700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Test frame = new Test();

        frame.setVisible(true);
        JToast.setFrame(frame);
        if(JToast.isAvailable())
            JToast.sendToastMessage(JToast.TYPE.ERROR,"Hello world",JToast.HORIZONTAL_POSITION.RIGHT,JToast.VERTICAL_POSITION.TOP);

    }

}
