package org.etlforge.integration.server.implementation.webservice.interchange.ldap.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.ldap.ui.DefaultClassStructureRetrieverCallback;
import org.etlforge.integration.client.ldap.ui.ObjectClassStructureRetriever;
import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.ldap.LdapStructureTreeNodeFactory;
import org.etlforge.integration.ldap.client.StructureAttributeFilter;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.AttributeComparators;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.integration.ldap.structure.LdapToCromConverter;
import org.etlforge.integration.ldap.structure.LdapToCromConverterFactory;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.mapping.CromToMappingStructureWriter;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;
import org.etlforge.ui.wait.WaitService;

abstract class MappingStructureBuilder {
   public abstract void create(LdapStructure var1, GetMappingStructureCallback var2);

   public static MappingStructureBuilder newSourceStructureFactory() {
      return new MappingStructureBuilder.FactoryImpl(false);
   }

   public static MappingStructureBuilder newTargetStructureFactory() {
      return new MappingStructureBuilder.FactoryImpl(true);
   }

   private static final class FactoryImpl extends MappingStructureBuilder {
      private final boolean forTarget;

      public FactoryImpl(boolean var1) {
         this.forTarget = var1;
      }

      @Override
      public void create(LdapStructure var1, GetMappingStructureCallback var2) {
         ObjectClassStructure[] var3 = this.getClassStructures(var1, var2);
         if (var3 != null) {
            try {
               CROM var4 = this.createCromTree(var1, var3, this.forTarget);
               String var5 = this.convertCromTreeToXml(var4);
               MappingTreeStructure var6 = MappingTreeStructureFactory.fromString(var5);
               var2.jtrGenerated(var6);
            } catch (IOException var7) {
               var2.caught(var7);
            }
         }
      }

      private ObjectClassStructure[] getClassStructures(LdapStructure var1, GetMappingStructureCallback var2) {
         DefaultClassStructureRetrieverCallback var3 = new DefaultClassStructureRetrieverCallback();
         ObjectClassStructureRetriever var4 = new ObjectClassStructureRetriever();
         LdapStructureServerCache var5 = getLdapCache();
         var4.setLdapCache(var5);
         var4.retrieveStructures(var1, var3, true, WaitService.NONE);
         ObjectClassStructure[] var6 = var3.getStructures();
         var3.transferStateTo(var2);
         return var6;
      }

      private static LdapStructureServerCache getLdapCache() {
         DataCacheService var0 = Application.getCacheService();
         return var0.getCache(LdapStructureServerCache.class);
      }

      private CROM createCromTree(LdapStructure var1, ObjectClassStructure[] var2, boolean var3) {
         CROM var4 = new CROM(null, true, "LDAP", 0, CromType.CROM_t_Ldap);
         var4.addType(32769L);
         LdapToCromConverter var5 = var3 ? LdapToCromConverterFactory.newTargetStructureConverter() : LdapToCromConverterFactory.newSourceStructureConverter();
         var5.setAttributeOrder(AttributeComparators.nameOrder(true));
         StructureAttributeFilter var6 = new StructureAttributeFilter(var1);

         for (LdapStructureEntryNode var8 : this.getEntryNodes(var1, var2)) {
            CROM var9 = var5.toCrom(var8, var6);
            var4.addChild(var9);
         }

         return var4;
      }

      private Iterable<LdapStructureEntryNode> getEntryNodes(LdapStructure var1, ObjectClassStructure[] var2) {
         LdapStructureTreeNodeFactory var3 = new LdapStructureTreeNodeFactory(var1, var2);
         List var4 = var3.getEntryNodes();
         Collections.sort(var4, new Comparator<LdapStructureEntryNode>() {
            public int compare(LdapStructureEntryNode var1, LdapStructureEntryNode var2x) {
               String var3x = var1.getName();
               if (var3x == null) {
                  var3x = var1.getEntry().getOid().toString();
               }

               String var4x = var2x.getName();
               if (var4x == null) {
                  var4x = var2x.getEntry().getOid().toString();
               }

               return var3x.compareToIgnoreCase(var4x);
            }
         });
         return var4;
      }

      private String convertCromTreeToXml(CROM var1) throws IOException {
         StringWriter var2 = new StringWriter();
         CromToMappingStructureWriter var3 = new CromToMappingStructureWriter(var1, new CROMHeader("Ldap"));
         var3.writeXml(var2);
         return var2.getBuffer().toString();
      }
   }
}
