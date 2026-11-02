package org.etlforge.integration.salesforce;

import org.etlforge.integration.salesforce.wsdl.DefaultSObjectField;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class FakeSObjects {
   public static final SObjectSchema ACCOUNT = createObject("Account", "Accounts", "ExternalId__c");
   public static final SObjectSchema CONTACT = createObject("Contact", "Contacts", null);

   private static SObjectSchema createObject(String var0, String var1, String var2) {
      DefaultSObjectSchema var3 = new DefaultSObjectSchema(var0);
      var3.setLabel(var0);
      var3.setLabelPlural(var1);
      var3.addField(new DefaultSObjectField("Id", SObjectSchema.Field.Type.ID, SObjectSchema.Field.SoapType.ID, false, true));
      var3.addField(new DefaultSObjectField("Type", SObjectSchema.Field.Type.STRING, SObjectSchema.Field.SoapType.STRING, false, false));
      var3.addField(new DefaultSObjectField("Name", SObjectSchema.Field.Type.STRING, SObjectSchema.Field.SoapType.STRING, false, false));
      var3.addField(new DefaultSObjectField("CreatedDate", SObjectSchema.Field.Type.DATE, SObjectSchema.Field.SoapType.DATETIME, false, false));
      if (var2 != null) {
         var3.addField(new DefaultSObjectField(var2, SObjectSchema.Field.Type.STRING, SObjectSchema.Field.SoapType.STRING, true, false));
      }

      return var3;
   }

   private FakeSObjects() {
   }
}
