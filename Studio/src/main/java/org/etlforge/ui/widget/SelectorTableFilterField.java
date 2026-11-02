package org.etlforge.ui.widget;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.text.KongaTextField;

public final class SelectorTableFilterField<E> implements UiProvider {
   private final SelectorTable<E> table;
   private final Function<String, ? extends Predicate<? super E>> filterFunction;
   private final KongaTextField field;
   private final List<Receiver<Predicate<? super E>>> receivers = Lists.newArrayList();
   private boolean alwaysAcceptSelectedRows;

   public static <E> SelectorTableFilterField<E> forTable(SelectorTable<E> var0, Function<String, ? extends Predicate<? super E>> var1) {
      return new SelectorTableFilterField<>(var0, var1);
   }

   public SelectorTableFilterField(SelectorTable<E> var1, Function<String, ? extends Predicate<? super E>> var2) {
      this.table = var1;
      this.filterFunction = var2;
      this.field = new KongaTextField(20);
      ActionKeyBinding.install(new SelectorTableFilterField.ArrowDown(), this.field);
      this.listenToField();
   }

   private void listenToField() {
      DelayedDocumentContentChangeReaction var1 = new DelayedDocumentContentChangeReaction(new Runnable() {
         @Override
         public void run() {
            String var1x = SelectorTableFilterField.this.field.getText();
            Predicate var2 = SelectorTableFilterField.this.filterFunction.apply(var1x);
            SelectorTableFilterField.this.table.setFilter(var2, SelectorTableFilterField.this.alwaysAcceptSelectedRows);
            SelectorTableFilterField.this.table.highlightFirstRow();

            for (Receiver var4 : SelectorTableFilterField.this.receivers) {
               var4.handle(var2);
            }
         }
      });
      var1.install(this.field);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JLabel var2 = this.field.getLabel();
      if (var2 != null) {
         Horizontal.box().of(var2, this.field).decorate(var1);
      } else {
         var1.display(this.field);
      }
   }

   public SelectorTableFilterField<E> alwaysAcceptSelectedRows() {
      this.alwaysAcceptSelectedRows = true;
      return this;
   }

   public SelectorTableFilterField<E> withLabel(String var1) {
      this.field.setLabel(var1);
      return this;
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   public boolean isEmpty() {
      return this.field.getDocument().getLength() == 0;
   }

   public void addReceiver(Receiver<Predicate<? super E>> var1) {
      this.receivers.add(Preconditions.checkNotNull(var1));
   }

   public void removeReceiver(Receiver<Predicate<? super E>> var1) {
      this.receivers.remove(var1);
   }

   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
   }

   private class ArrowDown extends AbstractAction {
      public ArrowDown() {
         this.putValue("AcceleratorKey", KeyUtils.DOWN);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         SelectorTableFilterField.this.table.requestFocus();
         SelectorTableFilterField.this.table.highlightFirstRow();
      }
   }
}
