package org.etlforge.integration.filetransfer.client;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import java.io.Serializable;
import java.util.Comparator;
import org.etlforge.collections.Iterables2;
import org.etlforge.system.ArgCheck;

public final class ServerFileFolder implements Serializable {
   private final String name;
   private final String description;
   private final ImmutableMap<String, ServerFile> files;

   public ServerFileFolder(String var1, String var2, Iterable<ServerFile> var3) {
      this.name = ArgCheck.checkNotEmpty(var1);
      this.description = Strings.nullToEmpty(var2);
      Builder var4 = ImmutableMap.builder();

      for (ServerFile var6 : var3) {
         var4.put(var6.getFilePath(), var6);
      }

      this.files = var4.build();
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public Iterable<ServerFile> allFiles() {
      return Lists.newArrayList(this.files.values());
   }

   public Iterable<ServerFile> allFilesAlphabetically() {
      return Iterables2.sortedCopy(this.files.values(), new Comparator<ServerFile>() {
         public int compare(ServerFile var1, ServerFile var2) {
            return var1.getFilePath().compareToIgnoreCase(var2.getFilePath());
         }
      });
   }

   public ServerFile getFile(String var1) {
      return this.files.get(var1);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ServerFileFolder ? Objects.equal(this.name, ((ServerFileFolder)var1).name) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.name == null ? 0 : this.name.hashCode();
   }
}
