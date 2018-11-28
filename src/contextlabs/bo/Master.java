package contextlabs.bo;

import java.util.List;

/**
 * Abstract Class Master was provided in the initial project specification.
 * Its concrete implementation is therefore deferred to subclass(es) derived
 * from Class Master.
 */
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
