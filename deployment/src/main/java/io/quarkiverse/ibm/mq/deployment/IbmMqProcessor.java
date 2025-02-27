package io.quarkiverse.ibm.mq.deployment;

import com.ibm.mq.MQJavaComponent;
import com.ibm.mq.jakarta.jms.MQJMSComponent;
import com.ibm.mq.jmqi.remote.api.RemoteFAP;
import com.ibm.msg.client.commonservices.componentmanager.ComponentManager;
import com.ibm.msg.client.commonservices.j2se.J2SEComponent;
import com.ibm.msg.client.jakarta.jms.internal.JMSComponent;
import com.ibm.msg.client.jakarta.jms.internal.JmsFactoryFactoryImpl;
import com.ibm.msg.client.jakarta.wmq.factories.WMQComponent;
import com.ibm.msg.client.jakarta.wmq.factories.WMQFactoryFactory;
import com.ibm.msg.client.jakarta.wmq.factories.admin.WMQJmsFactory;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class IbmMqProcessor {

    private static final String FEATURE = "ibm-mq";

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

}
