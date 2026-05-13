package io.quarkiverse.ibm.mq.deployment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedClassBuildItem;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.maven.dependency.ResolvedDependency;

/**
 * Extracts the {@code com.ibm.mq.jakarta.connector.jar} from the
 * {@code com.ibm.mq:wmq.jakarta.jmsra} RAR artifact at Quarkus augmentation time.
 * <p>
 * IBM does not publish {@code com.ibm.mq:com.ibm.mq.jakarta.connector} as a standalone
 * JAR on Maven Central. The connector classes are embedded inside the
 * {@code wmq.jakarta.jmsra.rar} archive. This build step transparently extracts them
 * so that consumers of the extension never need to manually unpack the RAR.
 *
 * @see <a href="https://github.com/ibm-messaging/mq-container/issues/591">
 *      Request to publish com.ibm.mq.jakarta.connector.jar on Maven Central</a>
 */
class IbmMqRarExtractorProcessor {

    private static final String RAR_GROUP_ID = "com.ibm.mq";
    private static final String RAR_ARTIFACT_ID = "wmq.jakarta.jmsra";
    private static final String CONNECTOR_JAR_NAME = "com.ibm.mq.jakarta.connector.jar";
    private static final Logger log = LoggerFactory.getLogger(IbmMqRarExtractorProcessor.class);

    @BuildStep
    void extractConnectorJarFromRar(
            CurateOutcomeBuildItem curateOutcome,
            BuildProducer<GeneratedClassBuildItem> generatedClasses,
            BuildProducer<GeneratedResourceBuildItem> generatedResources) throws IOException {

        Path rarPath = findRarPath(curateOutcome);
        if (rarPath == null) {
            throw new IllegalStateException(
                    "Could not find resolved artifact " + RAR_GROUP_ID + ":" + RAR_ARTIFACT_ID
                            + " (type=rar) in the dependency tree. "
                            + "Make sure it is declared as a dependency in the deployment module.");
        }

        try (ZipFile rar = new ZipFile(rarPath.toFile())) {
            ZipEntry connectorEntry = rar.getEntry(CONNECTOR_JAR_NAME);
            if (connectorEntry == null) {
                throw new IllegalStateException(
                        "Could not find " + CONNECTOR_JAR_NAME + " inside " + rarPath
                                + ". The RAR structure may have changed in this IBM MQ version.");
            }

            try (InputStream rarStream = rar.getInputStream(connectorEntry);
                    JarInputStream jarStream = new JarInputStream(rarStream)) {

                JarEntry jarEntry;
                while ((jarEntry = jarStream.getNextJarEntry()) != null) {
                    String entryName = jarEntry.getName();
                    if (jarEntry.isDirectory() || entryName.startsWith("META-INF/")
                            || entryName.contains("..")) {
                        continue;
                    }

                    byte[] data = jarStream.readAllBytes();
                    if (entryName.endsWith(".class")) {
                        String className = entryName
                                .substring(0, entryName.length() - ".class".length())
                                .replace('/', '.');
                        log.debug("Extracting class {}", className);
                        generatedClasses.produce(
                                new GeneratedClassBuildItem(false, className, data));
                    } else {
                        log.debug("Extracting resource {}", entryName);
                        generatedResources.produce(
                                new GeneratedResourceBuildItem(entryName, data));
                    }
                }
            }
        }
    }

    /**
     * Walks the resolved dependencies from the Quarkus application model and returns
     * the local filesystem path of the RAR artifact.
     */
    private Path findRarPath(CurateOutcomeBuildItem curateOutcome) {
        for (ResolvedDependency dep : curateOutcome.getApplicationModel().getDependencies()) {
            if (RAR_GROUP_ID.equals(dep.getGroupId())
                    && RAR_ARTIFACT_ID.equals(dep.getArtifactId())) {
                return dep.getResolvedPaths().iterator().next();
            }
        }
        return null;
    }
}
