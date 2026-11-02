package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import org.etlforge.ui.widget.table.renderer.DateRenderer;

public final class Utils {
   public static DateRenderer createDateTableRenderer() {
      return new DateRenderer("yyyy-MM-dd'T'HH:mm:ss");
   }

   private Utils() {
   }
}
