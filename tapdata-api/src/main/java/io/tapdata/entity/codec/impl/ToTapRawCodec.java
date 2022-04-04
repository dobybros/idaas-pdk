package io.tapdata.entity.codec.impl;

import io.tapdata.entity.annotations.Implementation;
import io.tapdata.entity.codec.TapDefaultCodecs;
import io.tapdata.entity.codec.ToTapValueCodec;
import io.tapdata.entity.value.TapRawValue;

@Implementation(value = ToTapValueCodec.class, type = TapDefaultCodecs.TAP_RAW_VALUE, buildNumber = 0)
public class ToTapRawCodec implements ToTapValueCodec<TapRawValue> {

    @Override
    public TapRawValue toTapValue(Object value) {

        TapRawValue arrayValue = new TapRawValue(value);

        return arrayValue;
    }
}
