package org.etlforge.integration.client.structure.cache;

import com.google.common.base.Optional;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.io.NullWriter;

@ParametersAreNonnullByDefault
public interface InputAndOutputMappingTreeStructureCache {
   InputAndOutputMappingTreeStructureCache EMPTY = new InputAndOutputMappingTreeStructureCache() {
      @Override
      public Optional<MappingTreeStructure> getInputStructure(IntegrationEntityId var1) {
         return Optional.absent();
      }

      @Override
      public Optional<MappingTreeStructure> getOutputStructure(IntegrationEntityId var1) {
         return Optional.absent();
      }

      @Override
      public Writer getInputStructureWriter(IntegrationEntityId var1) {
         return new NullWriter();
      }

      @Override
      public Writer getOutputStructureWriter(IntegrationEntityId var1) {
         return new NullWriter();
      }

      @Override
      public void delete(IntegrationEntityId var1) {
      }

      @Override
      public void copy(IntegrationEntityId var1, IntegrationEntityId var2) {
      }
   };

   Optional<MappingTreeStructure> getInputStructure(IntegrationEntityId var1) throws DataCacheException;

   Optional<MappingTreeStructure> getOutputStructure(IntegrationEntityId var1) throws DataCacheException;

   Writer getInputStructureWriter(IntegrationEntityId var1) throws IOException;

   Writer getOutputStructureWriter(IntegrationEntityId var1) throws IOException;

   void delete(IntegrationEntityId var1);

   void copy(IntegrationEntityId var1, IntegrationEntityId var2) throws DataCacheException;
}
