package org.etlforge.integration.client.ui.script.builder.docs;

import java.util.ResourceBundle;
import org.etlforge.application.ui.help.DocumentedResource;

public final class ProjectItemTreeHelp {
   private static final ResourceBundle docs = ResourceBundle.getBundle(
      "org.etlforge.integration.client.ui.script.builder.docs.ProjectItemTreeHtmlDocumentation"
   );

   public static String lookupTitle(DocumentedResource var0) {
      String var1 = var0.getHelpId() + ".Title";
      return docs.containsKey(var1) ? docs.getString(var1) : "";
   }

   public static String lookupDocs(DocumentedResource var0) {
      String var1;
      if (docs.containsKey(var0.getHelpId())) {
         var1 = docs.getString(var0.getHelpId());
      } else {
         var1 = "<em>Error</em>: No documentation found for the ID <code>" + var0.getHelpId() + "</code>.";
      }

      return wrap(var1);
   }

   private static String wrap(String var0) {
      StringBuilder var1 = new StringBuilder("<html>");
      var1.append("<table width=400px>");
      var1.append(var0);
      var1.append("</table></html>");
      return var1.toString();
   }

   private ProjectItemTreeHelp() {
   }
}
