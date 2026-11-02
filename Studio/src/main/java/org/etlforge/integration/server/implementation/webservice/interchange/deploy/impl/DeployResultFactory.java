package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.collect.ImmutableSet;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.id.KongaGuid;

final class DeployResultFactory {
   private final IntegrationProject project;
   private final ServerInfo server;
   private final String user;

   public DeployResultFactory(IntegrationProject var1, ServerInfo var2, String var3) {
      this.project = var1;
      this.server = var2;
      this.user = var3;
   }

   public DeployResult failure(Exception var1) {
      return new DeployResultFactory.DeployResultImpl(this.project, this.server, this.user, var1, ImmutableSet.of(), ImmutableSet.of());
   }

   public DeployResult success(List<String> var1, List<EntityWarning> var2) {
      return new DeployResultFactory.DeployResultImpl(this.project, this.server, this.user, null, ImmutableSet.copyOf(var1), ImmutableSet.copyOf(var2));
   }

   private static final class DeployResultImpl implements DeployResult {
      private final String id = KongaGuid.createGuid().toString();
      private final long timestamp = System.currentTimeMillis();
      private final ServerInfo server;
      private final String user;
      private final DeployedEntityDescriptor dataRoot;
      private final Exception cause;
      private final ImmutableSet<String> globalWarnings;
      private final ImmutableSet<EntityWarning> entityWarnings;

      public DeployResultImpl(
         IntegrationProject var1, ServerInfo var2, String var3, Exception var4, ImmutableSet<String> var5, ImmutableSet<EntityWarning> var6
      ) {
         this.server = var2;
         this.user = var3;
         this.dataRoot = new DeployedEntityDescriptorFactory().create(var1);
         this.cause = var4;
         this.globalWarnings = var5;
         this.entityWarnings = var6;
      }

      @Override
      public String getId() {
         return this.id;
      }

      @Override
      public final ServerInfo getServer() {
         return this.server;
      }

      @Override
      public final String getUser() {
         return this.user;
      }

      @Override
      public final Date getTimestamp() {
         return new Date(this.timestamp);
      }

      @Override
      public DeployedEntityDescriptor getDeployedDataRoot() {
         return this.dataRoot;
      }

      @Override
      public Exception getCause() {
         return this.cause;
      }

      @Override
      public ImmutableSet<String> getGlobalWarnings() {
         return this.globalWarnings;
      }

      @Override
      public ImmutableSet<EntityWarning> getEntityWarnings() {
         return this.entityWarnings;
      }
   }
}
