package org.etlforge.util.zip;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

public final class CompressedAndBase64EncodedString {
   private final String data;

   public static CompressedAndBase64EncodedString compress(String var0) throws IOException {
      return new CompressedAndBase64EncodedString(ZipUtils.deflateAndBase64EncodeString(var0));
   }

   public CompressedAndBase64EncodedString(String var1) {
      this.data = var1;
   }

   public String inflate() throws DataFormatException {
      return ZipUtils.base64DecodeAndInflateString(this.data);
   }

   public void inflateTo(File var1) throws IOException {
      ZipUtils.base64DecodeAndInflateToFile(this.data, var1);
   }

   public String getCompressedRepresentation() {
      return this.data;
   }

   @Override
   public int hashCode() {
      return this.data.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof CompressedAndBase64EncodedString && this.data.equals(((CompressedAndBase64EncodedString)var1).data);
   }

   @Override
   public String toString() {
      return this.data;
   }
}
