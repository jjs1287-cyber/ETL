package org.etlforge.integration.client.structure.cache;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.ui.UiThreadSupplier;

public abstract class CacheWriter {
   private ExceptionHandler<? super Exception> errorHandler = ExceptionHandler.STANDARD_ERR;

   public static CacheWriter newInputWriter() {
      return new CacheWriter() {
         @Override
         protected Writer getWriter(InputAndOutputMappingTreeStructureCache var1, IntegrationEntityId var2) throws IOException {
            return var1.getInputStructureWriter(var2);
         }
      };
   }

   public static CacheWriter newOutputWriter() {
      return new CacheWriter() {
         @Override
         protected Writer getWriter(InputAndOutputMappingTreeStructureCache var1, IntegrationEntityId var2) throws IOException {
            return var1.getOutputStructureWriter(var2);
         }
      };
   }

   public final void addToCache(InputAndOutputMappingTreeStructureCache var1, IntegrationEntityId var2, MappingTreeStructure var3) {
      Preconditions.checkNotNull(var1, "cache");
      Preconditions.checkNotNull(var2, "itemId");
      if (var3 != null) {
         try {
            CacheWriter.StructureReaderSupplier var4 = new CacheWriter.StructureReaderSupplier(var3);
            OutputSupplier var5 = this.getOutputSupplier(var1, var2);
            CharStreams.copy(var4, var5);
         } catch (IOException var6) {
            this.errorHandler.caught(var6);
         }
      }
   }

   private OutputSupplier<Writer> getOutputSupplier(final InputAndOutputMappingTreeStructureCache var1, final IntegrationEntityId var2) {
      return new OutputSupplier<Writer>() {
         public Writer getOutput() throws IOException {
            return CacheWriter.this.getWriter(var1, var2);
         }
      };
   }

   protected abstract Writer getWriter(InputAndOutputMappingTreeStructureCache var1, IntegrationEntityId var2) throws IOException;

   public final void setErrorHandler(ExceptionHandler<? super Exception> var1) {
      if (var1 == null) {
         this.errorHandler = ExceptionHandler.STANDARD_ERR;
      } else {
         this.errorHandler = var1;
      }
   }

   private static class StructureReaderSupplier implements InputSupplier<Reader> {
      private final MappingTreeStructure structure;

      public StructureReaderSupplier(MappingTreeStructure var1) {
         this.structure = var1;
      }

      public Reader getInput() throws IOException {
         try {
            return UiThreadSupplier.call(new Supplier<Reader>() {
               public Reader get() {
                  try {
                     return StructureReaderSupplier.this.structure.getReader();
                  } catch (IOException var2) {
                     throw new RuntimeException(var2);
                  }
               }
            });
         } catch (RuntimeException var2) {
            if (var2.getCause() instanceof IOException) {
               throw (IOException)var2.getCause();
            } else {
               throw var2;
            }
         }
      }
   }
}
