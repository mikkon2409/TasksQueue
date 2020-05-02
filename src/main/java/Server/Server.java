package Server;

import Client.ClientDesc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static Logger log = Logger.getLogger(ClientDesc.class.getName());
    private static ExecutorService service = Executors.newCachedThreadPool();
    private static HashMap<UUID, MonoThreadClientHandler> clients = new HashMap<>();
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3345);
            log.info("Server has been started");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (!server.isClosed()) {
                if (br.ready()) {
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        break;
                    }
                }

                Socket client = server.accept();
                log.info("Client connection accepted");
                DataInputStream dis = new DataInputStream(client.getInputStream());
                try {
                    String clientID = dis.readUTF();
                    log.info(clientID);
                    UUID clientUUID = UUID.fromString(clientID);
                    MonoThreadClientHandler clientHandler;
                    if (!clients.containsKey(clientUUID)) {
                        clients.put(clientUUID, clientHandler = new MonoThreadClientHandler(clientUUID));
                    } else {
                        clientHandler = clients.get(clientUUID);
                    }
                    clientHandler.initConnection(client);
                    service.execute(clientHandler);
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Get ClientDesc:", e);
                }
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Server:", e);
        }
    }
}
