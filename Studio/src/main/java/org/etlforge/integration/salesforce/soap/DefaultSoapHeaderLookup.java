package org.etlforge.integration.salesforce.soap;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.SfdcMethod;
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
import org.etlforge.util.name.Nameables;

public final class DefaultSoapHeaderLookup {
   public static List<SoapHeader> getDefaultHeaders(SfdcMethod var0) {
      return getDefaultHeaders(var0, null);
   }

   public static List<SoapHeader> getDefaultHeaders(SfdcMethod var0, SalesforceConnectorId var1) {
      switch (var0) {
         case QUERY:
            return queryHeaders(var1);
         case UPSERT:
         case UPDATE:
            return updateAndUpsertHeaders(var1);
         case CREATE:
            return createHeaders(var1);
         case DELETE:
            return deleteHeaders(var1);
         default:
            throw new UnsupportedOperationException(var0 + " is not called through SOAP by us");
      }
   }

   private static List<SoapHeader> queryHeaders(SalesforceConnectorId var0) {
      return Arrays.asList(new SessionHeader(var0), new QueryOptions(), new MruHeader(), new CallOptions(), new PackageVersionHeader());
   }

   private static List<SoapHeader> createHeaders(SalesforceConnectorId var0) {
      return commonUcuHeaders(var0);
   }

   private static List<SoapHeader> updateAndUpsertHeaders(SalesforceConnectorId var0) {
      List var1 = commonUcuHeaders(var0);
      var1.add(new OwnerChangeOptionsHeader());
      return var1;
   }

   private static List<SoapHeader> commonUcuHeaders(SalesforceConnectorId var0) {
      return Lists.newArrayList(
         new SessionHeader(var0),
         new AllowFieldTruncationHeader(),
         new AssignmentRuleHeader(),
         new AllOrNoneHeader(),
         new EmailHeader(),
         new MruHeader(),
         new DebuggingHeader(),
         new DisableFeedTrackingHeader(),
         new StreamingEnabledHeader(),
         new CallOptions(),
         new PackageVersionHeader()
      );
   }

   private static List<SoapHeader> deleteHeaders(SalesforceConnectorId var0) {
      return Arrays.asList(
         new SessionHeader(var0),
         new CallOptions(),
         new PackageVersionHeader(),
         new EmailHeader(),
         new AllowFieldTruncationHeader(),
         new DisableFeedTrackingHeader(),
         new StreamingEnabledHeader(),
         new AllOrNoneHeader(),
         new UserTerritoryDeleteHeader(),
         new DebuggingHeader()
      );
   }

   public static List<SoapHeader> getDefaultHeadersWithOverrides(SfdcMethod var0, SalesforceConnectorId var1, SoapHeader... var2) {
      List var3 = getDefaultHeaders(var0, var1);
      if (var2.length == 0) {
         return var3;
      }

      LinkedHashMap var4 = Nameables.toLinkedHashMap(var2);
      ArrayList var5 = Lists.newArrayList();

      for (Object __etlItem102 : var3) {
         SoapHeader var7 = (SoapHeader) __etlItem102;
         SoapHeader var8 = (SoapHeader)var4.remove(var7.getName());
         if (var8 instanceof SessionHeader) {
            var8 = null;
         }

         var5.add(var8 == null ? var7 : var8);
      }

      var5.addAll(var4.values());
      return var5;
   }

   private DefaultSoapHeaderLookup() {
   }
}
