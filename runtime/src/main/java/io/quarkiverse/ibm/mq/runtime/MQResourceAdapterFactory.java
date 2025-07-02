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
    public ResourceAdapter createResourceAdapter(String id, Map<String, String> config) {
        ResourceAdapterImpl adapter = new ResourceAdapterImpl();
        if (config.containsKey("log-writer-enabled"))
            adapter.setLogWriterEnabled(config.get("log-writer-enabled"));
        if (config.containsKey("max-connections"))
            adapter.setMaxConnections(config.get("max-connections"));
        if (config.containsKey("native-library-path"))
            adapter.setNativeLibraryPath(config.get("native-library-path"));
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
        factory.setResourceAdapter(wrapper.delegate);
        Map<String, String> config = new HashMap<>(wrapper.config);
        if (config.containsKey("application-name"))
            factory.setApplicationName(config.get("application-name"));
        if (config.containsKey("arbitrary-properties"))
            factory.setArbitraryProperties(config.get("arbitrary-properties"));
        if (config.containsKey("broker-cc-sub-queue"))
            factory.setBrokerCCSubQueue(config.get("broker-cc-sub-queue"));
        if (config.containsKey("broker-control-queue"))
            factory.setBrokerControlQueue(config.get("broker-control-queue"));
        if (config.containsKey("broker-pub-queue"))
            factory.setBrokerPubQueue(config.get("broker-pub-queue"));
        if (config.containsKey("broker-queue-manager"))
            factory.setBrokerQueueManager(config.get("broker-queue-manager"));
        if (config.containsKey("broker-sub-queue"))
            factory.setBrokerSubQueue(config.get("broker-sub-queue"));
        if (config.containsKey("broker-version"))
            factory.setBrokerVersion(config.get("broker-version"));
        if (config.containsKey("ccdt-url"))
            factory.setCcdtURL(config.get("ccdt-url"));
        if (config.containsKey("ccsid"))
            factory.setCCSID(config.get("ccsid"));
        if (config.containsKey("channel"))
            factory.setChannel(config.get("channel"));
        if (config.containsKey("cleanup-interval"))
            factory.setCleanupInterval(config.get("cleanup-interval"));
        if (config.containsKey("cleanup-level"))
            factory.setCleanupLevel(config.get("cleanup-level"));
        if (config.containsKey("client-id"))
            factory.setClientId(config.get("client-id"));
        if (config.containsKey("clone-support"))
            factory.setCloneSupport(config.get("clone-support"));
        if (config.containsKey("connection-name-list"))
            factory.setConnectionNameList(config.get("connection-name-list"));
        if (config.containsKey("fail-if-quiesce"))
            factory.setFailIfQuiesce(config.get("fail-if-quiesce"));
        if (config.containsKey("header-compression"))
            factory.setHeaderCompression(config.get("header-compression"));
        if (config.containsKey("host-name"))
            factory.setHostName(config.get("host-name"));
        if (config.containsKey("local-address"))
            factory.setLocalAddress(config.get("local-address"));
        if (config.containsKey("message-compression"))
            factory.setMessageCompression(config.get("message-compression"));
        if (config.containsKey("message-selection"))
            factory.setMessageSelection(config.get("message-selection"));
        if (config.containsKey("password"))
            factory.setPassword(config.get("password"));
        if (config.containsKey("polling-interval"))
            factory.setPollingInterval(config.get("polling-interval"));
        if (config.containsKey("port"))
            factory.setPort(config.get("port"));
        if (config.containsKey("provider-version"))
            factory.setProviderVersion(config.get("provider-version"));
        if (config.containsKey("pub-ack-interval"))
            factory.setPubAckInterval(config.get("pub-ack-interval"));
        if (config.containsKey("put-async-allowed"))
            factory.setPutAsyncAllowed(config.get("put-async-allowed"));
        if (config.containsKey("queue-manager"))
            factory.setQueueManager(config.get("queue-manager"));
        if (config.containsKey("read-ahead-allowed"))
            factory.setReadAheadAllowed(config.get("read-ahead-allowed"));
        if (config.containsKey("receive-exit"))
            factory.setReceiveExit(config.get("receive-exit"));
        if (config.containsKey("receive-exit-init"))
            factory.setReceiveExitInit(config.get("receive-exit-init"));
        if (config.containsKey("rescan-interval"))
            factory.setRescanInterval(config.get("rescan-interval"));
        if (config.containsKey("security-exit"))
            factory.setSecurityExit(config.get("security-exit"));
        if (config.containsKey("security-exit-init"))
            factory.setSecurityExitInit(config.get("security-exit-init"));
        if (config.containsKey("send-check-count"))
            factory.setSendCheckCount(config.get("send-check-count"));
        if (config.containsKey("send-exit"))
            factory.setSendExit(config.get("send-exit"));
        if (config.containsKey("send-exit-init"))
            factory.setSendExitInit(config.get("send-exit-init"));
        if (config.containsKey("share-conv-allowed"))
            factory.setShareConvAllowed(config.get("share-conv-allowed"));
        if (config.containsKey("sparse-subscriptions"))
            factory.setSparseSubscriptions(config.get("sparse-subscriptions"));
        if (config.containsKey("ssl-cert-stores"))
            factory.setSslCertStores(config.get("ssl-cert-stores"));
        if (config.containsKey("ssl-cipher-suite"))
            factory.setSslCipherSuite(config.get("ssl-cipher-suite"));
        if (config.containsKey("ssl-fips-required"))
            factory.setSslFipsRequired(config.get("ssl-fips-required"));
        if (config.containsKey("ssl-peer-name"))
            factory.setSslPeerName(config.get("ssl-peer-name"));
        if (config.containsKey("ssl-reset-count"))
            factory.setSslResetCount(config.get("ssl-reset-count"));
        if (config.containsKey("ssl-socket-factory"))
            factory.setSslSocketFactory(config.get("ssl-socket-factory"));
        if (config.containsKey("status-refresh-interval"))
            factory.setStatusRefreshInterval(config.get("status-refresh-interval"));
        if (config.containsKey("subscription-store"))
            factory.setSubscriptionStore(config.get("subscription-store"));
        if (config.containsKey("target-client-matching"))
            factory.setTargetClientMatching(config.get("target-client-matching"));
        if (config.containsKey("temp-q-prefix"))
            factory.setTempQPrefix(config.get("temp-q-prefix"));
        if (config.containsKey("temp-topic-prefix"))
            factory.setTempTopicPrefix(config.get("temp-topic-prefix"));
        if (config.containsKey("temporary-model"))
            factory.setTemporaryModel(config.get("temporary-model"));
        if (config.containsKey("transport-type"))
            factory.setTransportType(config.get("transport-type"));
        if (config.containsKey("username"))
            factory.setUserName(config.get("username"));
        if (config.containsKey("wildcard-format"))
            factory.setWildcardFormat(config.get("wildcard-format"));
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

        if (config.containsKey("acknowledge-mode"))
            activationSpec.setAcknowledgeMode(config.get("acknowledge-mode"));
        if (mergedConfig.containsKey("application-name"))
            activationSpec.setApplicationName(mergedConfig.get("application-name"));
        if (config.containsKey("arbitrary-properties"))
            activationSpec.setArbitraryProperties(config.get("arbitrary-properties"));
        if (mergedConfig.containsKey("ccdt-url"))
            activationSpec.setCcdtURL(mergedConfig.get("ccdt-url"));
        if (mergedConfig.containsKey("ccsid"))
            activationSpec.setCCSID(mergedConfig.get("ccsid"));
        if (mergedConfig.containsKey("channel"))
            activationSpec.setChannel(mergedConfig.get("channel"));
        if (mergedConfig.containsKey("client-id"))
            activationSpec.setClientId(mergedConfig.get("client-id"));
        if (mergedConfig.containsKey("connection-name-list"))
            activationSpec.setConnectionNameList(mergedConfig.get("connection-name-list"));
        if (config.containsKey("destination"))
            activationSpec.setDestination(config.get("destination"));
        if (config.containsKey("destination-type"))
            activationSpec.setDestinationType(config.get("destination-type"));
        if (config.containsKey("dynamically-balanced"))
            activationSpec.setDynamicallyBalanced(mergedConfig.get("dynamically-balanced"));
        if (mergedConfig.containsKey("header-compression"))
            activationSpec.setHeaderCompression(mergedConfig.get("header-compression"));
        if (mergedConfig.containsKey("host-name"))
            activationSpec.setHostName(mergedConfig.get("host-name"));
        if (mergedConfig.containsKey("local-address"))
            activationSpec.setLocalAddress(mergedConfig.get("local-address"));
        if (config.containsKey("max-sequential-delivery-failures"))
            activationSpec.setMaxSequentialDeliveryFailures(Integer.parseInt(config.get("max-sequential-delivery-failures")));
        if (mergedConfig.containsKey("message-compression"))
            activationSpec.setMessageCompression(mergedConfig.get("message-compression"));
        if (mergedConfig.containsKey("message-selection"))
            activationSpec.setMessageSelection(mergedConfig.get("message-selection"));
        if (mergedConfig.containsKey("password"))
            activationSpec.setPassword(mergedConfig.get("password"));
        if (mergedConfig.containsKey("polling-interval"))
            activationSpec.setPollingInterval(mergedConfig.get("polling-interval"));
        if (mergedConfig.containsKey("port"))
            activationSpec.setPort(mergedConfig.get("port"));
        if (mergedConfig.containsKey("provider-version"))
            activationSpec.setProviderVersion(mergedConfig.get("provider-version"));
        if (mergedConfig.containsKey("queue-manager"))
            activationSpec.setQueueManager(mergedConfig.get("queue-manager"));
        if (mergedConfig.containsKey("receive-exit"))
            activationSpec.setReceiveExit(mergedConfig.get("receive-exit"));
        if (mergedConfig.containsKey("receive-exit-init"))
            activationSpec.setReceiveExitInit(mergedConfig.get("receive-exit-init"));
        if (mergedConfig.containsKey("security-exit"))
            activationSpec.setSecurityExit(mergedConfig.get("security-exit"));
        if (mergedConfig.containsKey("security-exit-init"))
            activationSpec.setSecurityExitInit(mergedConfig.get("security-exit-init"));
        if (mergedConfig.containsKey("send-exit"))
            activationSpec.setSendExit(mergedConfig.get("send-exit"));
        if (mergedConfig.containsKey("send-exit-init"))
            activationSpec.setSendExitInit(mergedConfig.get("send-exit-init"));
        if (mergedConfig.containsKey("ssl-cert-stores"))
            activationSpec.setSslCertStores(mergedConfig.get("ssl-cert-stores"));
        if (mergedConfig.containsKey("ssl-cipher-suite"))
            activationSpec.setSslCipherSuite(mergedConfig.get("ssl-cipher-suite"));
        if (mergedConfig.containsKey("ssl-fips-required"))
            activationSpec.setSslFipsRequired(mergedConfig.get("ssl-fips-required"));
        if (mergedConfig.containsKey("ssl-peer-name"))
            activationSpec.setSslPeerName(mergedConfig.get("ssl-peer-name"));
        if (mergedConfig.containsKey("ssl-reset-count"))
            activationSpec.setSslResetCount(mergedConfig.get("ssl-reset-count"));
        if (mergedConfig.containsKey("ssl-socket-factory"))
            activationSpec.setSslSocketFactory(mergedConfig.get("ssl-socket-factory"));
        if (mergedConfig.containsKey("transport-type"))
            activationSpec.setTransportType(mergedConfig.get("transport-type"));
        if (mergedConfig.containsKey("username"))
            activationSpec.setUserName(mergedConfig.get("username"));

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
