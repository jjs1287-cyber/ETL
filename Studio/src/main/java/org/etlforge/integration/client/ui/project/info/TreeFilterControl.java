package org.etlforge.integration.client.ui.project.info;

import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public interface TreeFilterControl extends UiProvider {
   TreeFilterControl NO_FILTER = new TreeFilterControl() {
      @Override
      public void setTree(TreePanel var1) {
      }

      @Override
      public void update() {
      }

      @Override
      public void decorate(UiCanvas var1) {
      }

      @Override
      public void requestFocus() {
      }
   };

   void setTree(TreePanel var1);

   void update();
}
