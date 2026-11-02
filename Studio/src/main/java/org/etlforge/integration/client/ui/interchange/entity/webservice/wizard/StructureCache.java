package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.webservice.WebServiceStructureCacher;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

final class StructureCache {
   private final Map<StructureCache.Key, WebServiceCallStructures> cachedJTRs = Collections.synchronizedMap(new HashMap<>());

   public StructureCache() {
   }

   public void add(WebServiceOperationInfo var1, WebServiceCallStructures var2) {
      this.cachedJTRs.put(new StructureCache.Key(var1), var2);
   }

   public WebServiceCallStructures get(WebServiceOperationInfo var1) {
      return this.cachedJTRs.get(new StructureCache.Key(var1));
   }

   public void moveToProjectCache(WebServiceCall var1, InputAndOutputMappingTreeStructureCache var2) {
      WebServiceOperationInfo var3 = var1.getOperationInfo();
      WebServiceCallStructures var4 = this.get(var3);
      if (var4 != null) {
         WebServiceStructureCacher var5 = new WebServiceStructureCacher(var1.getID(), var2);
         var5.cacheStructures(var4);
      }
   }

   private static final class Key {
      private static final String[] PROPERTIES = new String[]{"Identifier", "Operation", "InputName", "OutputName"};
      private final StringKeyValuePairs data = new StringKeyValuePairs();

      public Key(WebServiceOperationInfo var1) {
         for (String var5 : PROPERTIES) {
            this.data.put(var5, var1.getProperty(var5));
         }
      }

      @Override
      public boolean equals(Object var1) {
         return var1 instanceof StructureCache.Key ? this.data.equals(((StructureCache.Key)var1).data) : super.equals(var1);
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }
   }
}
