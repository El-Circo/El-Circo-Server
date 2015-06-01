package github.turtlehunter.ElCircoServer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggingManager {
    FileWriter log;

    public LoggingManager() {
        try {
            log = new FileWriter(Main.instance.references.getReference("logFile"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String str) {
        try {
            log.write(str+"\n");
            log.flush();
            sendNotice();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR LOGGING: "+str);

        }

    }

    private void sendNotice() {
        //TODO Send message to network admins
    }

    public void downloadLog() {
        //print content of log encrypted

    }


    public String crash(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public void info(String str) {
        try {
            log.write(str+"\n");
            log.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR LOGGING: "+str);

        }
    }
}
