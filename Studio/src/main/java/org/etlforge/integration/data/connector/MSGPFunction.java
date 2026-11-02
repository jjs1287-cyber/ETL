package org.etlforge.integration.data.connector;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.MSGPEndpointId;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public abstract class MSGPFunction extends AbstractConnectorCall<MSGPEndpoint, MSGPEndpointId> {
   public static final ImmutableSet<EntityType> allTypes() {
      return EntityType.MSGPFunction.getEncompassedTypes();
   }

   public MSGPFunction(String var1, EntityType var2) {
      this(null, var1, var2);
   }

   public MSGPFunction(IntegrationEntityId var1, String var2, EntityType var3) {
      super(var3, var1, var2, "MSGP", MSGPEndpoint.class, MSGPEndpointId.class);
      this.setTypeIdProperty();
   }

   private void setTypeIdProperty() {
      this.setIntProperty("msgp_type", this.getEntityType().getId());
   }

   @Override
   protected final String getEndpointIdPropertyName() {
      return "msgpendpoint_id";
   }

   public abstract MSGPFunction.FunctionType getFunctionType();

   @Override
   public final Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 0, 0, 500);
   }

   public final String getObjectName() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("object_name");
         return var2 == null ? null : var2;
      }
   }

   public final void setObjectName(String var1) {
      this.setStringProperty("object_name", Preconditions.checkNotNull(var1));
   }

   @Override
   protected final void restoreAdditionalData(Persistor var1) {
      this.setTypeIdProperty();
   }

   @Override
   public final XsdNamingStrategy getXsdNamingStrategy() {
      return new XsdNamingStrategy("MSGP", this.getFunctionType().name().toLowerCase() + "_" + this.getObjectName());
   }

   protected final String[] getPropertyKeys(String var1, String var2) {
      return new String[]{
         var1, var2, "msgpendpoint_id", "msgp_type", "object_name", "input_xsd_filename", "input_xsd_root", "output_xsd_filename", "output_xsd_root"
      };
   }

   public enum FunctionType implements ConnectorObject {
      CREATE,
      UPDATE,
      DELETE,
      RETRIEVE;

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
