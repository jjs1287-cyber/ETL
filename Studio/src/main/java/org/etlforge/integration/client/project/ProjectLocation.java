package org.etlforge.integration.client.project;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.info.ClientEdition;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.version.Version;

public final class ProjectLocation implements Serializable {
   private final String name;
   private File file;
   private Properties properties;

   public ProjectLocation(String var1, File var2) {
      this.name = var1;
      this.file = new File(var2, var1 + ".jip");
      this.updateProperties();
   }

   public IntegrationProjectId getProjectId() {
      if (this.properties != null) {
         String var1 = this.properties.getProperty("project-id");
         return new IntegrationProjectId(var1);
      } else {
         return null;
      }
   }

   public String getProjectName() {
      return this.name;
   }

   public File getLocation() {
      return this.file.getParentFile();
   }

   public String getLocationPath() {
      return this.getLocation().getAbsolutePath();
   }

   public Date getLastModified() {
      Date var1 = this.readDateProperty("modified");
      if (var1 == null) {
         long var2 = this.file.exists() ? this.file.lastModified() : this.getLocation().lastModified();
         var1 = new Date(var2);
      }

      return var1;
   }

   public Date getLastAccessed() {
      Date var1 = this.readDateProperty("accessed");
      return var1 != null ? var1 : this.getLastModified();
   }

   public Version getVersionRequired() {
      return this.readVersionProperty("requires_version");
   }

   public Version getVersionSavedIn() {
      return this.readVersionProperty("saved-version");
   }

   private Version readVersionProperty(String var1) {
      if (this.properties != null) {
         try {
            String var2 = this.properties.getProperty(var1);
            return VersionFactory.fromString(var2);
         } catch (Exception var3) {
         }
      }

      return Version.UNKNOWN;
   }

   public ClientEdition getEditionSavedIn() {
      ClientEdition var1 = ClientEdition.UNKNOWN;
      if (this.properties != null) {
         String var2 = this.properties.getProperty("saved-edition");
         if (var2 != null) {
            return EnumWithIdLookup.lookupOr(var2, var1);
         }
      }

      return var1;
   }

   public void deleteFromDisk() {
      try {
         FileUtils.deleteDirectory(this.file.getParentFile());
      } catch (IOException var2) {
         ErrorLog.log(ProjectLocation.class, "Failed to delete the project: " + var2.getMessage(), var2);
      }
   }

   public boolean exists() {
      return this.file.getParentFile().exists();
   }

   public void updateProjectFileWithName(String var1) throws IOException {
      File var2 = new File(this.file.getParent(), var1 + ".jip");
      if (!this.file.renameTo(var2)) {
         throw new IOException("Failed to rename the project file");
      }

      this.file = var2;
      ProjectFile var3 = ProjectFile.createForExistingProject(var1, this.file.getParentFile(), false);
      var3.touch();
      if (this.file.exists()) {
         this.properties = this.loadProjectProperties();
      }
   }

   public void updateProperties() {
      this.properties = this.file.exists() ? this.loadProjectProperties() : null;
   }

   private Date readDateProperty(String var1) {
      if (this.properties != null) {
         try {
            String var2 = this.properties.getProperty(var1);
            long var3 = Long.parseLong(var2);
            return new Date(var3);
         } catch (NumberFormatException var5) {
         }
      }

      return null;
   }

   private Properties loadProjectProperties() {
      try {
         return PropertiesReader.read(this.file);
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   @Override
   public String toString() {
      return this.file.getAbsolutePath();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof ProjectLocation) {
         ProjectLocation var2 = (ProjectLocation)var1;
         return this.file.equals(var2.file);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.file.hashCode();
   }
}
