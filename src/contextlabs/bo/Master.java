package contextlabs.bo;

import java.util.List;

public abstract class Master {
    protected int numTablets;
    protected List<String> serverNames;

    public Master(int numTablets, List<String> serverNames) {
        this.numTablets = numTablets;
        this.serverNames = serverNames;
    }
    public abstract String getServerForKey(long key);
    public abstract void addServer(String serverName);
    public abstract void removeServer(String serverName);
}
