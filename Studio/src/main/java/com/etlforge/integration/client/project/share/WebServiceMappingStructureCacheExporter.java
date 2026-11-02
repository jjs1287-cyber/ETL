package com.etlforge.integration.client.project.share;

import com.google.common.io.CharStreams;
import com.google.common.io.OutputSupplier;
import java.io.IOException;
import java.io.Writer;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.StructureReaderSupplier;

final class WebServiceMappingStructureCacheExporter {
   private final InputAndOutputMappingTreeStructureCache from;
   private final InputAndOutputMappingTreeStructureCache to;

   public WebServiceMappingStructureCacheExporter(ManagedProject var1, ManagedProject var2) {
      this(var1.getWebServiceMappingStructureCache(), var2.getWebServiceMappingStructureCache());
   }

   public WebServiceMappingStructureCacheExporter(InputAndOutputMappingTreeStructureCache var1, InputAndOutputMappingTreeStructureCache var2) {
      this.from = var1;
      this.to = var2;
   }

   public void export(WebServiceCall var1) throws DataCacheException, IOException {
      new WebServiceMappingStructureCacheExporter.RequestStructurePipe(var1).transfer();
      new WebServiceMappingStructureCacheExporter.ResponseStructurePipe(var1).transfer();
   }

   private abstract class Pipe {
      protected final WebServiceCallId wsCallId;

      public Pipe(WebServiceCall var2) {
         this.wsCallId = var2.getID();
      }

      protected abstract MappingTreeStructure getStructure() throws DataCacheException;

      protected abstract Writer getWriter() throws IOException;

      public final void transfer() throws DataCacheException, IOException {
         MappingTreeStructure var1 = this.getStructure();
         if (var1 != null) {
            StructureReaderSupplier var2 = new StructureReaderSupplier(var1);
            OutputSupplier var3 = new OutputSupplier<Writer>() {
               public Writer getOutput() throws IOException {
                  return Pipe.this.getWriter();
               }
            };
            CharStreams.copy(var2, var3);
         }
      }
   }

   private class RequestStructurePipe extends WebServiceMappingStructureCacheExporter.Pipe {
      public RequestStructurePipe(WebServiceCall var2) {
         super(var2);
      }

      @Override
      protected MappingTreeStructure getStructure() throws DataCacheException {
         return WebServiceMappingStructureCacheExporter.this.from.getInputStructure(this.wsCallId).orNull();
      }

      @Override
      protected Writer getWriter() throws IOException {
         return WebServiceMappingStructureCacheExporter.this.to.getInputStructureWriter(this.wsCallId);
      }
   }

   private class ResponseStructurePipe extends WebServiceMappingStructureCacheExporter.Pipe {
      public ResponseStructurePipe(WebServiceCall var2) {
         super(var2);
      }

      @Override
      protected MappingTreeStructure getStructure() throws DataCacheException {
         return WebServiceMappingStructureCacheExporter.this.from.getOutputStructure(this.wsCallId).orNull();
      }

      @Override
      protected Writer getWriter() throws IOException {
         return WebServiceMappingStructureCacheExporter.this.to.getOutputStructureWriter(this.wsCallId);
      }
   }
}
