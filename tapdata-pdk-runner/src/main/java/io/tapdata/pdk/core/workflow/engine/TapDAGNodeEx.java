package io.tapdata.pdk.core.workflow.engine;

import io.tapdata.entity.event.TapEvent;
import io.tapdata.entity.schema.TapTable;
import io.tapdata.entity.utils.DataMap;
import io.tapdata.pdk.apis.logger.PDKLogger;
import io.tapdata.pdk.apis.spec.TapNodeSpecification;
import io.tapdata.pdk.core.api.PDKIntegration;
import io.tapdata.pdk.core.api.SourceAndTargetNode;
import io.tapdata.pdk.core.dag.TapDAGNode;
import io.tapdata.pdk.core.executor.ExecutorsManager;
import io.tapdata.pdk.core.utils.queue.ListHandler;
import io.tapdata.pdk.core.utils.queue.SingleThreadBlockingQueue;
import io.tapdata.pdk.core.workflow.engine.driver.Driver;
import io.tapdata.pdk.core.workflow.engine.driver.ProcessorNodeDriver;
import io.tapdata.pdk.core.workflow.engine.driver.SourceNodeDriver;
import io.tapdata.pdk.core.workflow.engine.driver.TargetNodeDriver;

import java.util.List;

public class TapDAGNodeEx extends TapDAGNode {
    private static final String TAG = TapDAGNodeEx.class.getSimpleName();

    public TapDAGNodeEx nodeConfig(DataMap nodeConfig) {
        this.nodeConfig = nodeConfig;
        return this;
    }
    public TapDAGNodeEx connectionConfig(DataMap connectionConfig) {
        this.connectionConfig = connectionConfig;
        return this;
    }
    public TapDAGNodeEx id(String id) {
        this.id = id;
        return this;
    }
    public TapDAGNodeEx pdkId(String pdkId) {
        this.pdkId = pdkId;
        return this;
    }
    public TapDAGNodeEx group(String group) {
        this.group = group;
        return this;
    }
    public TapDAGNodeEx version(String version) {
        this.version = version;
        return this;
    }
    public TapDAGNodeEx type(String type) {
        this.type = type;
        return this;
    }
    public TapDAGNodeEx table(TapTable table) {
        this.table = table;
        return this;
    }
    public TapDAGNodeEx parentNodeIds(List<String> parentNodeIds) {
        this.parentNodeIds = parentNodeIds;
        return this;
    }
    public TapDAGNodeEx childNodeIds(List<String> childNodeIds) {
        this.childNodeIds = childNodeIds;
        return this;
    }

    ProcessorNodeDriver processorNodeDriver;
    SourceNodeDriver sourceNodeDriver;
    TargetNodeDriver targetNodeDriver;

    public void setup(TapDAG dag, JobOptions jobOptions) {
        switch (type) {
            case TapDAGNode.TYPE_SOURCE:
                if(sourceNodeDriver == null) {
                    sourceNodeDriver = new SourceNodeDriver();
                }
                if(sourceNodeDriver.getSourceNode() == null) {
                    sourceNodeDriver.setSourceNode(PDKIntegration.createSourceBuilder()
                            .withTapDAGNode(this)
                            .withDagId(dag.getId())
                            .build());
                }
                break;
            case TapDAGNode.TYPE_PROCESSOR:
                if(processorNodeDriver == null) {
                    processorNodeDriver = new ProcessorNodeDriver();
                }
                if(processorNodeDriver.getProcessorNode() == null) {
                    processorNodeDriver.setProcessorNode(PDKIntegration.createProcessorBuilder()
                            .withTapDAGNode(this)
                            .withDagId(dag.getId())
                            .build());
                }
                break;
            case TapDAGNode.TYPE_SOURCE_TARGET:
                SourceAndTargetNode sourceAndTargetNode = PDKIntegration.createSourceAndTargetBuilder()
                        .withTapDAGNode(this)
                        .withDagId(dag.getId())
                        .build();
                if(sourceNodeDriver == null) {
                    sourceNodeDriver = new SourceNodeDriver();
                }
                if(sourceNodeDriver.getSourceNode() == null) {
                    sourceNodeDriver.setSourceNode(sourceAndTargetNode.getSourceNode());
                }

                if(targetNodeDriver == null) {
                    targetNodeDriver = new TargetNodeDriver();
                }
                if(targetNodeDriver.getTargetNode() == null) {
                    targetNodeDriver.setTargetNode(sourceAndTargetNode.getTargetNode());
                }
                break;
            case TapDAGNode.TYPE_TARGET:
                if(targetNodeDriver == null) {
                    targetNodeDriver = new TargetNodeDriver();
                }
                if(targetNodeDriver.getTargetNode() == null) {
                    targetNodeDriver.setTargetNode(PDKIntegration.createTargetBuilder()
                            .withTapDAGNode(this)
                            .withDagId(dag.getId())
                            .build());
                    targetNodeDriver.setActionsBeforeStart(jobOptions.actionsBeforeStart);
                }
                break;
        }
        if(childNodeIds != null) {
            for(String childNodeId : childNodeIds) {
                TapDAGNodeEx nodeWorker = dag.getNodeMap().get(childNodeId);
                if(nodeWorker != null) {
                    nodeWorker.setup(dag, jobOptions);
                    buildPath(this, nodeWorker, jobOptions);
                }
            }
        }
    }

    private void buildPath(TapDAGNodeEx parent, TapDAGNodeEx child, JobOptions jobOptions) {
        String queueName = parent.id + " pdk " + TapNodeSpecification.idAndGroup(parent.pdkId, parent.group, parent.version);
        if(parent.sourceNodeDriver != null) {
            if(child.processorNodeDriver != null || child.targetNodeDriver != null) {
                connect(parent.sourceNodeDriver, child.processorNodeDriver, jobOptions, "Source queue " + queueName + " to processor " + child.id);
                connect(parent.sourceNodeDriver, child.targetNodeDriver, jobOptions, "Source queue " + queueName + " to target " + child.id);
            } else {
                PDKLogger.error(TAG, "Source build path failed, child's processorNodeDriver or targetNodeDriver not found, nodeId {} type {} pdkId {} pdkGroup {} pdkVersion {}", child.id, child.type, child.pdkId, child.group, child.version);
            }
        }
        if(parent.processorNodeDriver != null) {
            if(child.processorNodeDriver != null || child.targetNodeDriver != null) {
                connect(parent.processorNodeDriver, child.processorNodeDriver, jobOptions, "Processor queue " + queueName + " to processor " + child.id);
                connect(parent.processorNodeDriver, child.targetNodeDriver, jobOptions, "Processor queue " + queueName + " to target " + child.id);
            } else {
                PDKLogger.error(TAG, "Processor build path failed, child's processorNodeDriver or targetNodeDriver not found, nodeId {} type {} pdkId {} pdkGroup {} pdkVersion {}", child.id, child.type, child.pdkId, child.group, child.version);
            }
        }
    }

    private void connect(Driver driver, ListHandler<List<TapEvent>> queueReceiver, JobOptions jobOptions, String queueName) {
        if(driver != null && queueReceiver != null) {
            SingleThreadBlockingQueue<List<TapEvent>> queue = new SingleThreadBlockingQueue<List<TapEvent>>(queueName)
                    .withMaxSize(jobOptions.getQueueSize())
                    .withHandleSize(jobOptions.getQueueBatchSize())
                    .withExecutorService(ExecutorsManager.getInstance().getExecutorService())
                    .withHandler(queueReceiver)
                    .start();
            driver.registerQueue(queue);
        }
    }

}
