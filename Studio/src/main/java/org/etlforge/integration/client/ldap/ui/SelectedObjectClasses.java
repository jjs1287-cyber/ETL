package org.etlforge.integration.client.ldap.ui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.LdapFunctors;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.wait.WaitService;

public final class SelectedObjectClasses {
   private final LdapLocation ldapLocation;
   private final Map<NumericOid, ObjectClassDefinition> classes;

   public SelectedObjectClasses(LdapLocation var1, ObjectClassDefinition[] var2) {
      Preconditions.checkNotNull(var1, "loc");
      Preconditions.checkNotNull(var2, "classes");
      this.ldapLocation = var1;
      this.classes = Maps.newLinkedHashMap();

      for (ObjectClassDefinition var6 : var2) {
         this.classes.put(var6.getOid(), var6);
      }
   }

   public static SelectedObjectClasses fromStructure(LdapStructure var0, LdapStructureServerCache var1, WaitService var2) {
      LdapLocation var3 = var0.getLdapLocation();
      NumericOid[] var4 = var0.getStructuralObjectClassOids();
      SelectedObjectClasses.GetDefinitionsCallback var5 = new SelectedObjectClasses.GetDefinitionsCallback();
      ObjectClassDefinitionRetriever var6 = new ObjectClassDefinitionRetriever();
      var6.setLdapCache(var1);
      var6.retrieveClassDefinitions(var3, var4, var5, true, var2);
      ObjectClassDefinition[] var7 = var5.getClassDefinitions();
      if (var7 == null) {
         var7 = new ObjectClassDefinition[0];
      }

      return new SelectedObjectClasses(var3, var7);
   }

   public LdapLocation getLdapLocation() {
      return this.ldapLocation;
   }

   public ObjectClassDefinition[] getClassDefinitions() {
      return this.classes.values().toArray(new ObjectClassDefinition[this.classes.size()]);
   }

   public NumericOid[] getClassDefinitionOids() {
      return this.classes.keySet().toArray(new NumericOid[this.classes.size()]);
   }

   public LdapStructure asLdapStructure() {
      ObjectClassDefinition[] var1 = this.getClassDefinitions();
      LdapStructureEntry[] var2 = Arrays2.transform(var1, LdapStructureEntry.class, LdapFunctors.CLASS_TO_ENTRY);
      LdapStructure var3 = new LdapStructure(this.ldapLocation);
      var3.setClassEntries(var2);
      return var3;
   }

   public boolean contains(ObjectClassDefinition var1) {
      return this.classes.containsKey(var1.getOid());
   }

   public ObjectClassDefinition getClassDefinition(NumericOid var1) {
      return this.classes.get(var1);
   }

   public int size() {
      return this.classes.size();
   }

   private static final class GetDefinitionsCallback implements GetObjectClassesCallback {
      private final CountDownLatch latch = new CountDownLatch(1);
      private ObjectClassDefinition[] classes;

      private GetDefinitionsCallback() {
      }

      public ObjectClassDefinition[] getClassDefinitions() {
         try {
            this.latch.await();
            return this.classes;
         } catch (InterruptedException var2) {
            var2.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
         }
      }

      @Override
      public void objectClassesRetrieved(ObjectClassDefinition[] var1) {
         this.classes = var1;
         this.latch.countDown();
      }

      @Override
      public void cancelled() {
         this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         this.latch.countDown();
      }
   }
}
