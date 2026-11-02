package org.etlforge.integration.ldap.client.cache;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.ldap.structure.ClassStructureXmlPersistor;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.io.KongaIoUtils;

final class LdapStructureFileCache implements LdapStructureCache {
   private static final String EXTENSION = ".xml";
   private final File cacheFolder;
   private final File objectClassesFile;
   private final File numericOidToNameFile;
   private Properties numericOidToNameMapper;
   private final Object entryLock;

   public LdapStructureFileCache(File var1) {
      this.cacheFolder = var1;
      this.cacheFolder.mkdirs();
      this.deleteOldFormat(this.cacheFolder);
      this.objectClassesFile = new File(this.cacheFolder, "classes.xml");
      this.numericOidToNameFile = new File(this.cacheFolder, "oid_to_name.xml");
      this.numericOidToNameMapper = this.loadNumericOidToNameMapper();
      this.entryLock = new Object();
   }

   @Override
   public ObjectClassDefinition[] getObjectClasses() {
      synchronized (this.objectClassesFile) {
         if (this.objectClassesFile.exists()) {
            return ObjectClassDefinitionPersistor.restore(this.objectClassesFile);
         }
      }

      return new ObjectClassDefinition[0];
   }

   @Override
   public ObjectClassDefinition[] getObjectClasses(NumericOid[] var1) {
      HashMap var2 = Maps.newHashMap();

      for (ObjectClassDefinition var6 : this.getObjectClasses()) {
         var2.put(var6.getOid(), var6);
      }

      int var7 = var1.length;
      ObjectClassDefinition[] var8 = new ObjectClassDefinition[var7];

      for (int var9 = 0; var9 < var7; var9++) {
         var8[var9] = (ObjectClassDefinition)var2.get(var1[var9]);
      }

      return var8;
   }

   @Override
   public void storeObjectClasses(ObjectClassDefinition[] var1) {
      synchronized (this.objectClassesFile) {
         if (var1 == null) {
            this.objectClassesFile.delete();
            this.updateNumericOidToNameMapper(new ObjectClassDefinition[0]);
         } else {
            ObjectClassDefinitionPersistor.persist(var1, this.objectClassesFile);
            this.updateNumericOidToNameMapper(var1);
         }
      }
   }

   @Override
   public String getObjectClassName(NumericOid var1) {
      return this.numericOidToNameMapper.getProperty(var1.toString());
   }

   @Override
   public ObjectClassStructure getClassStructure(NumericOid var1) {
      File var2 = this.getEntryFile(var1);
      synchronized (this.entryLock) {
         if (var2.exists()) {
            ClassStructureXmlPersistor var4 = new ClassStructureXmlPersistor();
            return var4.load(var2);
         } else {
            return null;
         }
      }
   }

   @Override
   public void storeClassStructures(ObjectClassStructure[] var1) {
      synchronized (this.entryLock) {
         for (ObjectClassStructure var6 : var1) {
            if (var6 != null) {
               this.storeClassStructure(var6);
            }
         }
      }
   }

   private void storeClassStructure(ObjectClassStructure var1) {
      File var2 = this.getEntryFile(var1);
      ClassStructureXmlPersistor var3 = new ClassStructureXmlPersistor();
      var3.store(var1, var2);
   }

   @Override
   public String getID() {
      return "org.etlforge.integration.client.component.ldap.cache.LdapStructureCache";
   }

   private Properties loadNumericOidToNameMapper() {
      Properties var1 = new Properties();
      if (!this.numericOidToNameFile.exists()) {
         return var1;
      }

      FileInputStream var2 = null;

      try {
         var2 = new FileInputStream(this.numericOidToNameFile);
         var1.loadFromXML(var2);
      } catch (FileNotFoundException var9) {
         this.handleNumericOidToNameLoadingFailure(var9);
      } catch (InvalidPropertiesFormatException var10) {
         this.handleNumericOidToNameLoadingFailure(var10);
      } catch (IOException var11) {
         this.handleNumericOidToNameLoadingFailure(var11);
      } finally {
         KongaIoUtils.close(var2);
      }

      return var1;
   }

   private void updateNumericOidToNameMapper(ObjectClassDefinition[] var1) {
      this.numericOidToNameMapper.clear();

      for (ObjectClassDefinition var5 : var1) {
         String var6 = var5.getOid().toString();
         String var7 = LdapUtils.getRepresentation(var5);
         this.numericOidToNameMapper.setProperty(var6, var7);
      }

      this.storeNumericOidToNameMapper();
   }

   private void storeNumericOidToNameMapper() {
      FileOutputStream var1 = null;

      try {
         this.numericOidToNameFile.createNewFile();
         var1 = new FileOutputStream(this.numericOidToNameFile);
         this.numericOidToNameMapper.storeToXML(var1, null);
      } catch (FileNotFoundException var7) {
         this.handleNumericOidToNameWritingFailure(var7);
      } catch (IOException var8) {
         this.handleNumericOidToNameWritingFailure(var8);
      } finally {
         KongaIoUtils.close(var1);
      }
   }

   private void handleNumericOidToNameLoadingFailure(Exception var1) {
      ErrorLog.log(LdapStructureFileCache.class, "Failed to load the OID - Name mapper", var1);
   }

   private void handleNumericOidToNameWritingFailure(Exception var1) {
      ErrorLog.log(LdapStructureFileCache.class, "Failed to update the OID - Name mapper", var1);
   }

   private File getEntryFile(ObjectClassStructure var1) {
      return this.getEntryFile(var1.getOid());
   }

   private File getEntryFile(NumericOid var1) {
      String var2 = var1.toString() + ".xml";
      return new File(this.cacheFolder, var2);
   }

   private void deleteOldFormat(File var1) {
      File[] var2 = var1.listFiles();
      if (var2 != null) {
         long var3 = this.getCutOffTimestamp();

         for (File var8 : var2) {
            long var9 = var8.lastModified();
            if (var9 < var3) {
               var8.delete();
            }
         }
      }
   }

   private long getCutOffTimestamp() {
      Calendar var1 = Calendar.getInstance();
      var1.set(1, 2007);
      var1.set(2, 6);
      var1.set(5, 18);
      var1.set(11, 11);
      var1.set(12, 30);
      return var1.getTimeInMillis();
   }
}
