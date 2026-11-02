package org.etlforge.integration.client.ui.script;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;

public final class Icons {
   public static final Icon HELP_ICON = CommonIcons.HELP_16;
   public static final Icon TEST_ICON_16 = ScriptResources.getIcon("Test.Script.16");
   public static final Icon TEST_ICON_24 = ScriptResources.getIcon("Test.Script.24");
   public static final Icon TEST_ICON_32 = ScriptResources.getIcon("Test.Script.32");
   public static final Icon TRANSFER_UP_ICON = ScriptResources.getIcon("Transfer.Item.16");
   public static final Icon TRANSFER_LEFT_ICON = ScriptResources.getIcon("Transfer.Item.Left.16");
   public static final Icon FORMULA_ICON = ClientIcons.FORMULA_16;

   public static Icon getPreferredTransferIcon() {
      boolean var0 = LayoutTypePreference.get() == LayoutType.TABS;
      return var0 ? TRANSFER_UP_ICON : TRANSFER_LEFT_ICON;
   }

   private Icons() {
   }
}
