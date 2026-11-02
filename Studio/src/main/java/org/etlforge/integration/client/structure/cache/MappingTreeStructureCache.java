package org.etlforge.integration.client.structure.cache;

import com.google.common.base.Optional;
import java.io.IOException;
import java.io.Writer;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

interface MappingTreeStructureCache {
   Optional<MappingTreeStructure> retrieve(Object var1) throws DataCacheException;

   Writer getWriter(Object var1) throws IOException;

   void delete(Object var1);

   void copy(Object var1, Object var2) throws DataCacheException;
}
