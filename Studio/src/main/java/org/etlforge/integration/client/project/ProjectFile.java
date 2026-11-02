package org.etlforge.integration.client.project;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.info.ClientEdition;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.info.ProductNames;
import org.etlforge.integration.client.info.Versions;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.DownloadInfo;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class ProjectFile {
   public static final String FILE_EXTENSION = ".jip";
   public static final FileFilter FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile() && ".jip".equals(FileUtils.getExtension(var1, true));
      }
   };
   private static final String FILE_DESCRIPTION = ProductNames.COMPANY + " Integration Project";
   public static final String CREATED_WITH_VERSION = "client-version";
   public static final String SAVED_IN_VERSION = "saved-version";
   public static final String SAVED_IN_EDITION = "saved-edition";
   public static final String REQUIRES_VERSION = "requires_version";
   public static final String PROJECT_ID = "project-id";
   public static final String STRUCTURE_INFO = "structure-file";
   public static final String DELETED_INFO = "deleted-file";
   public static final String UI_STATE_INFO = "uistate-file";
   public static final String HAS_BEEN_DEPLOYED = "deployed";
   public static final String DEPLOY_DIRTY = "deploy-dirty";
   public static final String CREATED = "created";
   public static final String ACCESSED = "accessed";
   public static final String MODIFIED = "modified";
   public static final String DOWNLADED_FROM = "downloaded-from";
   public static final String DOWNLADED_AT = "downloaded-at";
   public static final String DESCRIPTION = "description";
   public static final String JITTERPACK_COMMENT = "jitterpack-comment";
   public static final String DEPLOYABLE = "deployable";
   public static final String PIPELINE_PLUGIN_INFO = "pipeline-plugin-file";
   private static final ProjectFile.DefaultProperty[] DEFAULT_PROPERTIES = new ProjectFile.DefaultProperty[]{
      new ProjectFile.DefaultProperty("structure-file", ".xml"),
      new ProjectFile.DefaultProperty("deleted-file", "_deleted.xml"),
      new ProjectFile.DefaultProperty("uistate-file", "_ui.xml"),
      new ProjectFile.DefaultProperty("pipeline-plugin-file", "_pipeline_plugins.xml")
   };
   private String projectName;
   private final File parentFolder;
   private final Properties properties = new Properties();
   private IntegrationProject project;

   private ProjectFile(String var1, File var2) {
      this.projectName = ArgCheck.checkNotEmpty(var1, "name");
      this.parentFolder = var2;
      this.setDefaultProperties();
   }

   public static ProjectFile createForNewProject(IntegrationProject var0, File var1) {
      ProjectFile var2 = new ProjectFile(var0.getName(), var1);
      var2.setProject(var0);
      var2.properties.setProperty("client-version", ClientInfo.getCurrentVersion().toString());
      var2.properties.setProperty("accessed", String.valueOf(System.currentTimeMillis()));
      return var2;
   }

   public static ProjectFile createForExistingProject(String var0, File var1, boolean var2) throws FileNotFoundException, IOException {
      ProjectFile var3 = new ProjectFile(var0, var1);
      var3.load();
      if (var2) {
         var3.properties.setProperty("accessed", String.valueOf(System.currentTimeMillis()));
      }

      var3.checkDefaultProperties();
      var3.save(false);
      return var3;
   }

   private void save(boolean var1) throws FileNotFoundException, IOException {
      FileOutputStream var2 = new FileOutputStream(this.getFile());

      try {
         if (var1) {
            this.setNewModificationDate();
            this.setVersionSavedIn();
            this.setMinimumVersionRequired();
            this.properties.setProperty("saved-edition", ClientInfo.getEdition().getId());
         }

         this.properties.store(var2, "Properties for the integration project \"" + this.projectName + "\"");
         this.setDeployable();
         if (var1 && this.project != null) {
            this.updateProjectModifiedDate();
         }
      } finally {
         var2.close();
      }
   }

   public void setProject(IntegrationProject var1) {
      this.project = var1;
   }

   public File getParentFolder() {
      return this.parentFolder;
   }

   public Version getVersionCreatedIn() {
      return this.readVersion("client-version", Versions.V_1_0_0);
   }

   public Version getVersionSavedWith() {
      try {
         String var1 = this.properties.getProperty("saved-version");
         return var1 == null ? Versions.V_1_1_0 : VersionFactory.fromString(var1);
      } catch (IllegalVersionException var2) {
         var2.printStackTrace();
         return Version.UNKNOWN;
      }
   }

   public ClientEdition getEditionSavedIn() {
      ClientEdition var1 = ClientEdition.UNKNOWN;
      String var2 = this.properties.getProperty("saved-edition");
      return var2 != null ? EnumWithIdLookup.lookupOr(var2, var1) : var1;
   }

   public Version getMinimumVersionRequired() {
      return this.readVersion("requires_version", Versions.V_1_2_0);
   }

   public Optional<Date> getModifiedAt() {
      return Optional.fromNullable(this.readDate("modified"));
   }

   public void setCreationTimestamp(long var1) {
      this.properties.setProperty("created", String.valueOf(var1));
   }

   public void setNewName(String var1) {
      File var2 = this.getFile();
      this.projectName = var1;
      this.setDefaultProperties();
      var2.renameTo(new File(this.parentFolder, this.projectName + ".jip"));
   }

   private void setDefaultProperties() {
      for (ProjectFile.DefaultProperty var4 : DEFAULT_PROPERTIES) {
         var4.set(this);
      }
   }

   private void checkDefaultProperties() {
      for (ProjectFile.DefaultProperty var4 : DEFAULT_PROPERTIES) {
         var4.setIfMissing(this);
      }
   }

   public File getFile() {
      File[] var1 = this.parentFolder.listFiles(FILE_FILTER);
      int var2 = var1.length;
      byte var3 = 0;
      return var3 < var2 ? var1[var3] : new File(this.parentFolder, this.projectName + ".jip");
   }

   public void save() throws FileNotFoundException, IOException {
      this.save(true);
   }

   public void touch() throws FileNotFoundException, IOException {
      String var1 = String.valueOf(System.currentTimeMillis());
      this.properties.setProperty("accessed", var1);
      this.properties.setProperty("modified", var1);
      this.save(false);
   }

   private void updateProjectModifiedDate() {
      Date var1 = this.readDate("modified");
      if (var1 != null) {
         this.project.setModifiedAt(var1);
      }
   }

   private void load() throws FileNotFoundException, IOException {
      File var1 = this.getFile();
      if (var1.exists()) {
         Properties var2 = PropertiesReader.read(var1);
         this.properties.clear();
         this.properties.putAll(var2);
      }
   }

   private void setNewModificationDate() {
      this.properties.setProperty("modified", String.valueOf(System.currentTimeMillis()));
   }

   private void setVersionSavedIn() {
      this.properties.setProperty("saved-version", ClientInfo.getCurrentVersion().toString());
   }

   private void setMinimumVersionRequired() {
      Version var1 = ApplicationConfiguration.getVersion("IntegrationProject.RequiredVersion", null);
      if (var1 == null) {
         ErrorLog.log(ProjectFile.class, "Invalid or missing Minimum Version Required property in the ApplicationConfiguration", new Exception());
         var1 = ClientInfo.getCurrentVersion();
      }

      if (this.project != null) {
         Version var2 = this.project.getRequiredVersion();
         if (var2.isLaterThan(var1)) {
            var1 = var2;
         }
      }

      this.properties.setProperty("requires_version", var1.toString());
   }

   private void setDeployable() {
      if (this.project != null) {
         this.properties.setProperty("deployable", String.valueOf(this.project.isDeployable()));
      }
   }

   public String getProperty(String var1) {
      return this.properties.getProperty(var1);
   }

   public IntegrationProjectId getProjectID() {
      String var1 = this.properties.getProperty("project-id");
      IntegrationProjectId var2 = null;
      if (!Strings.isNullOrEmpty(var1)) {
         try {
            var2 = new IntegrationProjectId(var1);
         } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
         }
      }

      return var2;
   }

   public void update(IntegrationProject var1) {
      this.properties.setProperty("project-id", var1.getID().toString());
      this.properties.setProperty("deployed", String.valueOf(var1.hasBeenDeployed()));
      this.properties.setProperty("deploy-dirty", String.valueOf(var1.isDeployDirty()));
      this.properties.setProperty("description", var1.getDescription());
      this.properties.setProperty("jitterpack-comment", var1.getJitterPackComment());
      this.properties.setProperty("deployable", String.valueOf(var1.isDeployable()));
      this.storeDownloadInfo();
   }

   private void storeDownloadInfo() {
      DownloadInfo var1 = this.project.getDownloadInfo();
      if (var1 != null) {
         this.properties.setProperty("downloaded-from", var1.getServerName());
         this.properties.setProperty("downloaded-at", String.valueOf(var1.getWhen().getTime()));
      } else {
         this.properties.remove("downloaded-from");
         this.properties.remove("downloaded-at");
      }
   }

   public void applyToLoadedProject(IntegrationProject var1) {
      var1.setDeployed(Boolean.valueOf(this.getProperty("deployed")));
      var1.setDeployDirty(Boolean.valueOf(this.getProperty("deploy-dirty")));
      var1.setDescription(this.getProperty("description"));
      var1.setJitterPackComment(this.getProperty("jitterpack-comment"));
      var1.setLocationOnDisk(this.getParentFolder());
      var1.setDeployable(this.readDeployable());
      Date var2 = this.readDate("created");
      if (var2 != null) {
         var1.setCreatedAt(this.readDate("created"));
      }

      Date var3 = this.readDate("modified");
      if (var3 != null) {
         var1.setModifiedAt(var3);
      }

      this.restoreDownloadInfo(var1);
   }

   private void restoreDownloadInfo(IntegrationProject var1) {
      String var2 = this.properties.getProperty("downloaded-from", null);
      if (var2 != null) {
         Date var3 = this.readDate("downloaded-at");
         if (var3 != null) {
            DownloadInfo var4 = new DownloadInfo(var2, var3);
            var1.setDownloadInfo(var4);
         }
      }
   }

   private boolean readDeployable() {
      String var1 = this.properties.getProperty("deployable");
      return var1 != null ? Boolean.parseBoolean(var1) : true;
   }

   private Date readDate(String var1) {
      try {
         String var2 = this.getProperty(var1);
         if (!Strings.isNullOrEmpty(var2)) {
            return new Date(Long.parseLong(var2));
         }
      } catch (NumberFormatException var3) {
         ErrorLog.log(ProjectFile.class, "Invalid value for the date property '" + var1 + "'.", var3);
      }

      return null;
   }

   private Version readVersion(String var1, Version var2) {
      String var3 = this.getProperty(var1);
      if (!Strings.isNullOrEmpty(var3)) {
         try {
            return VersionFactory.fromString(var3);
         } catch (IllegalVersionException var5) {
            ErrorLog.log(ProjectFile.class, "Illegal project file: Invalid value for the version property '" + var1 + "'.", var5);
         }
      }

      return var2;
   }

   private static class DefaultProperty {
      private final String key;
      private final String appendix;

      public DefaultProperty(String var1, String var2) {
         this.key = var1;
         this.appendix = var2;
      }

      public void set(ProjectFile var1) {
         var1.properties.setProperty(this.key, var1.projectName + this.appendix);
      }

      public void setIfMissing(ProjectFile var1) {
         if (var1.getProperty(this.key) == null) {
            this.set(var1);
         }
      }
   }

   public static final class Filter extends javax.swing.filechooser.FileFilter {
      @Override
      public boolean accept(File var1) {
         return var1.isDirectory() ? !this.isHidden(var1) : this.isProjectFile(var1);
      }

      @Override
      public String getDescription() {
         return ProjectFile.FILE_DESCRIPTION;
      }

      private boolean isProjectFile(File var1) {
         return var1.getName().toLowerCase().endsWith(".jip");
      }

      private boolean isHidden(File var1) {
         Folder var2 = Folder.parentOf(var1);
         return var2 != null && this.containsProjectFile(var2) ? ProjectFolders.isProjectFolder(var1) : false;
      }

      private boolean containsProjectFile(Folder var1) {
         for (File var5 : var1.listFiles()) {
            if (var5.isFile() && this.isProjectFile(var5)) {
               return true;
            }
         }

         return false;
      }
   }
}
