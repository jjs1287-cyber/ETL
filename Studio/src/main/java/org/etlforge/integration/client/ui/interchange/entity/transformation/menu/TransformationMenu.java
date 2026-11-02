package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.JMenuFactory;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuContribution;
import org.etlforge.application.ui.menu.MenuDictionary;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuDictionary;
import org.etlforge.integration.client.ui.interchange.component.menu.prefs.IncludeTransformationMenuPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.system.Disposable;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class TransformationMenu implements Menu, Disposable {
   private final EditorService editors;
   private final TransformationMenu.ActivePageListener editorListener;
   private TransformationPage page;
   private JMenu menu;

   public TransformationMenu(EditorService var1) {
      this.editors = var1;
      this.editorListener = new TransformationMenu.ActivePageListener();
      var1.addEditorServiceListener(this.editorListener);
   }

   @Override
   public String getID() {
      return "menu.ic.Transformation";
   }

   @Override
   public void dispose() {
      this.editors.removeEditorServiceListener(this.editorListener);
   }

   @Override
   public boolean isSupported() {
      return IncludeTransformationMenuPreference.get();
   }

   @Override
   public JMenu createJMenu(MenuDictionary var1) {
      JMenuFactory var2 = new JMenuFactory(this.getID(), var1);
      this.menu = var2.createEmptyMenu();
      this.menu.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
         @Override
         public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
            TransformationMenu.this.populateMenu();
         }

         @Override
         public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
            TransformationMenu.this.menu.removeAll();
         }

         @Override
         public void popupMenuCanceled(PopupMenuEvent var1) {
         }
      });
      this.menu.setEnabled(this.page != null);
      return this.menu;
   }

   private void populateMenu() {
      if (this.page != null) {
         this.addActions("action.ic.tx.LoadSource", "action.ic.tx.TestTransformation", "action.ic.tx.TestOperation");
         this.menu.addSeparator();
         this.addActions("action.ic.tx.Search", "action.ic.tx.Migrate", "action.ic.tx.Refresh");
         JMenu var1 = this.createEditMenu();
         this.menu.add(var1);
      }
   }

   private void addActions(String... var1) {
      for (String var5 : var1) {
         ApplicationAction var6 = this.page.getPageAction(var5);
         if (var6 != null) {
            this.menu.add(new KongaMenuItem(var6));
         }
      }
   }

   private JMenu createEditMenu() {
      EditTransformationMenu var1 = new EditTransformationMenu(this.page);
      return var1.createJMenu(InterchangeMenuDictionary.DICTIONARY);
   }

   @Override
   public void addExtension(MenuExtensionPoint var1, MenuContribution var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void removeExtension(MenuExtensionPoint var1, String var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void removeAllExtensions() {
      throw new UnsupportedOperationException();
   }

   @Override
   public void populate(JMenu var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void populate(JPopupMenu var1) {
      throw new UnsupportedOperationException();
   }

   private class ActivePageListener extends EditorServiceAdapter {
      private ActivePageListener() {
      }

      @Override
      public void activeEditorChanged(EditorServiceEvent var1) {
         Editor var2 = var1.getEditor();
         TransformationMenu.this.page = var2 instanceof TransformationPage ? (TransformationPage)var2 : null;
         if (TransformationMenu.this.menu != null) {
            TransformationMenu.this.menu.setEnabled(TransformationMenu.this.page != null);
         }
      }
   }
}
