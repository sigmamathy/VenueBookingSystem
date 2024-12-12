package sba.venuebooking.app;

import javax.swing.*;
import java.awt.*;

public abstract class FunctionPanel extends JPanel {

    abstract void onBegin();

    abstract void onRefresh();

    protected final void createSimpleLabel(String text, int x, int y, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 500, 100);
        label.setVerticalAlignment(JLabel.TOP);
        label.setForeground(Color.BLACK);
        label.setFont(font);
        add(label);
    }

    protected final void createSimpleLabel(String text, int x, int y) {
        createSimpleLabel(text, x, y, new Font("Constantia", Font.PLAIN, 26));
    }
}
