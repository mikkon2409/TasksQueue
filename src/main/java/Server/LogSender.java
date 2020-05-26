package Server;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSender implements Runnable {
    private final static Logger log = Logger.getLogger(FileDescriptor.class.getName());
    private Queue<String> logHistory;
    private ObjectOutputStream oos;
    private boolean canWrite;

    public LogSender(Queue<String> logHistory, ObjectOutputStream oos) {
        this.logHistory = logHistory;
        init(oos);
    }

    public void init(ObjectOutputStream oos) {
        this.oos = oos;
        canWrite = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String val = logHistory.peek();
                log.info(val);
                if (canWrite) {
                    if (val != null) {
                        oos.writeUTF("<output>" + val);
                        oos.flush();
                        logHistory.poll();
                    }
                }
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Logger", e);
        }
    }

    public void setCanWrite(boolean val) {
        canWrite = val;
    }
}
