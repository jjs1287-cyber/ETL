package org.etlforge.integration.server.implementation.webservice.admin.project.impl;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

class FileToDownload {
   private final IntegrationEntityId id;
   private final String clientPath;

   FileToDownload(IntegrationEntityId var1, String var2) {
      this.id = var1;
      this.clientPath = var2;
   }

   EntityType getEntityType() {
      return this.id.getEntityType();
   }

   IntegrationEntityId getIntegrationEntityId() {
      return this.id;
   }

   String getClientPath() {
      return this.clientPath;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.id, this.clientPath);
   }

   @Override
   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FileToDownload)) {
         return false;
      }

      FileToDownload var2 = (FileToDownload)var1;
      return this.id.equals(var2.id) && this.clientPath.equals(var2.clientPath);
   }

   @Override
   public String toString() {
      return this.clientPath;
   }
}
