package org.etlforge.application.ui.viewer.log.tree;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;

final class PackageResources {
   static final String TREE_TITLE = getString("LogRepository.Tree.Title");

   private static String getString(String var0) {
      return ApplicationResources.STRINGS.getString(var0);
   }

   private static Icon getIcon(String var0) {
      return ApplicationResources.ICONS.getIcon(var0);
   }

   private PackageResources() {
   }

   static class Controller {
      static final String MOUSE_ENTERED = PackageResources.getString("LogRepository.Tree.Controller.Mouse.Entered");
   }

   static class Nodes {
      static final String ROOT_NAME = PackageResources.getString("LogRepository.Tree.Node.Root.Name");
      static final String CURRENT_SESSION = PackageResources.getString("LogRepository.Tree.Node.CurrentSession");
      static final String PREVIOUS_SESSION = PackageResources.getString("LogRepository.Tree.Node.PreviousSession");
      static final Icon FILE_ICON = PackageResources.getIcon("TextFile.16");

      static class Folder {
         static final Icon OPEN = PackageResources.getIcon("FolderOpened.16");
         static final Icon CLOSED = PackageResources.getIcon("FolderClosed.16");
      }
   }
}
