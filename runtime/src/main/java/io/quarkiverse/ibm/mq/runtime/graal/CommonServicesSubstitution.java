package io.quarkiverse.ibm.mq.runtime.graal;

import com.ibm.msg.client.commonservices.CommonServices;
import com.ibm.msg.client.commonservices.provider.workqueue.CSPWorkQueueManager;
import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.RecomputeFieldValue;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(CommonServices.class)
public final class CommonServicesSubstitution {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    private static CSPWorkQueueManager workQueueManagerImplementation;
}
