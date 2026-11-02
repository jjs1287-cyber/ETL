package org.etlforge.integration.server.engine.json;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;

public final class ConversionParams {
   private final File jsonFile;
   private final File xmlFile;
   private final long txId;

   public ConversionParams(File var1, File var2, long var3) {
      Preconditions.checkNotNull(var1, "jsonFile");
      Preconditions.checkNotNull(var2, "xmlFile");
      this.jsonFile = var1;
      this.xmlFile = var2;
      this.txId = var3;
   }

   public File getJsonFile() {
      return this.jsonFile;
   }

   public void verifyJsonFileExists() throws IOException {
      if (!this.jsonFile.canRead()) {
         throw new IOException("The file " + this.jsonFile.getAbsolutePath() + " cannot be read. Does it exist?");
      }
   }

   public File getXmlFile() {
      return this.xmlFile;
   }

   public void verifyXmlFileExists() throws IOException {
      if (!this.xmlFile.canRead()) {
         throw new IOException("The file " + this.xmlFile.getAbsolutePath() + " cannot be read. Does it exist?");
      }
   }

   public long getTransformationId() {
      return this.txId;
   }
}
