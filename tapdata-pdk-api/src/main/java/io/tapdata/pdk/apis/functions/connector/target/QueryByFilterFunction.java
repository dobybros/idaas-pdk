package io.tapdata.pdk.apis.functions.connector.target;

import io.tapdata.pdk.apis.context.TapConnectorContext;
import io.tapdata.pdk.apis.entity.FilterResult;
import io.tapdata.pdk.apis.entity.TapFilter;
import io.tapdata.pdk.apis.context.TapProcessorContext;

import java.util.List;
import java.util.function.Consumer;

/**
 * Will be used when upsert function not implemented. for query update/insert model
 *
 */
public interface QueryByFilterFunction {
    void query(TapConnectorContext nodeContext, List<TapFilter> filters, Consumer<List<FilterResult>> consumer) throws Throwable;
}
