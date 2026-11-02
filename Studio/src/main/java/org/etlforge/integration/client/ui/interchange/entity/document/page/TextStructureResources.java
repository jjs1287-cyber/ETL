package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.util.ResourceBundle;
import javax.swing.Icon;

public final class TextStructureResources {
   private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
      "org.etlforge.integration.client.ui.interchange.entity.document.page.resources.PageResources"
   );

   private TextStructureResources() {
   }

   static String getString(String var0) {
      return BUNDLE.getString(var0);
   }

   static class Actions {
      static class NewField {
         static final String LABEL = TextStructureResources.getString("NewField.Label");
         static final Icon ICON = null;
      }
   }
}
