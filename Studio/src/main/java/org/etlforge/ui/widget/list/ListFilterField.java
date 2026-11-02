package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.selection.SelectFirstAction;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ListFilterField implements UiProvider {
   private final Function<String, ListFilter> filterFunction;
   private Receiver<ListFilter> callback;
   private final KongaTextField field;
   private JLabel label;
   private final DelayedDocumentContentChangeReaction changeHandler;

   public ListFilterField(Function<String, ListFilter> var1) {
      this.filterFunction = var1;
      this.callback = NoReceiver.create();
      this.field = new KongaTextField();
      this.field.setSelectAllWhenFocused(true);
      this.changeHandler = new DelayedDocumentContentChangeReaction(new Runnable() {
         @Override
         public void run() {
            ListFilterField.this.broadCastNewFilter();
         }
      });
      this.changeHandler.install(this.field);
   }

   private void broadCastNewFilter() {
      ListFilter var1 = this.getFilter();
      this.callback.handle(var1);
   }

   public ListFilter getFilter() {
      String var1 = this.field.getText();
      return this.filterFunction.apply(var1);
   }

   public void setDelay(int var1) {
      this.changeHandler.setDelay(var1);
   }

   public void setListModel(final FilteredListModel var1) {
      this.setCallback(new Receiver<ListFilter>() {
         public void handle(ListFilter var1x) {
            var1.setFilter(var1x);
         }
      });
   }

   public void setCallback(Receiver<ListFilter> var1) {
      this.callback = Preconditions.checkNotNull(var1);
   }

   public void setLabel(JLabel var1) {
      this.label = var1;
   }

   public JLabel getLabel() {
      return this.label;
   }

   public void setTextWhenEmpty(String var1) {
      this.field.setTextWhenEmpty(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      if (this.label != null) {
         this.label.setLabelFor(this.field);
         var2.add(this.label);
      }

      var2.add(this.field);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   public void addActionListener(ActionListener var1) {
      this.field.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.field.removeActionListener(var1);
   }

   public void associateWithList(final KongaList var1) {
      this.setCallback(new Receiver<ListFilter>() {
         public void handle(ListFilter var1x) {
            ((FilteredListModel)var1.getModel()).setFilter(var1x);
         }
      });
      SelectFirstAction.installInField(var1, this.field);
   }
}
