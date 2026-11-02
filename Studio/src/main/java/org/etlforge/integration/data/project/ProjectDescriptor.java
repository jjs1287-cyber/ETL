package org.etlforge.integration.data.project;

import java.io.File;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.util.persist.Persistor;

public final class ProjectDescriptor {
   private final IntegrationProjectId id;
   private final String name;
   private final File location;

   public static ProjectDescriptor restore(Persistor var0) {
      String var1 = var0.getString("guid");
      if (var1 == null) {
         return null;
      }

      String var2 = var0.getString("name");
      if (var2 == null) {
         return null;
      }

      String var3 = var0.getString("location");
      if (var3 == null) {
         return null;
      }

      try {
         return new ProjectDescriptor(new IntegrationProjectId(var1), var2, new File(var3));
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public ProjectDescriptor(IntegrationProject var1) {
      this(var1.getID(), var1.getName(), var1.getLocationOnDisk());
   }

   private ProjectDescriptor(IntegrationProjectId var1, String var2, File var3) {
      if (var1 == null) {
         throw new NullPointerException("id");
      }

      if (var2 == null) {
         throw new NullPointerException("name");
      }

      if (var3 == null) {
         throw new NullPointerException("location");
      }

      this.id = var1;
      this.name = var2;
      this.location = var3;
   }

   public final IntegrationProjectId getId() {
      return this.id;
   }

   public final String getName() {
      return this.name;
   }

   public final File getLocation() {
      return this.location;
   }

   @Override
   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + this.id.hashCode();
      var2 = 31 * var2 + this.location.hashCode();
      return 31 * var2 + this.name.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      }

      if (!(var1 instanceof ProjectDescriptor)) {
         return false;
      }

      ProjectDescriptor var2 = (ProjectDescriptor)var1;
      return this.id.equals(var2.id) && this.name.equals(var2.name) && this.location.equals(var2.location);
   }

   public void writeTo(Persistor var1) {
      var1.putString("guid", this.id.toString());
      var1.putString("name", this.name);
      var1.putString("location", this.location.getAbsolutePath());
   }
}
