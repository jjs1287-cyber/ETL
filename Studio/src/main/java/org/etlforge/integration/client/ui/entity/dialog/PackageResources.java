package org.etlforge.integration.client.ui.entity.dialog;

import org.etlforge.integration.client.ui.entity.resources.EntityResources;

final class PackageResources {
   private static String getString(String var0) {
      return EntityResources.STRINGS.getString(var0);
   }

   private PackageResources() {
   }

   static class DirtyDelegates {
      static final String TITLE = PackageResources.getString("Dialog.DirtyDelegates.Title");

      static class Messages {
         static final String TOP = PackageResources.getString("Dialog.DirtyDelegates.Message.Top");
      }
   }
}
