package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class ManageFileWidget extends JPanel {
    private static final Logger log = Logger.getLogger(ManageFileWidget.class.getName());
    private final JButton btn;
    private final JLabel lbl;
    public ManageFileWidget(final int width, final String name, final String btn_name, ActionListener expr) {
        setLayout(new BorderLayout());
        Dimension size = new Dimension(width, 50);
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        lbl = new JLabel(name);
        btn = new JButton(btn_name);
        btn.addActionListener(expr);
        add(lbl, BorderLayout.WEST);
        add(btn, BorderLayout.EAST);
    }

    public JButton getBtn() {
        return btn;
    }

    public JLabel getLbl() {
        return lbl;
    }
}
