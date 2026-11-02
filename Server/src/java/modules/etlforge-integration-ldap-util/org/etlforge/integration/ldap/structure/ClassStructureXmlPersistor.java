package org.etlforge.integration.ldap.structure;

import java.io.File;
import java.io.IOException;
import org.etlforge.util.persist.XmlPersistor;

public class ClassStructureXmlPersistor implements ClassStructureFilePersistor, ClassStructureStringPersistor {
   @Override
   public void store(ObjectClassStructure var1, File var2) {
      try {
         XmlPersistor var3 = XmlPersistor.newRoot("OID_" + var1.getOid());
         new PersistableObjectClassStructure(var1).save(var3);
         var3.store(var2);
      } catch (IOException var4) {
         var4.printStackTrace();
      }
   }

   @Override
   public ObjectClassStructure load(File var1) {
      try {
         XmlPersistor var2 = XmlPersistor.load(var1);
         return this.load(var2);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   @Override
   public ObjectClassStructure load(String var1) {
      try {
         XmlPersistor var2 = XmlPersistor.load(var1);
         return this.load(var2);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   @Override
   public String store(ObjectClassStructure var1) {
      XmlPersistor var2 = XmlPersistor.newRoot("OID_" + var1.getOid());
      new PersistableObjectClassStructure(var1).save(var2);
      return var2.toXml();
   }

   private ObjectClassStructure load(XmlPersistor var1) {
      PersistableObjectClassStructureFactory var2 = new PersistableObjectClassStructureFactory();
      PersistableObjectClassStructure var3 = var2.restore(var1);
      return var3.getStructure();
   }
}
