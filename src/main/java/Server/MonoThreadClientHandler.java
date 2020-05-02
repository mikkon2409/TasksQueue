package Server;

import Client.ClientDesc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MonoThreadClientHandler implements Runnable {
    private final static Logger log = Logger.getLogger(ClientDesc.class.getName());
    private static final Path commonSpace = Paths.get("workspaces");
    private Socket socket;
    private final UUID id;
    private Path workspace;

    public MonoThreadClientHandler(UUID clientUUID) {
        id = clientUUID;
        try {
            workspace = Files.createDirectories(Paths.get(commonSpace.toString() + "/" + id.toString()));
        } catch (IOException e) {
            log.log(Level.SEVERE, "Create Private Workspace:" , e);
        }
    }

    public void initConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(id.toString());
                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                log.log(Level.SEVERE, "Run: ", e);
                break;
            }
        }
    }
}