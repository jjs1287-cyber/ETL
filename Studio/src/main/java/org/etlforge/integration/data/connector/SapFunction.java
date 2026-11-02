package org.etlforge.integration.data.connector;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.SapEndpointId;
import org.etlforge.integration.data.entity.id.SapFunctionId;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;
import org.etlforge.util.version.Version;

public final class SapFunction extends AbstractConnectorCall<SapEndpoint, SapEndpointId> {
   @IdProperty
   public static final String SAP_FUNCTION_ID = "sapfunction_id";
   @NameProperty
   public static final String SAP_FUNCTION_NAME = "sapfunction_name";
   @VisibleForTesting
   static final String SAP_ENDPOINT_ID = "sapendpoint_id";
   public static final String TYPE = "type";
   public static final String CATEGORY = "category";
   public static final String API_FUNCTION = "api_function";
   private static final String CATEGORY_NAME = "category_name";
   private static final String CATEGORY_DESCRIPTION = "category_description";
   private static final String API_FUNCTION_NAME = "api_function_name";
   private static final String API_FUNCTION_DESCRIPTION = "api_function_description";

   public SapFunction(String var1) {
      this(null, var1);
   }

   public SapFunction(SapFunctionId var1, String var2) {
      super(EntityType.SapFunction, var1, var2, "SAP", SapEndpoint.class, SapEndpointId.class);
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{
         "sapfunction_id",
         "sapfunction_name",
         "sapendpoint_id",
         "type",
         "category_name",
         "category_description",
         "api_function_name",
         "api_function_description",
         "input_xsd_filename",
         "input_xsd_root",
         "output_xsd_filename",
         "output_xsd_root"
      };
   }

   @Override
   protected String getEndpointIdPropertyName() {
      return "sapendpoint_id";
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 0, 0, 300);
   }

   public SapFunction.FunctionType getFunctionType() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("type");
         return var2 == null ? null : SapFunction.FunctionType.valueOf(var2);
      }
   }

   public void setFunctionType(SapFunction.FunctionType var1) {
      Preconditions.checkNotNull(var1);
      SapFunction.FunctionType var2;
      synchronized (this.getDataLock()) {
         var2 = this.getFunctionType();
         this.setProperty("type", var1.name());
      }

      if (var2 != var1) {
         this.firePropertyChange("type", var2, var1);
      }
   }

   public ConnectorObject getCategory() {
      return this.getConnectorObject("category_name", "category_description");
   }

   private ConnectorObject getConnectorObject(String var1, String var2) {
      synchronized (this.getDataLock()) {
         String var4 = this.getProperty(var1);
         return Strings.isNullOrEmpty(var4) ? null : new SimpleConnectorObject(var4, this.getProperty(var2));
      }
   }

   public void setCategory(ConnectorObject var1) {
      this.setConnectorObject(var1, "category", "category_name", "category_description");
   }

   private void setConnectorObject(ConnectorObject var1, String var2, String var3, String var4) {
      String var5;
      String var6;
      if (var1 == null) {
         var5 = null;
         var6 = null;
      } else {
         var5 = var1.getName();
         var6 = var1.getDescription();
      }

      ConnectorObject var7;
      synchronized (this.getDataLock()) {
         var7 = this.getConnectorObject(var3, var4);
         this.setProperty(var3, var5);
         this.setProperty(var4, var6);
      }

      if (!Objects.equal(var7, var1)) {
         this.firePropertyChange(var2, var7, var1);
      }
   }

   public ConnectorObject getApiFunction() {
      return this.getConnectorObject("api_function_name", "api_function_description");
   }

   public void setApiFunction(ConnectorObject var1) {
      Preconditions.checkNotNull(var1);
      this.setConnectorObject(var1, "api_function", "api_function_name", "api_function_description");
   }

   public SapFunction clone() {
      return (SapFunction)super.clone();
   }

   @Override
   public XsdNamingStrategy getXsdNamingStrategy() {
      return new XsdNamingStrategy("SAP", this.getApiFunction().getName());
   }

   public enum FunctionType implements ConnectorObject {
      BAPI,
      RFC,
      IDOC;

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
