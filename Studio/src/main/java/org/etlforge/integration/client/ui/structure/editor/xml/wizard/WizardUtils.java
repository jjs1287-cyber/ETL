package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.ui.filestore.FileCollisionResolverFactory;
import org.etlforge.application.ui.window.ApplicationWindow;

final class WizardUtils {
   private static FileCollisionResolverFactory fileCollisionResolverFactory;

   static synchronized FileCollisionResolver getFileCollisionResolver(ApplicationWindow var0) {
      if (fileCollisionResolverFactory == null) {
         fileCollisionResolverFactory = FileCollisionResolverFactory.newInstance();
      }

      return fileCollisionResolverFactory.createResolver(var0);
   }

   private WizardUtils() {
   }
}
