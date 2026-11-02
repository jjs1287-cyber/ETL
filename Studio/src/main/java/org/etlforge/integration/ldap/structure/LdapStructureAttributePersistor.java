package org.etlforge.integration.ldap.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.util.persist.Persistor;

public final class LdapStructureAttributePersistor {
   public static void write(LdapStructureAttribute var0, Persistor var1) {
      Persistor var2 = var1.createChild("attribute");
      var2.putString("oid", var0.getOid().toString());
      var2.putString("name", var0.getName());
      var2.putString("desc", var0.getDescription());
      var2.putString("usageDesc", var0.getUsageDescription());
      var2.putInteger("use", var0.getUse().ordinal());
      var2.putInteger("cardinality", var0.getCardinality().ordinal());
      var2.putBoolean("readOnly", var0.isReadOnly());
      saveSyntaxInfo(var0, var2);
   }

   private static void saveSyntaxInfo(LdapStructureAttribute var0, Persistor var1) {
      Persistor var2 = var1.createChild("syntax_info");
      AttributeSyntaxInfo var3 = var0.getSyntaxInfo();
      var2.putString("oid", var3.getOid());
      var2.putString("desc", var3.getDescription());
      var2.putString("type", var3.getDataType().name());
   }

   public static LdapStructureAttribute read(Persistor var0) {
      LdapStructureAttributeBuilder var1 = new LdapStructureAttributeBuilder();
      var1.setOid(new NumericOid(var0.getString("oid")));
      var1.setName(var0.getString("name"));
      AttributeSyntaxInfo var2 = loadAttributeSyntaxInfo(var0);
      var1.setSyntaxInfo(var2);
      var1.setDescription(var0.getString("desc"));
      var1.setUsageDecription(var0.getString("usageDesc"));
      int var3 = var0.getInteger("use");
      var1.setUse(AttributeUse.values()[var3]);
      int var4 = var0.getInteger("cardinality");
      var1.setCardinality(AttributeCardinality.values()[var4]);
      var1.setReadOnly(var0.getBoolean("readOnly"));
      return var1.getAttribute();
   }

   private static AttributeSyntaxInfo loadAttributeSyntaxInfo(Persistor var0) {
      Persistor var1 = var0.getFirstChild("syntax_info");
      final String var2 = var1.getString("oid");
      final String var3 = var1.getString("desc");
      final AttributeDataType var4 = getSyntaxInfoDataType(var1);
      return new AttributeSyntaxInfo() {
         @Override
         public AttributeDataType getDataType() {
            return var4;
         }

         @Override
         public String getDescription() {
            return var3;
         }

         @Override
         public String getOid() {
            return var2;
         }
      };
   }

   private static AttributeDataType getSyntaxInfoDataType(Persistor var0) {
      try {
         String var1 = var0.getString("type");
         return AttributeDataType.valueOf(var1);
      } catch (RuntimeException var2) {
         return AttributeDataType.UNKNOWN;
      }
   }

   public static List<LdapStructureAttribute> readMultipleAttributes(Persistor var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Persistor var3 : var0.getChildren("attribute")) {
         LdapStructureAttribute var4 = read(var3);
         var1.add(var4);
      }

      return var1;
   }

   private LdapStructureAttributePersistor() {
   }
}
