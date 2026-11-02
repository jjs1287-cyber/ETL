package org.etlforge.integration.filetransfer.client;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import java.io.File;
import java.io.Serializable;
import java.util.zip.DataFormatException;
import javax.annotation.Nullable;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.crypto.DigestUtils;
import org.etlforge.util.zip.ZipUtils;

public final class ServerFile implements Serializable {
   private final String filePath;
   private final String md5;
   private final String contents;
   private byte[] contentsAsBytes;
   private final boolean compressed;
   private final String displayName;

   public static ServerFile fromPath(String var0) {
      return new ServerFile(var0);
   }

   public static ServerFile.Builder builder(String var0) {
      return new ServerFile.Builder(var0);
   }

   private ServerFile(String var1) {
      this(var1, var1, null, null, false);
   }

   private ServerFile(String var1, String var2, String var3, String var4, boolean var5) {
      this.filePath = ArgCheck.checkNotEmpty(var1, "filePath");
      this.displayName = ArgCheck.checkNotEmpty(var2, "displayName");
      this.md5 = var3;
      this.contents = var4;
      this.compressed = var5;
   }

   public String getName() {
      return new File(this.filePath).getName();
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public String getMD5() {
      return this.md5;
   }

   public String getContents() {
      return this.contents;
   }

   public boolean isCompressed() {
      return this.compressed;
   }

   public synchronized byte[] getContentsAsBytes() {
      if (this.contentsAsBytes == null) {
         this.calculateContentsAsBytes();
      }

      return this.contentsAsBytes;
   }

   private void calculateContentsAsBytes() {
      if (this.contents == null) {
         this.contentsAsBytes = new byte[0];
      } else if (this.compressed) {
         try {
            this.contentsAsBytes = ZipUtils.base64DecodeAndInflateStringAsBytes(this.contents);
         } catch (DataFormatException var2) {
            throw new RuntimeException("Invalid data format", var2);
         }
      } else {
         this.contentsAsBytes = this.contents.getBytes(Charsets.UTF_8);
      }
   }

   public boolean isRemote() {
      return this.filePath.contains("://");
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ServerFile)) {
         return false;
      }

      ServerFile var2 = (ServerFile)var1;
      return this.filePath.equals(var2.filePath) && Objects.equal(this.contents, var2.contents);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.filePath, this.contents);
   }

   @Override
   public String toString() {
      return this.getFilePath();
   }

   public static final class Builder {
      private final String filePath;
      private String displayName;
      private String md5;
      private String contents;
      private boolean compressed;

      public Builder(String var1) {
         this.filePath = ArgCheck.checkNotEmpty(var1);
         this.displayName = var1;
      }

      public Builder(ServerFile var1) {
         this.filePath = var1.filePath;
         this.displayName = var1.displayName;
         this.md5 = var1.md5;
         this.contents = var1.contents;
         this.compressed = var1.compressed;
      }

      public ServerFile.Builder withDisplayName(String var1) {
         this.displayName = ArgCheck.checkNotEmpty(var1);
         return this;
      }

      public ServerFile.Builder withContents(@Nullable String var1) {
         if (var1 != null) {
            this.contents = var1;
            this.md5 = DigestUtils.getMd5DigestAsHexString(var1);
            this.compressed = false;
         }

         return this;
      }

      public ServerFile.Builder withCompressedContents(@Nullable String var1) {
         if (var1 != null) {
            this.contents = var1;
            this.compressed = true;
         }

         return this;
      }

      public ServerFile.Builder withHash(@Nullable String var1) {
         this.md5 = var1;
         return this;
      }

      public ServerFile build() {
         return new ServerFile(this.filePath, this.displayName, this.md5, this.contents, this.compressed);
      }
   }
}
