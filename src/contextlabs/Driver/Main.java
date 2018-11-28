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

        System.out.println("Printing the available tablets and their ranges.\n");
        keyStoreBalancer.printAvailableTablets();

        keyStoreBalancer.balanceLoad();

        System.out.println("\nPrinting the mappings of tablets to their servers.\n");
        keyStoreBalancer.printTabletMapping();

        System.out.println("\nAdding TabletServer: tabletserver2");
        keyStoreBalancer.addServer("tabletserver2");
        keyStoreBalancer.printTabletMapping();

        System.out.println("\nRemoving TabletServer: tabletserver0");
        keyStoreBalancer.removeServer("tabletserver0");
        keyStoreBalancer.printTabletMapping();

        System.out.println("\nAdding or restoring TabletServer: tabletserver0");
        keyStoreBalancer.addServer("tabletserver0");
        keyStoreBalancer.printTabletMapping();
    }
}
