package io.quarkiverse.ibm.mq.runtime.graal;

import java.io.PrintStream;
import java.util.HashMap;

import com.ibm.msg.client.commonservices.CSIException;
import com.ibm.msg.client.commonservices.trace.DumpableComponent;
import com.ibm.msg.client.commonservices.trace.DumpableObject;
import com.ibm.msg.client.commonservices.trace.Trace;
import com.ibm.msg.client.commonservices.trace.TraceFFSTInfo;
import com.oracle.svm.core.annotate.RecomputeFieldValue;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@Substitute
@TargetClass(Trace.class)
public final class TraceSubstitution {
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    public static PrintStream errorStream;

    @Substitute
    public static boolean isOn = false;

    @Substitute
    public static void entry(Object parentClass, String parentClassName, String methodSignature, Object[] parameters) {
    }

    @Substitute
    public static void entry(Object parentClass, String parentClassName, String methodSignature) {
    }

    @Substitute
    public static void entry(String parentClassName, String methodSignature) {
    }

    @Substitute
    public static void entry(String parentClassName, String methodSignature, Object[] parameters) {
    }

    @Substitute
    public static void traceData(Object parentClassP, String parentClassName, String methodSignature, String uniqueDescription,
            Object data) {
    }

    @Substitute
    public static void traceData(int level, Object parentClassP, String parentClassName, String methodSignature,
            String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceData(Object parentClassP, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceData(int level, Object parentClassP, String methodSignature, String uniqueDescription,
            Object data) {
    }

    @Substitute
    public static void data(Object parentClassP, String parentClassName, String methodSignature, String uniqueDescription,
            Object data) {
    }

    @Substitute
    public static void data(int methodTraceLevel, Object parentClassP, String parentClassName, String methodSignature,
            String uniqueDescription, Object data) {
    }

    @Substitute
    public static void data(Object parentClassP, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceData(String parentClassName, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void data(String parentClassName, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceData(Object parentClassP, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void data(Object parentClassP, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceData(String parentClassName, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void data(String parentClassName, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void exit(Object parentClass, String parentClassName, String methodSignature, Object returnValue,
            int exitIndex) {
    }

    @Substitute
    public static void exit(Object parentClass, String parentClassName, String methodSignature) {
    }

    @Substitute
    public static void exit(Object parentClass, String parentClassName, String methodSignature, Object returnValue) {
    }

    @Substitute
    public static void exit(Object parentClass, String parentClassName, String methodSignature, int exitIndex) {
    }

    @Substitute
    public static void exit(String parentClassName, String methodSignature) {
    }

    @Substitute
    public static void exit(String parentClassName, String methodSignature, Object returnValue) {
    }

    @Substitute
    public static void exit(String parentClassName, String methodSignature, int exitIndex) {
    }

    @Substitute
    public static void exit(String parentClassName, String methodSignature, Object returnValue, int exitIndex) {
    }

    @Substitute
    public static void catchBlock(Object parentClassP, String parentClassName, String methodSignature, Throwable thrown,
            int exitIndex) {
    }

    @Substitute
    public static void catchBlock(Object parentClassP, String parentClassName, String methodSignature, Throwable thrown) {
    }

    @Substitute
    public static void catchBlock(String parentClassName, String methodSignature, Throwable thrown) {
    }

    @Substitute
    public static void catchBlock(String parentClassName, String methodSignature, Throwable thrown, int exitIndex) {
    }

    @Substitute
    public static void throwing(Object parentClassP, String parentClassName, String methodSignature, Throwable thrown,
            int exitIndex) {
    }

    @Substitute
    public static void throwing(Object parentClassP, String parentClassName, String methodSignature, Throwable thrown) {
    }

    @Substitute
    public static void throwing(String parentClassName, String methodSignature, Throwable thrown) {
    }

    @Substitute
    public static void throwing(String parentClassName, String methodSignature, Throwable thrown, int exitIndex) {
    }

    @Substitute
    public static void ffst(String sourceClassName, String methodSignature, String probeID,
            HashMap<String, ? extends Object> data, Class<? extends Throwable> exceptionClassToThrow) {
    }

    @Substitute
    public static synchronized void ffst(Object sourceClass, String methodSignature, String probeID,
            HashMap<String, ? extends Object> dataP, Class<? extends Throwable> exceptionClassToThrow) {
    }

    @Substitute
    public static void finallyBlock(Object parentClassP, String parentClassName, String methodSignature, int exitIndex) {
    }

    @Substitute
    public static void finallyBlock(Object parentClassP, String parentClassName, String methodSignature) {
    }

    @Substitute
    public static void finallyBlock(String parentClassName, String methodSignature) {
    }

    @Substitute
    public static void finallyBlock(String parentClassName, String methodSignature, int exitIndex) {
    }

    @Substitute
    public static void registerFFSTObject(Object object) {
    }

    @Substitute
    public static void deRegisterFFSTObject(Object object) {
    }

    @Substitute
    public static void traceWarning(Object parentClassP, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceWarning(Object parentClassP, String parentClassName, String methodSignature,
            String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceWarning(String parentClassName, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void registerFFSTInfo(TraceFFSTInfo providerFFST) {
    }

    @Substitute
    public static void registerDumpableComponent(DumpableComponent dumpable) {
    }

    @Substitute
    public static void traceInfo(Object parentClassP, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static void traceInfo(Object parentClassP, String parentClassName, String methodSignature, String uniqueDescription,
            Object data) {
    }

    @Substitute
    public static void traceInfo(String parentClassName, String methodSignature, String uniqueDescription, Object data) {
    }

    @Substitute
    public static boolean isClassTraced(Object classObject, String classString) {
        return false;
    }

    @Substitute
    public static String buildPrefix(int level) {
        return null;
    }

    @Substitute
    public static String getBuildIdentifier() {
        return null;
    }

    @Substitute
    public static void setTraceLevel(int newTraceLevel) {
    }

    @Substitute
    public static int getTraceLevel() {
        return 0;
    }

    @Substitute
    public static synchronized void setOn(boolean traceOn) {
    }

    @Substitute
    public static void registerDumpableObject(DumpableObject dumpable) {
    }

    @Substitute
    public static String formatTimeStamp(long millis) {
        return null;
    }

    @Substitute
    public static void deRegisterDumpableObject(DumpableObject dumpable) {
    }

    @Substitute
    public static void initialize() throws CSIException {
    }

    @Substitute
    private static void addPackageListToTree(String packageList, boolean included) {

    }
}
