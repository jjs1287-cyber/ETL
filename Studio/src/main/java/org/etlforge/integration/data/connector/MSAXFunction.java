package org.etlforge.integration.data.connector;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSAXEndpointId;
import org.etlforge.integration.data.entity.id.MSAXFunctionId;
import org.etlforge.util.version.Version;

public final class MSAXFunction extends AbstractConnectorCall<MSAXEndpoint, MSAXEndpointId> {
   @IdProperty
   public static final String MSAX_FUNCTION_ID = "msaxfunction_id";
   @NameProperty
   public static final String MSAX_FUNCTION_NAME = "msaxfunction_name";
   @VisibleForTesting
   static final String MSAX_ENDPOINT_ID = "msaxendpoint_id";
   public static final String MSAX_WSDLURL = "msaxwsdlurl";
   public static final String MSAX_SERVICE_OPERATION = "msaxserviceandoperation";

   public MSAXFunction(String var1) {
      this(null, var1);
   }

   public MSAXFunction(MSAXFunctionId var1, String var2) {
      super(EntityType.MSAXFunction, var1, var2, "MSAX", MSAXEndpoint.class, MSAXEndpointId.class);
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{
         "msaxfunction_id",
         "msaxfunction_name",
         "msaxendpoint_id",
         "msaxwsdlurl",
         "msaxserviceandoperation",
         "input_xsd_filename",
         "input_xsd_root",
         "output_xsd_filename",
         "output_xsd_root"
      };
   }

   @Override
   protected String getEndpointIdPropertyName() {
      return "msaxendpoint_id";
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 1, 0, 3);
   }

   public final String getWSDLUrl() {
      synchronized (this.getDataLock()) {
         return this.getProperty("msaxwsdlurl");
      }
   }

   public final void setWSDLUrl(String var1) {
      this.setStringProperty("msaxwsdlurl", Preconditions.checkNotNull(var1));
   }

   public final String getMSAXServiceOperation() {
      synchronized (this.getDataLock()) {
         return this.getProperty("msaxserviceandoperation");
      }
   }

   public final void setMSAXServiceOperation(String var1) {
      this.setStringProperty("msaxserviceandoperation", Preconditions.checkNotNull(var1));
   }

   public MSAXFunction clone() {
      return (MSAXFunction)super.clone();
   }

   @Override
   public XsdNamingStrategy getXsdNamingStrategy() {
      return new XsdNamingStrategy("MSAX", "");
   }
}
