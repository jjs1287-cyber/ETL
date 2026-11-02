package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import java.util.TimeZone;

enum TimeZoneMode {
   UTC,
   LOCAL;

   public TimeZone getTimeZone() {
      switch (this) {
         case UTC:
            return TimeZone.getTimeZone("UTC");
         case LOCAL:
            return TimeZone.getDefault();
         default:
            throw new RuntimeException(this + "?");
      }
   }
}
