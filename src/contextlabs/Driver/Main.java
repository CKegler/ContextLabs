package contextlabs.Driver;

import contextlabs.bo.KeyStoreBalancer;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int numTablets = 4;
        List<String> servers = Arrays.asList("tabletserver0", "tabletserver1");

        KeyStoreBalancer keyStoreBalancer =
                KeyStoreBalancer.createMasterImpl(numTablets, servers);

        keyStoreBalancer.printAvailableTablets();

        keyStoreBalancer.balanceLoad();

        keyStoreBalancer.printTabletMapping();

        System.out.println("Adding TabletServer: tabletserver2");
        keyStoreBalancer.addServer("tabletserver2");
        keyStoreBalancer.printTabletMapping();

        System.out.println("Remove TabletServer: tabletserver0");
        keyStoreBalancer.removeServer("tabletserver0");
        keyStoreBalancer.printTabletMapping();

        System.out.println("Adding TabletServer: tabletserver0");
        keyStoreBalancer.addServer("tabletserver0");
        keyStoreBalancer.printTabletMapping();
    }
}
