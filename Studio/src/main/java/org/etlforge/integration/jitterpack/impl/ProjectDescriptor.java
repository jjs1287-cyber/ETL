package org.etlforge.integration.jitterpack.impl;

import java.io.File;

class ProjectDescriptor {
   private final String projectName;
   private final File projectFolder;
   private final String comment;

   ProjectDescriptor(String var1, File var2, String var3) {
      this.projectName = var1;
      this.projectFolder = var2;
      this.comment = var3;
   }

   String projectName() {
      return this.projectName;
   }

   File projectFolder() {
      return this.projectFolder;
   }

   String comment() {
      return this.comment;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("[");
      var1.append("Name=").append(this.projectName).append(";");
      var1.append("Folder=").append(this.projectFolder).append(";");
      var1.append("Comment=").append(this.comment).append(";");
      return var1.append("]").toString();
   }
}
