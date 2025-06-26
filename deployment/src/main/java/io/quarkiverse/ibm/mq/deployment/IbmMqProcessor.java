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

import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.dev.devservices.DevServicesConfig;

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

    @SuppressWarnings("resource")
    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = DevServicesConfig.Enabled.class)
    public DevServicesResultBuildItem createContainer() {
        MQContainer container = new MQContainer(MQContainer.DEFAULT_IMAGE)
                .acceptLicense()
                .withAppUser("app")
                .withAppPassword("app")
                .withAdminUser("admin")
                .withAdminPassword("admin")
                .withCreateContainerCmdModifier(cmd -> cmd.withPlatform("linux/amd64"));

        container.start();

        Map<String, String> configOverrides = Map.of(
                "quarkus.ibm-mq.devservices.host-name", container.getHost(),
                "quarkus.ibm-mq.devservices.port", String.valueOf(container.getPort()),
                "quarkus.ibm-mq.devservices.admin-user", container.getAdminUser(),
                "quarkus.ibm-mq.devservices.admin-password", container.getAdminPassword()
        );

        System.out.println(configOverrides);
        return new DevServicesResultBuildItem.RunningDevService(FEATURE,
                container.getContainerId(),
                container::close,
                configOverrides).toBuildItem();
    }
}
