package org.etlforge.integration.client.ui.interchange.entity.webservice;

import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.structure.cache.CacheWriter;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public final class WebServiceStructureCacher {
   private final WebServiceCallId wsCallId;
   private final InputAndOutputMappingTreeStructureCache projectCache;

   public WebServiceStructureCacher(WebServiceCallId var1, InputAndOutputMappingTreeStructureCache var2) {
      this.wsCallId = var1;
      this.projectCache = var2;
   }

   public void cacheStructures(WebServiceCallStructures var1) {
      this.cacheRequestStructure(var1.request());
      this.cacheResponseStructure(var1.response());
   }

   private void cacheRequestStructure(MappingTreeStructure var1) {
      CacheWriter var2 = CacheWriter.newInputWriter();
      this.writeJtrToCache(var1, var2);
   }

   private void writeJtrToCache(MappingTreeStructure var1, CacheWriter var2) {
      var2.setErrorHandler(new LoggingExceptionHandler(this.getClass(), ErrorLevel.INTERNAL, "Failed to cache WS structure. This is not a fatal error."));
      var2.addToCache(this.projectCache, this.wsCallId, var1);
   }

   private void cacheResponseStructure(MappingTreeStructure var1) {
      CacheWriter var2 = CacheWriter.newOutputWriter();
      this.writeJtrToCache(var1, var2);
   }
}
