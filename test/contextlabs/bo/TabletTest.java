package contextlabs.bo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TabletTest {

    @org.junit.Before
    public void setUp() throws Exception {
      //  KeyStoreBalancer.createMasterImpl(4, Arrays.asList("tablerserver0", "tabletserver1"));
    }

    @org.junit.Test
    public void testTabletCreation()  {
        String tabletName = "testtablet0";
        // testing negative case where min < 0
        Exception testException = new Exception();
        try {
            Tablet test1 = new Tablet(tabletName, -1,1);
            fail("minIndex must be positive");
        } catch (Exception e) {
            assertEquals(String.format("%s: minIndex must be positive", testException), e.toString());
        }

        // testing negative case where max < 0
        try {
            Tablet test2 = new Tablet(tabletName, 2, -1);
            fail("maxIndex must be positive");
        } catch (Exception e) {
            assertEquals(String.format("%s: maxIndex must be positive", testException), e.toString());
        }
        // testing negative case where minIndex > maxIndex, which is an invalid range
        try {
            Tablet test3 = new Tablet(tabletName, 5, 1);
            fail("minIndex cannot be greater than maxIndex");
        } catch (Exception e) {
            assertEquals(String.format("%s: minIndex cannot be greater than maxIndex", testException), e.toString());
        }

        // testing positive case creating tablet of min=2 , max = 10, range = 8
        Tablet test4 = null;
        try {
            test4 = new Tablet(tabletName, 2, 10);
        } catch (Exception e) {

        }
        assertEquals("testtablet0", test4.getTabletName());
        assertEquals(2,  test4.getMinIndex());
        assertEquals(10, test4.getMaxIndex());
    }

    @org.junit.Test
    public void getRange() {
        String tabletName = "testtablet0";
        // testing positive case creating tablet of min=2 , max = 10, range = 8
        Tablet test = null;
        try {
            test = new Tablet(tabletName, 2, 10);
        } catch (Exception e) {

        }

        assertEquals(8,  test.getRange());
    }


}