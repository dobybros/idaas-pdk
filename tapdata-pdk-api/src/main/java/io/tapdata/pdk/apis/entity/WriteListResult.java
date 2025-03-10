package io.tapdata.pdk.apis.entity;

import java.util.Map;

public class WriteListResult<T> {
    private long insertedCount;
    public WriteListResult<T> insertedCount(long insertedCount) {
        this.insertedCount = insertedCount;
        return this;
    }
    private long removedCount;
    public WriteListResult<T> removedCount(long removedCount) {
        this.removedCount = removedCount;
        return this;
    }
    private long modifiedCount;
    public WriteListResult<T> modifiedCount(long modifiedCount) {
        this.modifiedCount = modifiedCount;
        return this;
    }

    private Map<T, Throwable> errorMap;

    public WriteListResult() {}
    public WriteListResult(long insertedCount, long modifiedCount, long removedCount) {
        this(insertedCount, modifiedCount, removedCount, null);
    }
    public WriteListResult(long insertedCount, long modifiedCount, long removedCount, Map<T, Throwable> errorMap) {
        this.insertedCount = insertedCount;
        this.modifiedCount = modifiedCount;
        this.removedCount = removedCount;
        this.errorMap = errorMap;
    }

    public long getInsertedCount() {
        return insertedCount;
    }

    public void setInsertedCount(long insertedCount) {
        this.insertedCount = insertedCount;
    }

    public long getRemovedCount() {
        return removedCount;
    }

    public void setRemovedCount(long removedCount) {
        this.removedCount = removedCount;
    }

    public long getModifiedCount() {
        return modifiedCount;
    }

    public void setModifiedCount(long modifiedCount) {
        this.modifiedCount = modifiedCount;
    }

    public Map<T, Throwable> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<T, Throwable> errorMap) {
        this.errorMap = errorMap;
    }
}
