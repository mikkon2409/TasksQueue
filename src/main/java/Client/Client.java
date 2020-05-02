package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {
    private GUIFrame superFrame;
    private static Logger log = Logger.getLogger(Client.class.getName());
    private ClientDesc clientDesc;
    private Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    public Client(GUIFrame superFrame) {
        this.superFrame = superFrame;
        try {
            clientDesc = new ClientDesc();
            superFrame.setUserID(clientDesc.getClientID().toString());
            connect();
        } catch (IOException e) {
            superFrame.showError(e.getMessage());
        }
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
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    private void authenticate() throws IOException {
        dos.writeUTF(clientDesc.getClientID().toString());
    }

    @Override
    public void run() {
        if (socket != null) {
            while (!socket.isClosed()) {
                try {
                    log.info(dis.readUTF());
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Receive", e);
                }
            }
        }
    }
}
