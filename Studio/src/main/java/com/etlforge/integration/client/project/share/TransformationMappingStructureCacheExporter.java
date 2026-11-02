package com.etlforge.integration.client.project.share;

import com.google.common.io.CharStreams;
import com.google.common.io.OutputSupplier;
import java.io.IOException;
import java.io.Writer;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.StructureReaderSupplier;

final class TransformationMappingStructureCacheExporter {
   private final InputAndOutputMappingTreeStructureCache from;
   private final InputAndOutputMappingTreeStructureCache to;

   public TransformationMappingStructureCacheExporter(ManagedProject var1, ManagedProject var2) {
      this(var1.getTransformationMappingStructureCache(), var2.getTransformationMappingStructureCache());
   }

   public TransformationMappingStructureCacheExporter(InputAndOutputMappingTreeStructureCache var1, InputAndOutputMappingTreeStructureCache var2) {
      this.from = var1;
      this.to = var2;
   }

   public void export(Transformation var1) throws DataCacheException, IOException {
      new TransformationMappingStructureCacheExporter.InputStructurePipe(var1).transfer();
      new TransformationMappingStructureCacheExporter.OutputStructurePipe(var1).transfer();
   }

   private class InputStructurePipe extends TransformationMappingStructureCacheExporter.Pipe {
      public InputStructurePipe(Transformation var2) {
         super(var2);
      }

      @Override
      protected MappingTreeStructure getStructure() throws DataCacheException {
         return TransformationMappingStructureCacheExporter.this.from.getInputStructure(this.tfId).orNull();
      }

      @Override
      protected Writer getWriter() throws IOException {
         return TransformationMappingStructureCacheExporter.this.to.getInputStructureWriter(this.tfId);
      }
   }

   private class OutputStructurePipe extends TransformationMappingStructureCacheExporter.Pipe {
      public OutputStructurePipe(Transformation var2) {
         super(var2);
      }

      @Override
      protected MappingTreeStructure getStructure() throws DataCacheException {
         return TransformationMappingStructureCacheExporter.this.from.getOutputStructure(this.tfId).orNull();
      }

      @Override
      protected Writer getWriter() throws IOException {
         return TransformationMappingStructureCacheExporter.this.to.getOutputStructureWriter(this.tfId);
      }
   }

   private abstract class Pipe {
      protected final TransformationId tfId;

      public Pipe(Transformation var2) {
         this.tfId = var2.getID();
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
}
