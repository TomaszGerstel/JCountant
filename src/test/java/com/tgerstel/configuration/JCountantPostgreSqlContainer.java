package com.tgerstel.configuration;

import org.testcontainers.containers.PostgreSQLContainer;

public class JCountantPostgreSqlContainer extends PostgreSQLContainer<JCountantPostgreSqlContainer> {

    private static final String IMAGE_VERSION = "postgres:11.3";
    private static JCountantPostgreSqlContainer container;

    private JCountantPostgreSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static JCountantPostgreSqlContainer getInstance() {
        if (container == null) container = new JCountantPostgreSqlContainer();
        return container;
    }

    @Override
    public String getJdbcUrl() {
        String additionalUrlParams = this.constructUrlParameters("?", "&");
        return "jdbc:tc:postgresql://" + this.getHost() + ":" + this.getMappedPort(POSTGRESQL_PORT) + "/" + this.getDatabaseName() + additionalUrlParams;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }

}
