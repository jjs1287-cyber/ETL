package org.etlforge.integration.activity.ui.component.filter;

import com.google.common.base.Objects;
import java.util.Date;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.ui.util.TimeWindowControl;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class TimeWindowFilterUi<T extends ActivityEntry> extends AbstractActivityEntryFilterUi<T> {
   private final String title;
   private final TimeWindowControl timeControl;
   private Date startDate;
   private Date endDate;
   private final ChangeListeners changeListeners;

   public TimeWindowFilterUi() {
      this(null);
   }

   public TimeWindowFilterUi(String var1) {
      this.title = var1;
      this.timeControl = new TimeWindowControl();
      this.changeListeners = new ChangeListeners();
   }

   @Override
   public void apply() {
      Date var1 = this.startDate;
      Date var2 = this.endDate;
      this.startDate = this.timeControl.getStart();
      this.endDate = this.timeControl.getEnd();
      this.notifyListenersOnChange(var1, var2);
   }

   private void notifyListenersOnChange(Date var1, Date var2) {
      if (!Objects.equal(var1, this.startDate) || !Objects.equal(var2, this.endDate)) {
         this.changeListeners.call().stateChanged(new ChangeEvent(this));
      }
   }

   @Override
   public void reset() {
      this.timeControl.setStartAndEndDates(this.startDate, this.endDate);
   }

   public TimeWindowFilter getFilter() {
      return new TimeWindowFilter(this.startDate, this.endDate);
   }

   public void setFieldLabels(String var1, String var2) {
      this.timeControl.setFieldLabels(var1, var2);
   }

   public void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   @Override
   public String getFilterString() {
      Date var1 = this.startDate;
      Date var2 = this.endDate;
      if (var1 == null && var2 == null) {
         return "";
      }

      String var3 = "";
      if (var1 == null) {
         var3 = PackageResources.TimeWindowFilter.getBefore(var2);
      } else if (var2 == null) {
         var3 = PackageResources.TimeWindowFilter.getAfter(var1);
      } else {
         var3 = PackageResources.TimeWindowFilter.getBetween(var1, var2);
      }

      return var3;
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.title == null) {
         this.timeControl.decorate(var1);
      } else {
         this.layoutWithTitle(var1);
      }
   }

   private void layoutWithTitle(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(this.timeControl).withBorder(StyleSheet.getDefaultTitleBorder(this.title));
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.timeControl.requestFocus();
   }

   @Override
   public boolean isActive() {
      return this.startDate != null || this.endDate != null;
   }
}
