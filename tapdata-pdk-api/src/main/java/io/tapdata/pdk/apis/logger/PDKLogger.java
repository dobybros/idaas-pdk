package io.tapdata.pdk.apis.logger;

import io.tapdata.pdk.apis.utils.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PDKLogger {
    private static final String LEVEL_FATAL = "FATAL";

    private static LogListener logListener;

    private PDKLogger() {
    }

    private static boolean enable = true;

    public static void enable(boolean enable1) {
        enable = enable1;
    }

    public interface LogListener {
        void debug(String log);

        void info(String log);

        void warn(String log);

        void error(String log);

        void fatal(String log);
    }

    public static String getClassTag(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static void debug(String tag, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsg(tag, FormatUtils.format(msg, params));
        if (logListener != null)
            logListener.debug(log);
        else
            System.out.println("[DEBUG] " + log);
    }

    public static void info(String tag, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsg(tag, FormatUtils.format(msg, params));
        if (logListener != null)
            logListener.info(log);
        else
            System.out.println("[INFO] " + log);
    }

    public static void info(String tag, Long spendTime, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsg(tag, FormatUtils.format(msg, params), spendTime);
        if (logListener != null)
            logListener.info(log);
        else
            System.out.println("[INFO] " + log);
    }

    public static void infoWithData(String tag, String dataType, String data, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsg(tag, FormatUtils.format(msg, params), dataType, data);
        if (logListener != null)
            logListener.info(log);
        else
            System.out.println("[INFO] " + log);
    }

    public static void warn(String tag, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsg(tag, FormatUtils.format(msg, params));
        if (logListener != null)
            logListener.warn(log);
        else
            System.out.println("[WARN] " + log);
    }

    public static void error(String tag, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsg(tag, FormatUtils.format(msg, params));
        if (logListener != null)
            logListener.error(log);
        else
            System.out.println("[ERROR] " + log);
    }

    public static void fatal(String tag, String msg, Object... params) {
        if(!enable) return;

        String log = getLogMsgFatal(tag, FormatUtils.format(msg, params));
        if (logListener != null)
            logListener.fatal(log);
        else
            System.out.println("[FATAL] " + log);
    }

    private static String getLogMsg(String tag, String msg) {
        StringBuilder builder = new StringBuilder();
        builder.append("$$time:: " + dateString()).
                append(" $$tag:: " + tag).
                append(" ").
                append("[" + msg + "]").
                append(" $$thread:: " + Thread.currentThread().getName());

        return builder.toString();
    }

    private static String getLogMsgFatal(String tag, String msg) {
        StringBuilder builder = new StringBuilder();
        builder.append(LEVEL_FATAL).
                append(" $$time:: " + dateString()).
                append(" $$tag:: " + tag).
                append(" ").
                append("[" + msg + "]").
                append(" $$thread:: " + Thread.currentThread().getName());
        return builder.toString();
    }

    private static String getLogMsg(String tag, String msg, Long spendTime) {
        StringBuilder builder = new StringBuilder();
        builder.append("$$time:: " + dateString()).
                append(" $$tag:: " + tag).
                append(" [" + msg + "]").
                append(" $$spendTime:: " + spendTime).
                append(" $$thread:: " + Thread.currentThread().getName());

        return builder.toString();
    }

    private static String getLogMsg(String tag, String msg, String dataType, String data) {
        StringBuilder builder = new StringBuilder();
        builder.append("$$time:: " + dateString()).
                append(" $$tag:: " + tag).
                append(" [" + msg + "]").
                append(" $$dataType:: " + dataType).
                append(" $$data:: " + data).
                append(" $$thread:: " + Thread.currentThread().getName());

        return builder.toString();
    }

    public static LogListener getLogListener() {
        return logListener;
    }

    public static void setLogListener(LogListener logListener) {
        PDKLogger.logListener = logListener;
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    public static String dateString() {
        return sdf.format(new Date());
    }
}
