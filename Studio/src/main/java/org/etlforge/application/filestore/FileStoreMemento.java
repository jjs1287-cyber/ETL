package org.etlforge.application.filestore;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.etlforge.application.appdir.ApplicationTempFolder;

public final class FileStoreMemento {
   private final FileStore fileStore;
   private final ImmutableList<FileStoreMemento.Atom> atoms;

   public static FileStoreMemento empty() {
      return new FileStoreMemento();
   }

   public FileStoreMemento(FileStore var1, Iterable<String> var2) throws FileStoreException {
      this.fileStore = Preconditions.checkNotNull(var1, "fileStore");
      this.atoms = this.buildAtoms(var2);
   }

   private FileStoreMemento() {
      this.fileStore = null;
      this.atoms = ImmutableList.of();
   }

   private ImmutableList<FileStoreMemento.Atom> buildAtoms(Iterable<String> var1) throws FileStoreException {
      Builder var2 = ImmutableList.builder();

      for (String var4 : Sets.newHashSet(var1)) {
         FileStoreMemento.Atom var5 = this.remember(var4);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2.build();
   }

   private FileStoreMemento.Atom remember(String var1) throws FileStoreException {
      try {
         FileStoreFile var2 = this.fileStore.getFile(var1);
         if (var2 == null) {
            return null;
         }

         File var3 = ApplicationTempFolder.newFolder("FileStoreMemento", true);
         File var4 = new File(var3, UUID.randomUUID().toString());
         Files.copy(var2.getPhysicalRepresentation(), var4);
         return new FileStoreMemento.Atom(var2, var4);
      } catch (IOException var5) {
         throw new FileStoreException(var5);
      }
   }

   public void restore() throws FileStoreException {
      for (FileStoreMemento.Atom var2 : this.atoms) {
         var2.restoreTo(this.fileStore);
      }
   }

   public void forget() {
      for (FileStoreMemento.Atom var2 : this.atoms) {
         var2.forget();
      }
   }

   private static class Atom {
      private final String path;
      private final File copy;

      public Atom(FileStoreFile var1, File var2) {
         this.path = var1.getPath();
         this.copy = var2;
      }

      public void restoreTo(FileStore var1) throws FileStoreException {
         var1.addFile(this.copy, this.path, FileCollisionResolver.REPLACE_EXISTING);
      }

      public void forget() {
         this.copy.delete();
      }
   }
}
