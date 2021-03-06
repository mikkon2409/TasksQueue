package Server;

import Client.ClientDesc;
import Shared.ExecutableFileDownloader;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MonoThreadClientHandler implements Runnable {
    private final static Logger log = Logger.getLogger(ClientDesc.class.getName());
    private static final Path commonSpace = Paths.get("workspaces");
    private final ExecutorService logThread = Executors.newSingleThreadExecutor();
    private final ExecutorService service = Executors.newCachedThreadPool();
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private final UUID id;
    private Path workspace;
    private final HashMap<String, File> files = new HashMap<>();
    private final HashMap<String, FileExecutor> processes = new HashMap<>();
    private final Queue<String> logHistory = new LinkedList<>();
    private LogSender logSender;

    public MonoThreadClientHandler(UUID clientUUID) {
        id = clientUUID;
        try {
            workspace = Files.createDirectories(Paths.get(commonSpace.toString() + "/" + id.toString()));
        } catch (IOException e) {
            log.log(Level.SEVERE, "Create Private Workspace:" , e);
        }
    }

    public LogSender getLogSender() {
        return logSender;
    }

    public void initConnection(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    public void sendUpdateOfState() {
        try {
            oos.writeUTF("<state_update_start>");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Update", e);
        }
        files.forEach((s, file) -> {
            try {
                oos.writeUTF(s);
                FileExecutor exec = processes.get(s);
                if (exec != null) {
                    if (exec.getProc() != null) {
                        if (exec.getProc().isAlive()) {
                            oos.writeBoolean(true);
                            return;
                        }
                    }
                }
                oos.writeBoolean(false);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Update", e);
            }
        });
        try {
            oos.writeUTF("<state_update_end>");
            oos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Update", e);
        }
    }

    @Override
    public void run() {
        sendUpdateOfState();
        if (logSender == null) {
            logThread.submit(logSender = new LogSender(logHistory, oos));
        } else {
            logSender.init(oos);
        }
        try {
            while (!socket.isClosed()) {
                String value = ois.readUTF();
                logSender.setCanWrite(false);
                switch (value) {
                    case "<file>":
                        File tmp = ExecutableFileDownloader.downloadExecutableFile(ois, workspace);
                        files.putIfAbsent(tmp.getName(), tmp);
                        break;
                    case "<run>":
                        File file = files.get(ois.readUTF());
                        FileExecutor executor;
                        if (!processes.containsKey(file.getName())) {
                            executor = new FileExecutor(file, logHistory, this);
                            processes.put(file.getName(), executor);
                        } else {
                            executor = processes.get(file.getName());
                        }
                        executor.exec();
                        service.submit(executor);
                        break;
                    case "<stop>":
                        FileExecutor exec = processes.get(ois.readUTF());
                        if (exec.getProc().isAlive()) {
                            exec.getProc().destroyForcibly();
                            while (exec.getProc().isAlive());
                        }
                        break;
                    case "<update>":
                        sendUpdateOfState();
                        break;
                }
                sendUpdateOfState();
                logSender.setCanWrite(true);
            }
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            logSender.setCanWrite(false);
            log.log(Level.SEVERE, "Run: ", e);
        }
    }
}