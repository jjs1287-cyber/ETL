package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.event.FocusListener;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.widget.FilteredListPopupSelector;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.util.time.TimeZones;

final class TimeZoneListSelector implements TimeZoneSelectionWidget {
   private final FilteredListPopupSelector<TimeZone> ui = this.buildUi();
   private boolean ready;
   private WaitLock waitLock;
   private TimeZoneId idToSelectWhenReady;

   public TimeZoneListSelector() {
      this.startWait();
   }

   private FilteredListPopupSelector<TimeZone> buildUi() {
      return FilteredListPopupSelector.builder(TimeZone.class)
         .withValueProducer(new TimeZoneListSelector.ValueProducer())
         .withDisplayNameFunction(displayNameFunction())
         .withListAppearance(new TimeZoneListAppearance().get())
         .withManualEntry(TimeZones.FROM_ID)
         .runWhenReady(this.runWhenDone())
         .build();
   }

   private static Function<TimeZone, String> displayNameFunction() {
      return new Function<TimeZone, String>() {
         public String apply(TimeZone var1) {
            return var1.getID();
         }
      };
   }

   private Runnable runWhenDone() {
      return new Runnable() {
         @Override
         public void run() {
            if (TimeZoneListSelector.this.waitLock != null) {
               TimeZoneListSelector.this.waitLock.release();
               TimeZoneListSelector.this.waitLock = null;
            }

            if (TimeZoneListSelector.this.idToSelectWhenReady != null) {
               TimeZoneListSelector.this.ui.setTextValue(TimeZoneListSelector.this.idToSelectWhenReady.getId());
               TimeZoneListSelector.this.idToSelectWhenReady = null;
            } else {
               TimeZoneListSelector.this.ui.setSelectedValue(TimeZone.getDefault());
            }

            TimeZoneListSelector.this.ready = true;
         }
      };
   }

   private void startWait() {
      if (!this.ready) {
         this.waitLock = this.ui.showWaitMessage("Fetching list of available Time Zones...");
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public void setSelectedTimeZoneId(TimeZoneId var1) {
      if (this.ready) {
         this.ui.setTextValue(var1.getId());
      } else {
         this.idToSelectWhenReady = var1;
      }
   }

   @Override
   public TimeZoneId getSelectedTimeZoneId() {
      String var1 = this.ui.getTextValue().trim();
      return TimeZoneId.valueOf(var1);
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.ui.addFocusListener(var1);
   }

   @Override
   public void setEnabled(boolean var1) {
      this.ui.setEnabled(var1);
   }

   @Override
   public void registerWithParent(final InputPanel var1) {
      this.ui.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1x) {
            var1.inputChanged(new InputChangeEvent(TimeZoneListSelector.this, null));
         }
      });
   }

   private static class ValueProducer implements Callable<Set<TimeZone>> {
      private ValueProducer() {
      }

      public Set<TimeZone> call() {
         LinkedHashSet var1 = Sets.newLinkedHashSet();
         var1.add(TimeZones.DEFAULT);
         var1.add(TimeZones.GMT);
         TreeMap var2 = Maps.newTreeMap();

         for (String var6 : TimeZone.getAvailableIDs()) {
            var2.put(var6, TimeZone.getTimeZone(var6));
         }

         var2.remove(TimeZones.DEFAULT.getID());
         var2.remove(TimeZones.GMT.getID());

         for (Object __etlItem139 : var2.values()) {
            TimeZone var8 = (TimeZone) __etlItem139;
            var1.add(var8);
         }

         return var1;
      }
   }
}
