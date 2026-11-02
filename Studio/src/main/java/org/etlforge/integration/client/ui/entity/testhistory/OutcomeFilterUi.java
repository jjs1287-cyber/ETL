package org.etlforge.integration.client.ui.entity.testhistory;

import com.google.common.collect.Lists;
import java.awt.Dialog.ModalityType;
import java.util.EnumSet;
import java.util.List;
import javax.swing.JCheckBox;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.integration.data.test.TestResultFilter;
import org.etlforge.integration.data.test.TestResultOutcomeFilter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

final class OutcomeFilterUi implements UiProvider {
   private final List<JCheckBox> selectors = Lists.newArrayList();

   public OutcomeFilterUi() {
      for (TestResult.Outcome var4 : TestResult.Outcome.values()) {
         this.selectors.add(this.createSelector(var4));
      }
   }

   private JCheckBox createSelector(TestResult.Outcome var1) {
      JCheckBox var2 = new JCheckBox("", true);
      var2.putClientProperty("outcome", var1);
      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Grid.oneColumn(10);
      var2.add("Select the types of outcomes to display:");

      for (JCheckBox var4 : this.selectors) {
         this.layoutSelector(var2, var4);
      }

      var2.decorate(var1);
   }

   private void layoutSelector(Grid var1, JCheckBox var2) {
      TestResult.Outcome var3 = (TestResult.Outcome)var2.getClientProperty("outcome");
      KongaLabel var4 = OutcomeDecorator.createLabel(var3);
      var4.setLabelForButton(var2);
      BoxBuilder var5 = BoxBuilder.horizontal();
      var5.addAllWithSpace(5, var2, var4);
      var1.add(var5);
   }

   @Override
   public void requestFocus() {
      this.selectors.get(0).requestFocusInWindow();
   }

   public TestResultFilter getFilter() {
      EnumSet var1 = EnumSet.noneOf(TestResult.Outcome.class);

      for (JCheckBox var3 : this.selectors) {
         if (var3.isSelected()) {
            var1.add((TestResult.Outcome)var3.getClientProperty("outcome"));
         }
      }

      return new TestResultOutcomeFilter(var1);
   }

   public TestResultFilter openSelector() {
      KongaDialog var1 = UiProviderDialogDisplayer.createDialog(
         UiUtils.getActiveWindow(), this, "Select Outcomes", ModalityType.DOCUMENT_MODAL, KongaDialog.OK_CANCEL, OutcomeFilterUi.class
      );
      var1.setResizable(false);
      var1.setVisible(true);
      return var1.wasOkPressed() ? this.getFilter() : null;
   }
}
