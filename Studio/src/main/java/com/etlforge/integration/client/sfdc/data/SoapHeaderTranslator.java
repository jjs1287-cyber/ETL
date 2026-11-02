package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.salesforce.soap.header.AllOrNoneHeader;
import org.etlforge.integration.salesforce.soap.header.AllowFieldTruncationHeader;
import org.etlforge.integration.salesforce.soap.header.AssignmentRuleHeader;
import org.etlforge.integration.salesforce.soap.header.CallOptions;
import org.etlforge.integration.salesforce.soap.header.DebuggingHeader;
import org.etlforge.integration.salesforce.soap.header.DisableFeedTrackingHeader;
import org.etlforge.integration.salesforce.soap.header.EmailHeader;
import org.etlforge.integration.salesforce.soap.header.MruHeader;
import org.etlforge.integration.salesforce.soap.header.OwnerChangeOptionsHeader;
import org.etlforge.integration.salesforce.soap.header.PackageVersionHeader;
import org.etlforge.integration.salesforce.soap.header.QueryOptions;
import org.etlforge.integration.salesforce.soap.header.SessionHeader;
import org.etlforge.integration.salesforce.soap.header.StreamingEnabledHeader;
import org.etlforge.integration.salesforce.soap.header.UserTerritoryDeleteHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class SoapHeaderTranslator {
   private final ImmutableBiMap<String, Class<? extends SoapHeader>> translationTable;

   public SoapHeaderTranslator() {
      HashMap var1 = Maps.newHashMap();
      var1.put("AssignmentRule", AssignmentRuleHeader.class);
      var1.put("CallOptions", CallOptions.class);
      var1.put("Debugging", DebuggingHeader.class);
      var1.put("Email", EmailHeader.class);
      var1.put("QueryOptions", QueryOptions.class);
      var1.put("Session", SessionHeader.class);
      var1.put("UserTerritoryDelete", UserTerritoryDeleteHeader.class);
      var1.put("PackageVersion", PackageVersionHeader.class);
      var1.put("AllOrNone", AllOrNoneHeader.class);
      var1.put("AllowFieldTruncation", AllowFieldTruncationHeader.class);
      var1.put("DisableFeedTracking", DisableFeedTrackingHeader.class);
      var1.put("MruHeader", MruHeader.class);
      var1.put("StreamingEnabledHeader", StreamingEnabledHeader.class);
      var1.put("OwnerChangeOptions", OwnerChangeOptionsHeader.class);
      this.translationTable = ImmutableBiMap.copyOf(var1);
   }

   public SoapHeader toSoapHeader(SalesforceWizardEntity.Header var1) {
      String var2 = var1.name;
      Class var3 = this.translationTable.get(var2);
      if (var3 == null) {
         throw new RuntimeException("Unknown SOAP header: " + var2 + ". This is a bug.");
      }

      try {
         Method var4 = var3.getMethod("fromString", String.class);
         return (SoapHeader)var4.invoke(null, var1.value);
      } catch (Exception var5) {
         throw new RuntimeException(
            "Invalid client configuration. This is a bug. Tried to create the SOAP header " + var2 + " but got this error: " + var5.getMessage(), var5
         );
      }
   }

   public SalesforceWizardEntity.Header toDataStoreRepresentation(SoapHeader var1) {
      String var2 = this.translationTable.inverse().get(var1.getClass());
      if (var2 == null) {
         throw new RuntimeException(
            "Failed to store a SOAP header of type "
               + var1.getClass().getName()
               + " in the project data, because there is no corresponding internal name for it. This is a bug."
         );
      } else {
         return new SalesforceWizardEntity.Header(var2, var1.getStringRepresentation());
      }
   }
}
