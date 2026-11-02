package org.etlforge.ui.widget.tree;

import com.google.common.base.Function;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.border.Border;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class SimpleTreeFilterField implements TreeFilterField {
   private final KongaTree tree;
   private final Function<String, KongaTreeFilter> filterFunction;
   private final KongaTextField field;
   private boolean removeFilterOnClose;
   private Border border;
   private final SimpleTreeFilterField.CloseAction closeAction;
   private String label;
   private Receiver<KongaTreeFilter> interceptor;
   private final DelayedDocumentContentChangeReaction changeReaction;

   public SimpleTreeFilterField(KongaTree var1, Function<String, KongaTreeFilter> var2) {
      this.tree = var1;
      this.filterFunction = var2;
      this.field = new KongaTextField();
      this.field.setSelectAllWhenFocused(true);
      this.closeAction = this.createCloseAction();
      this.removeFilterOnClose = true;
      this.label = "Filter:";
      this.installTransferActions();
      this.changeReaction = this.installChangeHandler();
   }

   private SimpleTreeFilterField.CloseAction createCloseAction() {
      SimpleTreeFilterField.CloseAction var1 = new SimpleTreeFilterField.CloseAction();
      ActionKeyBinding.install(var1, this.field);
      return var1;
   }

   private void installTransferActions() {
      ActionKeyBinding.install(new SimpleTreeFilterField.ArrowDownAction(), this.field);
      ActionKeyBinding.install(new SimpleTreeFilterField.ArrowUpAction(), this.field);
   }

   private DelayedDocumentContentChangeReaction installChangeHandler() {
      SimpleTreeFilterField.FilterInstaller var1 = new SimpleTreeFilterField.FilterInstaller();
      DelayedDocumentContentChangeReaction var2 = new DelayedDocumentContentChangeReaction(var1);
      var2.install(this.field);
      return var2;
   }

   @Override
   public void setBorder(Border var1) {
      this.border = var1;
   }

   public void setFieldWidth(int var1) {
      this.field.setColumns(var1);
   }

   public void setTextWhenEmpty(String var1) {
      this.field.setTextWhenEmpty(var1);
   }

   @Override
   public void setCloseable(ActionListener var1) {
      this.closeAction.setCloseHandler(var1);
   }

   @Override
   public void setRemoveFilterOnClose(boolean var1) {
      this.removeFilterOnClose = var1;
   }

   @Override
   public void clear() {
      this.field.setText("");
      this.updateTree();
   }

   public void updateTree() {
      this.changeReaction.sendNow();
   }

   @Override
   public void setDescription(String var1) {
      this.field.setToolTipText(var1);
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   @Override
   public void setInterceptor(Receiver<KongaTreeFilter> var1) {
      this.interceptor = var1;
      if (var1 != null) {
         var1.handle(this.createFilter());
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal().withBorder(this.border);
      KongaLabel var3 = this.createLabel();
      var2.addAllWithSpace(3, var3, this.field);
      if (this.closeAction.isEnabled()) {
         this.field.addActionToContextMenu(this.closeAction);
         this.field.addActionToContextMenu(null);
         KongaButton var4 = KongaButton.forToolbar(this.closeAction);
         var2.rigid(10).add(var4);
      }

      var2.nonOpaque().decorate(var1);
   }

   private KongaLabel createLabel() {
      KongaLabel var1 = KongaLabel.withMnemonic(this.label);
      var1.setLabelFor(this.field);
      return var1;
   }

   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.createLabel(), this.field);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   private KongaTreeFilter createFilter() {
      String var1 = this.field.getText();
      return this.filterFunction.apply(var1);
   }

   private class ArrowDownAction extends AbstractAction {
      public ArrowDownAction() {
         this.putValue("AcceleratorKey", KeyUtils.DOWN);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         SimpleTreeFilterField.this.tree.requestFocusInWindow();
         if (SimpleTreeFilterField.this.tree.getSelectionCount() == 0) {
            SimpleTreeFilterField.this.tree.selectNextRow();
         }
      }
   }

   private class ArrowUpAction extends AbstractAction {
      public ArrowUpAction() {
         this.putValue("AcceleratorKey", KeyUtils.UP);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         SimpleTreeFilterField.this.tree.requestFocusInWindow();
         SimpleTreeFilterField.this.tree.selectLastRow();
      }
   }

   private class CloseAction extends AbstractAction {
      private ActionListener closeHandler;

      public CloseAction() {
         super("Close", UiResourceBundle.getCloseIcon16());
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(87));
         this.setEnabled(false);
      }

      public void setCloseHandler(ActionListener var1) {
         this.closeHandler = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.closeHandler.actionPerformed(var1);
         if (SimpleTreeFilterField.this.removeFilterOnClose) {
            SimpleTreeFilterField.this.tree.setFilter(null);
         }
      }
   }

   private class FilterInstaller implements Runnable {
      private FilterInstaller() {
      }

      @Override
      public void run() {
         KongaTreeFilter var1 = SimpleTreeFilterField.this.createFilter();
         if (SimpleTreeFilterField.this.interceptor != null) {
            SimpleTreeFilterField.this.interceptor.handle(var1);
         } else {
            SimpleTreeFilterField.this.tree.setFilter(var1);
         }
      }
   }
}
