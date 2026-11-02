package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class LayoutUtils {
   public static final int DETAIL_FIELD_WIDTH = 30;

   public static TwoColumnPanel getDetailsViewerLayout() {
      return TwoColumnPanel.forFormEntry();
   }

   public static KongaTextField getDetailField() {
      KongaTextField var0 = new KongaTextField(30);
      var0.setSelectAllWhenFocused(true);
      return var0;
   }

   private LayoutUtils() {
   }
}
