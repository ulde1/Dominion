package de.tesd.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.eclipse.jdt.annotation.Nullable;


public class LoggingFormatter extends Formatter {


	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";


	public static String ansiColor(int level) {
		if (level>=Level.WARNING.intValue())
			return ANSI_RED;
		else if (level>=Level.INFO.intValue())
			return ANSI_PURPLE;
		else if (level>=Level.CONFIG.intValue())
			return ANSI_BLUE;
		else if (level>=Level.FINE.intValue())
			return ANSI_CYAN;
		else if (level>=Level.FINER.intValue())
			return ANSI_GREEN;
		else
			return ANSI_YELLOW;
	}


	protected String name;
	private Date date;
	protected LogManager mgr;
	protected boolean showDate, showTime, showLogger, showLevel, showMethod, useANSI;
	protected int messageWidth;
	protected @Nullable String tabReplacement;
	protected @Nullable String omitClassParents;


	public LoggingFormatter() {
		name = getClass().getName();
		date = new Date();
		mgr = LogManager.getLogManager();
		showDate = getBooleanProperty("showDate", true);
		showTime = getBooleanProperty("showTime", true);
		showLogger = getBooleanProperty("showLogger", false);
		showLevel = getBooleanProperty("showLevel", true);
		showMethod = getBooleanProperty("showMethod", true);
		useANSI = getBooleanProperty("useANSI", false);
		messageWidth = getIntProperty("messageWidth", 40);
		tabReplacement = getProperty("tabReplacement"); // z.B: ‣→
		if (tabReplacement==null)
			tabReplacement = "\t";
		omitClassParents = getProperty("omitClassParents");
	}


	public boolean getBooleanProperty(String prop, boolean def) {
		String value = getProperty(prop);
		if (value==null)
			return def;
		else
			return !"false".equals(value);
	}


	public int getIntProperty(String prop, int def) {
		String value = getProperty(prop);
		if (value==null)
			return def;
		else
			return Integer.parseInt(value);
	}


	public @Nullable String getProperty(String prop) {
		return mgr.getProperty(name+"."+prop);
	}


	@Override public String format(@Nullable LogRecord record) {
		assert record!=null;
		StringBuilder sb = new StringBuilder();
		if (useANSI)
			sb.append(ansiColor(record.getLevel().intValue()));
		date.setTime(record.getMillis());
		String className = record.getSourceClassName();
		if (className==null)
			className = record.getLoggerName();
		if (omitClassParents!=null)
			className = className.replaceFirst(omitClassParents, "");
		String methodName = record.getSourceMethodName();
		if (methodName==null)
			methodName = "";
		if (showDate)
			sb.append(String.format("%tF ", date));
		if (showTime)
			sb.append(String.format("%tT ", date));
		String message = formatMessage(record);
		if (message!=null)
			sb.append(String.format(String.format("%%-%ds ", messageWidth), message.replaceAll("\t", tabReplacement)));
		else
			sb.append(String.format(String.format("%%-%ds ", messageWidth), "(null)"));
		if (showLevel)
			sb.append(record.getLevel().toString());
		if (showMethod)
			sb.append(String.format(" %s:%s", className, methodName));
		if (showLogger) {
			String loggerName = record.getLoggerName();
			if (omitClassParents!=null)
				loggerName = loggerName.replaceFirst(omitClassParents, "");
			sb.append(String.format(" - %s", loggerName));
		}
		sb.append(String.format("%n"));
//		sb.append(String.format("%tF %tT %s\t %s (%s:%s) - %s%n", date, date, record.getLevel().toString(),
//				formatMessage(record), className, methodName, record.getLoggerName()));
		if (record.getThrown()!=null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(sw.toString());
			} catch (Exception ex) {
			}
		}
		if (useANSI)
			sb.append(ANSI_RESET);
		return sb.toString();
	}

}