package org.etlforge.integration.client.deploy.serverapi;

import com.google.common.collect.ImmutableSet;
import java.util.Date;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.client.server.ServerInfo;

public interface DeployResult {
   String getId();

   Date getTimestamp();

   ServerInfo getServer();

   String getUser();

   DeployedEntityDescriptor getDeployedDataRoot();

   Exception getCause();

   ImmutableSet<String> getGlobalWarnings();

   ImmutableSet<EntityWarning> getEntityWarnings();
}
