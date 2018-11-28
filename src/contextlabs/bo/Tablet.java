package contextlabs.bo;

public class Tablet {

    private String tabletName;
    private long minIndex;
    private long maxIndex;


    public Tablet(String tabletName, long minIndex, long maxIndex) throws Exception {
        if(tabletName == null || tabletName.isEmpty()){
            throw new Exception("tablet name cannot be blank");
        }
        if((minIndex < 0)){
           throw new Exception("minIndex must be positive");
        }
        if((maxIndex < 0)){
            throw new Exception("maxIndex must be positive");
        }
        if(minIndex > maxIndex){
            throw new Exception("minIndex cannot be greater than maxIndex");
        }
        this.tabletName = tabletName;
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

    /**
     *
     * @return - the difference between maxIndex and minIndex
     */
    public long getRange(){
        return (maxIndex - minIndex);
    }

    /**
     * A getter to retrieve the starting index of a tablet. There is no
     * corresponding setter to prevent future alterations.
     * @return - the starting index of the range on the tablet
     */
    public long getMinIndex() {
        return minIndex;
    }


    /**
     * A getter to retrieve the end index of a tablet. There is no
     * corresponding setter to prevent future alterations.
     * @return - the end index of the range on the tablet
     */
    public long getMaxIndex() {
        return maxIndex;
    }

    /**
     * A getter to retrieve name of a tablet. There is no
     * corresponding setter to prevent future alterations.
     * @return - name of the tablet
     */
    public String getTabletName() {
        return tabletName;
    }

    @Override
    public String toString() {
        return String.format("%s has keys from %d-%d", this.tabletName, this.minIndex, this.maxIndex);
    }
}
