package org.etlforge.integration.activity.ui.log;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.collections.EnumSets;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.LogStatusFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.component.filter.AbstractActivityEntryFilterUi;
import org.etlforge.system.KeyValue;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.preference.StringKeyValue;

final class LogStatusFilterUi extends AbstractActivityEntryFilterUi<OperationLogEntry> {
   private final LogStatusFilterPreference preference;
   private final EnumMap<LogStatus, KongaCheckBox> selectors;
   private EnumSet<LogStatus> lastSelection;

   public LogStatusFilterUi() {
      this(new StringKeyValue(OperationLogPage.class));
   }

   public LogStatusFilterUi(KeyValue<String, String> var1) {
      this.preference = new LogStatusFilterPreference(var1);
      this.lastSelection = this.preference.restore();
      this.selectors = this.createSelectors();
   }

   private EnumMap<LogStatus, KongaCheckBox> createSelectors() {
      EnumMap var1 = Maps.newEnumMap(LogStatus.class);

      for (LogStatus var5 : LogStatus.values()) {
         var1.put(var5, new KongaCheckBox(var5.toString(), this.lastSelection.contains(var5)));
      }

      return var1;
   }

   @Override
   public boolean isActive() {
      return this.lastSelection.size() < LogStatus.values().length;
   }

   @Override
   public void apply() {
      Set var1 = Maps.filterValues(this.selectors, ButtonUtils.TOGGLE_BUTTON_SELECTED).keySet();
      this.lastSelection = EnumSets.copyOf(var1, LogStatus.class);
      this.preference.store(this.lastSelection);
   }

   @Override
   public void reset() {
      for (Entry var2 : this.selectors.entrySet()) {
         ((KongaCheckBox)var2.getValue()).setSelected(this.lastSelection.contains(var2.getKey()));
      }
   }

   @Override
   public void requestFocus() {
      this.selectors.get(LogStatus.values()[0]).requestFocusInWindow();
   }

   public LogStatusFilter getFilter() {
      return new LogStatusFilter(this.lastSelection);
   }

   @Override
   public String getFilterString() {
      return PackageResources.LogStatusFilter.getFilterString(this.lastSelection);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Grid.oneColumn(6);
      var2.addAll(
         new LogStatusFilterUi.SelectAll(),
         new LogStatusFilterUi.SelectSet("Unselect All", EnumSet.noneOf(LogStatus.class), "Unselect all log statuses"),
         new LogStatusFilterUi.SelectSet("Completed", LogStatus.completed(), "Include only those operations that have completed"),
         new LogStatusFilterUi.SelectSet("Not Completed", LogStatus.stillInQueue(), "Include only those operations that are still running or in the queue")
      );
      Grid var3 = new Grid(0, 2, 8, 6);

      for (LogStatus var5 : displayOrder()) {
         var3.add(this.selectors.get(var5));
      }

      BorderLayoutBuilder var6 = new BorderLayoutBuilder(0, 8);
      var6.center(var3).east(InvisiblePanel.of(var2));
      var6.withBorder(StyleSheet.getDefaultTitleBorder(PackageResources.LogStatusFilter.LABEL));
      var6.decorate(var1);
   }

   private static List<LogStatus> displayOrder() {
      return Arrays.asList(
         LogStatus.SUCCESS,
         LogStatus.CANCELED,
         LogStatus.SUCCESS_WITH_INFO,
         LogStatus.PENDING,
         LogStatus.SUCCESS_WITH_WARNING,
         LogStatus.RUNNING,
         LogStatus.ERROR
      );
   }

   private class SelectAll extends AbstractAction {
      public SelectAll() {
         super("Select All");
         this.putValue("ShortDescription", "Include all statuses");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         for (KongaCheckBox var3 : LogStatusFilterUi.this.selectors.values()) {
            var3.setSelected(true);
         }
      }
   }

   private class SelectSet extends AbstractAction {
      private final EnumSet<LogStatus> statuses;

      public SelectSet(String var2, EnumSet<LogStatus> var3, String var4) {
         super(var2);
         this.statuses = var3;
         this.putValue("ShortDescription", var4);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         for (Entry var3 : LogStatusFilterUi.this.selectors.entrySet()) {
            ((KongaCheckBox)var3.getValue()).setSelected(this.statuses.contains(var3.getKey()));
         }
      }
   }
}
