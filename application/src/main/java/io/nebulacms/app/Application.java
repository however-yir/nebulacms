package io.nebulacms.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.integration.autoconfigure.IntegrationAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * NebulaCMS main class.
 *
 * @author ryanwang
 * @author JohnNiang
 * @author guqing
 * @date 2017-11-14
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "io.nebulacms.app", exclude =
    IntegrationAutoConfiguration.class)
@ConfigurationPropertiesScan(basePackages = "io.nebulacms.app.infra.properties")
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
            .applicationStartup(new BufferingApplicationStartup(1024))
            .run(args);
    }

}
