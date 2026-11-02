package org.etlforge.integration.client.ui.project.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.StringResources;

public final class ProjectUiResources {
   public static final StringResources STRINGS = loadStrings();
   public static final Icon PROJECT_16 = EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL);

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.project.resources.Strings");
   }

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String formatMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private ProjectUiResources() {
   }
}
