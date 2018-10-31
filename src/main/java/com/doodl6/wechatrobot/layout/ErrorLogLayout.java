package com.doodl6.wechatrobot.layout;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import java.text.SimpleDateFormat;

/**
 * 日志格式类
 */
public class ErrorLogLayout extends HTMLLayout {

    private final int BUF_SIZE = 256;

    private static final int MAX_CAPACITY = 1024;

    private StringBuilder logBuilder = new StringBuilder(BUF_SIZE);

    private boolean locationInfo = false;

    public void setLocationInfo(boolean flag) {
        locationInfo = flag;
    }

    public boolean getLocationInfo() {
        return locationInfo;
    }

    public void activateOptions() {
    }

    public String format(LoggingEvent event) {
        if (logBuilder.capacity() > MAX_CAPACITY) {
            logBuilder = new StringBuilder(BUF_SIZE);
        } else {
            logBuilder.setLength(0);
        }

        logBuilder.append(Layout.LINE_SEP);

        logBuilder.append("<DateTime>");
        logBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        logBuilder.append("</DateTime>").append(Layout.LINE_SEP);

        String escapedThread = Transform.escapeTags(event.getThreadName());
        logBuilder.append("<Thread>").append(escapedThread).append(" thread");
        logBuilder.append(escapedThread);
        logBuilder.append("</Thread>").append(Layout.LINE_SEP);

        logBuilder.append("<Level>");
        if (event.getLevel().equals(Level.DEBUG)) {
            logBuilder.append(Transform.escapeTags(String.valueOf(event.getLevel())));
        } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
            logBuilder.append(Transform.escapeTags(String.valueOf(event.getLevel())));
        } else {
            logBuilder.append(Transform.escapeTags(String.valueOf(event.getLevel())));
        }
        logBuilder.append("</Level>").append(Layout.LINE_SEP);

        String escapedLogger = Transform.escapeTags(event.getLoggerName());
        logBuilder.append("<Category>");
        logBuilder.append(escapedLogger);
        logBuilder.append("</Category>").append(Layout.LINE_SEP);

        if (locationInfo) {
            LocationInfo locInfo = event.getLocationInformation();
            logBuilder.append("<Location>");
            logBuilder.append(Transform.escapeTags(locInfo.getFileName()));
            logBuilder.append(':');
            logBuilder.append(locInfo.getLineNumber());
            logBuilder.append("</Location>").append(Layout.LINE_SEP);
        }

        logBuilder.append("<Message>");
        logBuilder.append(Transform.escapeTags(event.getRenderedMessage()));
        logBuilder.append("</Message>").append(Layout.LINE_SEP);
        logBuilder.append(Layout.LINE_SEP);

        if (event.getNDC() != null) {
            logBuilder.append("<NDC>");
            logBuilder.append(Transform.escapeTags(event.getNDC()));
            logBuilder.append("</NDC>").append(Layout.LINE_SEP);
        }

        String[] s = event.getThrowableStrRep();
        if (s != null) {
            logBuilder.append("<Throwable>");
            appendThrowableAsHTML(s, logBuilder);
            logBuilder.append("</Throwable>").append(Layout.LINE_SEP);
        }

        return logBuilder.toString();
    }

    private void appendThrowableAsHTML(String[] s, StringBuilder builder) {
        if (s != null) {
            int len = s.length;
            if (len == 0)
                return;
            builder.append(Transform.escapeTags(s[0]));
            builder.append(Layout.LINE_SEP);
            for (int i = 1; i < len; i++) {
                builder.append(Transform.escapeTags(s[i]));
                builder.append(Layout.LINE_SEP);
            }
        }
    }

}