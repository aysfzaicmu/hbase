package org.apache.hadoop.hbase.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.apache.hadoop.hbase.RegionLocations;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.testclassification.MediumTests;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;

/*
 * These tests check that a client can connect to a valid master, when master locations are passed
 * in configuration. See HBASE-18095
 */
@Category({ MediumTests.class })
public class TestConnectToMaster {
  private static final Log LOG = LogFactory.getLog(TestConnectToMaster.class);
  private final static HBaseTestingUtility TEST_UTIL = new HBaseTestingUtility();
  private Admin admin;
  public static final int NUM_MASTERS = 3;
  private ConnectionImplementation conn;
  private ServerName DUMMY_SERVERNAME =
      ServerName.valueOf("dummyServerName,9999,12121212121");
  private String dummy_conf;
  private static MiniHBaseCluster cluster;
  @Rule
  public TestName name = new TestName();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_UTIL.getConfiguration().setInt("hbase.regionserver.msginterval", 100);
    TEST_UTIL.getConfiguration().setInt("hbase.client.pause", 250);
    TEST_UTIL.getConfiguration().setInt("hbase.client.retries.number", 6);
    TEST_UTIL.getConfiguration().setInt("hbase.regionserver.metahandler.count", 30);
    TEST_UTIL.getConfiguration().setBoolean("hbase.master.enabletable.roundrobin", true);
    TEST_UTIL.startMiniCluster(NUM_MASTERS, 1, false);
    cluster = TEST_UTIL.getHBaseCluster();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    TEST_UTIL.shutdownMiniCluster();
  }

  @Before
  public void setUp() throws Exception {
    Configuration conf = TEST_UTIL.getConfiguration();
    String master_locs = constructMasterLocsStr();
    // conf.set("hbase.master.all", master_locs);

    Configuration newConf = new Configuration(conf);
    this.dummy_conf = DUMMY_SERVERNAME + ";" + master_locs;
    System.out.println("dummy conf " + dummy_conf);
    newConf.set("hbase.master.all", dummy_conf);

    conn = (ConnectionImplementation) ConnectionFactory.createConnection(newConf);
    // conn = (ConnectionImplementation) ConnectionFactory.createConnection(conf);// PASS IN CLONED
    this.admin = conn.getAdmin(); // VERSIONNNNNNN

  }

  @After
  public void tearDown() throws Exception {
    for (HTableDescriptor htd : this.admin.listTables()) {
      TEST_UTIL.deleteTable(htd.getTableName());
    }
  }

  /**
   * A dummy master servername has been passed in the configuration. Since the connection loops over
   * the configuration locations, it should try the first location which is the dummy servername.
   * This would cause an IOException in connection which is caught, and the next master location is
   * tried. The test simply creates a table and accesses it. It also checks that the region info
   * servername returned is valid
   */
  @Test
  public void testConnectingToNonDummyServer() throws IOException {
    System.out.println("in test find active master");

    final String name = this.name.getMethodName();
    HTableDescriptor htd1 = new HTableDescriptor(TableName.valueOf(name));
    htd1.addFamily(new HColumnDescriptor(HConstants.CATALOG_FAMILY));
    this.admin.createTable(htd1);
    // conn.getTable(htd1.getTableName()).close();

    Table table = conn.getTable(TableName.valueOf(name));
    RegionLocations metaLocs = conn.locateMeta(table.getName(), false, -1);
    assertNotEquals(metaLocs, null);
    assertEquals(metaLocs.getRegionLocations().length, 1); // contains loc for a single meta
    HRegionLocation regionLoc = metaLocs.getRegionLocation();
    assertTrue(checkValidServerName(regionLoc.getServerName()));

  }

  public boolean checkValidServerName(ServerName serverName) {

    boolean serverNameFound = false;
    for (int i = 0; i < NUM_MASTERS; i++) {
      HMaster master = cluster.getMaster(i);
      if (serverName.equals(master.getServerName())) {
        serverNameFound = true;
      }
    }
    return serverNameFound;
  }

  public String constructMasterLocsStr() {
    StringBuilder confMasterLocsSb = new StringBuilder();
    for (int i = 0; i < NUM_MASTERS - 1; i++) {
      HMaster master = cluster.getMaster(i);
      // master.start();
      System.out.println("master " + master.getServerName());
      confMasterLocsSb.append(master.getServerName() + ";");
    }
    confMasterLocsSb.append(cluster.getMaster(NUM_MASTERS - 1).getServerName());
    return confMasterLocsSb.toString();

  }

}
