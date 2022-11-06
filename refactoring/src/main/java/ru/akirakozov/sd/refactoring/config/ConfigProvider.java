package ru.akirakozov.sd.refactoring.config;

import lombok.experimental.UtilityClass;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;

import java.nio.file.Paths;
import java.util.Collections;

@UtilityClass
public class ConfigProvider {
    private static final String CONFIG_FILE_NAME = "application.properties";
    private static final String SERVER_CONFIG_PREFIX = "server";
    private static final String ENDPOINT_CONFIG_PREFIX = SERVER_CONFIG_PREFIX.concat(".endpoint");
    private static final String DB_CONFIG_PREFIX = "db";

    private static final DbConfig DB_CONFIG;
    private static final ServerConfig SERVER_CONFIG;
    private static final EndpointConfig ENDPOINT_CONFIG;

    static {
        ConfigFilesProvider configFilesProvider = () ->
                Collections.singletonList(Paths.get(CONFIG_FILE_NAME));
        ClasspathConfigurationSource classpathConfigurationSource =
                new ClasspathConfigurationSource(configFilesProvider);

        ConfigurationProvider configurationProvider = new ConfigurationProviderBuilder()
                .withConfigurationSource(classpathConfigurationSource)
                .build();

        DB_CONFIG = configurationProvider.bind(DB_CONFIG_PREFIX, DbConfig.class);
        SERVER_CONFIG = configurationProvider.bind(SERVER_CONFIG_PREFIX, ServerConfig.class);
        ENDPOINT_CONFIG = configurationProvider.bind(ENDPOINT_CONFIG_PREFIX, EndpointConfig.class);
    }

    public static DbConfig dbConfig() {
        return DB_CONFIG;
    }

    public static ServerConfig serverConfig() {
        return SERVER_CONFIG;
    }

    public static EndpointConfig endpointPathConfig() {
        return ENDPOINT_CONFIG;
    }
}
