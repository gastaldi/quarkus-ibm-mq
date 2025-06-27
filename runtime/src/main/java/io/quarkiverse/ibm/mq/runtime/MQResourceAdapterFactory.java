package io.quarkiverse.ibm.mq.runtime;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.xa.XAResource;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.XAConnectionFactory;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.ResourceAdapterInternalException;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;

import com.ibm.mq.jakarta.connector.ResourceAdapterImpl;
import com.ibm.mq.jakarta.connector.inbound.ActivationSpecImpl;
import com.ibm.mq.jakarta.connector.outbound.ManagedConnectionFactoryImpl;

import io.quarkiverse.ironjacamar.ResourceAdapterFactory;
import io.quarkiverse.ironjacamar.ResourceAdapterKind;
import io.quarkiverse.ironjacamar.ResourceAdapterTypes;
import io.quarkiverse.ironjacamar.runtime.endpoint.MessageEndpointWrapper;

@ResourceAdapterKind("ibm-mq")
@ResourceAdapterTypes(connectionFactoryTypes = { ConnectionFactory.class, XAConnectionFactory.class })
public class MQResourceAdapterFactory implements ResourceAdapterFactory {

    @Override
    public String getProductName() {
        return "IBM MQ Resource Adapter";
    }

    @Override
    public String getProductVersion() {
        return "9.4.3.0";
    }

    @Override
    public ResourceAdapter createResourceAdapter(String id, Map<String, String> config) throws ResourceException {
        ResourceAdapterImpl adapter = new ResourceAdapterImpl();
        if (config.containsKey("max-connections"))
            adapter.setMaxConnections(config.get("max-connections"));
        if (config.containsKey("reconnection-retry-count"))
            adapter.setReconnectionRetryCount(config.get("reconnection-retry-count"));
        if (config.containsKey("reconnection-retry-interval"))
            adapter.setReconnectionRetryInterval(config.get("reconnection-retry-interval"));
        if (config.containsKey("startup-retry-count"))
            adapter.setStartupRetryCount(config.get("startup-retry-count"));
        if (config.containsKey("startup-retry-interval"))
            adapter.setStartupRetryInterval(config.get("startup-retry-interval"));
        if (config.containsKey("support-mq-extensions"))
            adapter.setSupportMQExtensions(config.get("support-mq-extensions"));
        if (config.containsKey("native-library-path"))
            adapter.setNativeLibraryPath(config.get("native-library-path"));
        if (config.containsKey("log-writer-enabled"))
            adapter.setLogWriterEnabled(config.get("log-writer-enabled"));
        if (config.containsKey("trace-enabled"))
            adapter.setTraceEnabled(config.get("trace-enabled"));
        if (config.containsKey("trace-level"))
            adapter.setTraceLevel(config.get("trace-level"));
        return new ResourceAdapterWrapper(adapter, config);
    }

    @Override
    public ManagedConnectionFactory createManagedConnectionFactory(String id, ResourceAdapter adapter) {
        ResourceAdapterWrapper wrapper = (ResourceAdapterWrapper) adapter;
        ManagedConnectionFactoryImpl factory = new ManagedConnectionFactoryImpl();
        Map<String, String> config = new HashMap<>(wrapper.config);
        factory.setHostName(config.get("host-name"));
        factory.setPort(config.get("port"));
        factory.setQueueManager(config.get("queue-manager"));
        factory.setChannel(config.get("channel"));
        factory.setUserName(config.get("user"));
        factory.setPassword(config.get("password"));
        factory.setSslCipherSuite(config.get("ssl-cipher-suite"));
        factory.setResourceAdapter(wrapper.delegate);
        return factory;
    }

    @Override
    public ActivationSpec createActivationSpec(String id, ResourceAdapter adapter, Class<?> type, Map<String, String> config) {
        ResourceAdapterWrapper wrapper = (ResourceAdapterWrapper) adapter;
        Map<String, String> mergedConfig = new HashMap<>(wrapper.config);
        mergedConfig.putAll(config);
        ActivationSpecImpl activationSpec = new ActivationSpecImpl();
        activationSpec.setResourceAdapter(wrapper.delegate);
        activationSpec.setUseJNDI(false);
        activationSpec.setDestination(mergedConfig.get("destination"));
        activationSpec.setDestinationType(mergedConfig.get("destination-type"));
        activationSpec.setUserName(mergedConfig.get("user"));
        activationSpec.setPassword(mergedConfig.get("password"));
        activationSpec.setQueueManager(mergedConfig.get("queue-manager"));
        activationSpec.setChannel(mergedConfig.get("channel"));
        activationSpec.setHostName(mergedConfig.get("host-name"));
        activationSpec.setPort(mergedConfig.get("port"));
        activationSpec.setSslCipherSuite(mergedConfig.get("ssl-cipher-suite"));
        return activationSpec;
    }

    @Override
    public MessageEndpoint wrap(MessageEndpoint endpoint, Object resourceEndpoint) {
        return new JMSMessageEndpoint(endpoint, (MessageListener) resourceEndpoint);
    }

    private static class ResourceAdapterWrapper implements ResourceAdapter {

        private final ResourceAdapter delegate;
        private final Map<String, String> config;

        private ResourceAdapterWrapper(ResourceAdapter delegate, Map<String, String> config) {
            this.delegate = delegate;
            this.config = config;
        }

        @Override
        public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
            delegate.start(ctx);
        }

        @Override
        public void stop() {
            delegate.stop();
        }

        @Override
        public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) throws ResourceException {
            delegate.endpointActivation(endpointFactory, spec);
        }

        @Override
        public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
            delegate.endpointDeactivation(endpointFactory, spec);
        }

        @Override
        public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
            return delegate.getXAResources(specs);
        }
    }

    private static class JMSMessageEndpoint extends MessageEndpointWrapper implements MessageListener {

        private final MessageListener listener;

        public JMSMessageEndpoint(MessageEndpoint messageEndpoint, MessageListener listener) {
            super(messageEndpoint);
            this.listener = listener;
        }

        @Override
        public void onMessage(Message message) {
            listener.onMessage(message);
        }
    }
}
