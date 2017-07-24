package org.apache.hadoop.hbase.client;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.apache.hadoop.hbase.RegionLocations;
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

@Category({ MediumTests.class })
public class TestFindActiveMaster {
  private static final Log LOG = LogFactory.getLog(TestFindActiveMaster.class);
  private final static HBaseTestingUtility TEST_UTIL = new HBaseTestingUtility();
  private Admin admin;
  public static final int NUM_MASTERS = 3;
  private ConnectionImplementation conn;

  @Rule
  public TestName name = new TestName();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_UTIL.getConfiguration().setInt("hbase.regionserver.msginterval", 100);
    TEST_UTIL.getConfiguration().setInt("hbase.client.pause", 250);
    TEST_UTIL.getConfiguration().setInt("hbase.client.retries.number", 6);
    TEST_UTIL.getConfiguration().setInt("hbase.regionserver.metahandler.count", 30);
    TEST_UTIL.getConfiguration().setBoolean("hbase.master.enabletable.roundrobin", true);
    // locs of masters(backups as well) comma separated
    // TEST_UTIL.getConfiguration().set("hbase.master.all",
    // "localhost,-1,-1;localhost,8282,1122212122;localhost,5543,131141413");
    TEST_UTIL.startMiniCluster(NUM_MASTERS, 3, false);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    TEST_UTIL.shutdownMiniCluster();
  }

  @Before
  public void setUp() throws Exception {
    Configuration conf = TEST_UTIL.getConfiguration();
    String master_locs = constructMasterLocsStr();
    conf.set("hbase.master.all", master_locs);

    Configuration newConf = new Configuration(conf);
    newConf.set("hbase.master.all", master_locs);

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
   * This test checks that the meta locations in master and connection are same, thus ensuring the
   * rpc from client to master serialized and deserialized locations correctly
   */
  @Test
  public void testLocateMeta() throws IOException {
    MiniHBaseCluster cluster = TEST_UTIL.getHBaseCluster();
    HMaster m = cluster.getMaster();
    HRegionLocation[] metaLocsFromMaster = m.locateMeta().getRegionLocations();

    // connection calls admin for locate meta
    HRegionLocation[] metaLocsFromConnection = admin.locateMeta().getRegionLocations();

    assertEquals(metaLocsFromMaster.length, metaLocsFromConnection.length);
    for (int i = 0; i < metaLocsFromMaster.length; i++) {

      HRegionLocation locFromMaster = metaLocsFromMaster[i];
      HRegionLocation locFromConnection = metaLocsFromConnection[i];
      assertEquals(locFromMaster.getServerName(), locFromConnection.getServerName());
      assertEquals(locFromMaster.getSeqNum(), locFromConnection.getSeqNum());
      HRegionInfo rlFromMaster = locFromMaster.getRegionInfo();
      HRegionInfo rlFromConnection = locFromConnection.getRegionInfo();
      assertEquals(rlFromMaster.getTable(), rlFromMaster.getTable());
      assertEquals(rlFromMaster.getReplicaId(), rlFromConnection.getReplicaId());
      assertEquals(rlFromMaster.getRegionId(), rlFromConnection.getRegionId());

    }
  }


  @Test
  public void testFoo() throws IOException {
    System.out.println("in test find active master");

    final String name = this.name.getMethodName();
    HTableDescriptor htd1 = new HTableDescriptor(TableName.valueOf(name));
    htd1.addFamily(new HColumnDescriptor(HConstants.CATALOG_FAMILY));
    this.admin.createTable(htd1);
    // conn.getTable(htd1.getTableName()).close();

    Table table1 = conn.getTable(TableName.valueOf(name));
    // List<HRegionLocation> table1Loc = conn.locateRegions(table1.getName());
    RegionLocations metaLocs = conn.locateMeta(table1.getName(), false, -1);
    // System.out.println("table1Loc " + table1Loc.toString());
    System.out.println("meta locs " + metaLocs.toString());

    HMaster firstMaster = TEST_UTIL.getMiniHBaseCluster().getMaster(0);

    System.out.println("aborting master " + firstMaster.getServerName());
    // firstMaster.stopMaster();
    firstMaster.abort(null);

    RegionLocations metaLocsDuplicate = conn.locateMeta(table1.getName(), false, -1);
    System.out.println("meta locs duplicate" + metaLocsDuplicate.toString());

  }

  public String constructMasterLocsStr() {
    MiniHBaseCluster cluster = TEST_UTIL.getMiniHBaseCluster();
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
