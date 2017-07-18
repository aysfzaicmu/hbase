package org.apache.hadoop.hbase.client;


import java.io.IOException;
import java.util.List;

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
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.testclassification.ClientTests;
import org.apache.hadoop.hbase.testclassification.MediumTests;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;

@Category({ MediumTests.class, ClientTests.class })
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
    // this.admin = TEST_UTIL.getHBaseAdmin();
    conn = (ConnectionImplementation) ConnectionFactory.createConnection(conf);// PASS IN CLONED
    this.admin = conn.getAdmin(); // VERSIONNNNNNN

  }

  @After
  public void tearDown() throws Exception {
    for (HTableDescriptor htd : this.admin.listTables()) {
      TEST_UTIL.deleteTable(htd.getTableName());
    }
  }


  @Test
  public void testFoo() throws IOException {
    System.out.println("in test find active master");

    final String name = this.name.getMethodName();
    HTableDescriptor htd1 = new HTableDescriptor(TableName.valueOf(name));
    htd1.addFamily(new HColumnDescriptor(HConstants.CATALOG_FAMILY));
    this.admin.createTable(htd1);
    conn.getTable(htd1.getTableName()).close();

    Table table1 = conn.getTable(TableName.valueOf(name));
    List<HRegionLocation> table1Loc = conn.locateRegions(table1.getName());
    RegionLocations metaLocs = conn.locateMeta(table1.getName(),false,-1);

  }

  public String constructMasterLocsStr() {
    MiniHBaseCluster cluster = TEST_UTIL.getMiniHBaseCluster();
    StringBuilder confMasterLocsSb = new StringBuilder();

    for (int i = 0; i < NUM_MASTERS - 1; i++) {
      HMaster master = cluster.getMaster(i);
      System.out.println("master " + master.getServerName());
      confMasterLocsSb.append(master.getServerName() + ";");
    }
    confMasterLocsSb.append(cluster.getMaster(NUM_MASTERS - 1).getServerName());
    return confMasterLocsSb.toString();

  }

}
