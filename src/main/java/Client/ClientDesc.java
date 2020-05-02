package Client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ClientDesc {
    private static Logger log = Logger.getLogger(ClientDesc.class.getName());
    private File clientDescFile = new File("client.save");
    private UUID clientID;
    public ClientDesc() throws IOException {
        try {
            String tmp = new String(Files.readAllBytes(clientDescFile.toPath()));
            clientID = UUID.fromString(tmp);
        } catch (IOException | IllegalArgumentException e) {
            createNewID();
        }
        log.info("Client UUID: " + clientID.toString());
    }

    private void createNewID() throws IOException {
        clientDescFile.createNewFile();
        clientID = UUID.randomUUID();
        FileWriter fw = new FileWriter(clientDescFile);
        fw.write(clientID.toString());
        fw.flush();
        fw.close();
    }

    public UUID getClientID() {
        return clientID;
    }
}
