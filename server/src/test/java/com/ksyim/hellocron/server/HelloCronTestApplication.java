package com.ksyim.hellocron.server;

import com.linecorp.armeria.server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HelloCronTestApplication {

    @Inject
    private Server server;

    @Test
    public void serverTest() {}
}
