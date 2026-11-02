package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import org.etlforge.integration.client.project.jitterpack.FileToJitterpackConverter;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.util.version.UnsupportedVersionException;

final class SingleFileCollector implements JitterPackCollector {
   private final Importer importer;
   private final File file;
   private final ConsoleWriter consoleWriter;

   public SingleFileCollector(Importer var1, File var2, ConsoleWriter var3) {
      assert var2 != null && var2.exists() && var2.isFile();
      this.importer = var1;
      this.file = var2;
      this.consoleWriter = var3;
   }

   @Override
   public JitterPack[] collect() {
      try {
         this.consoleWriter.log("Checking the file " + this.file.getAbsolutePath());
         FileToJitterpackConverter var1 = new FileToJitterpackConverter();
         JitterPack var2 = var1.convert(this.file, this.importer);
         return new JitterPack[]{var2};
      } catch (JitterPackException var3) {
         this.consoleWriter.log(var3);
      } catch (UnsupportedVersionException var4) {
         this.consoleWriter.log(var4);
      }

      return new JitterPack[0];
   }
}
