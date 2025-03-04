package org.custom.log4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.Level;
import org.json.JSONObject;
import static org.apache.logging.log4j.Level.DEBUG;
import static org.apache.logging.log4j.Level.ERROR;
import static org.apache.logging.log4j.Level.FATAL;
import static org.apache.logging.log4j.Level.INFO;
import static org.apache.logging.log4j.Level.TRACE;
import static org.apache.logging.log4j.Level.WARN;

@Plugin(name = "LogRhythmLayout", category = "Core", elementType = "layout", printObject = true)
public class LogRhythmLayout extends AbstractStringLayout {

	private static String machineName = "Unknown";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	protected LogRhythmLayout(String machineName) {
		super(StandardCharsets.UTF_8);
		this.setMachineName(machineName);
	}

	@PluginFactory
	public static LogRhythmLayout createLayout(@PluginAttribute("MachineName") String machineName) {
		return new LogRhythmLayout(machineName);
	}

	public synchronized void setMachineName(String machineName) {
		LogRhythmLayout.machineName = machineName;
	}

	@Override
	public String toSerializable(LogEvent logEvent) {
		JSONObject jsonObject = new JSONObject();

		appendProperties(jsonObject, logEvent);
		appendDateTime(jsonObject, logEvent);
		appendLevel(jsonObject, logEvent);
		appendMessage(jsonObject, logEvent);
		appendException(jsonObject, logEvent);
		appendMachine(jsonObject);
		appendThreadId(jsonObject, logEvent);
		appendLocationDetails(jsonObject, logEvent.getSource());

		return jsonObject.toString() + "\r\n";
	}

	private void appendDateTime(JSONObject jsonObject, LogEvent logEvent) {
		jsonObject.put("DateTime", formatDate(logEvent.getTimeMillis()));
	}
	
	private String formatDate(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZonedDateTime date = instant.atZone(ZoneOffset.UTC);		
		String dateText = formatter.format(date);
		
		return dateText;
	}
	
	private void appendLevel(JSONObject jsonObject, LogEvent logEvent) {
		jsonObject.put("Level", formatLevel(logEvent.getLevel()));
	}

	private String formatLevel(Level level) {
		if (level.equals(FATAL)) {
			return "Fatal";
		} else if (level.equals(ERROR)) {
			return "Error";
		} else if (level.equals(WARN)) {
			return "Warn";
		} else if (level.equals(INFO)) {
			return "Info";
		} else if (level.equals(DEBUG)) {
			return "Debug";
		} else if (level.equals(TRACE)) {
			return "Trace";
		}
		return level.name();
	}

	private void appendProperties(JSONObject jsonObject, LogEvent logEvent) {
		for (Map.Entry<String, String> property : logEvent.getContextData().toMap().entrySet()) {
			jsonObject.put(property.getKey(), property.getValue());
		}
	}

	private void appendLocationDetails(JSONObject jsonObject, StackTraceElement locationInfo) {
		jsonObject.put("Class Method", locationInfo.getClassName() + "." + locationInfo.getMethodName() + "()");
	}

	private void appendMessage(JSONObject jsonObject, LogEvent logEvent) {
		jsonObject.put("Message", logEvent.getMessage().getFormattedMessage());
	}

	private void appendException(JSONObject jsonObject, LogEvent logEvent) {
		Throwable throwable = logEvent.getThrown();

		if (throwable == null) {
			return;
		}

		String stackTrace = getStackTrace(throwable);
		String type = "Unknown";
		String message = "An exception occurred. See stack trace for details.";

		// start Exception object
		JSONObject exceptionObject = new JSONObject();
		// add Type
		exceptionObject.put("Type", type);
		// add Message
		exceptionObject.put("Message", message);
		// add StackTrace
		exceptionObject.put("StackTrace", stackTrace);
		// add the Exception object to the main json object
		jsonObject.put("BaseException", exceptionObject);
	}

	private String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}
		
	private void appendMachine(JSONObject jsonObject) {
		jsonObject.put("Machine", machineName);
	}
	
	private void appendThreadId(JSONObject jsonObject, LogEvent logEvent) {
		jsonObject.put("ThreadId", logEvent.getThreadName());
	}
}
