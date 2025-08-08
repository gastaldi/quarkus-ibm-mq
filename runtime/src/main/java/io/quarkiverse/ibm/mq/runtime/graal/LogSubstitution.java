package io.quarkiverse.ibm.mq.runtime.graal;

import java.util.HashMap;

import com.ibm.msg.client.commonservices.Log.Log;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@Substitute
@TargetClass(Log.class)
public final class LogSubstitution {

    @Substitute
    public static void log(Object parentClass, String methodSignature, String key, HashMap<String, ? extends Object> inserts) {
    }

    @Substitute
    public static void log(String parentClassName, String methodSignature, String key,
            HashMap<String, ? extends Object> inserts) {
    }

    @Substitute
    public static void logNLS(Object parentClass, String methodSignature, String NLSMessage) {
    }

    @Substitute
    public static void logNLS(String parentClassName, String methodSignature, String NLSMessage) {

    }

    @Substitute
    public static boolean getOn() {
        return false;
    }

    @Substitute
    public static void setOn(boolean LogOn) {
    }

    @Substitute
    public static boolean isJeeOn() {
        return false;
    }

    @Substitute
    public static void setJeeOn(boolean jeeOn) {
    }

    @Substitute
    public static void logNLS(Object parentClass, String parentClassName, String methodSignature, String NLSMessage) {
    }
}
