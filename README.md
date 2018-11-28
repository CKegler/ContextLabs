# ContextLabs

# Load Balancing Assignment

**Background**

This problem relates to a sorted key/value store.  It stores mappings of keys to values, and all key/value entries are sorted by key. This allows fast indexed access for get and put calls, and also supports quickly scanning all entries starting at or near a specific key. 

The database distributes work across a cluster of servers by designating responsibility for different ranges of keys to different servers. Each range is a 'tablet', and each server is a 'tablet server'.  A tablet server is responsible for all reads and writes to entries within its ranges of keys. Every tablet is served by exactly one tablet server at a time.  Each tablet server can serve zero to many tablets at once. 

**Exercise**

For this exercise, write a class that 

1. takes as input a fixed number of tablets and a set of tablet servers
2. calculates the ranges that define each tablet
3. assigns each tablet to a tablet server

You can make a few simplifying assumptions for the purpose of this exercise

- The key space is the range of values from 0 to Java's Long.MAX_VALUE 
- Ranges should be defined by simply splitting up the key space into equal sized chunks
- Once the ranges that define a set of tablets are defined, they do not change

After getting the basics in place, add support for adding or removing servers to the list. A change to the list of servers should result in a rebalancing of the load of tablets across the servers such that each tablet server has as close to an equal number of tablets as possible.

Your rebalancing behavior should minimize reassignments of tablets from one server to another. This preference is important to the performance characteristics of the database, and we can discuss the reason why in person. 

##### Java version:  1.8.0_121 . 

  This exercise was compiled and tested against the Java version stated above.
  
**Project Organization **

This code project was authored in IntelliJ v 2018.1 Community, and this GitHub directory contains all of the project artefacts and libraries needed to compile and run the project.  It is structured in a conventional hierarchical enterprise package format. 

- source directory
  - src/contextlabs/bo .  Plain Old Java Objects (POJO) files exist here.
  - src/contextlabs/Driver . The class with the **Main method** is located here. Run this class to see sample output of functionality.
  
- testing directory
  - test/contextlabs/bo .  Test objects corresponding to POJOs in the src directory live here

**Test Coverage**

  Automated unit tests are authored with JUnit v4.12 to use the standardized library of annotations.  The library used for testing, junit-4.12.jar is included in the lib directory or it can be directly downloaded from the JUnit website and incorporated into the classpath of the project.
  
  For each POJO in the src/ directory, there is a corresponding Junit test class file.  Each test file contains test methods corresponding to the non-trivial methods in the POJOs
  
 **Design choices**

- src/contextlabs/bo
  - Tablet.java .  THis is used to model a tablet object providing a name, minIndex, and maxIndex. The difference in the min and mix index defines the range.
  
  - __(optional)__  TabletServer.java .  I did not implement this object because the initial model only uses a List of names of TabletServers. If tabletservers had more characteristics in addition to a name, then a full TabletServer object with the appropriate member variables is warranted.  
 
  - KeyStoreBalancer.java .  is the concrete Factory Pattern implementation of the load balancer. It is an extension of the abstract class in Master.java. The **crucial data structure** for the implmentation of a Load Balancer is a **SortedMap** to maintain the key ordering of tablets based on their minIndex.  The tablets are essentially ordered in increasing order of their intervals which will not change after their first instantiation.
  
  **The Load Balancing Algorithm for the Load Balancer**
    
    The most straight-forward way to implement load balancing in this simulation is to use a round-robin approach.  Place a Tablet on each available TabletServer; if there are Tablets still remaining, start from the beginning again and place the tablets on the servers.
It gives equal priority to the loads of all the tablets through the equal distribution of ranges.  If we wish to extend the load balancing to switch between more than one algorithm, then I would extend the implementation with another directory, such as src/contextlabs/algorithms. A design pattern, such as the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern) , could be implemented as way of separating an algorithm from an object structure on which it operates.

  
  **The Load Balancaer and Multi-threading Considerations**
  
  The load balancer must be considered in the context of multi-threading.  Multiple threads could potentially read or update the SortedMap data structure which associates Tablets to TabletServers. To provide consistency of the data, access to the SortedMap should be synchronized. It guarantees to provide mutually exclusive access to a shared resource with multiple threads in Java.
