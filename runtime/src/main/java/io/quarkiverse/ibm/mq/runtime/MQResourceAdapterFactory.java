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

import org.jboss.logging.Logger;

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

    private static final Logger log = Logger.getLogger(MQResourceAdapterFactory.class);

    @Override
    public String getProductName() {
        return "IBM MQ Resource Adapter";
    }

    @Override
    public String getProductVersion() {
        return "9.4.3.0";
    }

    @Override
    public ResourceAdapter createResourceAdapter(String id, Map<String, String> config) {
        ResourceAdapterImpl adapter = new ResourceAdapterImpl();
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "log-writer-enabled" -> adapter.setLogWriterEnabled(value);
                case "max-connections" -> adapter.setMaxConnections(value);
                case "native-library-path" -> adapter.setNativeLibraryPath(value);
                case "reconnection-retry-count" -> adapter.setReconnectionRetryCount(value);
                case "reconnection-retry-interval" -> adapter.setReconnectionRetryInterval(value);
                case "startup-retry-count" -> adapter.setStartupRetryCount(value);
                case "startup-retry-interval" -> adapter.setStartupRetryInterval(value);
                case "support-mq-extensions" -> adapter.setSupportMQExtensions(value);
                case "trace-enabled" -> adapter.setTraceEnabled(value);
                case "trace-level" -> adapter.setTraceLevel(value);
            }
        }
        return new ResourceAdapterWrapper(adapter, config);
    }

    @Override
    public ManagedConnectionFactory createManagedConnectionFactory(String id, ResourceAdapter adapter) {
        ResourceAdapterWrapper wrapper = (ResourceAdapterWrapper) adapter;
        ManagedConnectionFactoryImpl factory = new ManagedConnectionFactoryImpl();
        factory.setResourceAdapter(wrapper.delegate);
        Map<String, String> config = new HashMap<>(wrapper.config);
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "application-name" -> factory.setApplicationName(value);
                case "arbitrary-properties" -> factory.setArbitraryProperties(value);
                case "broker-cc-sub-queue" -> factory.setBrokerCCSubQueue(value);
                case "broker-control-queue" -> factory.setBrokerControlQueue(value);
                case "broker-pub-queue" -> factory.setBrokerPubQueue(value);
                case "broker-queue-manager" -> factory.setBrokerQueueManager(value);
                case "broker-sub-queue" -> factory.setBrokerSubQueue(value);
                case "broker-version" -> factory.setBrokerVersion(value);
                case "ccdt-url" -> factory.setCcdtURL(value);
                case "ccsid" -> factory.setCCSID(value);
                case "channel" -> factory.setChannel(value);
                case "cleanup-interval" -> factory.setCleanupInterval(value);
                case "cleanup-level" -> factory.setCleanupLevel(value);
                case "client-id" -> factory.setClientId(value);
                case "clone-support" -> factory.setCloneSupport(value);
                case "connection-name-list" -> factory.setConnectionNameList(value);
                case "fail-if-quiesce" -> factory.setFailIfQuiesce(value);
                case "header-compression" -> factory.setHeaderCompression(value);
                case "host-name" -> factory.setHostName(value);
                case "local-address" -> factory.setLocalAddress(value);
                case "message-compression" -> factory.setMessageCompression(value);
                case "message-selection" -> factory.setMessageSelection(value);
                case "password" -> factory.setPassword(value);
                case "polling-interval" -> factory.setPollingInterval(value);
                case "port" -> factory.setPort(value);
                case "provider-version" -> factory.setProviderVersion(value);
                case "pub-ack-interval" -> factory.setPubAckInterval(value);
                case "put-async-allowed" -> factory.setPutAsyncAllowed(value);
                case "queue-manager" -> factory.setQueueManager(value);
                case "read-ahead-allowed" -> factory.setReadAheadAllowed(value);
                case "receive-exit" -> factory.setReceiveExit(value);
                case "receive-exit-init" -> factory.setReceiveExitInit(value);
                case "rescan-interval" -> factory.setRescanInterval(value);
                case "security-exit" -> factory.setSecurityExit(value);
                case "security-exit-init" -> factory.setSecurityExitInit(value);
                case "send-check-count" -> factory.setSendCheckCount(value);
                case "send-exit" -> factory.setSendExit(value);
                case "send-exit-init" -> factory.setSendExitInit(value);
                case "share-conv-allowed" -> factory.setShareConvAllowed(value);
                case "sparse-subscriptions" -> factory.setSparseSubscriptions(value);
                case "ssl-cert-stores" -> factory.setSslCertStores(value);
                case "ssl-cipher-suite" -> factory.setSslCipherSuite(value);
                case "ssl-fips-required" -> factory.setSslFipsRequired(value);
                case "ssl-peer-name" -> factory.setSslPeerName(value);
                case "ssl-reset-count" -> factory.setSslResetCount(value);
                case "ssl-socket-factory" -> factory.setSslSocketFactory(value);
                case "status-refresh-interval" -> factory.setStatusRefreshInterval(value);
                case "subscription-store" -> factory.setSubscriptionStore(value);
                case "target-client-matching" -> factory.setTargetClientMatching(value);
                case "temp-q-prefix" -> factory.setTempQPrefix(value);
                case "temp-topic-prefix" -> factory.setTempTopicPrefix(value);
                case "temporary-model" -> factory.setTemporaryModel(value);
                case "transport-type" -> factory.setTransportType(value);
                case "username" -> factory.setUserName(value);
                case "wildcard-format" -> factory.setWildcardFormat(value);
                default -> log.warnf("Unknown property: %s", key);
            }
        }
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
        for (Map.Entry<String, String> entry : mergedConfig.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "acknowledge-mode" -> activationSpec.setAcknowledgeMode(value);
                case "application-name" -> activationSpec.setApplicationName(value);
                case "arbitrary-properties" -> activationSpec.setArbitraryProperties(value);
                case "ccdt-url" -> activationSpec.setCcdtURL(value);
                case "ccsid" -> activationSpec.setCCSID(value);
                case "channel" -> activationSpec.setChannel(value);
                case "client-id" -> activationSpec.setClientId(value);
                case "connection-name-list" -> activationSpec.setConnectionNameList(value);
                case "destination" -> activationSpec.setDestination(value);
                case "destination-type" -> activationSpec.setDestinationType(value);
                case "dynamically-balanced" -> activationSpec.setDynamicallyBalanced(value);
                case "header-compression" -> activationSpec.setHeaderCompression(value);
                case "host-name" -> activationSpec.setHostName(value);
                case "local-address" -> activationSpec.setLocalAddress(value);
                case "max-sequential-delivery-failures" ->
                    activationSpec.setMaxSequentialDeliveryFailures(Integer.parseInt(value));
                case "message-compression" -> activationSpec.setMessageCompression(value);
                case "message-selection" -> activationSpec.setMessageSelection(value);
                case "password" -> activationSpec.setPassword(value);
                case "polling-interval" -> activationSpec.setPollingInterval(value);
                case "port" -> activationSpec.setPort(value);
                case "provider-version" -> activationSpec.setProviderVersion(value);
                case "queue-manager" -> activationSpec.setQueueManager(value);
                case "receive-exit" -> activationSpec.setReceiveExit(value);
                case "receive-exit-init" -> activationSpec.setReceiveExitInit(value);
                case "rescan-interval" -> activationSpec.setRescanInterval(value);
                case "security-exit" -> activationSpec.setSecurityExit(value);
                case "security-exit-init" -> activationSpec.setSecurityExitInit(value);
                case "send-exit" -> activationSpec.setSendExit(value);
                case "send-exit-init" -> activationSpec.setSendExitInit(value);
                case "share-conv-allowed" -> activationSpec.setShareConvAllowed(value);
                case "sparse-subscriptions" -> activationSpec.setSparseSubscriptions(value);
                case "ssl-cert-stores" -> activationSpec.setSslCertStores(value);
                case "ssl-cipher-suite" -> activationSpec.setSslCipherSuite(value);
                case "ssl-fips-required" -> activationSpec.setSslFipsRequired(value);
                case "ssl-peer-name" -> activationSpec.setSslPeerName(value);
                case "ssl-reset-count" -> activationSpec.setSslResetCount(value);
                case "ssl-socket-factory" -> activationSpec.setSslSocketFactory(value);
                case "status-refresh-interval" -> activationSpec.setStatusRefreshInterval(value);
                case "subscription-store" -> activationSpec.setSubscriptionStore(value);
                case "target-client-matching" -> activationSpec.setTargetClientMatching(value);
                case "transport-type" -> activationSpec.setTransportType(value);
                case "username" -> activationSpec.setUserName(value);
                case "wildcard-format" -> activationSpec.setWildcardFormat(value);
            }
        }
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
