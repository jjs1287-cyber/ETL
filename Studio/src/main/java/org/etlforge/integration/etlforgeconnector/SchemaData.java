package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Preconditions;
import java.io.IOException;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;

public final class SchemaData {
   private final String rootName;
   private final CompressedAndBase64EncodedString content;

   public static SchemaData fromCompressedContents(String var0, String var1) {
      return new SchemaData(var0, new CompressedAndBase64EncodedString(var1));
   }

   public static SchemaData fromOriginalContents(String var0, String var1) throws IOException {
      return new SchemaData(var0, CompressedAndBase64EncodedString.compress(var1));
   }

   public SchemaData(String var1, CompressedAndBase64EncodedString var2) {
      this.rootName = Preconditions.checkNotNull(var1, "rootName");
      this.content = Preconditions.checkNotNull(var2);
   }

   public CompressedAndBase64EncodedString getContents() {
      return this.content;
   }

   public String getRootName() {
      return this.rootName;
   }
}
