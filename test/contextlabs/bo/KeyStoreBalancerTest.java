package contextlabs.bo;

import contextlabs.bo.KeyStoreBalancer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KeyStoreBalancerTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * In function createMasterImpl(), test that number of tablets must be positive
     */
    @Test
    public void testCreateMasterImpl_numberTabletsIllegalArgumentException(){
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("The number of tablets must be positive");
        KeyStoreBalancer.createMasterImpl(0, null);

    }

    /**
     * In function createMasterImpl(), test that list of servers is not empty
     */
    @Test
    public void testCreateMasterImpl_numberServersIllegalArgumentException(){
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("The list of server names cannot be empty");
        KeyStoreBalancer.createMasterImpl(4, null);
    }

    /**
     * A test to model 4 tables and 2 tabletservers.
     */
    @Test
    public void testCreateMasterImpl() {
        int numTablets = 4;
        List<String> servers = Arrays.asList("tabletserver0", "tabletserver1");

        KeyStoreBalancer testBalancer = KeyStoreBalancer.createMasterImpl(4, servers);

        assertEquals(4, testBalancer.getAvailableTablets().size());
        assertEquals(2, testBalancer.getAvailableTabletServers().size());
    }

    /**
     *  A test of functionality of method getServerForKey(long key)
     */
    @Test
    public void testGetServerForKey() {
        int numTablets = 4;
        List<String> servers = Arrays.asList("tabletserver0", "tabletserver1");

        KeyStoreBalancer tb = KeyStoreBalancer.createMasterImpl(numTablets, servers);
        tb.balanceLoad();

        String serverName = null;
        try {

            serverName = tb.getServerForKey(-1);
            // fail("The key must be a positive value");
        } catch (Exception e) {
            assertEquals("java.lang.AssertionError: The key must be a postive value", e.toString());
        }

        try {

            serverName = tb.getServerForKey(200);
            // fail("The key must be a positive value");
        } catch (Exception e) {
            assertEquals("java.lang.AssertionError: The key must be a postive value", e.toString());
        }

        assertEquals("tabletserver0", serverName);


    }

    /**
     * a test of functinality for addServer(String serverName).  Ensure that a duplicate listing
     * does not appear on the load balancer.
     */
    @Test
    public void testAddServer() {
        int numTablets = 2;
        List<String> servers = Arrays.asList("tabletserver0");

        KeyStoreBalancer tb = KeyStoreBalancer.createMasterImpl(numTablets, servers);

        try {
            tb.addServer("tabletserver1");
            // fail("The key must be a postive value");
        } catch (Exception e) {
            assertEquals("java.lang.Exception: No items are available for removal from the balancer", e.toString());
        }

        // Positive Case: Adding a new TabletServer to the loab balancer yields 2 TabletServers
        assert(tb.theBalancer.entrySet().size() == 2);

        String[] expectedServers =  new String[] {"tabletserver0","tabletserver1"};
        String[] theServers = tb.theBalancer.values().toArray(new String[0]);
        assertArrayEquals(expectedServers, theServers);

        //Negative Case: Adding a third TabletServer, called "tabletserver1", does not introduce
        //           a duplicate entry to the load balancer.
        try {
            tb.addServer("tabletserver1");
            // fail("The key must be a postive value");
        } catch (Exception e) {
            assertEquals("java.lang.Exception: No items are available for removal from the balancer", e.toString());
        }

        assert(tb.theBalancer.entrySet().size() == 2);
    }

    /**
     * A test of functionality of method removerServer(String serverName). Ensure that items cannot be
     * removed from an empty load balancer
     */
    @Test
    public void testRemoveServer() {
        int numTablets = 2;
        List<String> servers = Arrays.asList("tabletserver0");

        KeyStoreBalancer tb = KeyStoreBalancer.createMasterImpl(numTablets, servers);

        try {
            tb.removeServer("tabletserver0");
            // fail("The key must be a postive value");
        } catch (Exception e) {
            assertEquals("java.lang.Exception: No items are available for removal from the balancer", e.toString());
        }

        // Positive Case: Removing the last remaining item on the loab balancer yields an empty balancer.
        assert(tb.theBalancer.isEmpty());

        // Negative Case: Now try to remove a server when the load balancer is empty.
        try {
            tb.removeServer("tabletserver0");
            // fail("The key must be a postive value");
        } catch (Exception e) {
            assertEquals("java.lang.Exception: No items are available for removal from the balancer", e.toString());
        }
    }
}