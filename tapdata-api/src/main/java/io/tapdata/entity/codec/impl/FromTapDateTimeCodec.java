package io.tapdata.entity.codec.impl;

import io.tapdata.entity.annotations.Implementation;
import io.tapdata.entity.codec.FromTapValueCodec;
import io.tapdata.entity.codec.TapDefaultCodecs;
import io.tapdata.entity.value.TapDateTimeValue;

@Implementation(value = FromTapValueCodec.class, type = TapDefaultCodecs.TAP_DATE_TIME_VALUE, buildNumber = 0)
public class FromTapDateTimeCodec implements FromTapValueCodec<TapDateTimeValue> {
    @Override
    public Object fromTapValue(TapDateTimeValue tapValue) {
        if(tapValue == null)
            return null;
        //TODO need more code
        return tapValue.getValue();
    }
}
