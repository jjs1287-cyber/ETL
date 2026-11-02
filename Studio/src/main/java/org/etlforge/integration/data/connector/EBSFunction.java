package org.etlforge.integration.data.connector;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.EBSEndpointId;
import org.etlforge.integration.data.entity.id.EBSFunctionId;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;
import org.etlforge.util.version.Version;

public final class EBSFunction extends AbstractConnectorCall<EBSEndpoint, EBSEndpointId> {
   @IdProperty
   public static final String EBS_FUNCTION_ID = "ebsfunction_id";
   @NameProperty
   public static final String EBS_FUNCTION_NAME = "ebsfunction_name";
   @VisibleForTesting
   static final String EBS_ENDPOINT_ID = "ebsendpoint_id";
   public static final String CATALOG = "catalog";
   public static final String PROGRAM = "program";
   private static final String CATALOG_NAME = "ebscatalog";
   private static final String CATALOG_DESCRIPTION = "ebscatalog_description";
   private static final String PROGRAM_NAME = "ebsprogram";
   private static final String PROGRAM_DESCRIPTION = "ebsprogram_description";

   public EBSFunction(String var1) {
      this(null, var1);
   }

   public EBSFunction(EBSFunctionId var1, String var2) {
      super(EntityType.EBSFunction, var1, var2, "EBS", EBSEndpoint.class, EBSEndpointId.class);
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{
         "ebsfunction_id",
         "ebsfunction_name",
         "ebsendpoint_id",
         "ebscatalog",
         "ebscatalog_description",
         "ebsprogram",
         "ebsprogram_description",
         "input_xsd_filename",
         "input_xsd_root",
         "output_xsd_filename",
         "output_xsd_root"
      };
   }

   @Override
   protected String getEndpointIdPropertyName() {
      return "ebsendpoint_id";
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 2, 0, 0);
   }

   public ConnectorObject getCatalog() {
      return this.getConnectorObject("ebscatalog", "ebscatalog_description");
   }

   private ConnectorObject getConnectorObject(String var1, String var2) {
      synchronized (this.getDataLock()) {
         String var4 = this.getProperty(var1);
         return Strings.isNullOrEmpty(var4) ? null : new SimpleConnectorObject(var4, this.getProperty(var2));
      }
   }

   public void setCatalog(ConnectorObject var1) {
      this.setConnectorObject(var1, "catalog", "ebscatalog", "ebscatalog_description");
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

   public ConnectorObject getProgram() {
      return this.getConnectorObject("ebsprogram", "ebsprogram_description");
   }

   public void setProgram(ConnectorObject var1) {
      Preconditions.checkNotNull(var1);
      this.setConnectorObject(var1, "program", "ebsprogram", "ebsprogram_description");
   }

   public EBSFunction clone() {
      return (EBSFunction)super.clone();
   }

   @Override
   public XsdNamingStrategy getXsdNamingStrategy() {
      return new XsdNamingStrategy("EBS", this.getProgram().getName());
   }
}
