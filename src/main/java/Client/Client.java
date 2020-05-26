package Client;

import Server.FileExecutor;
import Shared.ExecutableFileUploader;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {
    private ExecutorService service = Executors.newCachedThreadPool();
    private GUIFrame superFrame;
    private static Logger log = Logger.getLogger(Client.class.getName());
    private static final Path commonSpace = Paths.get("clientWorkspace");
    private Path workspace;
    private ClientDesc clientDesc;
    private Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    public Client(GUIFrame superFrame) {
        this.superFrame = superFrame;
        try {
            workspace = createWorkspace();
        } catch (IOException e) {
            superFrame.showError(e.getMessage());
        }
        try {
            clientDesc = new ClientDesc();
            superFrame.setUserID(clientDesc.getClientID().toString());
            connect();
        } catch (IOException e) {
            superFrame.showError(e.getMessage());
        }
    }

    private Path createWorkspace() throws IOException {
        return Files.createDirectories(Paths.get(commonSpace.toString()));
    }

    public void connect() {
        try {
            createSocket();
            authenticate();
            superFrame.setReconnectBtnVisible(false);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Connection: ", e);
            superFrame.showError(e.getMessage());
            superFrame.setReconnectBtnVisible(true);
        }
    }

    private void createSocket() throws IOException {
        socket = new Socket("localhost", 3345);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    private void authenticate() throws IOException {
        oos.writeUTF(clientDesc.getClientID().toString());
        oos.flush();
    }

    private void getStateUpdate() {
        log.info("State update");
        String value;
        try {
            while (!(value = ois.readUTF()).equals("<state_update_end>")) {
                String filename = value;
                boolean state = ois.readBoolean();
                log.info(filename + "\t" + state);
                superFrame.updateTask(filename, state ? "Stop" : "Run",
                        state ? actionEvent -> {
                            stopTask(filename);
                        } : actionEvent -> {
                            runTask(filename);
                        });
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Update", e);
        }
    }

    @Override
    public void run() {
        if (socket != null) {
            while (!socket.isClosed()) {
                try {
                    String value = ois.readUTF();
                    switch (value) {
                        case "<state_update_start>":
                            getStateUpdate();
                            break;
                        default:
                            if (value.contains("<output>")) {
                                superFrame.appendText(value.replace("<output>", ""));
                            }
                            break;
                    }
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Receive", e);
                }
            }
        }
    }

    public void uploadFileToServer(File file) {
        service.execute(new ExecutableFileUploader(oos, file));
    }

    private void runTask(String taskName) {
        try {
            oos.writeUTF("<run>");
            oos.writeUTF(taskName);
            oos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Run task", e);
        }
    }

    private void stopTask(String taskName) {
        try {
            oos.writeUTF("<stop>");
            oos.writeUTF(taskName);
            oos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Stop task", e);
        }
    }
}
