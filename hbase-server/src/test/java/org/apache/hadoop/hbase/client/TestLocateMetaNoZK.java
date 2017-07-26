package org.apache.hadoop.hbase.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MiniHBaseCluster;
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
public class TestLocateMetaNoZK {
  private static final Log LOG = LogFactory.getLog(TestLocateMetaNoZK.class);
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
    Configuration newConf = new Configuration(conf);
    newConf.set("hbase.master.all", master_locs);
    conn = (ConnectionImplementation) ConnectionFactory.createConnection(newConf);
    this.admin = conn.getAdmin();
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
  public void testMetaSentCorrectly() throws IOException {
    MiniHBaseCluster cluster = TEST_UTIL.getHBaseCluster();
    HMaster m = cluster.getMaster();
    HRegionLocation[] metaLocsFromMaster = m.locateMeta().getRegionLocations();
    HRegionLocation[] metaLocsFromConnection = conn.getAdmin().locateMeta().getRegionLocations();

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
    System.out.println("regionlocs " + metaLocsFromMaster.toString());
  }

  /**
   * This test ensures that the meta location found from client->connection->ZooKeeper is same as
   * client->connection->Master->ZooKeeper
   */
  @Test
  public void testMetaFromMaster() throws IOException {
    MiniHBaseCluster cluster = TEST_UTIL.getHBaseCluster();
    HMaster m = cluster.getMaster();
    HRegionLocation[] metaLocsViaMaster = conn.getAdmin().locateMeta().getRegionLocations();
    HRegionLocation[] metaLocsFromZK = conn.registry.getMetaRegionLocation().getRegionLocations();
    assertEquals(metaLocsViaMaster.length, metaLocsFromZK.length);
    for (int i = 0; i < metaLocsViaMaster.length; i++) {
      HRegionLocation locFromMaster = metaLocsViaMaster[i];
      HRegionLocation locFromZK = metaLocsFromZK[i];
      assertEquals(locFromMaster.getServerName(), locFromZK.getServerName());
      assertEquals(locFromMaster.getSeqNum(), locFromZK.getSeqNum());
      HRegionInfo rlFromMaster = locFromMaster.getRegionInfo();
      HRegionInfo rlFromZK = locFromZK.getRegionInfo();
      assertEquals(rlFromMaster.getTable(), rlFromMaster.getTable());
      assertEquals(rlFromMaster.getReplicaId(), rlFromZK.getReplicaId());
      assertEquals(rlFromMaster.getRegionId(), rlFromZK.getRegionId());
    }
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
