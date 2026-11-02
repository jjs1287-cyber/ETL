package org.etlforge.integration.structure.crom;

import java.io.Writer;
import org.etlforge.integration.structure.mapping.StringIndexMap;

final class NullExtension extends CROM_ext {
   static final CROM_ext INSTANCE = new NullExtension();

   private NullExtension() {
   }

   @Override
   public void writeXml(Writer var1, StringIndexMap var2) {
   }

   @Override
   public CROM_ext clone() {
      return INSTANCE;
   }
}
