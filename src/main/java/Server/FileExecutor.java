package Server;

import java.util.concurrent.Callable;

public class FileExecutor implements Callable {
    private String cmd;
    private boolean wait;
    public FileExecutor(String cmd, boolean wait) {
        this.cmd = cmd;
        this.wait = wait;
    }
    @Override
    public Process call() throws Exception {
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
        }
        catch(java.io.IOException e) {
            return null;
        }
        if (wait) {
            try {
                p.waitFor();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return p;
    }
}
