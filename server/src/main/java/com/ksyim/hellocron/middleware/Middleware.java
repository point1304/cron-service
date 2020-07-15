package com.ksyim.hellocron.middleware;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;

@FunctionalInterface
public interface Middleware {
    void handle(ServiceRequestContext ctx, AggregatedHttpRequest req, Middleware next);
}
