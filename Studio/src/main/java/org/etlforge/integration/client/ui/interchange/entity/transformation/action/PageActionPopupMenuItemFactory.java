package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class PageActionPopupMenuItemFactory extends AbstractPopupMenuItemFactory {
   private final EditorService editors;
   private final String actionId;

   public PageActionPopupMenuItemFactory(EditorService var1, String var2) {
      this.editors = var1;
      this.actionId = var2;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new PageActionPopupMenuItemFactory.ItemImpl();
   }

   private final class ItemImpl implements PopupMenuItem {
      private boolean visible;

      private ItemImpl() {
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         TransformationPage var2 = this.getPage();
         if (var2 != null) {
            JMenuItem var3 = this.createMenuItem(var2);
            if (var3 != null) {
               var1.add(var3);
            }
         }
      }

      private JMenuItem createMenuItem(TransformationPage var1) {
         ApplicationAction var2 = var1.getPageAction(PageActionPopupMenuItemFactory.this.actionId);
         return var2 != null && var2.isSupported() ? new KongaMenuItem(var2) : null;
      }

      private TransformationPage getPage() {
         Editor var1 = PageActionPopupMenuItemFactory.this.editors.getActiveEditor();
         return var1 instanceof TransformationPage ? (TransformationPage)var1 : null;
      }

      @Override
      public boolean isVisible() {
         return this.visible;
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.visible = this.isSupportedSite(var1) && var2.size() == 1 && var2.get(0) instanceof Transformation;
      }

      private boolean isSupportedSite(PopupMenuSite var1) {
         return var1 == PopupMenuSites.PAGE || var1 == PopupMenuSites.DETACHED_PAGE;
      }
   }
}
