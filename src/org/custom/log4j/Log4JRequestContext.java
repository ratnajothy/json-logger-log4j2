package org.custom.log4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import org.apache.logging.log4j.ThreadContext;

public class Log4JRequestContext {
	
	public static final HashSet<String> HeadersToLog = new HashSet<>(
			Arrays.asList("X-LISTING-CONTEXT", "X-IDENTITY-CONTEXT", "REFERER"));
		
	public static void SetRequestUrlContextForThread(Object requestUrl)
	{
		if(requestUrl != null)
		{
			ThreadContext.put("URL", requestUrl.toString());
		}
	}
		
	public static void SetRequestMethodContextForThread(Object method)
	{
		if(method != null) {
			ThreadContext.put("Method", method.toString());
		}
	}
	
	public static void SetRequestHeadersContextForThread(Map<String, String> headers)
	{
		if(headers != null) {
	    	for(Map.Entry<String,String> entry : headers.entrySet())
	    	{
	    		SetRequestHeaderContextForThread(entry.getKey(), entry.getValue());
	    	}
	    }
	}
	
	public static void SetRequestHeaderContextForThread(String headerName, String headerValue)
	{
		if(headerName != null && headerValue != null) {
    		// whitelist of things we actually care about. Don't log the world.
			String headerNameUpper = headerName.toUpperCase();
			if(HeadersToLog.contains(headerNameUpper))
			{
				ThreadContext.put("Headers." + headerName, headerValue);
			}
		}
	}
}