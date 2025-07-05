package io.quarkiverse.ibm.mq.deployment;

import java.util.function.BooleanSupplier;

import io.quarkus.deployment.dev.devservices.DevServicesConfig;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.ibm-mq")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface IbmMqBuildTimeConfig {

    /**
     * Retrieves the configuration for IBM MQ DevServices at build time.
     * This configuration determines the behavior of DevServices for IBM MQ,
     * including whether it is enabled.
     *
     * @return an instance of {@code DevServicesConfig}, representing the DevServices settings for IBM MQ.
     */
    DevServicesConfig devservices();

    @ConfigGroup
    interface DevServicesConfig {
        /**
         * Indicates whether the DevServices for IBM MQ is enabled.
         * When true, the DevServices functionality for IBM MQ will be active.
         * This includes automatically starting a container for IBM MQ if necessary.
         *
         * @return true if DevServices for IBM MQ is enabled; false otherwise.
         */
        @WithDefault("true")
        boolean enabled();

        final class Enabled implements BooleanSupplier {
            final IbmMqBuildTimeConfig config;

            public Enabled(IbmMqBuildTimeConfig config) {
                this.config = config;
            }

            public boolean getAsBoolean() {
                return this.config.devservices().enabled();
            }
        }

    }
}
