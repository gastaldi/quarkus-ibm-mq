package io.quarkiverse.ibm.mq.runtime.graal;

import java.util.Random;

import com.ibm.mq.jmqi.remote.impl.RemoteReconnectThread;
import com.oracle.svm.core.annotate.*;

@TargetClass(RemoteReconnectThread.class)
public final class RemoteReconnectThreadSubstitution {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    private static Random rand;

}
