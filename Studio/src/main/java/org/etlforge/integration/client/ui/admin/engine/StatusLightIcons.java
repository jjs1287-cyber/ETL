package org.etlforge.integration.client.ui.admin.engine;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;

final class StatusLightIcons {
   public static final Icon NONE = AdminViewResources.ICONS.getIcon("StatusLight.None");
   public static final Icon GREEN = AdminViewResources.ICONS.getIcon("StatusLight.Green");
   public static final Icon RED = AdminViewResources.ICONS.getIcon("StatusLight.Red");
   public static final Icon YELLOW = AdminViewResources.ICONS.getIcon("StatusLight.Yellow");

   private StatusLightIcons() {
   }
}
