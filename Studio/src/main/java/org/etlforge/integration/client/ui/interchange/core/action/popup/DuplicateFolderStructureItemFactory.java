package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.core.action.DuplicateFolderStructureAction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class DuplicateFolderStructureItemFactory extends AbstractPopupMenuItemFactory {
   private final ProjectContentViewerManager contentViewers;
   private final ProjectPersistor persistor;
   private final ImmutableSet<EntityType> selectabeTypes;

   public DuplicateFolderStructureItemFactory(ProjectContentViewerManager var1, ProjectPersistor var2, Iterable<EntityType> var3) {
      this.contentViewers = var1;
      this.persistor = var2;
      this.selectabeTypes = ImmutableSet.copyOf(var3);
   }

   @Override
   protected PopupMenuItem createItem() {
      return new DuplicateFolderStructureItemFactory.ItemImpl();
   }

   private class ItemImpl implements PopupMenuItem {
      private RootFolder rootFolder;

      private ItemImpl() {
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.rootFolder = null;
         if (var2.size() == 1) {
            Object var3 = var2.get(0);
            if (var3 instanceof RootFolder) {
               RootFolder var4 = (RootFolder)var3;
               if (this.isApplicable(var4)) {
                  this.rootFolder = var4;
               }
            }
         }
      }

      private boolean isApplicable(RootFolder var1) {
         return !var1.getItemType().isWizardType();
      }

      @Override
      public boolean isVisible() {
         return this.rootFolder != null;
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         DuplicateFolderStructureAction var2 = new DuplicateFolderStructureAction(
            this.rootFolder, DuplicateFolderStructureItemFactory.this.selectabeTypes, DuplicateFolderStructureItemFactory.this.persistor
         );
         var2.setProjectViewer(DuplicateFolderStructureItemFactory.this.contentViewers.getViewer());
         var1.add(var2);
      }
   }
}
