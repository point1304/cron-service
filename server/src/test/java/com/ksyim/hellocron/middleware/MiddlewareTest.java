package com.ksyim.hellocron.middleware;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MiddlewareTest {

    @Test
    public void testUseMiddleware(Middlewares mw) {
        Middleware[] handlers = {(ctx, req, next) -> {}, (ctx, req, next) -> {}, (ctx, req, next) -> {}};

        for (Middleware handler : handlers) {
            mw.use(handler);
        }

        assertEquals(mw.getHandlers().size(), handlers.length);
    }

    @Test
    public void testRunMiddlewares(Middlewares mw) {

    }
}
