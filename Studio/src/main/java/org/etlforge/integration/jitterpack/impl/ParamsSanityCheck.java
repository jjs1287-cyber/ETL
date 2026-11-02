package org.etlforge.integration.jitterpack.impl;

import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;

final class ParamsSanityCheck {
   public static ExportParameters ensureForPacking(JitterPackParameters var0) {
      if (var0 instanceof ExportParameters) {
         return (ExportParameters)var0;
      } else {
         throw new IllegalStateException("Expected packing parameters, got unpacking parameters.");
      }
   }

   public static ImportParameters ensureForUnpacking(JitterPackParameters var0) {
      if (var0 instanceof ImportParameters) {
         return (ImportParameters)var0;
      } else {
         throw new IllegalStateException("Expected unpacking parameters, got packing parameters.");
      }
   }

   private ParamsSanityCheck() {
   }
}
