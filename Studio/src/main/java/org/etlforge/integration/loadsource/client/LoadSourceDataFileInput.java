package org.etlforge.integration.loadsource.client;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.loadsource.serverapi.SourceDataEncodingException;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.util.zip.ZipUtils;

public final class LoadSourceDataFileInput implements LoadSourceDataInput {
   private final File file;
   private final PipelinePosition[] plugins;

   public LoadSourceDataFileInput(File var1) {
      this(var1, null);
   }

   public LoadSourceDataFileInput(File var1, PipelinePosition[] var2) {
      Preconditions.checkNotNull(var1, "file");
      this.file = var1;
      this.plugins = var2 != null ? var2 : new PipelinePosition[0];
   }

   public File getFile() {
      return this.file;
   }

   @Override
   public String getCompressedBase64EncodedData() throws SourceDataEncodingException {
      try {
         if (ZipUtils.isLikelyZipped(this.file)) {
            byte[] var1 = Files.toByteArray(this.file);
            byte[] var2 = Base64.encodeBase64Chunked(var1);
            return new String(var2, Charsets.UTF_8);
         } else {
            return ZipUtils.deflateAndBase64EncodeFileContents(this.file);
         }
      } catch (IOException var3) {
         throw new SourceDataEncodingException(
            "Failed to compress and encode the contents of the file " + this.file.getAbsolutePath() + ". Reported reason: " + var3.getMessage(), var3
         );
      }
   }

   @Override
   public PipelinePosition[] getPlugins() {
      return this.plugins;
   }
}
