package org.custom.log4j;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JRequestContextAccessLogValve extends AccessLogValve {

	private static final Logger log = LogManager.getLogger(Log4JRequestContextAccessLogValve.class);

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		super.invoke(request, response);

		try {
			Log4JCatalinaRequestContext.SetRequestContextForThread(request);
		} catch(Exception e) {
			log.warn("Unable to add request context to the Log4J Mapped Diagnostic Context.", e);
		}
	}
}
