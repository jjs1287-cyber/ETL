package org.etlforge.ui.text;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

abstract class AbstractTabIndentAction extends AbstractAction {
   private final JTextComponent textPane;
   private Interval selection;
   private int[] rowInterval;

   public AbstractTabIndentAction(JTextComponent var1) {
      this.textPane = var1;
   }

   public final JTextComponent getTextPane() {
      return this.textPane;
   }

   protected final Document getDocument() {
      return this.textPane.getDocument();
   }

   protected final int getSelectionStart() {
      return this.textPane.getSelectionStart();
   }

   protected final int getSelectionEnd() {
      return this.textPane.getSelectionEnd();
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      this.rowInterval = this.getAffectedRowsInterval();
      this.checkSelection(this.selection, this.rowInterval);

      for (int var2 = this.rowInterval[1]; var2 >= this.rowInterval[0]; var2--) {
         this.processRow(var2);
      }
   }

   protected void checkSelection(Interval var1, int[] var2) {
   }

   private int[] getAffectedRowsInterval() {
      this.selection = this.getSelection();
      if (this.selection == null) {
         int var5 = DocumentUtils.getRowNumber(this.textPane, this.textPane.getCaretPosition());
         return new int[]{var5, var5};
      }

      Document var1 = this.textPane.getDocument();
      int var2 = DocumentUtils.getRowNumber(var1, this.selection.startsAt());
      int var3 = this.selection.startsAt() + this.selection.length();
      int var4 = DocumentUtils.getRowNumber(var1, var3);
      if (var4 > var2 && var3 == DocumentUtils.getRowStart(var1, var4)) {
         var4--;
      }

      return new int[]{var2, var4};
   }

   private Interval getSelection() {
      int var1 = this.textPane.getSelectionStart();
      int var2 = this.textPane.getSelectionEnd();
      return var2 > var1 ? IntervalFactory.fromStartAndEnd(var1, var2) : null;
   }

   protected abstract void processRow(int var1);
}
