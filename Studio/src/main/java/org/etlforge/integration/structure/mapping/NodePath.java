package org.etlforge.integration.structure.mapping;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.KongaStringUtils;

public final class NodePath {
   public final String path;

   public static String toGeneratorPath(String var0) {
      return var0.startsWith("[") && var0.endsWith("]") ? var0 : "[" + var0 + "]";
   }

   public static NodePath of(String var0) {
      return new NodePath(var0);
   }

   public NodePath(String var1) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "path cannot be empty");
      this.path = var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof NodePath ? this.path.equals(((NodePath)var1).path) : false;
      }
   }

   public String pathWithoutBrackets() {
      return KongaStringUtils.removeStartAndEnd(this.path, "[", "]");
   }

   @Override
   public int hashCode() {
      return this.path.hashCode();
   }

   @Override
   public String toString() {
      return this.path;
   }

   public String getLeafName() {
      return getLeafName(this.path);
   }

   public static String getLeafName(String var0) {
      if (var0.isEmpty()) {
         return "";
      }

      int var1 = StringUtils.lastIndexOfAny(var0, PathSeparator.allSeparators());
      if (var1 < var0.length() - 1) {
         return var0.substring(var1 + 1);
      }

      int var2 = KongaStringUtils.lastIndexOfAny(var0, PathSeparator.allSeparatorsAsString(), var1 - 1);
      String var3 = var0.substring(var2 + 1, var1);
      if (var3.endsWith("#")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   public boolean isAncestorOf(NodePath var1) {
      if (this.equals(var1)) {
         return false;
      } else {
         String var2 = this.pathWithoutBrackets();
         String var3 = var1.pathWithoutBrackets();
         if (var3.startsWith(var2)) {
            String var4 = var3.substring(var2.length());
            return PathSeparator.terminates(var2) ? !var4.isEmpty() : var4.length() > 1 && PathSeparator.starts(var4);
         } else {
            return false;
         }
      }
   }

   public boolean containsLoop() {
      return PathSeparator.containsLoop(this.path);
   }

   public Optional<String> getPathToLastLoop() {
      if (!this.containsLoop()) {
         return Optional.absent();
      }

      String var1 = this.pathWithoutBrackets();
      int var2 = var1.lastIndexOf(".");
      String var3 = var1.substring(0, var2 + 1);
      return Optional.of(var3);
   }

   public Iterable<String> split() {
      return PathSeparator.split(this.pathWithoutBrackets());
   }
}
