package io.tapdata.entity.mapping.type;

import io.tapdata.entity.type.TapRaw;
import io.tapdata.entity.type.TapType;
import io.tapdata.entity.type.TapYear;

import java.util.List;
import java.util.Map;

/**
 */
public class TapYearMapping extends TapMapping {
    public static final String KEY_RANGE = "range";

    private Integer minRange;
    private Integer maxRange;

    @Override
    public void from(Map<String, Object> info) {
        Object precisionObj = getObject(info, KEY_RANGE);
        if(precisionObj instanceof List) {
            List<?> list = (List<?>) precisionObj;
            if(list.size() == 2) {
                if(list.get(0) instanceof Number) {
                    minRange = ((Number) list.get(0)).intValue();
                }
                if(list.get(1) instanceof Number) {
                    maxRange = ((Number) list.get(1)).intValue();
                }
            }
        }
    }

    @Override
    public TapType toTapType(String originType, Map<String, String> params) {
        return new TapYear();
    }

    @Override
    public String fromTapType(String typeExpression, TapType tapType) {
        if (tapType instanceof TapYear) {
            return removeBracketVariables(typeExpression, 0);
        }
        return null;
    }

    public Integer getMinRange() {
        return minRange;
    }

    public void setMinRange(Integer minRange) {
        this.minRange = minRange;
    }

    public Integer getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Integer maxRange) {
        this.maxRange = maxRange;
    }
}
