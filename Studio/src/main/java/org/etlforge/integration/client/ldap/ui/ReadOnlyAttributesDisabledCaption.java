package org.etlforge.integration.client.ldap.ui;

import java.awt.Container;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ReadOnlyAttributesDisabledCaption {
   private ReadOnlyAttributesDisabledCaption(Container var1, Object var2) {
      KongaLabel var3 = TextStyles.InfoText.makeLabel("Read-only attributes cannot be mapped to, so they cannot be selected");
      var3.setIcon(CommonIcons.INFO_16);
      var1.add(var3, var2);
   }

   public static void addTo(Container var0, Object var1) {
      new ReadOnlyAttributesDisabledCaption(var0, var1);
   }
}
