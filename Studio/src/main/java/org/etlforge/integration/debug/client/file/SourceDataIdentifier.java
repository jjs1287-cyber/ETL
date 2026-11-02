package org.etlforge.integration.debug.client.file;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.util.id.KongaGuid;

public final class SourceDataIdentifier implements Serializable {
   private static final KongaGuid NO_SOURCE = KongaGuid.createGuid("b1dd59ca-4484-489e-bbf1-b5acaecbec3d");
   private final ServerInfo serverInfo;
   private final KongaGuid guid;

   public static SourceDataIdentifier noSource(ServerInfo var0) {
      return new SourceDataIdentifier(var0, NO_SOURCE);
   }

   public SourceDataIdentifier(ServerInfo var1, KongaGuid var2) {
      Preconditions.checkNotNull(var1, "serverInfo");
      Preconditions.checkNotNull(var2, "guid");
      this.serverInfo = var1;
      this.guid = var2;
   }

   public KongaGuid getGuid() {
      return this.guid;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   public ServerGuid getServerId() {
      return this.serverInfo.getGuid();
   }

   public String getServerName() {
      return this.serverInfo.getName();
   }

   public boolean isNoSource() {
      return this.guid.equals(NO_SOURCE);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.guid, this.getServerId());
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SourceDataIdentifier)) {
         return false;
      }

      SourceDataIdentifier var2 = (SourceDataIdentifier)var1;
      return this.guid.equals(var2.guid) && this.getServerId().equals(var2.getServerId());
   }

   @Override
   public String toString() {
      return this.guid + " @ " + this.getServerName();
   }
}
