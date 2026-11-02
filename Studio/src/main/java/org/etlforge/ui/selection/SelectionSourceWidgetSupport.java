package org.etlforge.ui.selection;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import org.etlforge.ui.widget.DefaultActionHandler;

public final class SelectionSourceWidgetSupport {
   private final SelectionSource selectionSource;
   private final JComponent component;
   private SelectionAction defaultAction;
   private DefaultActionHandler defaultActionHandler;
   private final SelectionListeners selectionListeners;
   private boolean singleClickSelectionEnabled;

   public SelectionSourceWidgetSupport(SelectionSource var1, JComponent var2) {
      this.selectionSource = Preconditions.checkNotNull(var1, "source");
      this.component = Preconditions.checkNotNull(var2);
      this.selectionListeners = new SelectionListeners();
   }

   public void addListener(SelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   public void removeListener(SelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   public void fireSelectionChange(Selection var1) {
      this.selectionListeners.call().selectionChanged(new SelectionChangedEvent(this.selectionSource, var1));
   }

   public void setDefaultAction(SelectionAction var1) {
      if (this.defaultAction != null) {
         this.uninstallDefaultAction();
      }

      this.defaultAction = var1;
      if (var1 != null) {
         this.installDefaultAction(var1);
      }
   }

   public SelectionAction getDefaultAction() {
      return this.defaultAction;
   }

   public void setSingleClickSelectionEnabled(boolean var1) {
      this.singleClickSelectionEnabled = var1;
      if (this.defaultActionHandler != null) {
         this.defaultActionHandler.setSingleClickSelectionEnabled(var1);
      }
   }

   private void uninstallDefaultAction() {
      this.defaultAction.setSelectionSource(null);
      this.defaultAction = null;
      this.defaultActionHandler.uninstall();
      this.defaultActionHandler = null;
   }

   private void installDefaultAction(SelectionAction var1) {
      var1.setSelectionSource(this.selectionSource);
      this.defaultActionHandler = new DefaultActionHandler(this.component, var1);
      this.defaultActionHandler.setSingleClickSelectionEnabled(this.singleClickSelectionEnabled);
      this.defaultActionHandler.install();
   }
}
