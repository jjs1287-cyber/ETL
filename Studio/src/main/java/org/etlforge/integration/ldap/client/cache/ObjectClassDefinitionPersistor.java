package org.etlforge.integration.ldap.client.cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassType;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;
import org.xml.sax.SAXException;

final class ObjectClassDefinitionPersistor {
   private static final String ROOT = "ObjectClassDefinitions";
   private static final String CLASS_DEFINITION = "Class";
   private static final String OID = "oid";
   private static final String NAME = "name";
   private static final String DESCRIPTION = "desc";
   private static final String OBSOLETE = "obsolete";
   private static final String TYPE = "type";

   static void persist(ObjectClassDefinition[] var0, File var1) {
      try {
         XmlPersistor var2 = XmlPersistor.newRoot("ObjectClassDefinitions");

         for (ObjectClassDefinition var6 : var0) {
            persistDefinition(var6, var2);
         }

         var2.store(var1);
      } catch (IOException var7) {
         logPersistError(var7);
      }
   }

   private static void logPersistError(Exception var0) {
      ErrorLog.log(ObjectClassDefinition.class, "Failed to store object class definitions in local cache", var0);
   }

   private static void persistDefinition(ObjectClassDefinition var0, Persistor var1) {
      Persistor var2 = var1.createChild("Class");
      var2.putString("oid", var0.getOid().toString());
      var2.putString("name", var0.getName());
      var2.putString("desc", var0.getDescription());
      var2.putBoolean("obsolete", var0.isObsolete());
      var2.putString("type", var0.getType().name());
   }

   static ObjectClassDefinition[] restore(File var0) {
      try {
         XmlPersistor var1 = XmlPersistor.load(var0);
         ArrayList var2 = new ArrayList();

         for (Persistor var4 : var1.getChildren("Class")) {
            try {
               ObjectClassDefinition var5 = restoreDefinition(var4);
               var2.add(var5);
            } catch (RuntimeException var6) {
               ApplicationLog.getMainLogger().severe("Invalid data encountered in LDAP object class cache, stored in the file " + var0);
            }
         }

         return (ObjectClassDefinition[]) var2.toArray(new ObjectClassDefinition[var2.size()]);
      } catch (IOException var7) {
         logRestoreError(var7);
      } catch (SAXException var8) {
         logRestoreError(var8);
      }

      return new ObjectClassDefinition[0];
   }

   private static void logRestoreError(Exception var0) {
      ErrorLog.log(ObjectClassDefinition.class, "Failed to restore object class definitions from local cache", var0);
   }

   private static ObjectClassDefinition restoreDefinition(Persistor var0) {
      String var1 = var0.getString("oid");
      String var2 = var0.getString("name");
      String var3 = var0.getString("desc");

      ObjectClassType var4;
      try {
         var4 = ObjectClassType.valueOf(var0.getString("type"));
      } catch (RuntimeException var6) {
         var4 = ObjectClassType.STRUCTURAL;
      }

      boolean var5 = var0.getBoolean("obsolete");
      return new ObjectClassDefinitionPersistor.ClassDefImpl(var1, var2, var4, var3, var5);
   }

   private ObjectClassDefinitionPersistor() {
   }

   private static class ClassDefImpl implements ObjectClassDefinition {
      private final NumericOid oid;
      private final String name;
      private final ObjectClassType type;
      private final String description;
      private final boolean isObsolete;

      public ClassDefImpl(String var1, String var2, ObjectClassType var3, String var4, boolean var5) {
         checkArg(var1, "oid");
         checkArg(var3, "type");
         this.oid = new NumericOid(var1);
         this.name = var2;
         this.type = var3;
         this.description = var4 != null ? var4 : "";
         this.isObsolete = var5;
      }

      @Override
      public String getDescription() {
         return this.description;
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public NumericOid getOid() {
         return this.oid;
      }

      @Override
      public ObjectClassType getType() {
         return this.type;
      }

      @Override
      public boolean isObsolete() {
         return this.isObsolete;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof ObjectClassDefinition ? this.oid.equals(((ObjectClassDefinition)var1).getOid()) : false;
         }
      }

      @Override
      public int hashCode() {
         return this.oid.hashCode();
      }

      @Override
      public String toString() {
         return LdapUtils.getRepresentation(this);
      }

      private static void checkArg(Object var0, String var1) {
         if (var0 == null) {
            throw new NullPointerException(var1);
         }
      }
   }
}
