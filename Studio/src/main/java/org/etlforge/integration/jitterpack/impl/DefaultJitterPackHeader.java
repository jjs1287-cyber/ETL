package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.project.jitterpack.DefaultJitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;
import org.etlforge.integration.client.project.jitterpack.VersionInfo;
import org.etlforge.util.properties.PropertiesWriter;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

final class DefaultJitterPackHeader implements JitterPackHeader {
   public static final String FILE_NAME = ".jitterpak.properties";
   private static final String PROJECT_NAME = "project_name";
   private static final String COMMENT = "comment";
   private static final String CREATED_AT = "created_at";
   private static final String CREATED_VERSION = "created_with_version";
   private static final String REQUIRED_VERSION = "required_version";
   private static final String READ_ONLY = "read_only";
   private final JitterPackParameters params;

   public DefaultJitterPackHeader(JitterPackParameters var1) {
      this.params = var1;
   }

   @Override
   public JitterPackDescriptor extractFromPackedData(File var1) throws IllegalDataFormatException, IOException, InvalidPasswordException {
      DescriptorDataFactory var2 = DescriptorDataFactory.newInstance(var1, this.params.getPassword(), ".jitterpak.properties");
      DescriptorData var3 = var2.create(var1);
      return this.createDescriptorFromData(var3.getProperties(), var3.getProjectFolderName(), var3.isEncrypted());
   }

   private JitterPackDescriptor createDescriptorFromData(Properties var1, String var2, boolean var3) throws IllegalDataFormatException {
      String var4 = this.readProjectName(var1);
      String var5 = this.readComment(var1);
      Date var6 = this.readCreatedDate(var1);
      VersionInfo var7 = this.readVersionInfo(var1);
      boolean var8 = this.isReadOnly(var1);
      return new DefaultJitterPackDescriptor(var4, var7, var2, var6, var5, var3, var8);
   }

   @Override
   public void write(JitterPackDescriptor var1, File var2) throws IOException {
      Properties var3 = new Properties();
      this.writeProjectName(var1.getProjectName(), var3);
      this.writeComment(var1.getComment(), var3);
      this.writeVersionInfo(var1.getVersionInfo(), var3);
      this.writeCreatedAt(var1.createdAt(), var3);
      this.writeReadOnlyProperty(var3);
      this.persist(var2, var3);
   }

   private void persist(File var1, Properties var2) throws IOException {
      File var3 = new File(var1, ".jitterpak.properties");
      PropertiesWriter.write(var2, var3);
   }

   private void writeProjectName(String var1, Properties var2) {
      var2.put("project_name", var1);
   }

   private void writeComment(String var1, Properties var2) {
      var2.put("comment", var1);
   }

   private void writeVersionInfo(VersionInfo var1, Properties var2) {
      var2.put("created_with_version", String.valueOf(var1.createdIn().getVersionNumber()));
      var2.put("required_version", String.valueOf(var1.minimumRequired().getVersionNumber()));
   }

   private void writeCreatedAt(Date var1, Properties var2) {
      var2.put("created_at", String.valueOf(var1.getTime()));
   }

   private void writeReadOnlyProperty(Properties var1) {
      ExportParameters var2 = ParamsSanityCheck.ensureForPacking(this.params);
      var1.put("read_only", String.valueOf(var2.isPackedProjectReadOnly()));
   }

   private String readProjectName(Properties var1) throws IllegalDataFormatException {
      String var2 = var1.getProperty("project_name");
      if (var2 == null) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "The name field is missing.");
      } else {
         return var2;
      }
   }

   private String readComment(Properties var1) throws IllegalDataFormatException {
      String var2 = var1.getProperty("comment");
      if (var2 == null) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "The comment field is missing.");
      } else {
         return var2;
      }
   }

   private VersionInfo readVersionInfo(Properties var1) throws IllegalDataFormatException {
      Version var2 = this.readVersion(var1, "created_with_version");
      Version var3 = this.readVersion(var1, "required_version");
      return new DefaultVersionInfo(var2, var3);
   }

   private boolean isReadOnly(Properties var1) {
      String var2 = var1.getProperty("read_only");
      return var2 != null ? Boolean.parseBoolean(var2) : false;
   }

   private Date readCreatedDate(Properties var1) throws IllegalDataFormatException {
      String var2 = var1.getProperty("created_at");
      if (var2 == null) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "Creation date is missing.");
      }

      try {
         long var3 = Long.parseLong(var2);
         return new Date(var3);
      } catch (NumberFormatException var5) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "Creation date is invalid.");
      }
   }

   private Version readVersion(Properties var1, String var2) throws IllegalDataFormatException {
      String var3 = var1.getProperty(var2);
      if (var3 == null) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "Version information is missing.");
      }

      try {
         long var4 = Long.parseLong(var3);
         return VersionFactory.fromNumber(var4);
      } catch (NumberFormatException var6) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "Version information is invalid.");
      } catch (IllegalVersionException var7) {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "Version information is invalid.");
      }
   }

   private static String getErrorMessageHeader() {
      return "Not a valid Jitterpak: ";
   }
}
