package io.tapdata.pdk.tdd.tests.target.benchmark;

import io.tapdata.entity.event.control.PatrolEvent;
import io.tapdata.entity.schema.TapTable;
import io.tapdata.entity.utils.DataMap;
import io.tapdata.pdk.apis.functions.ConnectorFunctions;
import io.tapdata.pdk.apis.logger.PDKLogger;
import io.tapdata.pdk.apis.spec.TapNodeSpecification;
import io.tapdata.pdk.cli.entity.DAGDescriber;
import io.tapdata.pdk.core.api.SourceNode;
import io.tapdata.pdk.core.api.TargetNode;
import io.tapdata.pdk.core.dag.TapDAGNode;
import io.tapdata.pdk.core.utils.CommonUtils;
import io.tapdata.pdk.core.workflow.engine.*;
import io.tapdata.pdk.tdd.core.PDKTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@DisplayName("Tests for target intermediate test")
public class BenchmarkTest extends PDKTestBase {
    private static final String TAG = BenchmarkTest.class.getSimpleName();
    TargetNode targetNode;
    SourceNode tddSourceNode;
    DataFlowWorker dataFlowWorker;
    String targetNodeId = "t2";
    String sourceNodeId = "s1";
    Instant startTime;

    @Test
    @DisplayName("Test method createTable")
    void benchmarkTest() throws Throwable {
        consumeQualifiedTapNodeInfo(nodeInfo -> {
            try {
                DataFlowEngine dataFlowEngine = DataFlowEngine.getInstance();
//                dataFlowEngine.start();

                DAGDescriber dataFlowDescriber = new DAGDescriber();
                dataFlowDescriber.setId("createTableTest->" + nodeInfo.getTapNodeSpecification().getId());

                String tableId = dataFlowDescriber.getId() + "_" + UUID.randomUUID();
                tableId = tableId.replace('-', '_').replace('>', '_');

                TapNodeSpecification spec = nodeInfo.getTapNodeSpecification();
                dataFlowDescriber.setNodes(Arrays.asList(
                        new TapDAGNodeEx().id(sourceNodeId).pdkId("tdd-benchmark-source").group("io.tapdata.connector").type(TapDAGNode.TYPE_SOURCE).version("1.0-SNAPSHOT").
                                table(new TapTable("tdd-table")).connectionConfig(new DataMap()),
                        new TapDAGNodeEx().id(targetNodeId).pdkId(spec.getId()).group(spec.getGroup()).type(nodeInfo.getNodeType()).version(spec.getVersion()).
                                table(new TapTable(tableId)).connectionConfig(connectionOptions)
                ));
                dataFlowDescriber.setDag(Collections.singletonList(Arrays.asList("s1", "t2")));
                dataFlowDescriber.setJobOptions(new JobOptions().actionsBeforeStart(Arrays.asList(JobOptions.ACTION_DROP_TABLE, JobOptions.ACTION_CREATE_TABLE)));

                dag = dataFlowDescriber.toDag();

                if (dag != null) {
                    JobOptions jobOptions = dataFlowDescriber.getJobOptions();
                    dataFlowWorker = dataFlowEngine.startDataFlow(dag, jobOptions, (fromState, toState, dataFlowWorker) -> {
                        if (toState.equals(DataFlowWorker.STATE_INITIALIZING)) {
                            initConnectorFunctions();
                            checkFunctions();
                            startTime = Instant.now();
                        } else if (toState.equals(DataFlowWorker.STATE_INITIALIZED)) {
                            PatrolEvent patrolEvent = new PatrolEvent().patrolListener((nodeId, state) -> {
                                if (nodeId.equals(targetNodeId) && state == PatrolEvent.STATE_LEAVE) {
                                    PDKLogger.info("PATROL STATE_RECORDS_SENT", "NodeId {} state {}", nodeId, (state == PatrolEvent.STATE_ENTER ? "enter" : "leave"));
                                    PDKLogger.info("[PERFORMANCE_TEST_FINISH]", "Millis : {}", Duration.between(startTime, Instant.now()).toMillis());
                                    PDKLogger.info("[PERFORMANCE_TEST_FINISH]", "QPS : {}", String.format("%f", 10000.0 / Duration.between(startTime, Instant.now()).toMillis()));
                                }
                            });
                            //Line up after batch read
                            dataFlowEngine.sendExternalTapEvent(dag.getId(), patrolEvent);
                        }
                    });
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                CommonUtils.logError(TAG, "Start failed", throwable);
                if (throwable instanceof AssertionFailedError) {
                    $(() -> {
                        throw ((AssertionFailedError) throwable);
                    });
                } else {
                    $(() -> Assertions.fail("Unknown error " + throwable.getMessage()));
                }
            }
        });
        waitCompleted(5);
    }

    private void checkFunctions() {
        ConnectorFunctions connectorFunctions = targetNode.getConnectorFunctions();
        $(() -> Assertions.assertNotNull(connectorFunctions.getWriteRecordFunction(), "WriteRecord is a must to implement a Target"));
//        $(() -> Assertions.assertNotNull(connectorFunctions.getQueryByFilterFunction(), "QueryByFilter is needed for TDD to verify the record is written correctly"));
//        $(() -> Assertions.assertNotNull(connectorFunctions.getCreateTableFunction(), "CreateTable is needed for database who need create table before insert records"));
//        $(() -> Assertions.assertNotNull(connectorFunctions.getBatchCountFunction(), "BatchCount is needed for verify how many records have inserted"));
    }

    private void initConnectorFunctions() {
        targetNode = dataFlowWorker.getTargetNodeDriver(targetNodeId).getTargetNode();
        tddSourceNode = dataFlowWorker.getSourceNodeDriver(sourceNodeId).getSourceNode();
    }


}
