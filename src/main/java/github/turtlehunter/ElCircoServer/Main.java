package github.turtlehunter.ElCircoServer;

import org.jivesoftware.smack.SmackException;

import java.util.HashMap;
import java.util.Map;

public class Main {
//TODO AdminShell handleMessages sendMessages allFeatures initReferences

    public static Main instance;
    public boolean runServer = true;
    public References references;
    public AdminShell adminShell;
    public NetworkManager networkManager;
    public DatabaseManager databaseManager;
    public LoggingManager loggingManager;
    public SecurityManager securityManager;

    public Main() {
        preInit();
        init();
        posInit();
        while (runServer) {
            loop();
        }
        endAll();
    }

    private void endAll() {

    }

    private void loop() {
        //crude loop to keep connection open for receiving messages
    }

    private void posInit() {

    }

    private void init() {
        //TODO delete sample
        try {
            networkManager.connect(references.getReference("projectid"),references.getReference("apikey"));
        } catch (Exception e) {
            Main.instance.loggingManager.write("Error connecting. ---Log Start--- "+Main.instance.loggingManager.crash(e)+"---Log End---");
        }

        // Send a sample hello downstream message to a device.
        String messageId = networkManager.nextMessageId();
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("Message", "Ahha, it works!");
        payload.put("CCS", "Dummy Message");
        payload.put("EmbeddedMessageId", messageId);
        String collapseKey = "sample";
        Long timeToLive = 10000L;
        String message = NetworkManager.createJsonMessage(references.getReference("testphoneregid"), messageId, payload,
                collapseKey, timeToLive, true);

        try {
            networkManager.sendDownstreamMessage(message);
        } catch (SmackException.NotConnectedException e) {
            Main.instance.loggingManager.write("NO CONNECTION. ---Log Start--- "+Main.instance.loggingManager.crash(e)+"---Log End---");
        }


    }

    private void preInit() {
        loggingManager = new LoggingManager();
        references = new References();
        adminShell = new AdminShell();
        networkManager = new NetworkManager();
        databaseManager = new DatabaseManager();
        securityManager = new SecurityManager();
    }

    public static void main(String[] args) {
        instance = new Main();
    }
}
