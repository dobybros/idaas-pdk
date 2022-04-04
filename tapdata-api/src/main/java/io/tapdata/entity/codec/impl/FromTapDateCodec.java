package io.tapdata.entity.codec.impl;

import io.tapdata.entity.annotations.Implementation;
import io.tapdata.entity.codec.FromTapValueCodec;
import io.tapdata.entity.codec.TapDefaultCodecs;
import io.tapdata.entity.value.TapDateValue;

@Implementation(value = FromTapValueCodec.class, type = TapDefaultCodecs.TAP_DATE_VALUE, buildNumber = 0)
public class FromTapDateCodec implements FromTapValueCodec<TapDateValue> {
    @Override
    public Object fromTapValue(TapDateValue tapValue) {
        if(tapValue == null)
            return null;
        //TODO need more code
        return tapValue.getValue();
    }
}
