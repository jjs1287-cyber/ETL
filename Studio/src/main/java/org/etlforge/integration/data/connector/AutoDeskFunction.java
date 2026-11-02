package org.etlforge.integration.data.connector;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nullable;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.AutoDeskEndpointId;
import org.etlforge.integration.etlforgeconnector.AutoDeskConnectorWorkspace;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public abstract class AutoDeskFunction extends AbstractConnectorCall<AutoDeskEndpoint, AutoDeskEndpointId> {
   public static final ImmutableSet<EntityType> allTypes() {
      return EntityType.AutoDeskFunction.getEncompassedTypes();
   }

   public AutoDeskFunction(String var1, EntityType var2) {
      this(null, var1, var2);
   }

   public AutoDeskFunction(IntegrationEntityId var1, String var2, EntityType var3) {
      super(var3, var1, var2, "AUTODESK", AutoDeskEndpoint.class, AutoDeskEndpointId.class);
      this.setTypeIdProperty();
   }

   private void setTypeIdProperty() {
      this.setIntProperty("autodesk_type", this.getEntityType().getId());
   }

   @Override
   protected final String getEndpointIdPropertyName() {
      return "autodeskendpoint_id";
   }

   public abstract AutoDeskFunction.FunctionType getFunctionType();

   @Override
   public final Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 1, 0, 1);
   }

   @Nullable
   public final String getWorkspaceNameAndId() {
      synchronized (this.getDataLock()) {
         return this.getProperty("object_name");
      }
   }

   public final void setWorkspaceNameAndId(String var1) {
      this.setStringProperty("object_name", Preconditions.checkNotNull(var1));
   }

   @Nullable
   public final String getWorkspaceName() {
      AutoDeskConnectorWorkspace var1 = this.getWorkspace();
      return var1 == null ? null : var1.getWorkspaceName();
   }

   @Nullable
   private AutoDeskConnectorWorkspace getWorkspace() {
      String var1 = this.getWorkspaceNameAndId();
      return var1 == null ? null : AutoDeskConnectorWorkspace.fromWorkspaceKey(var1);
   }

   @Nullable
   public final String getWorkspaceId() {
      AutoDeskConnectorWorkspace var1 = this.getWorkspace();
      return var1 == null ? null : var1.getWorkspaceID();
   }

   public final void setWorkspaceId(String var1) {
      Preconditions.checkNotNull(var1);
      synchronized (this.getDataLock()) {
         String var3 = this.getWorkspaceName();
         Preconditions.checkState(var3 != null);
         AutoDeskConnectorWorkspace var4 = new AutoDeskConnectorWorkspace(var1, var3);
         String var5 = var4.toWorkspaceKey();
         this.setWorkspaceNameAndId(var5);
      }
   }

   @Override
   protected final void restoreAdditionalData(Persistor var1) {
      this.setTypeIdProperty();
   }

   @Override
   public final XsdNamingStrategy getXsdNamingStrategy() {
      return new XsdNamingStrategy("AUTODESK", this.getFunctionType().name().toLowerCase() + "_" + this.getWorkspaceNameAndId());
   }

   protected final String[] getPropertyKeys(String var1, String var2) {
      return new String[]{
         var1, var2, "autodeskendpoint_id", "autodesk_type", "object_name", "input_xsd_filename", "input_xsd_root", "output_xsd_filename", "output_xsd_root"
      };
   }

   public enum FunctionType implements ConnectorObject {
      CREATE,
      UPDATE,
      UPSERT,
      DELETE,
      GET;

      @Override
      public String getName() {
         return this.name();
      }

      @Override
      public String getDescription() {
         return "";
      }
   }
}
