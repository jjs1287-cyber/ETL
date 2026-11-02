package org.etlforge.application.ui.filestore;

import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.ui.window.ApplicationWindow;

public abstract class FileCollisionResolverFactory {
   public abstract FileCollisionResolver createResolver(ApplicationWindow var1);

   public static FileCollisionResolverFactory newInstance() {
      return new FileCollisionResolverFactory() {
         @Override
         public FileCollisionResolver createResolver(ApplicationWindow var1) {
            return new FileCollisionResolverImpl(var1);
         }
      };
   }
}
