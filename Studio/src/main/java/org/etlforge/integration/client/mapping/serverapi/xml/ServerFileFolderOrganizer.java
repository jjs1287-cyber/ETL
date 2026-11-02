package org.etlforge.integration.client.mapping.serverapi.xml;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public final class ServerFileFolderOrganizer {
   private final ImmutableList<FolderFactory> factories;
   private final StandardFolderFactory standardFolderFactory;

   public static ServerFileFolderOrganizer defaultOrganizer() {
      return new ServerFileFolderOrganizer(Collections.singletonList(new StandardFolderFactory()), false);
   }

   public static ServerFileFolderOrganizer standalone(FolderFactory var0) {
      return new ServerFileFolderOrganizer(Collections.singletonList(Preconditions.checkNotNull(var0)), false);
   }

   public ServerFileFolderOrganizer(List<? extends FolderFactory> var1, boolean var2) {
      this.factories = ImmutableList.copyOf(var1);
      this.standardFolderFactory = var2 ? new StandardFolderFactory() : null;
   }

   public Collection<ServerFileFolder> organizeFiles(Collection<ServerFile> var1) {
      Multimap var2 = this.sortFilesIntoBins(var1);
      return this.createFolders(var2);
   }

   private Multimap<FolderFactory, ServerFile> sortFilesIntoBins(Collection<ServerFile> var1) {
      HashMultimap var2 = HashMultimap.create();

      for (ServerFile var4 : var1) {
         boolean var5 = false;

         for (FolderFactory var7 : this.factories) {
            if (var7.accepts(var4.getFilePath())) {
               var5 = true;
               var2.put(var7, var4);
            }
         }

         if (!var5 && this.standardFolderFactory != null) {
            var2.put(this.standardFolderFactory, var4);
         }
      }

      return var2;
   }

   private List<ServerFileFolder> createFolders(Multimap<FolderFactory, ServerFile> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (FolderFactory var4 : this.getFolderOrder()) {
         Collection var5 = var1.get(var4);
         ServerFileFolder var6 = var4.createFolder(var5);
         var2.add(var6);
      }

      return var2;
   }

   private List<FolderFactory> getFolderOrder() {
      ArrayList var1 = Lists.newArrayList();
      if (this.standardFolderFactory != null) {
         var1.add(this.standardFolderFactory);
      }

      var1.addAll(this.factories);
      return var1;
   }
}
