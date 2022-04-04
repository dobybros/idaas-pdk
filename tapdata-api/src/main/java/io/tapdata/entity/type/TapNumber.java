package io.tapdata.entity.type;

public class TapNumber extends TapType {
    /**
     * 存储位数， 字节数， 不同数据库实现不一样
     * 数字的几位数， 小数点后面几位数
     *
     * 有length是单精度， float(4), length就是4
     *
     * 双精度， float(8, 2), length没有值， precision是8， scale是2， 小数点前8位， 小数点后2位
     */
    private Integer length;
    public TapNumber length(Integer length) {
        this.length = length;
        return this;
    }

    private Boolean unsigned;
    public TapNumber unsigned(Boolean unsigned) {
        this.unsigned = unsigned;
        return this;
    }

    private Boolean zerofill;
    public TapNumber zerofill(Boolean zerofill) {
        this.zerofill = zerofill;
        return this;
    }
//    /**
//     * 最小值
//     */
//    private Long min;
//    public TapNumber min(Long min) {
//        this.min = min;
//        return this;
//    }
//    /**
//     * 最大值
//     */
//    private Long max;
//    public TapNumber max(Long max) {
//        this.max = max;
//        return this;
//    }
    /**
     *
     */
    private Integer precision;
    public TapNumber precision(Integer precision) {
        this.precision = precision;
        return this;
    }
    private Integer scale;
    public TapNumber scale(Integer scale) {
        this.scale = scale;
        return this;
    }

    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }

//    public Long getMin() {
//        return min;
//    }
//    public void setMin(Long min) {
//        this.min = min;
//    }
//
//    public Long getMax() {
//        return max;
//    }
//    public void setMax(Long max) {
//        this.max = max;
//    }

    public Integer getPrecision() {
        return precision;
    }
    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Boolean getUnsigned() {
        return unsigned;
    }

    public void setUnsigned(Boolean unsigned) {
        this.unsigned = unsigned;
    }

    public Boolean getZerofill() {
        return zerofill;
    }

    public void setZerofill(Boolean zerofill) {
        this.zerofill = zerofill;
    }
}
