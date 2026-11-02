package org.etlforge.integration.data.entity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.id.SalesforceUpsertId;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.SoapHeaders;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.util.version.Version;

public final class SalesforceUpsert extends AbstractSalesforceUCU {
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(3, 6, 0, 3);
   @IdProperty
   public static final String SALESFORCE_UPSERT_ID = "salesforceupsert_id";
   @NameProperty
   public static final String SALESFORCE_UPSERT_NAME = "salesforceupsert_name";
   public static final String EXTERNAL_ID_FIELD_NAME = "external_id_field";

   public SalesforceUpsert(String var1) {
      this(null, var1);
   }

   public SalesforceUpsert(SalesforceUpsertId var1, String var2) {
      super(EntityType.SalesforceUpsert, var1, var2, SfdcMethod.UPSERT);
   }

   @Override
   public String[] getPropertyKeys() {
      ArrayList var1 = Lists.newArrayList(
         "salesforceupsert_id",
         "salesforceupsert_name",
         "salesforce_object",
         "operation_id",
         "truncate_fields",
         "trigger_email",
         "debug_mode",
         "external_id_field",
         "insert_null_values",
         "connector_id",
         "compress",
         "source_file_has_header",
         "start_at_row",
         "source_encoding",
         "timezone",
         "date_fmt",
         "datetime_fmt",
         "related_objects"
      );
      var1.addAll(SoapHeaders.getExistingKeys(this));
      return (String[]) var1.toArray(new String[var1.size()]);
   }

   @Override
   public Version getRequiredVersion() {
      return VERSION_INTRODUCED;
   }

   public void setExternalIdFieldName(String var1) {
      this.setStringProperty("external_id_field", var1);
   }

   public String getExternalIdFieldName() {
      return this.getProperty("external_id_field", "");
   }

   @Override
   public boolean hasExternalIdField() {
      return true;
   }
}
