package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Strings;
import com.sforce.soap.partner.ChildRelationship;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectChild;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectField;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectParent;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectSchema;
import org.etlforge.integration.salesforce.wsdl.Picklist;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class SchemaBuilder {
   public static DefaultSObjectSchema createFlatDescription(DescribeSObjectResult var0) {
      DefaultSObjectSchema var1 = new DefaultSObjectSchema(var0.getName());
      fillInPropertiesAndFields(var1, var0);
      addParentRelations(var0, var1);
      addChildRelations(var0, var1);
      return var1;
   }

   private static void fillInPropertiesAndFields(DefaultSObjectSchema var0, DescribeSObjectResult var1) {
      var0.setLabel(var1.getLabel());
      var0.setLabelPlural(var1.getLabelPlural());
      var0.setCustom(var1.isCustom());
      var0.setQueryable(var1.isQueryable());
      var0.setUpdatable(var1.isUpdateable());
      var0.setCreatable(var1.isCreateable());
      var0.setDeletable(var1.isDeletable());
      addFields(var0, var1);
   }

   private static void addFields(DefaultSObjectSchema var0, DescribeSObjectResult var1) {
      Field[] var2 = var1.getFields();

      for (Field var6 : var2) {
                                                                                         
                                                                         
         if (var6.getType() == FieldType.address || var6.getType() == FieldType.location) continue;
         DefaultSObjectField var7 = new DefaultSObjectField(
            var6.getName(), translateType(var6), translateSoapType(var6), var6.isExternalId(), var6.isIdLookup()
         );
         Picklist var8 = PicklistBuilder.fromField(var6);
         var7.setPicklist(var8);
         var7.setLabel(var6.getLabel());
         var7.setCreateable(var6.isCreateable());
         var7.setUpdateable(var6.isUpdateable());
         var7.setFilterable(var6.isFilterable());
         var7.setRelationshipName(var6.getRelationshipName());
         String[] var9 = var6.getReferenceTo();
         if (var9 != null) {
            for (String var13 : var9) {
               var7.addReferenceTo(var13);
            }
         }

         var0.addField(var7);
      }
   }

   private static SObjectSchema.Field.Type translateType(Field var0) {
      FieldType var1 = var0.getType();
      switch (var1) {
         case string:
            return SObjectSchema.Field.Type.STRING;
         case picklist:
            return SObjectSchema.Field.Type.PICKLIST;
         case multipicklist:
            return SObjectSchema.Field.Type.MULTIPICKLIST;
         case combobox:
            return SObjectSchema.Field.Type.COMBOBOX;
         case reference:
            return SObjectSchema.Field.Type.REFERENCE;
         case _boolean:
            return SObjectSchema.Field.Type.BOOLEAN;
         case currency:
            return SObjectSchema.Field.Type.CURRENCY;
         case textarea:
            return SObjectSchema.Field.Type.TEXTAREA;
         case _int:
            return SObjectSchema.Field.Type.INT;
         case _double:
            return SObjectSchema.Field.Type.DOUBLE;
         case phone:
            return SObjectSchema.Field.Type.PHONE;
         case percent:
            return SObjectSchema.Field.Type.PERCENT;
         case id:
            return SObjectSchema.Field.Type.ID;
         case date:
            return SObjectSchema.Field.Type.DATE;
         case datetime:
            return SObjectSchema.Field.Type.DATETIME;
         case time:
            return SObjectSchema.Field.Type.TIME;
         case url:
            return SObjectSchema.Field.Type.URL;
         case email:
            return SObjectSchema.Field.Type.EMAIL;
         case encryptedstring:
            return SObjectSchema.Field.Type.ENCRYPTED_STRING;
         case datacategorygroupreference:
            return SObjectSchema.Field.Type.DATA_CATEGORY_GROUP_REF;
         case base64:
            return SObjectSchema.Field.Type.BASE64;
         case anyType:
            return SObjectSchema.Field.Type.ANY;
         default:
            return SObjectSchema.Field.Type.STRING;
      }
   }

   private static SObjectSchema.Field.SoapType translateSoapType(Field var0) {
      switch (var0.getSoapType()) {
         case ID:
            return SObjectSchema.Field.SoapType.ID;
         case anyType:
            return SObjectSchema.Field.SoapType.ANY_TYPE;
         case base64Binary:
            return SObjectSchema.Field.SoapType.BASE64_BINARY;
         case _boolean:
            return SObjectSchema.Field.SoapType.BOOLEAN;
         case date:
            return SObjectSchema.Field.SoapType.DATE;
         case dateTime:
            return SObjectSchema.Field.SoapType.DATETIME;
         case time:
            return SObjectSchema.Field.SoapType.TIME;
         case _double:
            return SObjectSchema.Field.SoapType.DOUBLE;
         case _int:
            return SObjectSchema.Field.SoapType.INT;
         case string:
            return SObjectSchema.Field.SoapType.STRING;
         default:
            throw new RuntimeException("Unexpected SoapType: " + var0.getSoapType());
      }
   }

   private static void addParentRelations(DescribeSObjectResult var0, DefaultSObjectSchema var1) {
      for (Field var5 : var0.getFields()) {
         String var6 = var5.getRelationshipName();
         if (!Strings.isNullOrEmpty(var6)) {
            String[] var7 = var5.getReferenceTo();
            if (var7 != null && var7.length != 0) {
               DefaultSObjectParent var8 = new DefaultSObjectParent(var6, var7, var5.isNamePointing());
               var1.addParent(var8);
            }
         }
      }
   }

   private static void addChildRelations(DescribeSObjectResult var0, DefaultSObjectSchema var1) {
      ChildRelationship[] var2 = var0.getChildRelationships();
      if (var2 != null) {
         for (ChildRelationship var6 : var2) {
            String var7 = var6.getRelationshipName();
            if (!Strings.isNullOrEmpty(var7)) {
               String var8 = var6.getChildSObject();
               if (!Strings.isNullOrEmpty(var8)) {
                  DefaultSObjectChild var9 = new DefaultSObjectChild(var7, var8);
                  var1.addChild(var9);
               }
            }
         }
      }
   }

   private SchemaBuilder() {
   }
}
