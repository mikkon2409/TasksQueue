package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIFrame extends JFrame {
    private JPanel rootPanel;
    private JLabel userID;
    private JButton reconnectButton;
    private JButton selectFile;
    private JButton uploadButton;
    private JScrollPane fileManagerPane;
    private JTextArea textArea1;
    private JFileChooser fileChooser;
    private static Logger log = Logger.getLogger(GUIFrame.class.getName());
    private ExecutorService service = Executors.newFixedThreadPool(2);
    private Client client;

    public GUIFrame(String name) {
        setContentPane(rootPanel);
        setName(name);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fileManagerPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        reconnectButton.addActionListener(actionEvent -> client.connect());
        reconnectButton.setEnabled(false);
        fileChooser = new JFileChooser();
        selectFile.addActionListener(actionEvent -> {
            fileChooser.setDialogTitle("Select file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(rootPanel) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
//                service.execute(new ExecutableFileUploader(dos, file));
            }
        });
        uploadButton.addActionListener(actionEvent -> {
            textArea1.append("xyu\n");
            JButton btn = new JButton("Text");
            btn.setSize(new Dimension(150, 150));
            fileManagerPane.add(btn);
        });
        setVisible(true);
        service.execute(client = new Client(this));
    }

    public void setUserID(String userID) {
        this.userID.setText(userID);
    }

    public void setReconnectBtnVisible(boolean visible) {
        reconnectButton.setEnabled(visible);
    }

    public void showError(String name) {
        JOptionPane.showMessageDialog(this, name);
    }

    public static void main(String[] args) {
        new GUIFrame("TasksQueue");
    }
}
