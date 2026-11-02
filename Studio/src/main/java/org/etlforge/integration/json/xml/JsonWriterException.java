package org.etlforge.integration.json.xml;

import java.io.IOException;

final class JsonWriterException extends RuntimeException {
   public JsonWriterException(IOException var1) {
      super(var1);
   }

   public IOException getIOException() {
      return (IOException)this.getCause();
   }
}
