package com.lln.link.style;

import com.lln.link.component.ImageButton;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/14 16:02
 */

public class ButtonStyle extends BasicButtonUI {

    public void paintButtonPressed(Graphics g, ImageButton b) {
        g.drawImage(b.getPressImage(), 0, 0, b.getWidth(), b.getHeight(), null);
    }


    private void paintButtonRollover(Graphics g, ImageButton b) {
        g.drawImage(b.getRollOverImage(), 0, 0, b.getWidth(), b.getHeight(), null);
    }

    private void paintButtonSimple(Graphics g, ImageButton b) {
        g.drawImage(b.getSimpleImage(), 0, 0, b.getWidth(), b.getHeight(), null);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        ImageButton b = (ImageButton) c;
        ButtonModel model = b.getModel();

        clearTextShiftOffset();

        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(g, b);
            return;
        }

        if (model.isRollover()) {
            paintButtonRollover(g, b);
            return;
        }
        paintButtonSimple(g, b);
    }
}
