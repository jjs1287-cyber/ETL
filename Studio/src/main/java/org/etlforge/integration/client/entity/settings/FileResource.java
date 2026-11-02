package org.etlforge.integration.client.entity.settings;

import java.io.IOException;
import java.io.Serializable;

public interface FileResource {
   boolean exists();

   String read();

   void write(String var1);

   void serialize(Serializable var1) throws IOException;

   <T> T deserialize(Class<T> var1) throws IOException, ClassNotFoundException;

   void delete();

   void setLifeTime(FileResource.LifeTime var1);

   FileResource.LifeTime getLifeTime();

   enum LifeTime {
      PROJECT_SESSION,
      ENTITY;
   }
}
