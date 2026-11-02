package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.QuickSwitchEditorWindow;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPaneCloseEvent;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPaneCloseHandler;
import org.etlforge.ui.widget.tabbedpane.TabReorderHandler;

final class TabbedEditorServiceUi extends AbstractEditorServiceUi implements EditorServiceUi {
   private final KongaTabbedPane tabPane;
   private final Map<Editor, DetachedEditorUi> detachedEditors;
   private Editor activeDetachedEditor;
   private final List<Editor> editorsInTabPane = new ArrayList<>();
   private final TabbedEditorServiceUi.TabbedPaneListener tabPaneListener;
   private final Map<Editor, TabbedEditorServiceUi.FocusRoot> focusRoots;
   private final TabNotificationService notifications;

   public TabbedEditorServiceUi(DefaultEditorService var1) {
      super(var1);
      this.detachedEditors = Maps.newIdentityHashMap();
      this.focusRoots = Maps.newIdentityHashMap();
      this.tabPane = this.createUI();
      this.tabPaneListener = new TabbedEditorServiceUi.TabbedPaneListener();
      this.tabPane.setCloseHandler(this.tabPaneListener);
      this.tabPane.addChangeListener(this.tabPaneListener);
      this.notifications = new TabbedEditorServiceUi.Notifications(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tabPane.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tabPane.requestFocus();
   }

   @Override
   public void addEditor(Editor var1) {
      this.addEditorToTabbedPane(var1, -1);
   }

   private void addEditorToTabbedPane(Editor var1, int var2) {
      if (var2 == -1) {
         var2 = this.editorsInTabPane.size();
      }

      this.editorsInTabPane.add(var2, var1);
      JPanel var3 = BorderLayoutBuilder.fillWith(var1);
      this.tabPane.insertTab(var1.getTitle(), var1.getSmallIcon(), var3, null, var2);
      TabbedEditorServiceUi.FocusRoot var4 = new TabbedEditorServiceUi.FocusRoot(var3);
      var4.activate();
      this.focusRoots.put(var1, var4);
   }

   @Override
   public Editor getActiveEditor() {
      return this.activeDetachedEditor != null ? this.activeDetachedEditor : this.getSelectedAttachedEditor();
   }

   private Editor getSelectedAttachedEditor() {
      int var1 = this.tabPane.getSelectedIndex();
      return this.isValidIndex(var1) ? this.editorsInTabPane.get(var1) : null;
   }

   private boolean isValidIndex(int var1) {
      return var1 >= 0 && var1 < this.editorsInTabPane.size();
   }

   @Override
   public boolean isDisplayingEditor(Editor var1) {
      return this.isDetached(var1) || this.isAttached(var1);
   }

   private boolean isDetached(Editor var1) {
      return this.detachedEditors.containsKey(var1);
   }

   private boolean isAttached(Editor var1) {
      for (Editor var3 : this.editorsInTabPane) {
         if (var3 == var1) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void selectEditor(Editor var1) {
      this.selectEditorImpl(var1, false);
   }

   private void selectEditorImpl(Editor var1, boolean var2) {
      if (!this.isDetached(var1)) {
         int var3 = 0;

         for (int var4 = this.editorsInTabPane.size(); var3 < var4; var3++) {
            if (this.editorsInTabPane.get(var3) == var1) {
               this.tabPane.setSelectedIndex(var3);
               if (var2) {
                  this.tabPaneListener.stateChanged(null);
               }
               break;
            }
         }
      }
   }

   @Override
   public int getNumberOfOpenEditors() {
      return this.tabPane.getTabCount() + this.detachedEditors.size();
   }

   @Override
   public List<Editor> getOpenEditors() {
      ArrayList var1 = new ArrayList<>(this.editorsInTabPane);

      for (Editor var3 : this.detachedEditors.keySet()) {
         var1.add(var3);
      }

      return var1;
   }

   @Override
   public void updateTitleAndIcon(Editor var1) {
      int var2 = this.getTabIndex(var1);
      if (var2 >= 0) {
         this.tabPane.setTitleAt(var2, var1.getTitle());
         this.tabPane.setIconAt(var2, var1.getSmallIcon());
      } else if (this.isDetached(var1)) {
         DetachedEditorUi var3 = this.detachedEditors.get(var1);
         var3.updateTitle();
      }
   }

   private int getTabIndex(Editor var1) {
      return this.editorsInTabPane.indexOf(var1);
   }

   @Override
   public void removeEditor(Editor var1, Editor var2) {
      if (this.isDetached(var1)) {
         this.closeDetachedEditor(var1);
      } else {
         this.closeAttachedEditor(var1);
      }

      if (var2 != null) {
         this.selectEditorImpl(var2, true);
      }
   }

   private void closeDetachedEditor(Editor var1) {
      DetachedEditorUi var2 = this.detachedEditors.remove(var1);
      var2.close();
      if (var1 == this.activeDetachedEditor) {
         this.activeDetachedEditor = null;
      }
   }

   private void closeAttachedEditor(Editor var1) {
      int var2 = this.getTabIndex(var1);
      if (var2 >= 0) {
         this.editorsInTabPane.remove(var2);
         this.tabPane.removeTabAt(var2);
         TabbedEditorServiceUi.FocusRoot var3 = this.focusRoots.remove(var1);
         var3.dispose();
      }
   }

   private KongaTabbedPane createUI() {
      KongaTabbedPane var1 = new KongaTabbedPane();
      var1.disableCtrlUp();
      var1.setUnselectedTabBackground(Colors.UNSELECTED_TAB_BACKGROUND);
      var1.setCloseable(true);
      var1.setKeyboardNavigationEnabled(false);
      var1.enableTabReordering(new TabbedEditorServiceUi.Reorderer());
      var1.setInterpretTitlesAsMnemonicStrings(false);

      for (int var2 = 1; var2 <= 10; var2++) {
         var1.installKeyBinding(new TabbedEditorServiceUi.ActivatorKeyAction(var2), 2);
      }

      return var1;
   }

   @Override
   public void addDetachedEditor(Editor var1) {
      this.detachEditorImpl(var1, new TabbedEditorServiceUi.RemoveDetachedEditorAction());
   }

   @Override
   public void detachEditor(Editor var1) {
      this.detachEditorImpl(var1, new TabbedEditorServiceUi.RemoveDetachedEditorAction());
   }

   private void detachEditorImpl(Editor var1, DetachedEditorUi.Attacher var2) {
      if (var1 != null) {
         if (!this.detachedEditors.containsKey(var1)) {
            this.closeAttachedEditor(var1);
            final DetachedEditorUi var3 = new DetachedEditorUi(
               this.getEditorService().getAppWin(), var1, var2, new TabbedEditorServiceUi.DetachedEditorUiFocusListener(var1)
            );
            var1.appendState(WindowElementState.OPEN);
            var1.appendState(WindowElementState.DETACHED);
            this.detachedEditors.put(var1, var3);
            this.activeDetachedEditor = var1;
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  var3.show();
               }
            });
         }
      }
   }

   @Override
   public void addNotification(Editor var1) {
      this.notifications.addNotification(var1);
   }

   @Override
   public void openQuickSelector() {
      QuickSwitchEditorWindow var1 = QuickSwitchEditorWindow.open(this.getEditorService().getAppWin());
      if (var1 != null) {
         var1.setActiveArea(this.tabPane.asComponent());
      }
   }

   private class ActivatorKeyAction extends AbstractAction {
      private final int index;

      public ActivatorKeyAction(int var2) {
         assert var2 >= 1 && var2 <= 10 : "index is 1-based";
         this.index = var2;
         int var3 = var2 == 10 ? 48 : 48 + var2;
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(var3));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.index - 1;
         if (var2 < TabbedEditorServiceUi.this.tabPane.getTabCount()) {
            TabbedEditorServiceUi.this.tabPane.setSelectedIndex(var2);
         }
      }
   }

   private class DetachedEditorUiFocusListener extends FocusAdapter {
      private final Editor editor;

      public DetachedEditorUiFocusListener(Editor var2) {
         this.editor = var2;
      }

      @Override
      public void focusGained(FocusEvent var1) {
         TabbedEditorServiceUi.this.activeDetachedEditor = this.editor;
         TabbedEditorServiceUi.this.getEditorService().fireActiveEditorChanged();
      }
   }

   private class FocusRoot extends FocusAdapter implements Disposable {
      private JComponent component;

      public FocusRoot(JComponent var2) {
         this.component = var2;
      }

      public void activate() {
         ApplicationUi.getFocusManager().addFocusRoot(this.component, this);
      }

      @Override
      public void dispose() {
         ApplicationUi.getFocusManager().removeFocusRoot(this.component);
         this.component = null;
      }

      @Override
      public void focusGained(FocusEvent var1) {
         TabbedEditorServiceUi.this.getEditorService().fireActiveEditorChanged();
      }
   }

   private class Notifications extends TabNotificationService {
      public Notifications(EditorService var2) {
         super(var2, TabbedEditorServiceUi.this);
      }

      @Override
      protected boolean noNeedToNotify(Editor var1) {
         return var1 == TabbedEditorServiceUi.this.getSelectedAttachedEditor() || TabbedEditorServiceUi.this.isDetached(var1);
      }

      @Override
      protected Component getTabComponent(Editor var1) {
         int var2 = TabbedEditorServiceUi.this.getTabIndex(var1);
         return var2 >= 0 && var2 < TabbedEditorServiceUi.this.tabPane.getTabCount() ? TabbedEditorServiceUi.this.tabPane.getTabComponentAt(var2) : null;
      }
   }

   private class RemoveDetachedEditorAction implements DetachedEditorUi.Attacher {
      private RemoveDetachedEditorAction() {
      }

      @Override
      public boolean attach(Editor var1) {
         return TabbedEditorServiceUi.this.getEditorService().closeEditorImpl(var1, false);
      }
   }

   private class Reorderer implements TabReorderHandler {
      private Reorderer() {
      }

      @Override
      public void handle(KongaTabbedPane var1, int var2, int var3) {
         if (var2 != var3) {
            Editor var4 = TabbedEditorServiceUi.this.editorsInTabPane.remove(var2);
            TabbedEditorServiceUi.this.tabPane.removeTabAt(var2);
            TabbedEditorServiceUi.this.focusRoots.remove(var4);
            TabbedEditorServiceUi.this.addEditorToTabbedPane(var4, var3);
            TabbedEditorServiceUi.this.tabPane.setSelectedIndex(var3);
         }
      }
   }

   private class TabbedPaneListener implements ChangeListener, KongaTabbedPaneCloseHandler {
      private TabbedPaneListener() {
      }

      @Override
      public void closeAllTabsRequested(KongaTabbedPaneCloseEvent var1) {
         TabbedEditorServiceUi.this.getEditorService().closeAllEditors();
      }

      @Override
      public void closeCurrentTabRequested(KongaTabbedPaneCloseEvent var1) {
         int var2 = var1.getSelectedIndex();
         if (var2 >= 0 && var2 < TabbedEditorServiceUi.this.tabPane.getTabCount()) {
            TabbedEditorServiceUi.this.tabPane.setSelectedIndex(var2);
            Editor var3 = TabbedEditorServiceUi.this.editorsInTabPane.get(var2);
            TabbedEditorServiceUi.this.getEditorService().closeEditor(var3);
         }
      }

      @Override
      public void closeOtherTabsRequested(KongaTabbedPaneCloseEvent var1) {
         int var2 = var1.getSelectedIndex();
         if (var2 >= 0 && var2 < TabbedEditorServiceUi.this.tabPane.getTabCount()) {
            TabbedEditorServiceUi.this.tabPane.setSelectedIndex(var2);
            Editor var3 = TabbedEditorServiceUi.this.editorsInTabPane.get(var2);
            TabbedEditorServiceUi.this.getEditorService().closeOtherEditors(var3);
         }
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         this.updateEditorStates();
         if (TabbedEditorServiceUi.this.getNumberOfOpenEditors() > 0) {
            TabbedEditorServiceUi.this.getEditorService().fireActiveEditorChanged();
         }
      }

      private void updateEditorStates() {
         int var1 = TabbedEditorServiceUi.this.tabPane.getSelectedIndex();

         for (int var2 = 0; var2 < TabbedEditorServiceUi.this.tabPane.getTabCount(); var2++) {
            if (var2 != var1) {
               TabbedEditorServiceUi.this.editorsInTabPane.get(var2).revokeState(WindowElementState.ACTIVE);
            }
         }

         if (TabbedEditorServiceUi.this.activeDetachedEditor == null) {
            Editor var3 = TabbedEditorServiceUi.this.getActiveEditor();
            if (var3 != null) {
               var3.appendState(WindowElementState.ACTIVE);
            }
         }
      }
   }
}
