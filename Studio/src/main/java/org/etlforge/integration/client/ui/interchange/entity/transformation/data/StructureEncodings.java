package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import com.google.common.base.Charsets;
import java.nio.charset.Charset;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;

public final class StructureEncodings {
   public static Charset getCharset(DataStructure var0) {
      return getCharset(var0.getStructureType());
   }

   public static Charset getCharset(DataStructureType var0) {
      return var0.isTextBased() ? Charsets.ISO_8859_1 : Charsets.UTF_8;
   }

   private StructureEncodings() {
   }
}
