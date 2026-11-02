package org.etlforge.application.ui.window.editor.service;

import java.awt.AWTKeyStroke;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorListDecorator;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.ui.widget.ListSelectorWindow;
import org.etlforge.ui.widget.ListSelectorWindowListener;

public class EditorSelectorList {
   private final EditorService editorSvc;
   private final ListSelectorWindow<EditorListDecorator> window;

   public EditorSelectorList(EditorService var1) {
      this.editorSvc = var1;
      this.window = this.createWindow();
      this.registerKeyHandlers();
   }

   private ListSelectorWindow<EditorListDecorator> createWindow() {
      EditorListDecorator[] var1 = this.createListItems();
      ListSelectorWindow var2 = new ListSelectorWindow<>(var1);
      var2.setListFactory(ComponentFactories.newKongaListFactory());
      JLabel var3 = new JLabel(PackageResources.Popup.TITLE);
      var3.setBorder(BorderFactory.createBevelBorder(0));
      var2.setCaption(var3);
      var2.addListener(new EditorSelectorList.Listener());
      return var2;
   }

   private EditorListDecorator[] createListItems() {
      Editor[] var1 = this.editorSvc.getNavigationOrder();
      EditorListDecorator[] var2 = new EditorListDecorator[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = new EditorListDecorator(var1[var3]);
      }

      return var2;
   }

   private void registerKeyHandlers() {
      this.window.addMoveDownKey(AWTKeyStroke.getAWTKeyStroke(9, 128));
      this.window.addMoveDownKey(AWTKeyStroke.getAWTKeyStroke(40, 128));
      this.window.addMoveDownKey(AWTKeyStroke.getAWTKeyStroke(40, 192));
      this.window.addMoveUpKey(AWTKeyStroke.getAWTKeyStroke(9, 192));
      this.window.addMoveUpKey(AWTKeyStroke.getAWTKeyStroke(38, 128));
      this.window.addMoveUpKey(AWTKeyStroke.getAWTKeyStroke(38, 192));
   }

   public void display(ListSelectorWindow.Direction var1) {
      this.window.display(var1);
      this.window.updateSelection(var1);
   }

   private class Listener implements ListSelectorWindowListener<EditorListDecorator> {
      private Listener() {
      }

      public void done(EditorListDecorator var1) {
         if (var1 != null) {
            EditorSelectorList.this.editorSvc.openEditor(var1.getEditor());
         }
      }

      @Override
      public void cancelled() {
      }

      public void selectionChanged(EditorListDecorator var1) {
      }
   }
}
