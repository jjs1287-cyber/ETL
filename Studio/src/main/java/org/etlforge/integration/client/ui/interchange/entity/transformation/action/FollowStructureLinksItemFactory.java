package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StructureLink;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.entity.Transformation;

public final class FollowStructureLinksItemFactory extends AbstractPopupMenuItemFactory {
   private final EditorService editors;

   public FollowStructureLinksItemFactory(EditorService var1) {
      this.editors = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new FollowStructureLinksItemFactory.ItemImpl();
   }

   private final class ItemImpl implements PopupMenuItem {
      private boolean visible;

      private ItemImpl() {
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         TransformationPage var2 = this.getPage();
         if (var2 != null) {
            JMenu var3 = this.createMenu(var2);
            var1.add(var3);
         }
      }

      private TransformationPage getPage() {
         Editor var1 = FollowStructureLinksItemFactory.this.editors.getActiveEditor();
         return var1 instanceof TransformationPage ? (TransformationPage)var1 : null;
      }

      private JMenu createMenu(TransformationPage var1) {
         JMenu var2 = new JMenu("Go To");
         TreeMapper var3 = var1.getTreeMapper();
         this.addLinkActions(var2, var3);
         var2.setEnabled(var2.getItemCount() > 0);
         return var2;
      }

      private void addLinkActions(JMenu var1, TreeMapper var2) {
         StructureLink var3 = var2.getInputLink();
         if (var3.isEnabled()) {
            var1.add(new FollowStructureLinksItemFactory.LinkAction(var3));
         }

         StructureLink var4 = var2.getOutputLink();
         if (var4.isEnabled() && !var3.equals(var4)) {
            var1.add(new FollowStructureLinksItemFactory.LinkAction(var4));
         }
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

   private static final class LinkAction extends AbstractAction {
      private final StructureLink link;

      public LinkAction(StructureLink var1) {
         super(var1.getName());
         assert var1.isEnabled();
         this.link = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.link.follow();
      }
   }
}
