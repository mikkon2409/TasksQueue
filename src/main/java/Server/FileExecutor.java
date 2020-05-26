package Server;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileExecutor implements Runnable {
    private final static Logger log = Logger.getLogger(FileDescriptor.class.getName());
    private final Queue<String> logHistory;
    private final File execFile;
    private Process proc;
    private InputStream is;
    private MonoThreadClientHandler monoThreadClientHandler;

    public FileExecutor(File execFile, Queue<String> logHistory, MonoThreadClientHandler m) {
        this.execFile = execFile;
        this.logHistory = logHistory;
        monoThreadClientHandler = m;
    }

    public Process getProc() {
        return proc;
    }

    public void exec() {
        try {
            proc = Runtime.getRuntime().exec(execFile.getAbsolutePath());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Run process", e);
        }
    }

    @Override
    public void run() {
        is = proc.getInputStream();
        byte[] arr = new byte[1024];
        try {
            while(proc.isAlive()) {
                int len = is.read(arr);
                if (len > 0) {
                    String out = execFile.getName() + " : " + new String(arr);
                    logHistory.offer(out);
                }
            }
        } catch (IOException e) {
        } finally {
            monoThreadClientHandler.getLogSender().setCanWrite(false);
            monoThreadClientHandler.sendUpdateOfState();
            monoThreadClientHandler.getLogSender().setCanWrite(true);
        }
    }
}
