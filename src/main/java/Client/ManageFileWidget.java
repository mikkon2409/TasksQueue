package Client;

import javax.swing.*;
import java.beans.Expression;

public class ManageFileWidget {
    JButton btn;
    JLabel lbl;
    String name;
    Expression fun;
    public ManageFileWidget(JPanel panel) {
        btn = new JButton();
        btn.setText("Run");
        btn.setVisible(true);

    }
}
