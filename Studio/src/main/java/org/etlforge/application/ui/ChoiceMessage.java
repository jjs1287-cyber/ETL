package org.etlforge.application.ui;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class ChoiceMessage<T extends Enum<T>> {
   private final Object message;
   private final String title;
   private final T[] choices;
   private Alert.Type type;
   private T defaultChoice;
   private T choiceIfCancelled;
   private Action defaultAction;
   private T selected;
   private KongaDialog dialog;
   private boolean forceButtonsToHaveSameSize;

   public ChoiceMessage(Object var1, String var2, T[] var3) {
      this.message = var1;
      this.title = var2;
      this.choices = (T[])var3;
   }

   public ChoiceMessage(Object var1, String var2, Class<T> var3) {
      this(var1, var2, (T[])var3.getEnumConstants());
   }

   public void setType(Alert.Type var1) {
      this.type = var1;
   }

   public void setDefaultChoice(T var1) {
      this.defaultChoice = (T)var1;
   }

   public void setChoiceIfCancelled(T var1) {
      this.choiceIfCancelled = (T)var1;
   }

   public void setForceButtonsToHaveSameSize(boolean var1) {
      this.forceButtonsToHaveSameSize = var1;
   }

   public T show(Component var1) {
      this.selected = this.choiceIfCancelled;
      this.defaultAction = null;
      this.dialog = this.createDialog(var1);
      this.setDefaultButton();
      this.displayDialog();
      return this.selected;
   }

   private void displayDialog() {
      this.dialog.pack();
      this.dialog.setLocationRelativeTo(null);
      this.dialog.setVisible(true);
   }

   private void setDefaultButton() {
      if (this.defaultAction != null) {
         UiUtils.makeDefaultButton(this.dialog.getContentPane(), this.defaultAction);
      }
   }

   private KongaDialog createDialog(Component var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10).withEmptyBorder(10);
      var2.center(Alert.getDisplayMessage(this.message));
      var2.west(this.layoutTypeIcon());
      var2.south(this.layoutButtons());
      Window var3 = var1 != null ? SwingUtilities.getWindowAncestor(var1) : UiUtils.getActiveWindow();
      KongaDialog var4 = new KongaDialog(var3, this.title);
      var2.asContentPaneFor(var4);
      return var4;
   }

   private Object layoutTypeIcon() {
      if (this.type != null) {
         Icon var1 = this.type.getIcon();
         if (var1 != null) {
            JLabel var2 = new JLabel(var1);
            var2.setVerticalAlignment(1);
            return var2;
         }
      }

      return null;
   }

   private Object layoutButtons() {
      JButton[] var1 = this.createButtons();
      return this.forceButtonsToHaveSameSize ? this.getFixedSizeButtonLayout(var1) : this.getVariableSizeButtonLayout(var1);
   }

   private Object getVariableSizeButtonLayout(JButton[] var1) {
      return Horizontal.box().glue().addAllWithSpace(10, var1);
   }

   private Object getFixedSizeButtonLayout(JButton[] var1) {
      Grid var2 = Horizontal.grid().of(var1).withHGap(10);
      return Horizontal.Align.east(var2);
   }

   private JButton[] createButtons() {
      JButton[] var1 = new JButton[this.choices.length];

      for (int var2 = 0; var2 < this.choices.length; var2++) {
         ChoiceMessage.ButtonAction var3 = new ChoiceMessage.ButtonAction(this.choices[var2]);
         var1[var2] = new KongaButton(var3);
         if (this.choices[var2] == this.defaultChoice) {
            this.defaultAction = var3;
         }
      }

      return var1;
   }

   private class ButtonAction extends AbstractAction {
      private final T choice;

      public ButtonAction(T var2) {
         super(var2.toString());
         this.choice = (T)var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ChoiceMessage.this.selected = this.choice;
         ChoiceMessage.this.dialog.dispose();
      }
   }
}
