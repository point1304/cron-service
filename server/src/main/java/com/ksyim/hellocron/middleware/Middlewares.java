package com.ksyim.hellocron.middleware;

import java.util.List;

interface Middlewares {
    void use(Middleware mw);
    void run();
    List<Middleware> getHandlers();
}