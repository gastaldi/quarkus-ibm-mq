package io.quarkiverse.ibm.mq.runtime;

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
        return "9.4.2.0";
    }

    @Override
    public ResourceAdapter createResourceAdapter(String id, Map<String, String> config) throws ResourceException {
        ResourceAdapterImpl adapter = new ResourceAdapterImpl();
        //TODO: Set the properties
        return new ResourceAdapterWrapper(adapter, config);
    }

    @Override
    public ManagedConnectionFactory createManagedConnectionFactory(String id, ResourceAdapter adapter)
            throws ResourceException {
        ResourceAdapterWrapper wrapper = (ResourceAdapterWrapper) adapter;
        ManagedConnectionFactoryImpl factory = new ManagedConnectionFactoryImpl();
        factory.setHostName(wrapper.config.get("host"));
        factory.setPort(wrapper.config.get("port"));
        factory.setQueueManager(wrapper.config.get("queue-manager"));
        factory.setChannel(wrapper.config.get("channel"));
        factory.setResourceAdapter(adapter);
        return factory;
    }

    @Override
    public ActivationSpec createActivationSpec(String id, ResourceAdapter adapter, Class<?> type, Map<String, String> config)
            throws ResourceException {
        ActivationSpecImpl activationSpec = new ActivationSpecImpl();
        activationSpec.setResourceAdapter(adapter);
        activationSpec.setUseJNDI(false);
        //TODO: Set the properties

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
