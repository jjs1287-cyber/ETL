package org.etlforge.integration.data.connector;

import com.google.common.hash.Hasher;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.system.ArgCheck;

public abstract class ConnectorEndpoint extends IntegrationEntity {
   public static final String CONNECTION_PARAMS = "connectionParams";
   private final String connectorTypeId;

   protected ConnectorEndpoint(EntityType var1, IntegrationEntityId var2, String var3, String var4) {
      super(var1, var2, var3);
      this.connectorTypeId = ArgCheck.checkNotEmpty(var4);
   }

   public final String getConnectorTypeId() {
      return this.connectorTypeId;
   }

   public void contributeToCachingKey(Hasher var1) {
      throw new UnsupportedOperationException();
   }

   public final void setConnectionParameters(EndpointParameters var1) {
      EndpointParameters var2;
      synchronized (this.getDataLock()) {
         var2 = this.getConnectionParameters();
         var1.writeToKeyValueStore(this.getData());
      }

      if (!var1.equals(var2)) {
         this.firePropertyChange("connectionParams", var2, var1);
      }
   }

   public abstract EndpointParameters getConnectionParameters();
}
