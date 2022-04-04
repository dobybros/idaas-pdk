package io.tapdata.pdk.apis.functions.connector.target;

import io.tapdata.pdk.apis.context.TapConnectorContext;
import io.tapdata.pdk.apis.entity.FilterResult;
import io.tapdata.pdk.apis.entity.FilterResults;
import io.tapdata.pdk.apis.entity.TapAdvanceFilter;
import io.tapdata.pdk.apis.entity.TapFilter;

import java.util.List;
import java.util.function.Consumer;

/**
 * Will be used when upsert function not implemented. for query update/insert model
 *
 */
public interface QueryByAdvanceFilterFunction {
    void query(TapConnectorContext nodeContext, List<TapAdvanceFilter> filters, Consumer<List<FilterResults>> consumer) throws Throwable;
}
