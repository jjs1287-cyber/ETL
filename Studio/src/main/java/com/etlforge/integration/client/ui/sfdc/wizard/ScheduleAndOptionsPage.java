package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.system.Disposables;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.undo.InputPanelUndoRedoEnabler;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ScheduleAndOptionsPage extends AbstractWizardPage {
   private final ProjectItemSelector<Schedule> scheduleSelector;
   private final InputPanel inputPanel = new InputPanel();
   private InputPanelUndoRedoEnabler undoRedo;
   private final ImmutableList<WizardOption> options;

   public ScheduleAndOptionsPage(Wizard<?> var1, EntityExplorerSupport var2, EditorPanel var3, List<? extends WizardOption> var4) {
      super(var1, "Schedule & Options");
      this.options = ImmutableList.copyOf(var4);
      this.scheduleSelector = ProjectItemSelector.forSchedules(var2, var3);
      this.inputPanel.addInputField(this.scheduleSelector.asInputField("Schedule"));

      for (WizardOption var6 : var4) {
         var6.registerWithParent(this.inputPanel);
      }

      this.inputPanel.setSendInputChangeEvents(true);
      this.setCanContinue(true);
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return this.scheduleSelector.save() && this.askOptionsIfNavigationIsAllowed();
   }

   private boolean askOptionsIfNavigationIsAllowed() {
      for (WizardOption var2 : this.options) {
         if (!var2.allowsNavigation()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      return this.scheduleSelector.closeGracefully() && this.askOptionsIfNavigationIsAllowed();
   }

   @Override
   protected void dispose() {
      this.scheduleSelector.dispose();
      Disposables.disposeAll(this.options);
      this.undoRedo.uninstall();
      this.inputPanel.dispose();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      var1.add(InvisiblePanel.of(this.scheduleSelector), true, false);

      for (WizardOption var3 : this.options) {
         var1.add(var3.getUi(), false, false);
      }

      JPanel var4 = var1.asNorthBorderLayout();
      this.undoRedo = new InputPanelUndoRedoEnabler(this.inputPanel, var4);
      return var4;
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ScheduleAndOptionsPage.this.scheduleSelector.requestFocus();
         }
      });
   }

   public Schedule getSchedule() {
      return this.scheduleSelector.getSelectedItem();
   }
}
