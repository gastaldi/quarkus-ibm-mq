package io.quarkiverse.ibm.mq.runtime;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class MQResourceAdapterRecorder {

    public void setProductVersion(String version) {
        MQResourceAdapterFactory.productVersion = version;
    }
}
