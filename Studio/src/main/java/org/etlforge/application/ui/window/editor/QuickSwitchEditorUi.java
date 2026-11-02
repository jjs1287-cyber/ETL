package org.etlforge.application.ui.window.editor;

import com.google.common.base.Predicate;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import org.etlforge.string.WildcardStringMatcher;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.selection.SelectFirstAction;
import org.etlforge.ui.widget.text.KongaTextField;

public final class QuickSwitchEditorUi extends AbstractEditorSelector implements UiProvider, Disposable {
   private final KongaTextField searchField;
   private final EditorListWidget editorList;
   private final SelectionAction selector = new QuickSwitchEditorUi.Selector();
   private boolean includeSearchField;
   private Disposable notificationIndicator;

   public QuickSwitchEditorUi(List<Editor> var1) {
      this.editorList = this.createList(var1);
      this.searchField = this.createSearchField();
      this.includeSearchField = true;
   }

   private EditorListWidget createList(List<Editor> var1) {
      EditorListWidget var2 = new EditorListWidget(var1) {
         @Override
         protected void customizeFactory(KongaListFactory var1, ListModel var2x) {
            int var3 = Math.min(var2x.getSize(), 20);
            var1.setVisibleRowCount(var3);
            var1.setBorder(Empty.border(2, 5, 5, 5));
            var1.setPopupMenuRequestHandler(QuickSwitchEditorUi.this.new ContextMenuFactory());
         }
      };
      var2.setTransient(true);
      var2.setDefaultAction(this.selector);
      var2.addSelectionListener(new QuickSwitchEditorUi.SelectionRouter());
      return var2;
   }

   private KongaTextField createSearchField() {
      KongaTextField var1 = new KongaTextField();
      var1.getDocument().addDocumentListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            QuickSwitchEditorUi.this.editorList.setFilter(new QuickSwitchEditorUi.Filter(QuickSwitchEditorUi.this.searchField.getText()));
            QuickSwitchEditorUi.this.editorList.selectFirst();
         }
      });
      var1.addActionListener(this.selector);
      SelectFirstAction.installInField(this.editorList, var1);
      return var1;
   }

   public void setSelectedEditor(Editor var1) {
      this.editorList.setSelectedEditor(var1);
   }

   public Editor getSelectedEditor() {
      IndexedSelection var1 = this.editorList.getSelection();
      return var1.isEmpty() ? null : (Editor)var1.head();
   }

   public void setIncludeSearchField(boolean var1) {
      this.includeSearchField = var1;
   }

   public void setNotifications(Collection<Editor> var1) {
      this.notificationIndicator = this.editorList.setNotifications(var1);
   }

   @Override
   public void dispose() {
      if (this.notificationIndicator != null) {
         this.notificationIndicator.dispose();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderBuilder var2 = new BorderBuilder().rounded(8, 8, new Color(64, 64, 64)).empty(2);
      Vertical.borderLayout().of(this.includeSearchField ? this.searchField : null, this.editorList).withGap(3).withBorder(var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.searchField.requestFocusInWindow();
   }

   private class ContextMenuFactory implements PopupMenuRequestHandler {
      private ContextMenuFactory() {
      }

      @Override
      public boolean popupMenuRequested(Component var1, MouseEvent var2) {
         Editor var3 = QuickSwitchEditorUi.this.getSelectedEditor();
         if (var3 == null) {
            return false;
         }

         boolean var4 = true;
         JPopupMenu var5 = QuickSwitchEditorUi.this.createContextMenu(var3, var4);
         var5.show(var1, var2.getX(), var2.getY());
         return true;
      }
   }

   private static final class Filter implements ListFilter {
      private final Predicate<String> impl;

      public Filter(final String var1) {
         final WildcardStringMatcher var2 = new WildcardStringMatcher(false);
         this.impl = new Predicate<String>() {
            public boolean apply(String var1x) {
               return var2.matches(var1, var1x);
            }
         };
      }

      @Override
      public boolean test(Object var1) {
         String var2 = ((Editor)var1).getTitle();
         return this.impl.apply(var2);
      }
   }

   private final class SelectionRouter implements SelectionListener {
      private SelectionRouter() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         IndexedSelection var2 = (IndexedSelection)var1.getSelection();
         if (!var2.isEmpty()) {
            Editor var3 = (Editor)var2.head();
            QuickSwitchEditorUi.this.fireTemporarySelection(var3);
         }
      }
   }

   private final class Selector extends AbstractSingleObjectSelectionAction<Editor> {
      public Selector() {
         super(Editor.class);
      }

      protected void run(Editor var1, ActionEvent var2) {
         QuickSwitchEditorUi.this.fireFinalSelection(var1);
      }
   }
}
