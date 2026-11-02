package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import org.etlforge.util.string.StringLimiter;

public class AutoDeskConnectorObject implements ConnectorObject {
   private final AutoDeskConnectorWorkspace workspace;
   private String key;
   private String description;

   public AutoDeskConnectorObject(String var1) {
      this(var1, null);
   }

   public AutoDeskConnectorObject(String var1, @Nullable String var2) {
      this.workspace = AutoDeskConnectorWorkspace.fromWorkspaceKey(var1);
      this.update();
   }

   private void update() {
      this.key = this.workspace.toWorkspaceKey();
      this.description = this.key;
   }

   public String getWorkspaceId() {
      return this.workspace.getWorkspaceID();
   }

   public void setWorkspaceId(Integer var1) {
      this.workspace.setWorkspaceID(var1.toString());
      this.update();
   }

   public static AutoDeskConnectorObject castAsAutoDeskConnectorObject(ConnectorObject var0) {
      if (var0 instanceof AutoDeskConnectorObject) {
         return (AutoDeskConnectorObject)var0;
      } else {
         throw new RuntimeException(var0 + " cannot be casted as a autodesk object");
      }
   }

   @Override
   public String getName() {
      return this.key;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.key, this.description);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof AutoDeskConnectorObject)) {
         return false;
      }

      AutoDeskConnectorObject var2 = (AutoDeskConnectorObject)var1;
      return this.key.equals(var2.key) && this.description.equals(var2.description);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("key", this.key).add("description", StringLimiter.limit(this.description, 100)).toString();
   }
}
