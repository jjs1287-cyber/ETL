package org.etlforge.application.ui.groovy;

public final class CommonImport implements Comparable<CommonImport> {
   private final String name;
   private final String path;

   public CommonImport(Class<?> var1) {
      this(var1.getSimpleName(), var1.getName());
   }

   public CommonImport(String var1, String var2) {
      this.name = var1;
      this.path = var2;
   }

   String getPath() {
      return this.path;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public int compareTo(CommonImport var1) {
      return this.name.compareToIgnoreCase(var1.name);
   }
}
