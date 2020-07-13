plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom("io.micrometer:micrometer-bom:1.5.1")
        mavenBom("io.netty:netty-bom:4.1.50.Final")
        mavenBom("com.linecorp.armeria:armeria-bom:0.99.7")
        mavenBom("org.junit:junit-bom:5.6.1")
    }
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    implementation("io.projectreactor:reactor-core:3.3.5.RELEASE")
    implementation("com.linecorp.armeria:armeria-spring-boot-starter")
    implementation("org.hibernate.validator:hibernate-validator")

    runtimeOnly("com.linecorp.armeria:armeria-spring-boot-actuator-starter")
}