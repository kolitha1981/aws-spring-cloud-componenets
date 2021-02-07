package org.persistent.studentservice.studentserviceapigateway.filters;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.persistent.studentservice.studentserviceapigateway.constants.GatewayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class AuthorizationFilter extends ZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		final RequestContext requestContext = RequestContext.getCurrentContext();
		final HttpServletRequest httpServletRequest = requestContext.getRequest();
		LOGGER.info("@@@@@ Processing request URI -----> :" + httpServletRequest.getRequestURI());
		LOGGER.info("@@@@@ Checkin for the header -----> :" + GatewayConstants.REQUEST_HEADER_NAME_AUTHORIZATION);
		final String header = httpServletRequest.getHeader(GatewayConstants.REQUEST_HEADER_NAME_AUTHORIZATION);
		if (header == null || header.isEmpty()) {
			LOGGER.info(
					"@@@@@ No header found with name -----> :" + GatewayConstants.REQUEST_HEADER_NAME_AUTHORIZATION);
			requestContext.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
			requestContext.setResponseBody(GatewayConstants.REQUEST_BODY_MESSAGE_FORBIDDEN);
			requestContext.setSendZuulResponse(false);
		} else {
			LOGGER.info("@@@@@ Header found with name -----> :" + GatewayConstants.REQUEST_HEADER_NAME_AUTHORIZATION);
		}
		return null;
	}

	@Override
	public String filterType() {
		return GatewayConstants.PRE_FILTER_PREFIX;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
