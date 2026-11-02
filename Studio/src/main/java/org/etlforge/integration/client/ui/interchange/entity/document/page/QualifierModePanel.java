package org.etlforge.integration.client.ui.interchange.entity.document.page;

import javax.swing.JRadioButton;
import org.etlforge.integration.data.structure.text.QualifierMode;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.undo.UndoableButtonGroup;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.KongaRadioButton;

final class QualifierModePanel implements UiProvider {
   private final JRadioButton always = new KongaRadioButton("Always");
   private final JRadioButton whenNeeded = new KongaRadioButton("Only when needed", true);

   public QualifierModePanel(InputPanel var1) {
      this.registerWithParent(var1);
   }

   private void registerWithParent(InputPanel var1) {
      UndoableButtonGroup var2 = this.createButtonGroup();
      var2.add(this.always);
      var2.add(this.whenNeeded);
      var1.addChangeSource(var2);
      var1.addInputField(var2.createInputField(this.always, "Always", null));
      var1.addInputField(var2.createInputField(this.whenNeeded, "When Needed", null));
   }

   private UndoableButtonGroup createButtonGroup() {
      UndoableButtonGroup var1 = new UndoableButtonGroup();
      var1.setUndoRedoPresentationName("Qualifier Mode");
      return var1;
   }

   public QualifierMode getSelectedMode() {
      return this.always.isSelected() ? QualifierMode.ALWAYS : QualifierMode.WHEN_NEEDED;
   }

   public void setSelectedMode(QualifierMode var1) {
      JRadioButton var2 = var1 == QualifierMode.ALWAYS ? this.always : this.whenNeeded;
      var2.setSelected(true);
   }

   public void setEnabled(boolean var1) {
      this.always.setEnabled(var1);
      this.whenNeeded.setEnabled(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Horizontal.box().addAllWithSpace(5, "Use qualifier: ", this.always, this.whenNeeded).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.always.requestFocusInWindow();
   }
}
