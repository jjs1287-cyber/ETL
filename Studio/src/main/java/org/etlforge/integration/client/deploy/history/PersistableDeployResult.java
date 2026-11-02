package org.etlforge.integration.client.deploy.history;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Date;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.exception.ExceptionPersistor;
import org.etlforge.util.persist.Persistable;
import org.etlforge.util.persist.PersistableFactory;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class PersistableDeployResult implements Persistable, DeployResult {
   private static final String ID = "ResultId";
   private static final String TIMESTAMP = "Time";
   private static final String SERVER_NAME = "ServerName";
   private static final String SERVER_ID = "ServerId";
   private static final String SERVER_VERSION = "ServerVersion";
   private static final String USER = "User";
   private static final String DATA_ROOT = "DataRoot";
   private static final String VALUE = "Value";
   private static final String GLOBAL_WARNING = "GlobalWarning";
   private static final String ITEM_WARNING = "ItemWarning";
   private static final String ENTITY_TYPE = "EntityType";
   private static final String ENTITY_ID = "EntityId";
   private final String id;
   private final long timestamp;
   private final ServerInfo server;
   private final String user;
   private final DeployedEntityDescriptor deployedDataRoot;
   private final Exception cause;
   private final ImmutableSet<String> globalWarnings;
   private final ImmutableSet<EntityWarning> entityWarnings;

   public PersistableDeployResult(DeployResult var1) {
      this(
         var1.getId(),
         var1.getTimestamp(),
         var1.getServer(),
         var1.getUser(),
         var1.getDeployedDataRoot(),
         var1.getCause(),
         var1.getGlobalWarnings(),
         var1.getEntityWarnings()
      );
   }

   public PersistableDeployResult(
      String var1,
      Date var2,
      ServerInfo var3,
      String var4,
      DeployedEntityDescriptor var5,
      Exception var6,
      ImmutableSet<String> var7,
      ImmutableSet<EntityWarning> var8
   ) {
      this.id = var1;
      this.timestamp = var2.getTime();
      this.server = var3;
      this.user = var4;
      this.deployedDataRoot = var5;
      this.cause = var6;
      this.globalWarnings = var7;
      this.entityWarnings = var8;
   }

   @Override
   public final String getId() {
      return this.id;
   }

   public String getPersistName() {
      if (this.cause != null) {
         return "DeployFailure";
      } else {
         return this.hasWarnings() ? "DeployWarnings" : "DeploySuccess";
      }
   }

   private boolean hasWarnings() {
      return !this.globalWarnings.isEmpty() || !this.entityWarnings.isEmpty();
   }

   @Override
   public void save(Persistor var1) {
      var1.putString("ResultId", this.id);
      var1.putLong("Time", this.timestamp);
      var1.putString("ServerName", this.server.getName());
      var1.putString("ServerId", this.server.getGuid().toString());
      var1.putString("ServerVersion", this.server.getVersion().toString());
      var1.putString("User", this.user);
      this.saveDataTree(var1);
      if (this.cause != null) {
         ExceptionPersistor.persist(this.cause, var1);
      }

      this.saveGlobalWarnings(var1);
      this.saveEntityWarnings(var1);
   }

   private void saveDataTree(Persistor var1) {
      DeployedEntityDescriptorPersistor var2 = new DeployedEntityDescriptorPersistor();
      var2.persist(this.deployedDataRoot, var1.createChild("DataRoot"));
   }

   private void saveGlobalWarnings(Persistor var1) {
      for (String var3 : this.globalWarnings) {
         Persistor var4 = var1.createChild("GlobalWarning");
         var4.putString("Value", var3);
      }
   }

   private void saveEntityWarnings(Persistor var1) {
      for (EntityWarning var3 : this.entityWarnings) {
         Persistor var4 = var1.createChild("ItemWarning");
         this.saveEntityId(var3.getIntegrationEntityId(), var4);
         var4.putString("Value", var3.getWarning());
      }
   }

   private void saveEntityId(IntegrationEntityId var1, Persistor var2) {
      var2.putString("EntityType", var1.getEntityType().name());
      var2.putString("EntityId", var1.toString());
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
   public final DeployedEntityDescriptor getDeployedDataRoot() {
      return this.deployedDataRoot;
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

   public static final class Factory implements PersistableFactory<PersistableDeployResult> {
      public PersistableDeployResult restore(Persistor var1) {
         String var2 = this.restoreId(var1);
         Date var3 = this.restoreTimestamp(var1);
         ServerInfo var4 = this.restoreServerInfo(var1);
         String var5 = this.restoreUser(var1);
         DeployedEntityDescriptor var6 = this.restoreDataRoot(var1);
         Optional var7 = ExceptionPersistor.restore(var1);
         ImmutableSet var8 = this.restoreGlobalWarnings(var1);
         ImmutableSet var9 = this.restoreItemWarnings(var1);
         return new PersistableDeployResult(var2, var3, var4, var5, var6, (Exception)var7.orNull(), var8, var9);
      }

      private Date restoreTimestamp(Persistor var1) {
         return new Date(var1.getLong("Time"));
      }

      private String restoreId(Persistor var1) {
         return var1.getString("ResultId");
      }

      private String restoreUser(Persistor var1) {
         String var2 = var1.getString("User");
         return var2 != null ? var2 : "";
      }

      private ServerInfo restoreServerInfo(Persistor var1) {
         String var2 = var1.getString("ServerName");
         String var3 = var1.getString("ServerId");
         Version var4 = null;

         try {
            var4 = VersionFactory.fromString(var1.getString("ServerVersion"));
         } catch (IllegalVersionException var6) {
            var6.printStackTrace();
            var4 = Version.UNKNOWN;
         }

         return new ServerInfo(new ServerGuid(var3), var2, var4);
      }

      private DeployedEntityDescriptor restoreDataRoot(Persistor var1) {
         Persistor var2 = var1.getFirstChild("DataRoot");
         Preconditions.checkArgument(var2 != null, "Invalid persistor");
         return new DeployedEntityDescriptorPersistor().restore(var2);
      }

      private ImmutableSet<String> restoreGlobalWarnings(Persistor var1) {
         return FluentIterable.from(var1.getChildren("GlobalWarning")).transform(new Function<Persistor, String>() {
            public String apply(Persistor var1) {
               return var1.getString("Value");
            }
         }).toImmutableSet();
      }

      private ImmutableSet<EntityWarning> restoreItemWarnings(Persistor var1) {
         Builder var2 = ImmutableSet.builder();

         for (Persistor var4 : var1.getChildren("ItemWarning")) {
            final IntegrationEntityId var5 = this.restoreEntityId(var4);
            final String var6 = var4.getString("Value");
            var2.add(new EntityWarning() {
               @Override
               public IntegrationEntityId getIntegrationEntityId() {
                  return var5;
               }

               @Override
               public String getWarning() {
                  return var6;
               }
            });
         }

         return var2.build();
      }

      private IntegrationEntityId restoreEntityId(Persistor var1) {
         String var2 = var1.getString("EntityType");
         if (!var2.equals("User") && !var2.equals("Group")) {
            EntityType var3;
            if (var2.equals("InterchangeProject")) {
               var3 = EntityType.Project;
            } else {
               var3 = EntityType.valueOf(var2);
            }

            return var3.getIntegrationId(var1.getString("EntityId"));
         } else {
            return null;
         }
      }
   }
}
