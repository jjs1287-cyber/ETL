package org.etlforge.integration.client.entity.settings;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.util.KongaSerializationUtils;

class EntityFileResourceImpl implements FileResource {
   private final File file;
   private FileResource.LifeTime lifetime;
   private boolean deleted;
   private final Object lock;

   public EntityFileResourceImpl(File var1) {
      this.file = var1;
      this.lifetime = FileResource.LifeTime.ENTITY;
      this.lock = new Object();
   }

   @Override
   public void delete() {
      synchronized (this.lock) {
         this.deleted = true;
         if (this.file.exists()) {
            try {
               FileUtils.forceDelete(this.file);
            } catch (IOException var4) {
               ErrorLog.log(this.getClass(), "Failed to delete the entity file resource " + this.file.getAbsolutePath() + " from disk.", var4);
            }
         }
      }
   }

   public boolean isDeleted() {
      synchronized (this.lock) {
         return this.deleted;
      }
   }

   @Override
   public boolean exists() {
      return this.file.exists();
   }

   public File getFile() {
      return this.file;
   }

   @Override
   public String read() {
      try {
         return this.readImpl();
      } catch (IOException var2) {
         ErrorLog.log(this.getClass(), null, var2);
         return null;
      }
   }

   private String readImpl() throws IOException {
      return this.file.canRead() ? Files.toString(this.file, Charsets.UTF_8) : null;
   }

   @Override
   public void write(String var1) {
      try {
         Files.write(var1, this.file, Charsets.UTF_8);
      } catch (IOException var3) {
         ErrorLog.log(this.getClass(), null, var3);
      }
   }

   @Override
   public void serialize(Serializable var1) throws IOException {
      KongaSerializationUtils.serializeToFile(var1, this.file);
   }

   @Override
   public <T> T deserialize(Class<T> var1) throws IOException, ClassNotFoundException {
      return this.file.canRead() ? KongaSerializationUtils.deserializeFromFile(this.file, var1) : null;
   }

   @Override
   public FileResource.LifeTime getLifeTime() {
      synchronized (this.lock) {
         return this.lifetime;
      }
   }

   @Override
   public void setLifeTime(FileResource.LifeTime var1) {
      if (var1 == null) {
         throw new NullPointerException("t");
      }

      synchronized (this.lock) {
         this.lifetime = var1;
      }
   }
}
