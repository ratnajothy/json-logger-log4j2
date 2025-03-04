package org.custom.log4j;

import java.util.Map;

import org.apache.http.Header;
import org.apache.synapse.core.axis2.Axis2MessageContext;

public class Log4JAxis2RequestContext {
		
	public static void SetRequestContextForThread(org.apache.http.HttpRequest request)
	{
		SetRequestUrlContextForThread(request);
		SetRequestMethodContextForThread(request);
		SetRequestHeadersContextForThread(request);
	}
	
	public static void SetRequestContextForThread(org.apache.synapse.MessageContext messageContext)
	{
		SetRequestUrlContextForThread(messageContext);
		SetRequestMethodContextForThread(messageContext);
		SetRequestHeadersContextForThread(messageContext);
	}
	
	public static void SetRequestContextForThread(org.apache.axis2.context.MessageContext messageContext)
	{
		SetRequestUrlContextForThread(messageContext);
		SetRequestMethodContextForThread(messageContext);
		SetRequestHeadersContextForThread(messageContext);
	}
	
	public static void SetRequestUrlContextForThread(org.apache.http.HttpRequest request)
	{
		if(request != null)
		{
			Log4JRequestContext.SetRequestUrlContextForThread(request.getRequestLine().getUri());
		}
	}
	
	public static void SetRequestUrlContextForThread(org.apache.synapse.MessageContext messageContext)
	{
		if(messageContext != null)
		{
			Log4JRequestContext.SetRequestUrlContextForThread(messageContext.getProperty("REST_FULL_REQUEST_PATH"));
		}
	}
	
	public static void SetRequestUrlContextForThread(org.apache.axis2.context.MessageContext messageContext)
	{
		if(messageContext != null)
		{
			Log4JRequestContext.SetRequestUrlContextForThread(messageContext.getProperty("REST_FULL_REQUEST_PATH"));
		}
	}
	
	public static void SetRequestMethodContextForThread(org.apache.http.HttpRequest request)
	{
		if(request != null)
		{
			Log4JRequestContext.SetRequestMethodContextForThread(request.getRequestLine().getMethod());
		}
	}
	
	public static void SetRequestMethodContextForThread(org.apache.synapse.MessageContext messageContext)
	{
		if(messageContext != null)
		{
			org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext)messageContext).getAxis2MessageContext();
			SetRequestMethodContextForThread(axis2MessageContext);
		}
	}
	
	public static void SetRequestMethodContextForThread(org.apache.axis2.context.MessageContext messageContext)
	{
		if(messageContext != null)
		{
			Log4JRequestContext.SetRequestMethodContextForThread(messageContext.getProperty("HTTP_METHOD"));
		}
	}
	
	public static void SetRequestHeadersContextForThread(org.apache.http.HttpRequest request)
	{
		if(request != null)
		{
			for(String headerName : Log4JRequestContext.HeadersToLog){
				Header header = request.getLastHeader(headerName);
				if(header != null) {
					Log4JRequestContext.SetRequestHeaderContextForThread(headerName, header.getValue());
				}
			}
		}
	}
	
	public static void SetRequestHeadersContextForThread(org.apache.synapse.MessageContext messageContext)
	{
		if(messageContext != null)
		{
			org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext)messageContext).getAxis2MessageContext();
			SetRequestHeadersContextForThread(axis2MessageContext);
		}
	}
	
	public static void SetRequestHeadersContextForThread(org.apache.axis2.context.MessageContext messageContext)
	{
		if(messageContext != null)
		{
			Log4JRequestContext.SetRequestHeadersContextForThread(getTransportHeaders(messageContext));
		}
	}
	
	// gets transport headers from the request
	@SuppressWarnings("unchecked")
	// this is fine because API Manager default implementation does this
	private static Map<String, String> getTransportHeaders(org.apache.axis2.context.MessageContext messageContext) {
		return (Map<String, String>)messageContext.getProperty("TRANSPORT_HEADERS");
	}
}