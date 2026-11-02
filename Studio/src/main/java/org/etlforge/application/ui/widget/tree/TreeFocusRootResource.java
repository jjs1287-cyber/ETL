package org.etlforge.application.ui.widget.tree;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;

public final class TreeFocusRootResource extends AbstractWindowElementResource {
   private final TreePanel treePanel;
   private final ActiveObjectProvidingTree activeObjects;

   public TreeFocusRootResource(TreePanel var1, ActiveObjectProvidingTree var2) {
      this.treePanel = var1;
      this.activeObjects = var2;
   }

   public TreeFocusRootResource(TreePanel var1, ActiveObjectProvidingTree var2, WindowElementStateSpan var3) {
      super(var3);
      this.treePanel = var1;
      this.activeObjects = var2;
   }

   @Override
   public void create(WindowElement var1) {
      ApplicationUi.getFocusManager().addFocusRoot(this.treePanel.getUi(), new FocusListener() {
         @Override
         public void focusGained(FocusEvent var1) {
            TreeFocusRootResource.this.treePanel.activated();
            if (TreeFocusRootResource.this.activeObjects != null) {
               TreeFocusRootResource.this.activeObjects.setActive(true);
            }
         }

         @Override
         public void focusLost(FocusEvent var1) {
            TreeFocusRootResource.this.treePanel.deactivated();
            if (TreeFocusRootResource.this.activeObjects != null) {
               TreeFocusRootResource.this.activeObjects.setActive(false);
            }
         }
      });
   }

   @Override
   public void dispose(WindowElement var1) {
      ApplicationUi.getFocusManager().removeFocusRoot(this.treePanel.getUi());
   }
}
