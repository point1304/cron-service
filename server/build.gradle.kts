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
    implementation(project(":util"))

    implementation("com.google.code.findbugs:jsr305:3.0.2")

    implementation("io.projectreactor:reactor-core:3.3.5.RELEASE")
    implementation("com.linecorp.armeria:armeria-spring-boot-starter")
    implementation("org.hibernate.validator:hibernate-validator")

    implementation("com.beust:jcommander:1.78")

    runtimeOnly("com.linecorp.armeria:armeria-spring-boot-actuator-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage:junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")

    compileOnly("org.hibernate.validator:hibernate-validator-annotation-processor")
    annotationProcessor("org.hibernate.validator:hibernate-validator-annotation-processor")
}

tasks.withType<Test> {
    environment("LINE_MESSAGING_API_TOKEN", "mock-token")
    environment("PORT", "5000")
    environment("ARMERIA_ENV", "production")
}