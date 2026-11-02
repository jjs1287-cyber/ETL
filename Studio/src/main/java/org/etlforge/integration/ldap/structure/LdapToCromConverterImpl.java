package org.etlforge.integration.ldap.structure;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CromType;

final class LdapToCromConverterImpl implements LdapToCromConverter {
   private Comparator<LdapStructureAttribute> attributeOrder;
   private final LdapToCromConverterImpl.ClassNodeFactory classNodeFactory;
   private final LdapToCromConverterImpl.AttributeNodeFactory attributeNodeFactory;

   private LdapToCromConverterImpl(LdapToCromConverterImpl.Mode var1) {
      this.classNodeFactory = new LdapToCromConverterImpl.ClassNodeFactory(var1);
      this.attributeNodeFactory = new LdapToCromConverterImpl.AttributeNodeFactory(var1);
   }

   static LdapToCromConverterImpl newSourceStructureConverter() {
      return new LdapToCromConverterImpl(LdapToCromConverterImpl.Mode.SOURCE);
   }

   static LdapToCromConverterImpl newTargetStructureConverter() {
      return new LdapToCromConverterImpl(LdapToCromConverterImpl.Mode.TARGET);
   }

   @Override
   public CROM toCrom(LdapStructureEntryNode var1, AttributeFilter var2) {
      AttributeFilter var3 = var2 != null ? var2 : AttributeFilter.ACCEPTS_ALL;
      return this.toCromImpl(var1, var3, null);
   }

   @Override
   public void setAttributeOrder(Comparator<LdapStructureAttribute> var1) {
      this.attributeOrder = var1;
   }

   private CROM toCromImpl(LdapStructureEntryNode var1, AttributeFilter var2, CROM var3) {
      CROM var4 = this.createClassNode(var1);
      this.addLdapAttributes(var1, var2, var4);
      if (var3 != null) {
         var3.addChild(var4);
      }

      return var4;
   }

   private CROM createClassNode(LdapStructureEntryNode var1) {
      String var2 = var1.getName();
      if (var2 == null || var2.length() == 0) {
         var2 = var1.getEntry().getOid().toString();
      }

      return this.classNodeFactory.createCrom(var2);
   }

   private void addLdapAttributes(LdapStructureEntryNode var1, AttributeFilter var2, CROM var3) {
      LdapStructureEntry var4 = var1.getEntry();

      for (LdapStructureAttribute var6 : this.getLdapAttributes(var1)) {
         if (var2.test(var4, var6)) {
            CROM var7 = this.createLdapAttributeCrom(var6);
            var3.addChild(var7);
         }
      }
   }

   private List<LdapStructureAttribute> getLdapAttributes(LdapStructureEntryNode var1) {
      List var2 = var1.getAttributes();
      if (this.attributeOrder != null) {
         Collections.sort(var2, this.attributeOrder);
      }

      return var2;
   }

   private CROM createLdapAttributeCrom(LdapStructureAttribute var1) {
      return this.attributeNodeFactory.createCrom(var1);
   }

   private static void addCommandNode(CROM var0, String var1) {
      CROM var2 = new CROM(var0, false, var1, 1, CromType.CROM_t_Ldap);
      var2.addType(36L);
      var2.setValueType("String");
   }

   private static final class AttributeNodeFactory {
      private final LdapToCromConverterImpl.Mode mode;

      public AttributeNodeFactory(LdapToCromConverterImpl.Mode var1) {
         this.mode = var1;
      }

      public CROM createCrom(LdapStructureAttribute var1) {
         CROM var2 = new CROM(null, true, var1.getName(), 0, CromType.CROM_t_Ldap);
         this.setNodeType(var1, var2);
         this.insertValueNode(var1, var2);
         this.mode.addToAttributeNode(var2);
         if (var2.isLoopNode() && this.mode == LdapToCromConverterImpl.Mode.TARGET) {
            this.insertOldValueNode(var1, var2);
         }

         return var2;
      }

      private void setNodeType(LdapStructureAttribute var1, CROM var2) {
         var2.addType(1L);
         this.setCardinalityType(var1, var2);
         if (var1.isReadOnly()) {
            var2.addType(1073741824L);
         }
      }

      private void setCardinalityType(LdapStructureAttribute var1, CROM var2) {
         if (var1.getUse() == AttributeUse.OPTIONAL) {
            if (var1.getCardinality() == AttributeCardinality.MANY) {
               var2.addType(8L);
            } else {
               var2.addType(32L);
            }
         } else if (var1.getCardinality() == AttributeCardinality.MANY) {
            var2.addType(64L);
         }
      }

      private void insertValueNode(LdapStructureAttribute var1, CROM var2) {
         CROM var3 = new CROM(var2, false, "", 0, CromType.CROM_t_Ldap);
         var3.addType(34L);
         if (var1.isReadOnly()) {
            var3.addType(1073741824L);
         }

         AttributeDataType var4 = var1.getSyntaxInfo().getDataType();
         var3.setValueType(var4.toString());
      }

      private void insertOldValueNode(LdapStructureAttribute var1, CROM var2) {
         CROM var3 = new CROM(var2, false, "Old value", 1, CromType.CROM_t_Ldap);
         var3.addType(36L);
         AttributeDataType var4 = var1.getSyntaxInfo().getDataType();
         var3.setValueType(var4.toString());
      }
   }

   private static final class ClassNodeFactory {
      private final LdapToCromConverterImpl.Mode mode;

      public ClassNodeFactory(LdapToCromConverterImpl.Mode var1) {
         this.mode = var1;
      }

      public CROM createCrom(String var1) {
         CROM var2 = new CROM(null, true, var1, 0, CromType.CROM_t_Ldap);
         var2.addType(9L);
         this.addDistinguishedNameNode(var2);
         this.mode.addToClassNode(var2);
         return var2;
      }

      private void addDistinguishedNameNode(CROM var1) {
         CROM var2 = new CROM(var1, false, "Distinguished name", 0, CromType.CROM_t_Ldap);
         var2.setValueType("String");
      }
   }

   private enum Mode {
      SOURCE,
      TARGET {
         @Override
         public void addToClassNode(CROM var1) {
            LdapToCromConverterImpl.addCommandNode(var1, "Entry command");
         }
      };

      Mode() {
      }

      public void addToClassNode(CROM var1) {
      }

      public void addToAttributeNode(CROM var1) {
      }
   }
}
