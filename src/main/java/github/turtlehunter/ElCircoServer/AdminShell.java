package github.turtlehunter.ElCircoServer;

import java.util.Arrays;

public class AdminShell {
    public enum AdminCommands {reboot, update};

    public void handle(String str) {
        String[] strs = str.split(" ");
        for(AdminCommands value:AdminCommands.values()) {
            if(value.name().equals(strs[1])) {
                run(value, Arrays.copyOfRange(strs, 2, strs.length));
            }
        }
    }

    private void run(AdminCommands value, String[] strings) {
        if(value.equals(AdminCommands.reboot)) {
            reboot();
        } else if(value.equals(AdminCommands.update)) {
            update();
        }
    }

    private void update() {

    }

    private void reboot() {

    }
}
