package org.etlforge.integration.client.ui.interchange.component.page.entity;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.window.editor.DefaultEditorController;
import org.etlforge.application.ui.window.editor.UndoService;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandler;

public final class EntityPageControllerBuilder {
   public static DefaultEditorController buildController(IntegrationEntityPage var0) {
      DefaultEditorController var1 = new DefaultEditorController();
      var1.registerAction(KeyUtils.UNDO, new EntityPageControllerBuilder.UndoAction(var0));
      var1.registerAction(KeyUtils.REDO, new EntityPageControllerBuilder.RedoAction(var0));
      var1.setPopupMenuRequestHandler(new EntityPageControllerBuilder.ContextMenu(var0));
      return var1;
   }

   private EntityPageControllerBuilder() {
   }

   private abstract static class ActionBase extends EntityPageControllerBuilder.DetachedPageAction {
      public ActionBase(IntegrationEntityPage var1) {
         super(var1);
      }

      @Override
      protected final void execute() {
         UndoService var1 = this.page.getUndoService();
         UndoService.Activity var2 = this.getActivity(var1);
         if (var2.isAvailable()) {
            var2.invoke();
         }
      }

      protected abstract UndoService.Activity getActivity(UndoService var1);
   }

   private static class ContextMenu implements PopupMenuRequestHandler {
      private final IntegrationEntityPage page;

      public ContextMenu(IntegrationEntityPage var1) {
         this.page = var1;
      }

      @Override
      public boolean popupMenuRequested(Component var1, MouseEvent var2) {
         JComponent var3 = this.page.getMainPanel();
         Point var4 = var2.getPoint();
         if (var1 != var3) {
            var4 = SwingUtilities.convertPoint(var1, var4, var3);
         }

         return var3.contains(var4) ? this.page.showContextMenu(var4.x, var4.y) : false;
      }
   }

   private abstract static class DetachedPageAction extends AbstractAction {
      protected final IntegrationEntityPage page;

      public DetachedPageAction(IntegrationEntityPage var1) {
         this.page = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (!this.pageIsInMainWindow()) {
            this.execute();
         }
      }

      protected abstract void execute();

      private boolean pageIsInMainWindow() {
         Window var1 = SwingUtilities.getWindowAncestor(this.page.getMainPanel());
         return var1 == this.page.getWindow().getFrame();
      }

      @Override
      public boolean isEnabled() {
         return !this.pageIsInMainWindow() && super.isEnabled();
      }
   }

   private static class RedoAction extends EntityPageControllerBuilder.ActionBase {
      public RedoAction(IntegrationEntityPage var1) {
         super(var1);
      }

      @Override
      protected UndoService.Activity getActivity(UndoService var1) {
         return var1.getRedoActivity();
      }
   }

   private static class UndoAction extends EntityPageControllerBuilder.ActionBase {
      public UndoAction(IntegrationEntityPage var1) {
         super(var1);
      }

      @Override
      protected UndoService.Activity getActivity(UndoService var1) {
         return var1.getUndoActivity();
      }
   }
}
