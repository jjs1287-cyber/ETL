package com.etlforge.integration.client.ui.sfdc.page;

import javax.swing.border.Border;
import org.etlforge.application.ui.look.StyleSheet;

public final class PageLayoutUtils {
   public static Border getOptionsBorder() {
      return StyleSheet.getDefaultTitleBorder("Options");
   }

   private PageLayoutUtils() {
   }
}
