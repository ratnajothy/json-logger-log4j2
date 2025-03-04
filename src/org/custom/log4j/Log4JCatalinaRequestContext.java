package org.custom.log4j;

public class Log4JCatalinaRequestContext {
	
	public static void SetRequestContextForThread(org.apache.catalina.connector.Request request)
	{
		SetRequestUrlContextForThread(request);
		SetRequestMethodContextForThread(request);
		SetRequestHeadersContextForThread(request);
	}
	
	public static void SetRequestUrlContextForThread(org.apache.catalina.connector.Request request)
	{
		if(request != null)
		{
			Log4JRequestContext.SetRequestUrlContextForThread(request.getRequestURI());
		}
	}

	
	public static void SetRequestMethodContextForThread(org.apache.catalina.connector.Request request)
	{
		if(request != null)
		{
			Log4JRequestContext.SetRequestMethodContextForThread(request.getMethod());
		}
	}
	
	public static void SetRequestHeadersContextForThread(org.apache.catalina.connector.Request request)
	{
		if(request != null)
		{
			for(String headerName : Log4JRequestContext.HeadersToLog){
				Log4JRequestContext.SetRequestHeaderContextForThread(headerName, request.getHeader(headerName));
			}
		}
	}
}