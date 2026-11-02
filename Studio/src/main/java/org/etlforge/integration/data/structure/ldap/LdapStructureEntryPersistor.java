package org.etlforge.integration.data.structure.ldap;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureAttributePersistor;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.util.persist.Persistor;

public class LdapStructureEntryPersistor {
   private static final String OID = "oid";
   private static final String ATTRIBUTES = "Attributes";
   private static final String ATTRIBUTE = "Attribute";
   private static final String VALUE = "Value";
   private static final String AUX = "AuxClass";
   private static final String EXT_OBJECT = "ExtensibleObject";

   public void writeTo(LdapStructureEntry var1, Persistor var2) {
      this.writeOid(var1, var2);
      this.writeAttributes(var1, var2);
      this.writeAuxClasses(var1, var2);
      this.writeExtendedAttributes(var1, var2);
   }

   private void writeOid(LdapStructureEntry var1, Persistor var2) {
      var2.putString("oid", var1.getOid().toString());
   }

   private void writeAttributes(LdapStructureEntry var1, Persistor var2) {
      String[] var3 = var1.getAttributeNames();
      if (var3 != null) {
         Persistor var4 = var2.createChild("Attributes");

         for (String var8 : var3) {
            Persistor var9 = var4.createChild("Attribute");
            var9.putString("Value", var8);
         }
      }
   }

   private void writeAuxClasses(LdapStructureEntry var1, Persistor var2) {
      for (NumericOid var6 : var1.getAuxiliaryClasses()) {
         var2.createChild("AuxClass").putString("oid", var6.toString());
      }
   }

   private void writeExtendedAttributes(LdapStructureEntry var1, Persistor var2) {
      Set var3 = var1.getExtendedAttributes();
      if (!var3.isEmpty()) {
         Persistor var4 = var2.createChild("ExtensibleObject");

         for (Object __etlItem55 : var3) {
            LdapStructureAttribute var6 = (LdapStructureAttribute) __etlItem55;
            LdapStructureAttributePersistor.write(var6, var4);
         }
      }
   }

   public LdapStructureEntry restoreFrom(Persistor var1) {
      NumericOid var2 = this.readOid(var1);
      String[] var3 = this.readAttributes(var1);
      NumericOid[] var4 = this.readAuxClasses(var1);
      LdapStructureAttribute[] var5 = this.readExtendedAttributes(var1);
      return new LdapStructureEntry(var2, var3, var4, var5);
   }

   private NumericOid readOid(Persistor var1) {
      String var2 = var1.getString("oid");
      if (var2 == null) {
         throw new IllegalArgumentException("Invalid persistor: no OID value");
      } else {
         return new NumericOid(var2);
      }
   }

   private String[] readAttributes(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Attributes");
      if (var2 == null) {
         return null;
      }

      ArrayList var3 = Lists.newArrayList();

      for (Persistor var5 : var2.getChildren("Attribute")) {
         var3.add(var5.getString("Value"));
      }

      return (String[]) var3.toArray(new String[var3.size()]);
   }

   private NumericOid[] readAuxClasses(Persistor var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Persistor var4 : var1.getChildren("AuxClass")) {
         var2.add(new NumericOid(var4.getString("oid")));
      }

      return (NumericOid[]) var2.toArray(new NumericOid[var2.size()]);
   }

   private LdapStructureAttribute[] readExtendedAttributes(Persistor var1) {
      Persistor var2 = var1.getFirstChild("ExtensibleObject");
      if (var2 == null) {
         return null;
      }

      List var3 = LdapStructureAttributePersistor.readMultipleAttributes(var2);
      return (LdapStructureAttribute[]) var3.toArray(new LdapStructureAttribute[var3.size()]);
   }
}
