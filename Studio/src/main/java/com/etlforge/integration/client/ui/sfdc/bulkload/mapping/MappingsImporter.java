package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.io.InputSupplier;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.properties.PropertiesReader;

final class MappingsImporter {
   private final InputSupplier<? extends InputStream> inputSupplier;
   private final HeaderMappingsModel model;

   public MappingsImporter(InputSupplier<? extends InputStream> var1, HeaderMappingsModel var2) {
      this.inputSupplier = var1;
      this.model = var2;
   }

   public Computation<List<Mapping>> getComputation() {
      MappingsImporter.Reader var1 = new MappingsImporter.Reader();
      Receiver var2 = new Receiver<List<Mapping>>() {
         public void handle(List<Mapping> var1) {
            MappingsImporter.this.model.setMapList(var1);
         }
      };
      return Computation.create(var1, var2);
   }

   private class Reader implements Callable<List<Mapping>> {
      private Reader() {
      }

      public List<Mapping> call() throws Exception {
         Properties var1 = PropertiesReader.read(MappingsImporter.this.inputSupplier);
         return MapListToPropertiesTranslator.toMapList(var1, MappingsImporter.this.model);
      }
   }
}
