package org.apache.hadoop.hbase.client;

import org.apache.hadoop.hbase.client.ConnectionImplementation.MasterServiceState;
import org.apache.hadoop.hbase.shaded.com.google.protobuf.RpcController;
import org.apache.hadoop.hbase.shaded.com.google.protobuf.ServiceException;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.DrainRegionServersRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.DrainRegionServersResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.IsBalancerEnabledRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.IsBalancerEnabledResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.IsNormalizerEnabledRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.IsNormalizerEnabledResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.ListDrainingRegionServersRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.ListDrainingRegionServersResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.LocateMetaRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.LocateMetaResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.NormalizeRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.NormalizeResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.RemoveDrainFromRegionServersRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.RemoveDrainFromRegionServersResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.SecurityCapabilitiesRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.SecurityCapabilitiesResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.SetNormalizerRunningRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.MasterProtos.SetNormalizerRunningResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.QuotaProtos.GetQuotaStatesRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.QuotaProtos.GetQuotaStatesResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.QuotaProtos.GetSpaceQuotaRegionSizesRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.QuotaProtos.GetSpaceQuotaRegionSizesResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.AddReplicationPeerRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.AddReplicationPeerResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.DisableReplicationPeerRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.DisableReplicationPeerResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.EnableReplicationPeerRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.EnableReplicationPeerResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.GetReplicationPeerConfigRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.GetReplicationPeerConfigResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.ListReplicationPeersRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.ListReplicationPeersResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.RemoveReplicationPeerRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.RemoveReplicationPeerResponse;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.UpdateReplicationPeerConfigRequest;
import org.apache.hadoop.hbase.shaded.protobuf.generated.ReplicationProtos.UpdateReplicationPeerConfigResponse;

public class MasterKeepAliveConnectionDelegate implements MasterKeepAliveConnection {

  private MasterServiceState mss;
  private MasterProtos.MasterService.BlockingInterface stub;

  public MasterKeepAliveConnectionDelegate(MasterServiceState mss,
      MasterProtos.MasterService.BlockingInterface stub) {
    this.mss = mss;
    this.stub = stub;
  }

  public MasterProtos.AbortProcedureResponse abortProcedure(RpcController controller,
      MasterProtos.AbortProcedureRequest request) throws ServiceException {
    return stub.abortProcedure(controller, request);
  }

  public MasterProtos.ListProceduresResponse listProcedures(RpcController controller,
      MasterProtos.ListProceduresRequest request) throws ServiceException {
    return stub.listProcedures(controller, request);
  }

  public MasterProtos.ListLocksResponse listLocks(RpcController controller,
      MasterProtos.ListLocksRequest request) throws ServiceException {
    return stub.listLocks(controller, request);
  }

  public MasterProtos.AddColumnResponse addColumn(RpcController controller,
      MasterProtos.AddColumnRequest request) throws ServiceException {
    return stub.addColumn(controller, request);
  }

  public MasterProtos.DeleteColumnResponse deleteColumn(RpcController controller,
      MasterProtos.DeleteColumnRequest request) throws ServiceException {
    return stub.deleteColumn(controller, request);
  }

  public MasterProtos.ModifyColumnResponse modifyColumn(RpcController controller,
      MasterProtos.ModifyColumnRequest request) throws ServiceException {
    return stub.modifyColumn(controller, request);
  }

  public MasterProtos.MoveRegionResponse moveRegion(RpcController controller,
      MasterProtos.MoveRegionRequest request) throws ServiceException {
    return stub.moveRegion(controller, request);
  }

  public MasterProtos.MergeTableRegionsResponse mergeTableRegions(RpcController controller,
      MasterProtos.MergeTableRegionsRequest request) throws ServiceException {
    return stub.mergeTableRegions(controller, request);
  }

  public MasterProtos.DispatchMergingRegionsResponse dispatchMergingRegions(
      RpcController controller, MasterProtos.DispatchMergingRegionsRequest request)
      throws ServiceException {
    return stub.dispatchMergingRegions(controller, request);
  }

  public MasterProtos.AssignRegionResponse assignRegion(RpcController controller,
      MasterProtos.AssignRegionRequest request) throws ServiceException {
    return stub.assignRegion(controller, request);
  }

  public MasterProtos.UnassignRegionResponse unassignRegion(RpcController controller,
      MasterProtos.UnassignRegionRequest request) throws ServiceException {
    return stub.unassignRegion(controller, request);
  }

  public MasterProtos.OfflineRegionResponse offlineRegion(RpcController controller,
      MasterProtos.OfflineRegionRequest request) throws ServiceException {
    return stub.offlineRegion(controller, request);
  }

  public MasterProtos.SplitTableRegionResponse splitRegion(RpcController controller,
      MasterProtos.SplitTableRegionRequest request) throws ServiceException {
    return stub.splitRegion(controller, request);
  }

  public MasterProtos.DeleteTableResponse deleteTable(RpcController controller,
      MasterProtos.DeleteTableRequest request) throws ServiceException {
    return stub.deleteTable(controller, request);
  }

  public MasterProtos.TruncateTableResponse truncateTable(RpcController controller,
      MasterProtos.TruncateTableRequest request) throws ServiceException {
    return stub.truncateTable(controller, request);
  }

  public MasterProtos.EnableTableResponse enableTable(RpcController controller,
      MasterProtos.EnableTableRequest request) throws ServiceException {
    return stub.enableTable(controller, request);
  }

  public MasterProtos.DisableTableResponse disableTable(RpcController controller,
      MasterProtos.DisableTableRequest request) throws ServiceException {
    return stub.disableTable(controller, request);
  }

  public MasterProtos.ModifyTableResponse modifyTable(RpcController controller,
      MasterProtos.ModifyTableRequest request) throws ServiceException {
    return stub.modifyTable(controller, request);
  }

  public MasterProtos.CreateTableResponse createTable(RpcController controller,
      MasterProtos.CreateTableRequest request) throws ServiceException {
    return stub.createTable(controller, request);
  }

  public MasterProtos.ShutdownResponse shutdown(RpcController controller,
      MasterProtos.ShutdownRequest request) throws ServiceException {
    return stub.shutdown(controller, request);
  }

  public MasterProtos.StopMasterResponse stopMaster(RpcController controller,
      MasterProtos.StopMasterRequest request) throws ServiceException {
    return stub.stopMaster(controller, request);
  }

  public MasterProtos.IsInMaintenanceModeResponse isMasterInMaintenanceMode(
      final RpcController controller, final MasterProtos.IsInMaintenanceModeRequest request)
      throws ServiceException {
    return stub.isMasterInMaintenanceMode(controller, request);
  }

  public MasterProtos.BalanceResponse balance(RpcController controller,
      MasterProtos.BalanceRequest request) throws ServiceException {
    return stub.balance(controller, request);
  }

  public MasterProtos.SetBalancerRunningResponse setBalancerRunning(RpcController controller,
      MasterProtos.SetBalancerRunningRequest request) throws ServiceException {
    return stub.setBalancerRunning(controller, request);
  }

  public NormalizeResponse normalize(RpcController controller, NormalizeRequest request)
      throws ServiceException {
    return stub.normalize(controller, request);
  }

  public SetNormalizerRunningResponse setNormalizerRunning(RpcController controller,
      SetNormalizerRunningRequest request) throws ServiceException {
    return stub.setNormalizerRunning(controller, request);
  }

  public MasterProtos.RunCatalogScanResponse runCatalogScan(RpcController controller,
      MasterProtos.RunCatalogScanRequest request) throws ServiceException {
    return stub.runCatalogScan(controller, request);
  }

  public MasterProtos.EnableCatalogJanitorResponse enableCatalogJanitor(RpcController controller,
      MasterProtos.EnableCatalogJanitorRequest request) throws ServiceException {
    return stub.enableCatalogJanitor(controller, request);
  }

  public MasterProtos.IsCatalogJanitorEnabledResponse isCatalogJanitorEnabled(
      RpcController controller, MasterProtos.IsCatalogJanitorEnabledRequest request)
      throws ServiceException {
    return stub.isCatalogJanitorEnabled(controller, request);
  }

  public MasterProtos.RunCleanerChoreResponse runCleanerChore(RpcController controller,
      MasterProtos.RunCleanerChoreRequest request) throws ServiceException {
    return stub.runCleanerChore(controller, request);
  }

  public MasterProtos.SetCleanerChoreRunningResponse setCleanerChoreRunning(
      RpcController controller, MasterProtos.SetCleanerChoreRunningRequest request)
      throws ServiceException {
    return stub.setCleanerChoreRunning(controller, request);
  }

  public MasterProtos.IsCleanerChoreEnabledResponse isCleanerChoreEnabled(RpcController controller,
      MasterProtos.IsCleanerChoreEnabledRequest request) throws ServiceException {
    return stub.isCleanerChoreEnabled(controller, request);
  }

  public ClientProtos.CoprocessorServiceResponse execMasterService(RpcController controller,
      ClientProtos.CoprocessorServiceRequest request) throws ServiceException {
    return stub.execMasterService(controller, request);
  }

  public MasterProtos.SnapshotResponse snapshot(RpcController controller,
      MasterProtos.SnapshotRequest request) throws ServiceException {
    return stub.snapshot(controller, request);
  }

  public MasterProtos.GetCompletedSnapshotsResponse getCompletedSnapshots(RpcController controller,
      MasterProtos.GetCompletedSnapshotsRequest request) throws ServiceException {
    return stub.getCompletedSnapshots(controller, request);
  }

  public MasterProtos.DeleteSnapshotResponse deleteSnapshot(RpcController controller,
      MasterProtos.DeleteSnapshotRequest request) throws ServiceException {
    return stub.deleteSnapshot(controller, request);
  }

  public MasterProtos.IsSnapshotDoneResponse isSnapshotDone(RpcController controller,
      MasterProtos.IsSnapshotDoneRequest request) throws ServiceException {
    return stub.isSnapshotDone(controller, request);
  }

  public MasterProtos.RestoreSnapshotResponse restoreSnapshot(RpcController controller,
      MasterProtos.RestoreSnapshotRequest request) throws ServiceException {
    return stub.restoreSnapshot(controller, request);
  }

  public MasterProtos.ExecProcedureResponse execProcedure(RpcController controller,
      MasterProtos.ExecProcedureRequest request) throws ServiceException {
    return stub.execProcedure(controller, request);
  }

  public MasterProtos.ExecProcedureResponse execProcedureWithRet(RpcController controller,
      MasterProtos.ExecProcedureRequest request) throws ServiceException {
    return stub.execProcedureWithRet(controller, request);
  }

  public MasterProtos.IsProcedureDoneResponse isProcedureDone(RpcController controller,
      MasterProtos.IsProcedureDoneRequest request) throws ServiceException {
    return stub.isProcedureDone(controller, request);
  }

  public MasterProtos.GetProcedureResultResponse getProcedureResult(RpcController controller,
      MasterProtos.GetProcedureResultRequest request) throws ServiceException {
    return stub.getProcedureResult(controller, request);
  }

  public MasterProtos.IsMasterRunningResponse isMasterRunning(RpcController controller,
      MasterProtos.IsMasterRunningRequest request) throws ServiceException {
    return stub.isMasterRunning(controller, request);
  }

  public MasterProtos.ModifyNamespaceResponse modifyNamespace(RpcController controller,
      MasterProtos.ModifyNamespaceRequest request) throws ServiceException {
    return stub.modifyNamespace(controller, request);
  }

  public MasterProtos.CreateNamespaceResponse createNamespace(RpcController controller,
      MasterProtos.CreateNamespaceRequest request) throws ServiceException {
    return stub.createNamespace(controller, request);
  }

  public MasterProtos.DeleteNamespaceResponse deleteNamespace(RpcController controller,
      MasterProtos.DeleteNamespaceRequest request) throws ServiceException {
    return stub.deleteNamespace(controller, request);
  }

  public MasterProtos.GetNamespaceDescriptorResponse getNamespaceDescriptor(
      RpcController controller, MasterProtos.GetNamespaceDescriptorRequest request)
      throws ServiceException {
    return stub.getNamespaceDescriptor(controller, request);
  }

  public MasterProtos.ListNamespaceDescriptorsResponse listNamespaceDescriptors(
      RpcController controller, MasterProtos.ListNamespaceDescriptorsRequest request)
      throws ServiceException {
    return stub.listNamespaceDescriptors(controller, request);
  }

  public MasterProtos.ListTableDescriptorsByNamespaceResponse listTableDescriptorsByNamespace(
      RpcController controller, MasterProtos.ListTableDescriptorsByNamespaceRequest request)
      throws ServiceException {
    return stub.listTableDescriptorsByNamespace(controller, request);
  }

  public MasterProtos.ListTableNamesByNamespaceResponse listTableNamesByNamespace(
      RpcController controller, MasterProtos.ListTableNamesByNamespaceRequest request)
      throws ServiceException {
    return stub.listTableNamesByNamespace(controller, request);
  }

  public MasterProtos.GetTableStateResponse getTableState(RpcController controller,
      MasterProtos.GetTableStateRequest request) throws ServiceException {
    return stub.getTableState(controller, request);
  }

  public void close() {
  }

  public MasterProtos.GetSchemaAlterStatusResponse getSchemaAlterStatus(RpcController controller,
      MasterProtos.GetSchemaAlterStatusRequest request) throws ServiceException {
    return stub.getSchemaAlterStatus(controller, request);
  }

  public MasterProtos.GetTableDescriptorsResponse getTableDescriptors(RpcController controller,
      MasterProtos.GetTableDescriptorsRequest request) throws ServiceException {
    return stub.getTableDescriptors(controller, request);
  }

  public MasterProtos.GetTableNamesResponse getTableNames(RpcController controller,
      MasterProtos.GetTableNamesRequest request) throws ServiceException {
    return stub.getTableNames(controller, request);
  }

  public MasterProtos.GetClusterStatusResponse getClusterStatus(RpcController controller,
      MasterProtos.GetClusterStatusRequest request) throws ServiceException {
    return stub.getClusterStatus(controller, request);
  }

  public MasterProtos.SetQuotaResponse setQuota(RpcController controller,
      MasterProtos.SetQuotaRequest request) throws ServiceException {
    return stub.setQuota(controller, request);
  }

  public MasterProtos.MajorCompactionTimestampResponse getLastMajorCompactionTimestamp(
      RpcController controller, MasterProtos.MajorCompactionTimestampRequest request)
      throws ServiceException {
    return stub.getLastMajorCompactionTimestamp(controller, request);
  }

  public MasterProtos.MajorCompactionTimestampResponse getLastMajorCompactionTimestampForRegion(
      RpcController controller, MasterProtos.MajorCompactionTimestampForRegionRequest request)
      throws ServiceException {
    return stub.getLastMajorCompactionTimestampForRegion(controller, request);
  }

  public IsBalancerEnabledResponse isBalancerEnabled(RpcController controller,
      IsBalancerEnabledRequest request) throws ServiceException {
    return stub.isBalancerEnabled(controller, request);
  }

  public MasterProtos.SetSplitOrMergeEnabledResponse setSplitOrMergeEnabled(
      RpcController controller, MasterProtos.SetSplitOrMergeEnabledRequest request)
      throws ServiceException {
    return stub.setSplitOrMergeEnabled(controller, request);
  }

  public MasterProtos.IsSplitOrMergeEnabledResponse isSplitOrMergeEnabled(RpcController controller,
      MasterProtos.IsSplitOrMergeEnabledRequest request) throws ServiceException {
    return stub.isSplitOrMergeEnabled(controller, request);
  }

  public IsNormalizerEnabledResponse isNormalizerEnabled(RpcController controller,
      IsNormalizerEnabledRequest request) throws ServiceException {
    return stub.isNormalizerEnabled(controller, request);
  }

  public SecurityCapabilitiesResponse getSecurityCapabilities(RpcController controller,
      SecurityCapabilitiesRequest request) throws ServiceException {
    return stub.getSecurityCapabilities(controller, request);
  }

  public AddReplicationPeerResponse addReplicationPeer(RpcController controller,
      AddReplicationPeerRequest request) throws ServiceException {
    return stub.addReplicationPeer(controller, request);
  }

  public RemoveReplicationPeerResponse removeReplicationPeer(RpcController controller,
      RemoveReplicationPeerRequest request) throws ServiceException {
    return stub.removeReplicationPeer(controller, request);
  }

  public EnableReplicationPeerResponse enableReplicationPeer(RpcController controller,
      EnableReplicationPeerRequest request) throws ServiceException {
    return stub.enableReplicationPeer(controller, request);
  }

  public DisableReplicationPeerResponse disableReplicationPeer(RpcController controller,
      DisableReplicationPeerRequest request) throws ServiceException {
    return stub.disableReplicationPeer(controller, request);
  }

  public ListDrainingRegionServersResponse listDrainingRegionServers(RpcController controller,
      ListDrainingRegionServersRequest request) throws ServiceException {
    return stub.listDrainingRegionServers(controller, request);
  }

  public DrainRegionServersResponse drainRegionServers(RpcController controller,
      DrainRegionServersRequest request) throws ServiceException {
    return stub.drainRegionServers(controller, request);
  }

  public RemoveDrainFromRegionServersResponse removeDrainFromRegionServers(RpcController controller,
      RemoveDrainFromRegionServersRequest request) throws ServiceException {
    return stub.removeDrainFromRegionServers(controller, request);
  }

  public GetReplicationPeerConfigResponse getReplicationPeerConfig(RpcController controller,
      GetReplicationPeerConfigRequest request) throws ServiceException {
    return stub.getReplicationPeerConfig(controller, request);
  }

  public LocateMetaResponse locateMeta(RpcController controller, LocateMetaRequest request)
      throws ServiceException {
    return stub.locateMeta(controller, request);
  }

  public UpdateReplicationPeerConfigResponse updateReplicationPeerConfig(RpcController controller,
      UpdateReplicationPeerConfigRequest request) throws ServiceException {
    return stub.updateReplicationPeerConfig(controller, request);
  }

  public ListReplicationPeersResponse listReplicationPeers(RpcController controller,
      ListReplicationPeersRequest request) throws ServiceException {
    return stub.listReplicationPeers(controller, request);
  }

  public GetSpaceQuotaRegionSizesResponse getSpaceQuotaRegionSizes(RpcController controller,
      GetSpaceQuotaRegionSizesRequest request) throws ServiceException {
    return stub.getSpaceQuotaRegionSizes(controller, request);
  }

  public GetQuotaStatesResponse getQuotaStates(RpcController controller,
      GetQuotaStatesRequest request) throws ServiceException {
    return stub.getQuotaStates(controller, request);
  }

}
