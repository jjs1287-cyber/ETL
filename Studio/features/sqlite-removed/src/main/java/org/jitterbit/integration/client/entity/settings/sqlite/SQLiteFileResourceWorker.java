package org.jitterbit.integration.client.entity.settings.sqlite;

import org.jitterbit.integration.data.entity.IntegrationEntity;
import org.jitterbit.integration.data.entity.IntegrationEntityId;

final class SQLiteFileResourceWorker {
   private final FileResourcesTable table;
   private final IntegrationEntityId entityId;
   private final String resourceName;
   private boolean deleted;

   public SQLiteFileResourceWorker(FileResourcesTable var1, IntegrationEntity var2, String var3) {
      this.table = var1;
      this.entityId = var2.getID();
      this.resourceName = var3;
   }

   public boolean exists() {
      return this.deleted ? false : this.table.exists(this.entityId, this.resourceName);
   }

   public String read() {
      return this.deleted ? null : this.table.read(this.entityId, this.resourceName);
   }

   public void write(String var1) {
      if (!this.deleted) {
         this.table.write(this.entityId, this.resourceName, var1);
      }
   }

   public void delete() {
      if (!this.deleted) {
         this.deleted = this.table.delete(this.entityId, this.resourceName);
      }
   }
}
