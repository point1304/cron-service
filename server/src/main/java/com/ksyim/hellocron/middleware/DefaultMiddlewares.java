package com.ksyim.hellocron.middleware;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class DefaultMiddlewares implements Middlewares {

    private List<Middleware> middlewares;

    public DefaultMiddlewares() {}

    @Override
    public void use(Middleware mw) {
        requireNonNull(mw, "middleware is null");
        middlewares.add(mw);
    }

    @Override
    public void run() {

    }

    @Override
    public List<Middleware> getHandlers() {
        return middlewares;
    }
}
