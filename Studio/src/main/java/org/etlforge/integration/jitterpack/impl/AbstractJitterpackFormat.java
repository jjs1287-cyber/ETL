package org.etlforge.integration.jitterpack.impl;

import de.schlichtherle.io.ArchiveException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.jitterpack.DefaultJitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;
import org.etlforge.integration.client.project.jitterpack.UnpackException;
import org.etlforge.integration.client.project.jitterpack.VersionInfo;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;
import org.etlforge.util.zip.KongaZipException;

abstract class AbstractJitterpackFormat implements JitterPackFormat {
   private final JitterPackHeader header;
   private final JitterPackParameters params;

   AbstractJitterpackFormat(JitterPackParameters var1) {
      this.params = var1;
      this.header = new DefaultJitterPackHeader(var1);
   }

   @Override
   public JitterPackDescriptor extractDescriptor(File var1) throws IllegalDataFormatException, IOException, InvalidPasswordException {
      return this.header.extractFromPackedData(var1);
   }

   @Override
   public ProjectDescriptor unpack(File var1, File var2) throws IllegalDataFormatException, UnpackException, UnsupportedVersionException {
      this.logUnpackRequest(var1, var2);

      try {
         JitterPackDescriptor var3 = this.extractDescriptor(var1);
         this.checkVersion(var3);
         String var13 = var3.getFolderName();
         File var5 = this.clearDestinationFolder(var2, var13);
         this.createAndUnzipProjectFolder(var1, var2);
         String var6 = var3.getProjectName();
         String var7 = var3.getComment();
         return new ProjectDescriptor(var6, var5, var7);
      } catch (KongaZipException var8) {
         UnpackException var12 = new UnpackException("An error occurred when unpacking the Jitterpak: " + var8.getMessage(), var8);
         this.logUnpackException(var12);
         throw var12;
      } catch (IOException var9) {
         UnpackException var11 = new UnpackException("An IO error occurred: " + var9.getMessage(), var9);
         this.logUnpackException(var11);
         throw var11;
      } catch (InvalidPasswordException var10) {
         UnpackException var4 = new UnpackException(var10.getMessage(), var10);
         this.logUnpackException(var4);
         throw var4;
      }
   }

   private void logUnpackRequest(File var1, File var2) {
      JitterPackLog.getLogger().info("Received request to unpack the Jitterpak " + var1.getAbsolutePath() + " to the folder " + var2.getAbsolutePath());
   }

   private void logUnpackException(Exception var1) {
      JitterPackLog.getLogger().throwing(this.getClass().getName(), "unpack", var1);
   }

   private void createAndUnzipProjectFolder(File var1, File var2) throws ArchiveException, InvalidPasswordException {
      TrueZipUtils.unzip(var1, var2, this.params.getPassword());
   }

   private void checkVersion(JitterPackDescriptor var1) throws UnsupportedVersionException {
      Version var2 = ClientInfo.getCurrentVersion();
      Version var3 = var1.getVersionInfo().minimumRequired();
      if (var2.isEarlierThan(var3)) {
         throw new UnsupportedVersionException(var2, "This Jitterpak must be opened in version " + var3 + " or later. The current version is " + var2 + ".");
      }
   }

   private File clearDestinationFolder(File var1, String var2) throws UnpackException {
      File var3 = new File(var1, var2);
      JitterPackUtils.clearUnpackDestination(var3);
      return var3;
   }

   @Override
   public JitterPack pack(ManagedProject var1, String var2, File var3) throws JitterPackException {
      try {
         File var4 = var1.getLocation();
         JitterPackDescriptor var5 = this.createDescriptorForWriting(var1, var2);
         this.header.write(var5, var4);
         this.zipProject(var4, var3);
         return new JitterPack(var3, var5);
      } catch (KongaZipException var6) {
         throw new JitterPackException("An error occurred: " + var6.getMessage(), var6);
      } catch (IOException var7) {
         throw new JitterPackException("An IO error occurred: " + var7.getMessage(), var7);
      }
   }

   protected abstract Version getRequiredVersion();

   private JitterPackDescriptor createDescriptorForWriting(ManagedProject var1, String var2) {
      VersionInfo var3 = this.createVersionInfo(var1);
      ExportParameters var4 = ParamsSanityCheck.ensureForPacking(this.params);
      return new DefaultJitterPackDescriptor(
         var1.getProject().getName(), var3, var1.getLocation().getName(), new Date(), var2, var4.getPassword() != null, var4.isPackedProjectReadOnly()
      );
   }

   private VersionInfo createVersionInfo(ManagedProject var1) {
      Version var2 = this.getRequiredVersionForProject(var1);
      return new DefaultVersionInfo(ClientInfo.getCurrentVersion(), var2);
   }

   private Version getRequiredVersionForProject(ManagedProject var1) {
      Version var2 = this.getRequiredVersion();
      Version var3 = var1.getProjectFile().getMinimumVersionRequired();
      return var2.isLaterThan(var3) ? var2 : var3;
   }

   private void zipProject(File var1, File var2) throws ArchiveException, IOException {
      TrueZipUtils.zip(var1, var2, ParamsSanityCheck.ensureForPacking(this.params));
   }
}
