package org.etlforge.ui.selection;

import java.awt.Component;
import javax.swing.Action;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.Enablable;

public class SelectionBasedEnabler {
   private final SelectionSource source;
   private final Enablable enablable;
   private final SelectionBasedEnabler.Listener listener;

   public SelectionBasedEnabler(SelectionSource var1, Component var2) {
      this(var1, Enablabes.fromComponent(var2));
   }

   public SelectionBasedEnabler(SelectionSource var1, Action var2) {
      this(var1, Enablabes.fromAction(var2));
   }

   public SelectionBasedEnabler(SelectionSource var1, Enablable var2) {
      this.source = var1;
      this.enablable = var2;
      this.listener = new SelectionBasedEnabler.Listener();
      var1.addSelectionListener(this.listener);
      this.updateEnablable(var1.getSelection());
   }

   private void updateEnablable(Selection var1) {
      boolean var2 = this.isEnabledFor(var1);
      this.enablable.setEnabled(var2);
   }

   public final void uninstall() {
      this.source.removeSelectionListener(this.listener);
   }

   protected boolean isEnabledFor(Selection var1) {
      return !var1.isEmpty();
   }

   private final class Listener implements SelectionListener {
      private Listener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         SelectionBasedEnabler.this.updateEnablable(var1.getSelection());
      }
   }
}
