package org.etlforge.application.filestore;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatchers;
import org.etlforge.util.crypto.DigestUtils;

final class DefaultFile implements FileStoreFile {
   private static final StringMatcher EQUALITY = SystemUtils.IS_OS_WINDOWS ? StringMatchers.EQUALS_IGNORE_CASE : StringMatchers.EQUALS;
   private DefaultFile parent;
   private final String path;
   private final String name;
   private final List<DefaultFile> children;

   public DefaultFile(DefaultFile var1, String var2) {
      this.parent = var1;
      if (var1 != null) {
         var1.addChild(this);
      }

      this.path = var2;
      this.children = new ArrayList<>();
      this.name = new File(var2).getName();
   }

   public void setParent(DefaultFile var1) {
      if (this.parent != null) {
         this.parent.children.remove(this);
      }

      this.parent = var1;
      if (var1 != null) {
         var1.addChild(this);
      }
   }

   private void addChild(DefaultFile var1) {
      if (!this.children.contains(this)) {
         this.children.add(var1);
      }
   }

   @Override
   public Iterator<FileStoreFile> getChildren() {
      return new ArrayList<FileStoreFile>(this.children).iterator();
   }

   @Override
   public String getPath() {
      return this.path;
   }

   @Override
   public File getPhysicalRepresentation() {
      return this.parent == null ? new File(this.path) : new File(this.parent.getPhysicalRepresentation(), this.name);
   }

   public String getContents() throws IOException {
      return Files.toString(this.getPhysicalRepresentation(), Charsets.UTF_8);
   }

   public byte[] getContentsAsByte() throws IOException {
      return Files.toByteArray(this.getPhysicalRepresentation());
   }

   private byte[] getHash() throws IOException {
      return DigestUtils.getMd5FileDigest(this.getPhysicalRepresentation());
   }

   public boolean hasIdenticalContents(byte[] var1) throws IOException {
      byte[] var2 = this.getHash();
      return Arrays.equals(var2, var1);
   }

   @Override
   public String getMD5() throws IOException {
      return DigestUtils.getMd5FileDigestString(this.getPhysicalRepresentation());
   }

   public DefaultFile findChild(String var1) {
      if (var1 != null && var1.length() != 0) {
         ArrayList var2 = new ArrayList();
         String var3 = this.getPathParticle(var1);

         for (DefaultFile var5 : this.children) {
            String var6 = var5.getPathRelativeToParent();
            if (EQUALITY.matches(var1, var6)) {
               return var5;
            }

            if (this.isChildCandidate(var3, var6)) {
               var2.add(var5);
            }
         }

         return var3 != null ? this.checkChildCandidates(var1, var3, var2) : null;
      } else {
         return this;
      }
   }

   private String getPathParticle(String var1) {
      int var2 = var1.indexOf(File.separator);
      return var2 == -1 ? null : var1.substring(0, var2);
   }

   private boolean isChildCandidate(String var1, String var2) {
      return var1 != null && (EQUALITY.matches(var2, var1) || var2.startsWith(var1 + File.separator));
   }

   private DefaultFile checkChildCandidates(String var1, String var2, List<DefaultFile> var3) {
      String var4 = var1.substring(var2.length() + 1);

      for (DefaultFile var6 : var3) {
         DefaultFile var7 = var6.findChild(var4);
         if (var7 != null) {
            return var7;
         }
      }

      return null;
   }

   private String getPathRelativeToParent() {
      if (this.parent == null) {
         return "";
      }

      if (this.parent.parent == null) {
         return this.path;
      }

      String var1 = this.parent.path;
      return this.path.substring(var1.length() + 1);
   }

   @Override
   public String toString() {
      return this.path;
   }

   @Override
   public int hashCode() {
      return this.getPhysicalRepresentation().hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof FileStoreFile) {
         File var2 = ((FileStoreFile)var1).getPhysicalRepresentation();
         File var3 = this.getPhysicalRepresentation();
         return var3.equals(var2);
      } else {
         return false;
      }
   }
}
