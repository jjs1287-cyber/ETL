package org.etlforge.integration.server.engine;

import java.sql.Connection;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;

public abstract class EngineSession<S extends EngineSession> {
   private final EngineSessionId<S> id;
   private final ExternalApiSupport support;
   private volatile boolean active;

   protected EngineSession(EngineSessionId<S> var1, ExternalApiSupport var2) {
      this.id = var1;
      this.support = var2;
   }

   public EngineSessionId<S> getId() {
      return this.id;
   }

   public final void dispose() {
      try {
         this.preCloseConnectionCleanup();
      } finally {
         try {
            this.support.dispose();
         } finally {
            this.postCloseConnectionCleanup();
         }
      }
   }

   protected void preCloseConnectionCleanup() {
   }

   protected void postCloseConnectionCleanup() {
   }

   public final boolean isActive() {
      return this.active;
   }

   public final void setActive(boolean var1) {
      this.active = var1;
   }

   public final void verifyCredentials(String var1, String var2) throws EngineSessionException {
      if (!this.support.isSameCredentials(var1, var2)) {
         throw new EngineSessionException(
            "The requested session was created with different credentials than the ones provided here: [user: " + var1 + ", pwd: *REMOVED*]"
         );
      }
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 != null && var1.getClass().equals(this.getClass()) ? this.id.equals(((EngineSession)var1).id) : false;
      }
   }

   @Override
   public final int hashCode() {
      return this.id.hashCode();
   }

   protected final void closeBackendConnection() {
      this.support.closeConnectionToBackendDatabase();
   }

   protected final void verifyPermission(IntegrationEntityId var1, Permission var2) throws ServerAccessException {
      this.support.verifyPermission(var1, Permission.EXECUTE);
   }

   public final Connection getBackendConnection() throws ServerAccessException {
      return this.support.getTranDbConnection();
   }
}
