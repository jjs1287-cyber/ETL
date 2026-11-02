package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TreeMapperModeMenu;
import org.etlforge.integration.data.entity.Transformation;

public final class TreeMapperModeMenuItemFactory extends AbstractPopupMenuItemFactory {
   @Override
   protected PopupMenuItem createItem() {
      return new TreeMapperModeMenuItemFactory.PopupMenuItemImpl();
   }

   private class PopupMenuItemImpl implements PopupMenuItem {
      private boolean visible;
      private JMenu jmenu;

      private PopupMenuItemImpl() {
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         if (this.jmenu != null) {
            var1.add(this.jmenu);
         }
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.visible = false;
         this.jmenu = null;
         if (this.isSupportedSite(var1)) {
            TransformationPage var3 = this.getPage(var2);
            if (var3 != null) {
               this.visible = true;
               this.jmenu = this.createMenu(var3);
            }
         }
      }

      private boolean isSupportedSite(PopupMenuSite var1) {
         return var1 == PopupMenuSites.PAGE || var1 == PopupMenuSites.DETACHED_PAGE;
      }

      private TransformationPage getPage(List<?> var1) {
         if (var1.size() == 1) {
            Object var2 = var1.get(0);
            if (var2 instanceof Transformation) {
               Transformation var3 = (Transformation)var2;
               Editor var4 = UiCompanion.of(var3).findOpenPage();
               if (var4 instanceof TransformationPage) {
                  return (TransformationPage)var4;
               }
            }
         }

         return null;
      }

      private JMenu createMenu(TransformationPage var1) {
         TreeMapperModeMenu var2 = new TreeMapperModeMenu(var1);
         return var2.getMenu();
      }

      @Override
      public boolean isVisible() {
         return this.visible;
      }
   }
}
