package org.etlforge.integration.data.entity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.id.SalesforceUpdateId;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.SoapHeaders;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.util.version.Version;

public final class SalesforceUpdate extends AbstractSalesforceUCU {
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(4, 0, 0, 17);
   @IdProperty
   public static final String SALESFORCE_UPDATE_ID = "salesforceupdate_id";
   @NameProperty
   public static final String SALESFORCE_UPDATE_NAME = "salesforceupdate_name";

   public SalesforceUpdate(String var1) {
      this(null, var1);
   }

   public SalesforceUpdate(SalesforceUpdateId var1, String var2) {
      super(EntityType.SalesforceUpdate, var1, var2, SfdcMethod.UPDATE);
   }

   @Override
   public String[] getPropertyKeys() {
      ArrayList var1 = Lists.newArrayList(
         "salesforceupdate_id",
         "salesforceupdate_name",
         "salesforce_object",
         "operation_id",
         "truncate_fields",
         "trigger_email",
         "debug_mode",
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

   @Override
   public boolean hasExternalIdField() {
      return false;
   }
}
