package contextlabs.bo;

import java.util.*;

public class KeyStoreBalancer extends Master {

    //Maintain a list of tablets
    private List<Tablet> availableTablets = new ArrayList<Tablet>();

    //Maintain a unique listing of TabletServers
    private Set<String> availableTabletServers = new HashSet<String>();

    public static SortedMap<Tablet, String> theBalancer
            = new TreeMap<Tablet, String>(new TabletComparator());

    /**
     * This class constructor is intentionally declared as private so that only
     * code within this class can invoke the constructor.  Because of the call to super(),
     * which invokes the super class, we cannot check parameter values passed to the parent class.
     * We therefore create a public method wrapper and do parameter checking on the parameters before
     * invoking this private class constructor.
     * @param numTablets
     * @param serverNames
     */
    private KeyStoreBalancer(int numTablets, List<String> serverNames) {

        super(numTablets, serverNames);
        //calculate the range that defines each tablet.
        long increment = Long.MAX_VALUE / numTablets;
        String tabletName;
        //create tablets with with equally divided ranges.
        for(int a=0; a < numTablets; a++){
            try {
                Tablet t;
                tabletName = String.format("tablet%d", a);
                if(a==0){
                    t = new Tablet(tabletName, 0, increment );
                } else {
                    //find the maximum value of the previous tablet and add 1
                    // to determine the min and max positions of the current tablet
                    long start = availableTablets.get(a-1).getMaxIndex() + 1;
                    t = new Tablet(tabletName, start, start + increment );
                }

                availableTablets.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end for loop

        // Now track available
        availableTabletServers.addAll(serverNames);
    }

    /**
     * createMasterImpl is the Factory Pattern implementation of the load balancer.
     * Since the class construtor is intentionally private, createMasterImpl is used from
     * outside the class to creat a KeyStoreBalancer object and do parameter checking before
     * the private class constructor is called.
     * @param numTablets -  a positive number of tablets to be created
     * @param serverNames - a list of the names of the TabletServers
     * @return - an instance of KeyStoreBalanncer after call to private class constructor
     */
    public static KeyStoreBalancer createMasterImpl(int numTablets, List<String> serverNames){

        if(numTablets < 1 ){
            throw new IllegalArgumentException("The number of tablets must be positive");
        }
        if(serverNames == null || serverNames.isEmpty()){
            throw new IllegalArgumentException("The list of server names cannot be empty");
        }

        return new KeyStoreBalancer( numTablets, serverNames);
    }

    /**
     * balanceLoad() is a round-robin approach to distributing the Tablets
     * on the available TabletServers. One tablet is placed on each TabletServer. If there
     * are more tablets than servers, then we start at the beginning placing the remaining tablets on
     * TabletServers.
     */

    public synchronized void balanceLoad(){

        theBalancer.clear();

        int numServers = availableTabletServers.size();
        final String[] tabletServerArray = availableTabletServers.toArray(new String[0]);
        int a = 0;

        for(Tablet tablet: availableTablets){
            //use the startIndex of each tablet as the key in the Map.
            //String key = tablet.getTabletName();
            // Use modulo arithmetic to assign tablets to servers
            String serverName = tabletServerArray[a % numServers];
            theBalancer.put(tablet,serverName);
            a++;
        }

        //System.out.println(theBalancer.toString());
    }

    @Override
    public String getServerForKey(long key) {
        String serverName = null;
        if((key < 0)){
            try {
                throw new Exception("The key must be a postive value");
            } catch (Exception e) {
                e.getMessage();
            }
            return serverName;
        }

        for(Map.Entry<Tablet, String> t: theBalancer.entrySet()){
            if(key >= t.getKey().getMinIndex() && key <= t.getKey().getMaxIndex()){
                serverName = t.getValue();
            }
        }
//        serverName = theBalancer.entrySet()
//                                .stream()
//                                .filter(map -> (key >= map.getKey().getMinIndex() && key <= map.getKey().getMaxIndex()) )
//                                .map(map -> map.getValue())
//                                .collect(Collectors.joining());
        return serverName;
    }

    @Override
    public void addServer(String serverName) {

        if(serverName == null) return;
        // check for pre-existing servers of same name; don't allow duplicates
        if(availableTabletServers.contains(serverName)){
            return;
        }

        boolean added = availableTabletServers.add(serverName);

        if(added){
            // rebalance the load on the LoadBalancer
            balanceLoad();
        }
    }

    @Override
    public void removeServer(String serverName) {

        if(serverName == null) return;
        if(theBalancer == null || theBalancer.isEmpty() ) {
            try {
                throw new Exception("No items are available for removal from the balancer");
            } catch (Exception e) {
               System.out.println(e.getMessage());
            }
            return;
        }
        // remove from the list of available servers
        boolean removed = availableTabletServers.remove(serverName);

        if(removed){
            //rebalance the LoadBalancer
            balanceLoad();
        }
    }

    public void printAvailableTablets(){
        for(Tablet tb: availableTablets){
            System.out.println(tb.toString());
        }
    }


    public void printTabletMapping(){
        System.out.println("The Tablet to Server mapping is:");
        theBalancer.forEach((K,V) -> System.out.println( K.getTabletName() + " --> " + V) );
    }


    /* ------  Getters and Setters ------------*/
    public List<Tablet> getAvailableTablets() {
        return availableTablets;
    }

    public Set<String> getAvailableTabletServers() {
        return availableTabletServers;
    }
}


class TabletComparator implements Comparator<Tablet>{

    @Override
    public int compare(Tablet e1, Tablet e2) {
        if( e1.getMinIndex() > e2.getMinIndex() ){
            return 1;
        } else {
            return -1;
        }
    } // end of compare

}