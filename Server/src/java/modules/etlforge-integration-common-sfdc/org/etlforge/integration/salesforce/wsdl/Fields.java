package org.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.Ordering;

public final class Fields {
   public static final SObjectSchema.Field ID = ConstantField.createConstantIdField();
   public static final SObjectSchema.Field NAME = new ConstantField(
      new DefaultSObjectField("Name", SObjectSchema.Field.Type.STRING, SObjectSchema.Field.SoapType.STRING, false, false)
   );
   public static final Ordering<SObjectSchema.Field> ORDER = Ordering.from(new DefaultFieldOrder());

   private Fields() {
   }
}
