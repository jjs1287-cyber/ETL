package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.IOException;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;
import org.etlforge.integration.client.project.jitterpack.UnpackException;
import org.etlforge.integration.client.project.jitterpack.VersionInfo;
import org.etlforge.io.FileUtils;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;

final class JitterPackUtils {
   private JitterPackUtils() {
   }

   static JitterPack createFromFileSilently(File var0, Version var1, ImportParameters var2) {
      try {
         return createFromFile(var0, var1, var2);
      } catch (Exception var4) {
         return null;
      }
   }

   static JitterPack createFromFile(File var0, Version var1, ImportParameters var2) throws IOException, IllegalDataFormatException, UnsupportedVersionException {
      JitterPackFormat var3 = JitterPackFormatFactory.getFormat(var1, var2);
      JitterPackDescriptor var4 = var3.extractDescriptor(var0);
      return new JitterPack(var0, var4);
   }

   static VersionInfo isPossibleJitterPack(File var0, ImportParameters var1) {
      try {
         JitterPackDescriptor var2 = new DefaultJitterPackHeader(var1).extractFromPackedData(var0);
         return var2.getVersionInfo();
      } catch (IllegalDataFormatException var3) {
      } catch (IOException var4) {
      } catch (InvalidPasswordException var5) {
      }

      return null;
   }

   static void clearUnpackDestination(File var0) throws UnpackException {
      if (var0.exists()) {
         if (var0.isFile()) {
            if (!var0.delete()) {
               throw new UnpackException("Failed to overwrite the existing file " + var0.getAbsolutePath() + ".");
            }
         } else if (var0.isDirectory()) {
            try {
               clearUnpackDestinationFolder(var0);
            } catch (IOException var2) {
               throw new UnpackException("Failed to overwrite the existing file " + var0.getAbsolutePath() + ".", var2);
            }
         }
      }
   }

   private static void clearUnpackDestinationFolder(File var0) throws IOException {
      FileUtils.cleanFolderStructure(var0, true);

      try {
         FileUtils.cleanFolderStructure(var0, false);
      } catch (IOException var2) {
      }
   }

   static JitterPackFormat getPackFormat(JitterPackParameters var0) throws JitterPackException {
      return getPackFormat(ClientInfo.getCurrentVersion(), var0);
   }

   static JitterPackFormat getPackFormat(JitterPackDescriptor var0, JitterPackParameters var1) throws JitterPackException {
      return var0 == null ? getPackFormat(var1) : getPackFormat(var0.getVersionInfo().createdIn(), var1);
   }

   private static JitterPackFormat getPackFormat(Version var0, JitterPackParameters var1) throws JitterPackException {
      try {
         return JitterPackFormatFactory.getFormat(var0, var1);
      } catch (UnsupportedVersionException var3) {
         throw new JitterPackException(
            "Failed to create the Jitterpak due to an application configuration error. Please contact Please contact etlforge support: www.etlforge.invalid",
            var3
         );
      }
   }

   static JitterPackDescriptor extractDescriptor(File var0, ImportParameters var1) throws JitterPackException {
      try {
         if (EncryptionChecker.isPossibleEncryptedJitterPack(var0) && var1.getPassword() == null) {
            return JitterPackDescriptor.UNKNOWN_ENCRYPTED;
         }

         JitterPackFormat var2 = getPackFormat(var1);
         return var2.extractDescriptor(var0);
      } catch (IOException var3) {
         throw new JitterPackException("An IO error occurred: " + var3.getMessage(), var3);
      } catch (InvalidPasswordException var4) {
         throw new JitterPackException(var4.getMessage(), var4);
      }
   }
}
