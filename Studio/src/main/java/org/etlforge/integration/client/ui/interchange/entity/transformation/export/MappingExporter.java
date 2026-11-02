package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;

public interface MappingExporter {
   void exportMappings(Transformation var1);

   void exportMultipleFolders(Transformation var1);

   final class Factory {
      public static MappingExporter create(TransformationPage var0) {
         return new MappingImporterExporterImpl(var0);
      }

      private Factory() {
      }
   }
}
