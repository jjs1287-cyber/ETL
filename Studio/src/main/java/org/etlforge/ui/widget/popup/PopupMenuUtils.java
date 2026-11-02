package org.etlforge.ui.widget.popup;

import java.awt.event.MouseEvent;
import org.apache.commons.lang.SystemUtils;

public final class PopupMenuUtils {
   private PopupMenuUtils() {
   }

   public static boolean isPopupTrigger(MouseEvent var0) {
      return isPossiblePopupTriggeringEventId(var0.getID()) && var0.isPopupTrigger();
   }

   private static boolean isPossiblePopupTriggeringEventId(int var0) {
      return SystemUtils.IS_OS_WINDOWS ? var0 == 502 : var0 == 502 || var0 == 501;
   }
}
