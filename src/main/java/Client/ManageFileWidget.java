package Client;

import javax.swing.*;
import java.awt.*;

public class ManageFileWidget extends JPanel {
    public ManageFileWidget() {
        setLayout(new BorderLayout());
        add(new JLabel("Label"), BorderLayout.EAST);
        add(new JButton("Button"), BorderLayout.WEST);
    }
}
