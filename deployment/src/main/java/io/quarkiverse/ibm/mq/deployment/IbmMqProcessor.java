package io.quarkiverse.ibm.mq.deployment;

import java.util.Map;

import org.jboss.logging.Logger;

import com.ibm.mq.MQJavaComponent;
import com.ibm.mq.jakarta.jms.MQJMSComponent;
import com.ibm.mq.jmqi.remote.api.RemoteFAP;
import com.ibm.mq.testcontainers.MQContainer;
import com.ibm.msg.client.commonservices.componentmanager.ComponentManager;
import com.ibm.msg.client.commonservices.j2se.J2SEComponent;
import com.ibm.msg.client.jakarta.jms.internal.JMSComponent;
import com.ibm.msg.client.jakarta.jms.internal.JmsFactoryFactoryImpl;
import com.ibm.msg.client.jakarta.wmq.factories.WMQComponent;
import com.ibm.msg.client.jakarta.wmq.factories.WMQFactoryFactory;
import com.ibm.msg.client.jakarta.wmq.factories.admin.WMQJmsFactory;

import io.quarkus.deployment.IsProduction;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;
import io.quarkus.deployment.dev.devservices.DevServicesConfig;
import io.quarkus.devservices.common.JBossLoggingConsumer;

class IbmMqProcessor {

    private static final String FEATURE = "ibm-mq";

    private static final Logger logger = Logger.getLogger(IbmMqProcessor.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerForReflection(BuildProducer<ReflectiveClassBuildItem> producer) {
        producer.produce(ReflectiveClassBuildItem.builder(
                JmsFactoryFactoryImpl.class,
                RemoteFAP.class,
                WMQFactoryFactory.class,
                WMQJmsFactory.class,
                ComponentManager.class,
                J2SEComponent.class,
                MQJavaComponent.class,
                MQJMSComponent.class,
                JMSComponent.class,
                WMQComponent.class).constructors().methods().fields().build());
    }

    @BuildStep
    void runtimeInitialized(BuildProducer<RuntimeInitializedClassBuildItem> producer) {
        producer.produce(new RuntimeInitializedClassBuildItem(
                "com.ibm.msg.client.commonservices.workqueue.WorkQueueManager"));
        producer.produce(new RuntimeInitializedClassBuildItem(
                "com.ibm.msg.client.commonservices.j2se.workqueue.WorkQueueManagerImplementation"));
    }

    @SuppressWarnings("resource")
    @BuildStep(onlyIfNot = IsProduction.class, onlyIf = { DevServicesConfig.Enabled.class,
            IbmMqBuildTimeConfig.DevServicesConfig.Enabled.class })
    public DevServicesResultBuildItem createContainer(IbmMqBuildTimeConfig buildTimeConfig) {
        MQContainer container = new MQContainer(MQContainer.DEFAULT_IMAGE)
                .acceptLicense()
                // Setting these environment variables are the only way to make authentication work
                .withEnv("MQ_ADMIN_PASSWORD", "admin")
                .withEnv("MQ_APP_PASSWORD", "app")
                .withCreateContainerCmdModifier(cmd -> cmd.withPlatform("linux/amd64"));

        if (buildTimeConfig.devservices().webServerEnabled()) {
            container.withWebServer();
        }
        if (buildTimeConfig.devservices().logsEnabled()) {
            container.withLogConsumer(new JBossLoggingConsumer(logger));
        }
        container.start();

        Map<String, String> configOverrides = Map.of(
                "ibm-mq.host-name", container.getHost(),
                "ibm-mq.port", String.valueOf(container.getPort()),
                "ibm-mq.admin-user", container.getAdminUser(),
                "ibm-mq.admin-password", container.getAdminPassword(),
                "ibm-mq.app-user", container.getAppUser(),
                "ibm-mq.app-password", container.getAppPassword(),
                "ibm-mq.channel", container.getChannel(),
                "ibm-mq.queue-manager", container.getQueueManager(),
                "ibm-mq.webserver.port", String.valueOf(container.getWebServerPort()));

        return DevServicesResultBuildItem.discovered()
                .containerId(container.getContainerId())
                .name("IBM MQ Dev Service")
                .description("IBM MQ Container")
                .config(configOverrides)
                .build();
    }
}
