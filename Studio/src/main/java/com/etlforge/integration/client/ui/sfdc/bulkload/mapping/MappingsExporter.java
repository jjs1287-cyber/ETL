package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.collect.ImmutableList;
import com.google.common.io.OutputSupplier;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.properties.PropertiesWriter;

final class MappingsExporter {
   private final OutputSupplier<? extends OutputStream> outputSupplier;
   private final ImmutableList<Mapping> mappings;
   private volatile boolean showSuccessMessage = true;

   public MappingsExporter(OutputSupplier<? extends OutputStream> var1, List<Mapping> var2) {
      this.outputSupplier = var1;
      this.mappings = ImmutableList.copyOf(var2);
   }

   public void setShowSuccessMessage(boolean var1) {
      this.showSuccessMessage = var1;
   }

   public Computation<Void> getComputation() {
      MappingsExporter.Writer var1 = new MappingsExporter.Writer();
      Receiver var2 = new Receiver<Void>() {
         public void handle(Void var1) {
            if (MappingsExporter.this.showSuccessMessage) {
               Alert.info("The mappings have been saved.", "Mappings Saved");
            }
         }
      };
      return Computation.create(var1, var2);
   }

   private class Writer implements Callable<Void> {
      private Writer() {
      }

      public Void call() throws Exception {
         Properties var1 = MapListToPropertiesTranslator.toProperties(MappingsExporter.this.mappings);
         PropertiesWriter.write(var1, MappingsExporter.this.outputSupplier);
         return null;
      }
   }
}
