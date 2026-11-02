package org.etlforge.integration.data.connector;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.MSCrmEndpointId;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public abstract class MSCrmFunction extends AbstractConnectorCall<MSCrmEndpoint, MSCrmEndpointId> {
   public static ImmutableSet<EntityType> allTypes() {
      return EntityType.MSCrmFunction.getEncompassedTypes();
   }

   public MSCrmFunction(String var1, EntityType var2) {
      this(null, var1, var2);
   }

   public MSCrmFunction(IntegrationEntityId var1, String var2, EntityType var3) {
      super(var3, var1, var2, "MSCRM", MSCrmEndpoint.class, MSCrmEndpointId.class);
      this.setTypeIdProperty();
   }

   private void setTypeIdProperty() {
      this.setIntProperty("mscrm_type", this.getEntityType().getId());
   }

   @Override
   protected final String getEndpointIdPropertyName() {
      return "mscrmendpoint_id";
   }

   public abstract FunctionType getFunctionType();

   @Override
   public final Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 0, 0, 310);
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
      return new XsdNamingStrategy("MSCRM", this.getFunctionType().name().toLowerCase() + "_" + this.getObjectName());
   }

   protected final String[] getPropertyKeys(String var1, String var2) {
      return new String[]{
         var1,
         var2,
         "mscrmendpoint_id",
         "mscrm_type",
         "object_name",
         "input_xsd_filename",
         "input_xsd_root",
         "output_xsd_filename",
         "output_xsd_root",
         "query_xml"
      };
   }
}
