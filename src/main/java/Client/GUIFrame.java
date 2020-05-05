package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Expression;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
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
    private JLabel fileName;
    private JFileChooser fileChooser;
    private static Logger log = Logger.getLogger(GUIFrame.class.getName());
    private ExecutorService service = Executors.newFixedThreadPool(2);
    private Client client;
    private JPanel taskManager;
    private File file;
    private List<ManageFileWidget> files = new ArrayList<ManageFileWidget>();

    public GUIFrame(String name) {
        setContentPane(rootPanel);
        setName(name);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fileName.setText(null);
        fileManagerPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        taskManager = new JPanel();
        fileManagerPane.setViewportView(taskManager);
        taskManager.setLayout(new BoxLayout(taskManager, BoxLayout.Y_AXIS));
        reconnectButton.addActionListener(actionEvent -> client.connect());
        reconnectButton.setEnabled(false);
        fileChooser = new JFileChooser();
        selectFile.addActionListener(actionEvent -> {
            fileChooser.setDialogTitle("Select file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(rootPanel) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                fileName.setText(file.getName());
            }
        });
        uploadButton.addActionListener(actionEvent -> {
            client.uploadFileToServer(file);
        });
        setResizable(false);
        setVisible(true);
        service.execute(client = new Client(this));
    }

    public void addNewTask(String lbl, String btn, ActionListener expr) {
        ManageFileWidget file = new ManageFileWidget(fileManagerPane.getViewport().getWidth(), lbl, btn, expr);
        files.add(file);
        taskManager.add(file);
        fileManagerPane.revalidate();
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
