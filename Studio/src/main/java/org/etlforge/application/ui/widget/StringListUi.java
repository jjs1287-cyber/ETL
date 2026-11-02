package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ChangeSource;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.ui.widget.list.MutableKongaList;
import org.etlforge.ui.widget.text.KongaTextField;

public final class StringListUi implements UiProvider {
   private final KongaTextField valueField = new KongaTextField(20);
   private final MutableKongaList list;
   private final SelectionAction deleteAction;
   private DefaultListModel listModel = new DefaultListModel();
   private final StringListUi.AddValueAction addAction = new StringListUi.AddValueAction();
   private StringListUi.Orientation orientation = StringListUi.Orientation.FIELD_BELOW;

   public StringListUi() {
      this(6);
   }

   public StringListUi(int var1) {
      this.list = this.createList(var1);
      this.deleteAction = this.list.getDeleteAction();
      this.valueField.setSelectAllWhenFocused(true);
      this.valueField.addActionListener(this.addAction);
      NonEmptyTextEnablerCondition.enableWhenFieldNotEmpty(Enablabes.fromAction(this.addAction), this.valueField);
      StringListUi.ValueSelector var2 = new StringListUi.ValueSelector();
      var2.install(this.valueField);
   }

   private MutableKongaList createList(int var1) {
      KongaListFactory var2 = ComponentFactories.newKongaListFactory();
      var2.setVisibleRowCount(var1);
      KongaList var3 = var2.newList(this.listModel);
      return new MutableKongaList(var3);
   }

   public void setOrientation(StringListUi.Orientation var1) {
      this.orientation = Preconditions.checkNotNull(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.doLayout();
      var1.display(var2);
   }

   private JComponent doLayout() {
      BoxBuilder var1 = Horizontal.box().nonOpaque().addAllWithSpace(10, this.valueField, this.addAction, this.deleteAction);
      switch (this.orientation) {
         case FIELD_ABOVE:
            return CaptionLayout.of(var1, this.list).deeplyNonOpaque().container();
         case FIELD_BELOW:
            return Vertical.borderLayout(5).of(null, this.list, var1).deeplyNonOpaque().container();
         default:
            throw new AssertionError();
      }
   }

   @Override
   public void requestFocus() {
      this.valueField.requestFocusInWindow();
   }

   public boolean isEmpty() {
      return this.listModel.isEmpty();
   }

   public ImmutableList<String> getStringList() {
      return ImmutableList.copyOf(ListUtils.asIterable(this.listModel, String.class));
   }

   public void setStringList(Iterable<String> var1) {
      this.listModel = ListUtils.createModel(var1);
      this.list.setModel(this.listModel);
   }

   public void setEnabled(boolean var1) {
      this.list.setEnabled(var1);
      this.valueField.setEnabled(var1);
      this.addAction.setEnabled(var1 && !this.valueField.getText().isEmpty());
      this.deleteAction.setEnabled(var1 && !this.list.getSelection().isEmpty());
   }

   public ChangeSource asChangeSource() {
      return this.list.asChangeSource();
   }

   private void selectValue(String var1) {
      this.list.setSelection(SelectionFactory.newOrderedSelection(var1));
      this.list.ensureSelectedIndexVisible();
   }

   private class AddValueAction extends AbstractAction {
      public AddValueAction() {
         super("Add");
         this.putValue("ShortDescription", "Add the entered value to the list of values");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = StringListUi.this.valueField.getText();
         if (!var2.isEmpty()) {
            for (String var4 : ListUtils.asIterable(StringListUi.this.listModel, String.class)) {
               if (var4.equals(var2)) {
                  NonBlockingAlert.info("This value is already in the list.", "Duplicate value");
                  return;
               }
            }

            StringListUi.this.list.addItem(var2);
            StringListUi.this.selectValue(var2);
         }
      }
   }

   public enum Orientation {
      FIELD_ABOVE,
      FIELD_BELOW;
   }

   private class ValueSelector extends DelayedDocumentContentChangeReaction {
      private ValueSelector() {
      }

      @Override
      public void run() {
         String var1 = StringListUi.this.valueField.getText();
         if (this.isInList(var1)) {
            StringListUi.this.selectValue(var1);
         }
      }

      private boolean isInList(String var1) {
         if (var1.isEmpty()) {
            return false;
         }

         for (String var3 : ListUtils.asIterable(StringListUi.this.listModel, String.class)) {
            if (var3.equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }
}
