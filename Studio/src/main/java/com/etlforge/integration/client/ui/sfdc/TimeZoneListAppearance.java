package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Function;
import java.util.TimeZone;
import javax.swing.JComboBox;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListItemAppearance;

final class TimeZoneListAppearance {
   private final ListItemAppearance appearance = new ListItemAppearance()
      .withTextFunction(new TimeZoneListAppearance.TextFunction())
      .withToolTipFunction(new TimeZoneListAppearance.ToolTipFunction());

   public TimeZoneListAppearance() {
   }

   public ListItemAppearance get() {
      return this.appearance;
   }

   public void installIn(KongaListFactory var1) {
      this.appearance.configure(var1);
   }

   public void installIn(JComboBox var1) {
      var1.setRenderer(this.appearance.asCellRenderer());
   }

   private static class TextFunction implements Function<Object, String> {
      private TextFunction() {
      }

      public String apply(Object var1) {
         return var1 instanceof TimeZone ? ((TimeZone)var1).getID() : null;
      }
   }

   private static class ToolTipFunction implements Function<Object, String> {
      private ToolTipFunction() {
      }

      public String apply(Object var1) {
         return var1 instanceof TimeZone ? ((TimeZone)var1).getDisplayName() : null;
      }
   }
}
