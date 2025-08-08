package io.quarkiverse.ibm.mq.runtime.graal;

import java.util.Map;

import javax.management.ObjectName;
import javax.management.StandardMBean;

import com.ibm.msg.client.commonservices.CSIException;
import com.ibm.msg.client.commonservices.monitor.MonitorAgent;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@Substitute
@TargetClass(MonitorAgent.class)
public final class MonitorAgentSubstitution {

    @Substitute
    public static void initialize() throws CSIException {
    }

    @Substitute
    public static void registerMBean(final Object newMBean, String type, String newBeanName) {
    }

    @Substitute
    public static ObjectName registerMBean(StandardMBean newMBean, Map<String, String> properties) {
        return null;
    }

    @Substitute
    public static void unregisterMBean(final ObjectName name) {
    }

    @Substitute
    public static void unregisterMBean(String beanName, String type) {

    }
}
