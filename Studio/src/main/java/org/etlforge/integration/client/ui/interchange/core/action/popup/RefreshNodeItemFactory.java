package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.data.entity.Folder;

public final class RefreshNodeItemFactory extends AbstractPopupMenuItemFactory {
   private final ProjectContentViewerManager contentViewers;

   public RefreshNodeItemFactory(ProjectContentViewerManager var1) {
      this.contentViewers = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new RefreshNodeItemFactory.Item(new RefreshNodeItemFactory.ActionImpl());
   }

   private class ActionImpl extends AbstractAction {
      private Folder folder;

      public ActionImpl() {
         ActionDecorators.REFRESH.decorate(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.folder != null) {
            ProjectContentViewer var2 = RefreshNodeItemFactory.this.contentViewers.getViewer();
            var2.refresh(this.folder);
         }
      }

      public void setFolder(Folder var1) {
         this.folder = var1;
      }
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(RefreshNodeItemFactory.ActionImpl var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         boolean var3 = this.isVisibleFor(var1, var2);
         boolean var4 = var3 && var2.size() == 1 && var2.get(0) instanceof Folder;
         this.setVisible(var3);
         this.setEnabled(var4);
      }

      private boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
         if (var1.isTree()) {
            for (Object var4 : var2) {
               if (var4 instanceof Folder) {
                  ((RefreshNodeItemFactory.ActionImpl)this.getAction()).setFolder((Folder)var4);
                  return true;
               }
            }
         }

         return false;
      }
   }
}
