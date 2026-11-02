package org.etlforge.integration.server.implementation.webservice.sfdc.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectChild;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectField;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectParent;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectSchema;
import org.etlforge.integration.salesforce.wsdl.Picklist;
import org.etlforge.integration.salesforce.wsdl.PicklistEntry;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsPicklistEntry;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSObjectChild;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSObjectField;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSObjectParent;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSObjectSchema;

final class SchemaDeserializer {
   public static List<SObjectSchema> deserialize(WsSObjectSchema[] var0) {
      SObjectSchema.Field.Type[] var1 = SObjectSchema.Field.Type.values();
      SObjectSchema.Field.SoapType[] var2 = SObjectSchema.Field.SoapType.values();
      ArrayList var3 = Lists.newArrayListWithExpectedSize(var0.length);

      for (int var4 = 0; var4 < var0.length; var4++) {
         var3.add(deserialize(var0[var4], var1, var2));
         var0[var4] = null;
      }

      return var3;
   }

   public static SObjectSchema deserialize(WsSObjectSchema var0) {
      return deserialize(var0, SObjectSchema.Field.Type.values(), SObjectSchema.Field.SoapType.values());
   }

   private static SObjectSchema deserialize(WsSObjectSchema var0, SObjectSchema.Field.Type[] var1, SObjectSchema.Field.SoapType[] var2) {
      String var3 = var0.getName();
      DefaultSObjectSchema var4 = new DefaultSObjectSchema(var3);
      String var5 = var0.getLabel();
      if (Strings.isNullOrEmpty(var5)) {
         var5 = var3;
      }

      var4.setLabel(var5);
      var4.setLabelPlural(var0.getPlural());
      DefaultSObjectSchema.BooleanAttributes var6 = DefaultSObjectSchema.BooleanAttributes.deserializeFromInt(var0.getBooleans());
      var6.applyTo(var4);
      addFields(var0, var4, var1, var2);
      addParents(var0, var4);
      addChildren(var0, var4);
      return var4;
   }

   private static void addFields(WsSObjectSchema var0, DefaultSObjectSchema var1, SObjectSchema.Field.Type[] var2, SObjectSchema.Field.SoapType[] var3) {
      WsSObjectField[] var4 = var0.getFields();
      if (var4 != null) {
         for (WsSObjectField var8 : var4) {
            String var9 = var8.getName();
            DefaultSObjectField var10 = new DefaultSObjectField(var9, var2[var8.getType()], var3[var8.getSoapType()]);
            DefaultSObjectField.BooleanAttributes var11 = DefaultSObjectField.BooleanAttributes.deserializeFromInt(var8.getBooleans());
            var11.applyTo(var10);
            String var12 = var8.getLabel();
            if (Strings.isNullOrEmpty(var12)) {
               var12 = var9;
            }

            var10.setLabel(var12);
            var10.setRelationshipName(var8.getRelName());
            addReferences(var8, var10);
            addPicklist(var8, var10);
            var1.addField(var10);
         }
      }
   }

   private static void addReferences(WsSObjectField var0, DefaultSObjectField var1) {
      String var2 = var0.getRefTo();
      if (!Strings.isNullOrEmpty(var2)) {
         String[] var3 = var2.split(",");

         for (String var7 : var3) {
            var1.addReferenceTo(var7);
         }
      }
   }

   private static void addPicklist(WsSObjectField var0, DefaultSObjectField var1) {
      WsPicklistEntry[] var2 = var0.getPicklist();
      if (var2 != null) {
         ArrayList var3 = Lists.newArrayList();

         for (WsPicklistEntry var7 : var2) {
            String var8 = var7.getLabel();
            String var9 = var7.getValue();
            if (Strings.isNullOrEmpty(var9)) {
               var9 = var8;
            }

            PicklistEntry var10 = new PicklistEntry(var8, var9);
            var3.add(var10);
         }

         var1.setPicklist(new Picklist(var3));
      }
   }

   private static void addParents(WsSObjectSchema var0, DefaultSObjectSchema var1) {
      WsSObjectParent[] var2 = var0.getParents();
      if (var2 != null) {
         for (WsSObjectParent var6 : var2) {
            DefaultSObjectParent var7 = new DefaultSObjectParent(var6.getName(), var6.getObjectTypes(), var6.isNameObject());
            var1.addParent(var7);
         }
      }
   }

   private static void addChildren(WsSObjectSchema var0, DefaultSObjectSchema var1) {
      WsSObjectChild[] var2 = var0.getChildren();
      if (var2 != null) {
         for (WsSObjectChild var6 : var2) {
            DefaultSObjectChild var7 = new DefaultSObjectChild(var6.getName(), var6.getObjectType());
            var1.addChild(var7);
         }
      }
   }

   private SchemaDeserializer() {
   }
}
