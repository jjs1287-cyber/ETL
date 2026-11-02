package org.etlforge.integration.client.connector.schema;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public class SchemaRequestData {
   protected final SchemaRequestData.Mode mode;
   private final ConnectorEndpoint endpoint;
   private final ImmutableList<ConnectorObject> path;

   public static SchemaRequestData singleSchemas(ConnectorEndpoint var0, ConnectorObject... var1) {
      return new SchemaRequestData(SchemaRequestData.Mode.SINGLE, var0, var1);
   }

   public static SchemaRequestData multipleSchemas(ConnectorEndpoint var0, ConnectorObject... var1) {
      return new SchemaRequestData(SchemaRequestData.Mode.MULTIPLE_GET, var0, var1);
   }

   public static SchemaRequestData refreshingMultipleSchemas(ConnectorEndpoint var0, ConnectorObject... var1) {
      return new SchemaRequestData(SchemaRequestData.Mode.MULTIPLE_REFRESH, var0, var1);
   }

   protected SchemaRequestData(SchemaRequestData.Mode var1, ConnectorEndpoint var2, ConnectorObject... var3) {
      this(var1, var2, Arrays.asList(var3));
   }

   protected SchemaRequestData(SchemaRequestData.Mode var1, ConnectorEndpoint var2, Iterable<ConnectorObject> var3) {
      this.mode = var1;
      this.endpoint = Preconditions.checkNotNull(var2, "endpoint");
      this.path = FluentIterable.<ConnectorObject>from(var3).filter(Predicates.notNull()).toImmutableList();
   }

   public void askServerForSchemas(ConnectorCallInfoProvider var1, GetSchemasCallback var2) {
      switch (this.mode) {
         case SINGLE:
            var1.getSingleSchemas(this.endpoint, this.path, var2);
            break;
         case MULTIPLE_GET:
            var1.getMultipleSchemas(this.endpoint, this.path, false, var2);
            break;
         case MULTIPLE_REFRESH:
            var1.getMultipleSchemas(this.endpoint, this.path, true, var2);
            break;
         default:
            throw new AssertionError();
      }
   }

   public enum Mode {
      SINGLE,
      MULTIPLE_GET,
      MULTIPLE_REFRESH;
   }
}
