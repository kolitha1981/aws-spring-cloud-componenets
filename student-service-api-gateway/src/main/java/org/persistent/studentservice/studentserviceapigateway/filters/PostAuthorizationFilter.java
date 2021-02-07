package org.persistent.studentservice.studentserviceapigateway.filters;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.persistent.studentservice.studentserviceapigateway.constants.GatewayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostAuthorizationFilter extends ZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostAuthorizationFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		final RequestContext requestContext = RequestContext.getCurrentContext();
		final HttpServletRequest httpServletRequest = requestContext.getRequest();
		LOGGER.info("$$$$$$$$$ Successfully processed  request URI -----> :" + httpServletRequest.getRequestURI());
		Enumeration<String> headerEnumetation = httpServletRequest.getHeaderNames();
		while (headerEnumetation.hasMoreElements()) {
			final String headerName = headerEnumetation.nextElement();
			final String headerValue = httpServletRequest.getHeader(headerName);
			LOGGER.info("$$$$$$$$$ Header name :" + headerName + " $$$$$$$$$ Header value : " + headerValue);
		}
		return null;
	}

	@Override
	public String filterType() {
		return GatewayConstants.POST_FILTER_PREFIX;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
