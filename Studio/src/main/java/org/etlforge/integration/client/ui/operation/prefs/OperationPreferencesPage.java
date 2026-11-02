package org.etlforge.integration.client.ui.operation.prefs;

import java.awt.Container;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.integration.activity.ui.MaxActivityEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.MaxHistoryEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.MaxLogEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.MaxQueueEntriesToFetchPreference;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class OperationPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Operations";
   private final BooleanPreferenceUi showActicitySelector;
   private final FixedSetPreferenceUi<OperationType> defaultOpType;
   private final FixedSetPreferenceUi<SchedulePolicy> schedulePolicy;
   private final BooleanPreferenceUi sourceHistory;
   private final BooleanPreferenceUi runOperationMessage;
   private final boolean includeNonActivityPrefs = !ClientInfo.isDataLoader();
   private final OperationPreferencesPage.ActivityPreferences activityPrefs;

   public OperationPreferencesPage() {
      super(new String[]{"Operations"});
      this.showActicitySelector = new BooleanPreferenceUi(ShowActivitySelectorPreference.INSTANCE, Strings.get("Prefs.ShowActivitySelector"));
      this.defaultOpType = new FixedSetPreferenceUi<>(DefaultOperationTypePreference.instance(), Strings.get("Prefs.OpType"), false);
      this.schedulePolicy = new OperationPreferencesPage.SchedulePolicyPreferenceUi();
      this.sourceHistory = new BooleanPreferenceUi(DefaultSourceHistorySettingPreference.INSTANCE, Strings.get("Prefs.SourceHistory"));
      this.runOperationMessage = new BooleanPreferenceUi(RunOperationSuccessMessagePreference.INSTANCE, Strings.get("Prefs.RunOperation.Message"));
      this.activityPrefs = new OperationPreferencesPage.ActivityPreferences();
      if (this.includeNonActivityPrefs) {
         this.addControls(this.showActicitySelector, this.defaultOpType, this.schedulePolicy, this.sourceHistory, this.runOperationMessage);
      }

      this.bindActivityTypePreferences();
   }

   private void bindActivityTypePreferences() {
      this.defaultOpType.setEnabled(!this.showActicitySelector.isSelected());
      this.showActicitySelector.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1) {
            OperationPreferencesPage.this.defaultOpType.setEnabled(!OperationPreferencesPage.this.showActicitySelector.isSelected());
         }
      });
   }

   @Override
   public void display(Container var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(5, 5, 10, 5).withBorder(Empty.border(10, 0, 10, 0));
      var2.setWeightX(1.0);
      if (this.includeNonActivityPrefs) {
         this.layoutNonActivityPrefs(var2);
      }

      var2.add(this.activityPrefs);
      BorderLayoutBuilder.from(var1).north(var2);
   }

   private void layoutNonActivityPrefs(OneColumnPanel var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setWeightX(1.0);
      var2.withInsets(0, 0, 15, 0);
      var2.add(this.layoutNewOperationSettings());
      var2.add(this.runOperationMessage);
      var1.add(var2.invisible());
   }

   private UiProvider layoutNewOperationSettings() {
      TwoColumnPanel var1 = new TwoColumnPanel()
         .withLeftInsets(0, 5, 15, 10)
         .withRightInsets(0, 0, 15, 0)
         .withAnchor(TwoColumnPanel.WEST)
         .withBorder(StyleSheet.getDefaultTitleBorder(Strings.get("Prefs.NewOperations")));
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.showActicitySelector);
      var1.addRow(var2.container());
      this.defaultOpType.layout(var1);
      this.schedulePolicy.layout(var1);
      var1.addRow(new KongaLabel(Strings.get("Prefs.SourceHistory.Label")).asUiProvider(), this.sourceHistory);
      return var1;
   }

   private class ActivityPreferences implements UiProvider {
      private final IntPreferenceUi[] prefs = new IntPreferenceUi[]{
         this.createEntryUi(new MaxLogEntriesToFetchPreference(), Strings.get("Prefs.Activity.MaxLog")),
         this.createEntryUi(new MaxHistoryEntriesToFetchPreference(), Strings.get("Prefs.Activity.MaxHistory")),
         this.createEntryUi(new MaxQueueEntriesToFetchPreference(), Strings.get("Prefs.Activity.MaxQueue"))
      };

      public ActivityPreferences() {
         OperationPreferencesPage.this.addControls(this.prefs);
      }

      @Override
      public void decorate(UiCanvas var1) {
         new OperationPreferencesPage.ActivityPreferences.Layout().decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.prefs[0].requestFocus();
      }

      private IntPreferenceUi createEntryUi(MaxActivityEntriesToFetchPreference var1, String var2) {
         IntPreferenceUi var3 = new IntPreferenceUi(var1.getPreference(), var2, 5);
         var3.setSelectAllWhenFocused(true);
         return var3;
      }

      private class Layout {
         private final TwoColumnPanel grid = new TwoColumnPanel().withBorder(StyleSheet.getDefaultTitleBorder(Strings.get("Prefs.Activity.Title")));

         public Layout() {
            this.addCaption();
            this.addFields();
         }

         public void decorate(UiCanvas var1) {
            var1.display(InvisiblePanel.of(this.grid));
         }

         private void addCaption() {
            this.grid.withInsets(0, 0, 15, 0);
            this.grid.addRow(TextStyles.InfoText.makeLabel(Strings.get("Prefs.Activity.Caption")));
         }

         private void addFields() {
            this.grid.withLeftInsets(0, 0, 10, 5);
            this.grid.withRightInsets(0, 0, 10, 5);

            for (IntPreferenceUi var4 : ActivityPreferences.this.prefs) {
               var4.layout(this.grid);
            }
         }
      }
   }

   private static class SchedulePolicyPreferenceUi extends FixedSetPreferenceUi<SchedulePolicy> {
      public SchedulePolicyPreferenceUi() {
         super(DefaultSchedulePolicyPreference.instance(), Strings.get("Prefs.SchedulePolicy"), false);
      }

      protected String getDisplayString(SchedulePolicy var1) {
         switch (var1) {
            case STRICT:
               return Strings.get("Prefs.SchedulePolicy.Strict");
            case YIELD_IF_STILL_RUNNING:
               return Strings.get("Prefs.SchedulePolicy.Yield");
            default:
               return super.getDisplayString(var1);
         }
      }
   }
}
