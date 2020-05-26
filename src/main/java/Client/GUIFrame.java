package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private final JFileChooser fileChooser;
    private static final Logger log = Logger.getLogger(GUIFrame.class.getName());
    private final ExecutorService service = Executors.newCachedThreadPool();
    private Client client;
    private final JPanel taskManager;
    private File selectedFile;
    private final HashMap<String, ManageFileWidget> files = new HashMap<>();

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
            fileChooser.setCurrentDirectory(new File("/home/rvyunov/User/Projects/untitled/cmake-build-debug"));
            if (fileChooser.showOpenDialog(rootPanel) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileName.setText(selectedFile.getName());
            }
        });

        uploadButton.addActionListener(actionEvent -> {
            client.uploadFileToServer(selectedFile);
        });
        setResizable(false);
        setVisible(true);
        service.execute(client = new Client(this));
    }

    public void updateTask(String fileName, String actionName, ActionListener expr) {
        if (files.containsKey(fileName)) {
            ManageFileWidget file = files.get(fileName);
            file.getBtn().setText(actionName);
            file.getBtn().removeActionListener(file.getBtn().getActionListeners()[0]);
            file.getBtn().addActionListener(expr);
            file.revalidate();
        } else {
            ManageFileWidget file = new ManageFileWidget(
                    fileManagerPane.getViewport().getWidth(), fileName, actionName, expr);
            files.put(fileName, file);
            taskManager.add(file);
        }
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

    public void appendText(String text) {
        textArea1.append(text);
    }

    public static void main(String[] args) {
        new GUIFrame("TasksQueue");
    }
}
