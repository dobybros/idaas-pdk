package io.tapdata.pdk.core.api;

import io.tapdata.entity.schema.TapTable;
import io.tapdata.pdk.apis.TapConnectorNode;
import io.tapdata.pdk.apis.context.TapConnectionContext;
import io.tapdata.pdk.apis.entity.ConnectionTestResult;

import java.util.List;
import java.util.function.Consumer;

public class ConnectionNode extends Node {
    TapConnectorNode connectorNode;
    TapConnectionContext connectionContext;

    public void allTables(Consumer<List<TapTable>> consumer) {
        connectorNode.discoverSchema(connectionContext, consumer);
    }

    public ConnectionTestResult connectionTest() {
        return connectorNode.connectionTest(connectionContext);
    }

    public TapConnectorNode getConnectorNode() {
        return connectorNode;
    }

    public TapConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
