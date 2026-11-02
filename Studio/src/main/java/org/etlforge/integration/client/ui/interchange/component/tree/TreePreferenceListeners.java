package org.etlforge.integration.client.ui.interchange.component.tree;

import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;

public final class TreePreferenceListeners implements Disposable {
   private final DisposableCollection listeners = new DisposableCollection();
   private OperationNodeContentService opNodeContentService;

   public TreePreferenceListeners(IntegrationEntityTree var1) {
      this.listeners.add(new TreePreferenceListeners.OpNodeContentsPrefListener());
   }

   @Override
   public void dispose() {
      this.listeners.dispose();
   }

   void setOpNodeContentService(OperationNodeContentService var1) {
      this.opNodeContentService = var1;
      var1.setEnabled(ShowOperationContentsInTreePreference.get());
   }

   private class OpNodeContentsPrefListener implements PreferenceListener<Boolean>, Disposable {
      public OpNodeContentsPrefListener() {
         ShowOperationContentsInTreePreference.INSTANCE.addListener(this);
      }

      @Override
      public void dispose() {
         ShowOperationContentsInTreePreference.INSTANCE.removeListener(this);
      }

      public void valueChanged(Preference<Boolean> var1, Boolean var2, Boolean var3) {
         if (TreePreferenceListeners.this.opNodeContentService != null) {
            TreePreferenceListeners.this.opNodeContentService.setEnabled(var3);
         }
      }
   }
}
