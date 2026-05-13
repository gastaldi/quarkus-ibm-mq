package io.quarkiverse.ibm.mq.runtime;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class MQRecorder {

    public void setProductVersion(String version) {
        MQResourceAdapterFactory.setProductVersion(version);
    }
}
