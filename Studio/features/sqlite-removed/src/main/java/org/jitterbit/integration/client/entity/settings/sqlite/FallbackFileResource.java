package org.jitterbit.integration.client.entity.settings.sqlite;

import java.io.IOException;
import java.io.Serializable;
import org.jitterbit.integration.client.entity.settings.FileResource;

final class FallbackFileResource implements FileResource {
   @Override
   public boolean exists() {
      return false;
   }

   @Override
   public String read() {
      return null;
   }

   @Override
   public void write(String var1) {
   }

   @Override
   public void serialize(Serializable var1) throws IOException {
   }

   @Override
   public <T> T deserialize(Class<T> var1) throws IOException, ClassNotFoundException {
      return null;
   }

   @Override
   public void delete() {
   }

   @Override
   public void setLifeTime(FileResource.LifeTime var1) {
   }

   @Override
   public FileResource.LifeTime getLifeTime() {
      return FileResource.LifeTime.ENTITY;
   }
}
