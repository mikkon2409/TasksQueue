package Server;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileExecutor implements Runnable {
    private final static Logger log = Logger.getLogger(FileDescriptor.class.getName());
    private Queue<String> logHistory;
    private File execFile;
    private Process proc;
    private InputStream is;

    public FileExecutor(File execFile, Queue<String> logHistory) {
        this.execFile = execFile;
        this.logHistory = logHistory;
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
        byte arr[] = new byte[1024];
        try {
            while(proc.isAlive()) {
                int len = is.read(arr);
                if (len > 0) {
                    String out = execFile.getName() + " : " + new String(arr);
                    logHistory.offer(out);
                }
            }
        } catch (IOException e) {
        }
    }
}
