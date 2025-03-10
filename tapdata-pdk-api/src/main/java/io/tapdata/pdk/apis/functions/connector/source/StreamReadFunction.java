package io.tapdata.pdk.apis.functions.connector.source;

import io.tapdata.entity.event.TapEvent;
import io.tapdata.pdk.apis.context.TapConnectorContext;

import java.util.List;
import java.util.function.Consumer;

public interface StreamReadFunction {
    /**
     *
     * @param nodeContext the node context in a DAG
     * @param offsetState if null, means start from very beginning, otherwise is the start point for batch reading.
     *                    type can be any that comfortable for saving offset state.
     * @param consumer accept the table and offsetState for the record.
     */
    void streamRead(TapConnectorContext nodeContext, Object offsetState, Consumer<List<TapEvent>> consumer) throws Throwable;
}
