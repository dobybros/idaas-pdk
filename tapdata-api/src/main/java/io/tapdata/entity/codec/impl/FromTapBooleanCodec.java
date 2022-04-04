package io.tapdata.entity.codec.impl;

import io.tapdata.entity.annotations.Implementation;
import io.tapdata.entity.codec.FromTapValueCodec;
import io.tapdata.entity.codec.TapDefaultCodecs;
import io.tapdata.entity.type.TapBoolean;
import io.tapdata.entity.value.TapBooleanValue;

@Implementation(value = FromTapValueCodec.class, type = TapDefaultCodecs.TAP_BOOLEAN_VALUE, buildNumber = 0)
public class FromTapBooleanCodec implements FromTapValueCodec<TapBooleanValue> {
    @Override
    public Object fromTapValue(TapBooleanValue tapValue) {
        if(tapValue == null)
            return null;
        TapBoolean tapNumber = tapValue.getTapType();
        //TODO need more code
        return tapValue.getValue();
    }
}
