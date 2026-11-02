package org.etlforge.integration.client.project.jitterpack;

import java.util.Date;

public final class DefaultJitterPackDescriptor implements JitterPackDescriptor {
   private final String projectName;
   private final VersionInfo versionInfo;
   private final String folderName;
   private final Date createdAt;
   private final String comment;
   private final boolean encrypted;
   private final boolean readOnly;

   public DefaultJitterPackDescriptor(String var1, VersionInfo var2, String var3, Date var4) {
      this(var1, var2, var3, var4, "", false, false);
   }

   public DefaultJitterPackDescriptor(String var1, VersionInfo var2, String var3, Date var4, String var5, boolean var6, boolean var7) {
      checkArgument(var1, "projectName");
      checkArgument(var3, "folderName");
      this.projectName = var1;
      this.versionInfo = var2;
      this.folderName = var3;
      this.createdAt = var4 != null ? new Date(var4.getTime()) : null;
      this.comment = var5 != null ? var5 : "";
      this.encrypted = var6;
      this.readOnly = var7;
   }

   @Override
   public Date createdAt() {
      return this.createdAt != null ? new Date(this.createdAt.getTime()) : null;
   }

   @Override
   public String getComment() {
      return this.comment;
   }

   @Override
   public String getFolderName() {
      return this.folderName;
   }

   @Override
   public String getProjectName() {
      return this.projectName;
   }

   @Override
   public VersionInfo getVersionInfo() {
      return this.versionInfo;
   }

   @Override
   public boolean isEncrypted() {
      return this.encrypted;
   }

   @Override
   public boolean isPackedProjectReadOnly() {
      return this.readOnly;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("JitterPackDescriptor[");
      var1.append("Project=").append(this.projectName).append(";");
      var1.append("Version=").append(this.versionInfo).append(";");
      var1.append("Folder=").append(this.folderName).append(";");
      var1.append("Created=").append(this.createdAt).append(";");
      var1.append("ReadOnly=").append(this.readOnly).append(";");
      var1.append("Encrypted=").append(this.encrypted ? "Yes" : "No").append(";");
      return var1.append("]").toString();
   }

   private static void checkArgument(Object var0, String var1) {
      if (var0 == null) {
         throw new NullPointerException(var1);
      }
   }
}
