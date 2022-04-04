package io.tapdata.pdk.apis.functions;

import io.tapdata.pdk.apis.functions.connector.source.*;
import io.tapdata.pdk.apis.functions.connector.target.*;

public class ConnectorFunctions extends CommonFunctions<ConnectorFunctions> {
    private BatchReadFunction batchReadFunction;
    private StreamReadFunction streamReadFunction;
    private BatchCountFunction batchCountFunction;
    private BatchOffsetFunction batchOffsetFunction;
    private StreamOffsetFunction streamOffsetFunction;
    private WriteRecordFunction writeRecordFunction;
    private QueryByFilterFunction queryByFilterFunction;
    private QueryByAdvanceFilterFunction queryByAdvanceFilterFunction;
    private TransactionFunction transactionFunction;
    private CreateTableFunction createTableFunction;
    private AlterTableFunction alterTableFunction;
    private ClearTableFunction clearTableFunction;
    private DropTableFunction dropTableFunction;
    private ControlFunction controlFunction;
    /**
     * Flow engine may get current batch offset at any time.
     * To continue batch read for the batch offset when job resumed from pause or stopped accidentally.
     *
     * @param function
     * @return
     */
    public ConnectorFunctions supportBatchOffset(BatchOffsetFunction function) {
        batchOffsetFunction = function;
        return this;
    }

    public ConnectorFunctions supportControlFunction(ControlFunction function) {
        controlFunction = function;
        return this;
    }

    /**
     * Flow engine may get current stream offset at any time.
     * To continue stream read for the stream offset when job resumed from pause or stopped accidentally.
     *
     * @param function
     * @return
     */
    public ConnectorFunctions supportStreamOffset(StreamOffsetFunction function) {
        streamOffsetFunction = function;
        return this;
    }

    /**
     * Flow engine will call this method when batch read is started.
     * You need implement the batch feature in this method synchronously, once this method finished, Flow engine will consider the batch read is ended.
     *
     * Exception can be throw in this method, Flow engine will consider there is an error occurred in batch read.
     *
     * @param function
     * @return
     */
    public ConnectorFunctions supportBatchRead(BatchReadFunction function) {
        batchReadFunction = function;
        return this;
    }

    /**
     *
     */
    public ConnectorFunctions supportStreamRead(StreamReadFunction function) {
        streamReadFunction = function;
        return this;
    }

    public ConnectorFunctions supportBatchCount(BatchCountFunction function) {
        this.batchCountFunction = function;
        return this;
    }

    public ConnectorFunctions supportWriteRecord(WriteRecordFunction function) {
        this.writeRecordFunction = function;
        return this;
    }

    public ConnectorFunctions supportCreateTable(CreateTableFunction function) {
        this.createTableFunction = function;
        return this;
    }

    public ConnectorFunctions supportAlterTable(AlterTableFunction function) {
        this.alterTableFunction = function;
        return this;
    }

    public ConnectorFunctions supportClearTable(ClearTableFunction function) {
        this.clearTableFunction = function;
        return this;
    }

    public ConnectorFunctions supportDropTable(DropTableFunction function) {
        this.dropTableFunction = function;
        return this;
    }

    public ConnectorFunctions supportQueryByFilter(QueryByFilterFunction function) {
        this.queryByFilterFunction = function;
        return this;
    }

    public ConnectorFunctions supportQueryByAdvanceFilter(QueryByAdvanceFilterFunction function) {
        this.queryByAdvanceFilterFunction = function;
        return this;
    }

    public WriteRecordFunction getWriteRecordFunction() {
        return writeRecordFunction;
    }

    public QueryByAdvanceFilterFunction getQueryByAdvanceFilterFunction() {
        return queryByAdvanceFilterFunction;
    }

    public BatchReadFunction getBatchReadFunction() {
        return batchReadFunction;
    }

    public StreamReadFunction getStreamReadFunction() {
        return streamReadFunction;
    }

    public BatchCountFunction getBatchCountFunction() {
        return batchCountFunction;
    }

    public BatchOffsetFunction getBatchOffsetFunction() {
        return batchOffsetFunction;
    }

    public StreamOffsetFunction getStreamOffsetFunction() {
        return streamOffsetFunction;
    }

    public QueryByFilterFunction getQueryByFilterFunction() {
        return queryByFilterFunction;
    }

    public TransactionFunction getTransactionFunction() {
        return transactionFunction;
    }

    public CreateTableFunction getCreateTableFunction() {
        return createTableFunction;
    }

    public AlterTableFunction getAlterTableFunction() {
        return alterTableFunction;
    }

    public ClearTableFunction getClearTableFunction() {
        return clearTableFunction;
    }

    public DropTableFunction getDropTableFunction() {
        return dropTableFunction;
    }

    public ControlFunction getControlFunction() {
        return controlFunction;
    }
}
