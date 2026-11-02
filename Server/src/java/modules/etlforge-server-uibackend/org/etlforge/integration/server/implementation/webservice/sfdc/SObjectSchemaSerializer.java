package org.etlforge.integration.server.implementation.webservice.sfdc;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectField;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectSchema;
import org.etlforge.integration.salesforce.wsdl.Picklist;
import org.etlforge.integration.salesforce.wsdl.PicklistEntry;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class SObjectSchemaSerializer implements Function<SObjectSchema, WsSObjectSchema> {
   public static WsSObjectSchema[] serialize(List<SObjectSchema> var0) {
      List var1 = KongaListUtils.transform(var0, new SObjectSchemaSerializer());
      return (WsSObjectSchema[]) var1.toArray(new WsSObjectSchema[var1.size()]);
   }

   public WsSObjectSchema apply(SObjectSchema var1) {
      WsSObjectSchema var2 = new WsSObjectSchema();
      String var3 = var1.getName();
      var2.setName(var3);
      String var4 = var1.getLabel();
      if (var4.equals(var3)) {
         var2.setLabel("");
      } else {
         var2.setLabel(var4);
      }

      var2.setLabel(var4);
      var2.setPlural(var1.getLabelPlural());
      DefaultSObjectSchema.BooleanAttributes var5 = new DefaultSObjectSchema.BooleanAttributes(var1);
      var2.setBooleans(var5.serializeToInt());
      this.fillInFields(var1, var2);
      this.fillInParents(var1, var2);
      this.fillInChildren(var1, var2);
      return var2;
   }

   private void fillInFields(SObjectSchema var1, WsSObjectSchema var2) {
      ArrayList var3 = Lists.newArrayList();

      for (SObjectSchema.Field var5 : var1.getFields()) {
         WsSObjectField var6 = new WsSObjectField();
         DefaultSObjectField.BooleanAttributes var7 = new DefaultSObjectField.BooleanAttributes(var5);
         var6.setBooleans(var7.serializeToInt());
         String var8 = var5.getName();
         var6.setName(var8);
         String var9 = var5.getLabel();
         if (var9.equals(var8)) {
            var6.setLabel("");
         } else {
            var6.setLabel(var9);
         }

         var6.setRelName(var5.getRelationshipName());
         var6.setSoapType(var5.getSoapType().ordinal());
         var6.setType(var5.getType().ordinal());
         this.fillInPicklist(var5, var6);
         this.fillInReferenceTo(var5, var6);
         var3.add(var6);
      }

      var2.setFields((WsSObjectField[]) var3.toArray(new WsSObjectField[var3.size()]));
   }

   private void fillInPicklist(SObjectSchema.Field var1, WsSObjectField var2) {
      Picklist var3 = var1.getPicklist();
      if (!var3.isEmpty()) {
         ArrayList var4 = Lists.newArrayList();

         for (PicklistEntry var6 : var3) {
            WsPicklistEntry var7 = new WsPicklistEntry();
            String var8 = var6.getLabel();
            var7.setLabel(var8);
            String var9 = var6.getValue();
            if (var9.equals(var8)) {
               var7.setValue("");
            } else {
               var7.setValue(var9);
            }

            var4.add(var7);
         }

         var2.setPicklist((WsPicklistEntry[]) var4.toArray(new WsPicklistEntry[var4.size()]));
      }
   }

   private void fillInReferenceTo(SObjectSchema.Field var1, WsSObjectField var2) {
      List var3 = var1.getReferenceTo();
      var2.setRefTo(Joiner.on(",").join(var3));
   }

   private void fillInParents(SObjectSchema var1, WsSObjectSchema var2) {
      ArrayList var3 = Lists.newArrayList();

      for (SObjectSchema.Parent var5 : var1.getParents()) {
         ImmutableSet var6 = var5.getObjectTypes();
         var3.add(new WsSObjectParent(var5.getName(), (String[]) var6.toArray(new String[var6.size()]), "Name".equals(var5.getObjectTypeInQuery())));
      }

      var2.setParents((WsSObjectParent[]) var3.toArray(new WsSObjectParent[var3.size()]));
   }

   private void fillInChildren(SObjectSchema var1, WsSObjectSchema var2) {
      ArrayList var3 = Lists.newArrayList();

      for (SObjectSchema.Child var5 : var1.getChildren()) {
         var3.add(new WsSObjectChild(var5.getName(), var5.getObjectType()));
      }

      var2.setChildren((WsSObjectChild[]) var3.toArray(new WsSObjectChild[var3.size()]));
   }
}
