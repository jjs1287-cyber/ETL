package org.etlforge.application.ui.window.editor.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.view.ViewKeyDispatcher;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.EditorServiceListener;
import org.etlforge.application.ui.window.editor.EditorServiceListeners;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderEvent;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListeners;
import org.etlforge.lang.Arrays2;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.wait.WaitLock;

public final class DefaultEditorService implements EditorService, ActiveObjectProvider, PropertyChangeListener {
   private final ApplicationWindow window;
   private EditorServiceUi ui;
   private final JComponent uiContainer;
   private NavigationManager navigationManager;
   private BackwardForwardNavigator backwardForwardNavigator;
   private boolean active;
   private final EditorServiceListeners editorListeners = new EditorServiceListeners();
   private final ActiveObjectProviderListeners activeObjectListeners = new ActiveObjectProviderListeners();
   private final ViewKeyDispatcher keyDispatcher;
   private final Map<Editor, String> groups = Maps.newHashMap();
   private Editor lastActiveEditor;
   private final EditorPreferencesMemento memento = new EditorPreferencesMemento();
   private boolean blockActiveEditorChangeEvents;

   public DefaultEditorService(ApplicationWindow var1) {
      this.window = Preconditions.checkNotNull(var1);
      this.ui = this.createUi();
      this.uiContainer = BorderLayoutBuilder.fillWith(this.ui);
      this.keyDispatcher = new ViewKeyDispatcher(var1);
      EditorServiceUiModePreference.INSTANCE.addListener(new DefaultEditorService.UiModeListener());
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.keyDispatcher);
   }

   private EditorServiceUi createUi() {
      EditorServiceUiMode var1 = EditorServiceUiModePreference.get();
      return !var1.isSupported() ? new TabbedEditorServiceUi(this) : var1.createUi(this);
   }

   public void initializeActions() {
      this.navigationManager = new NavigationManager(this);
      this.backwardForwardNavigator = new BackwardForwardNavigator(this);
   }

   public ApplicationWindow getAppWin() {
      return this.window;
   }

   @Override
   public String getProviderId() {
      return EditorService.class.getName();
   }

   @Override
   public UiProvider getUi() {
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1) {
            var1.display(DefaultEditorService.this.uiContainer);
         }

         @Override
         public void requestFocus() {
            DefaultEditorService.this.ui.requestFocus();
         }
      };
   }

   @Override
   public Editor[] getNavigationOrder() {
      return this.navigationManager.getCurrentOrder();
   }

   @Override
   public final Editor getActiveEditor() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getActiveEditor");
      return this.ui.getActiveEditor();
   }

   @Override
   public boolean closeEditors(String var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "closeEditors");
      ArrayList var2 = new ArrayList();

      for (Entry var4 : this.groups.entrySet()) {
         if (KongaStringUtils.areEqual(var1, (String)var4.getValue())) {
            var2.add(var4.getKey());
         }
      }

      return this.closeEditors(var2);
   }

   @Override
   public void openEditor(Editor var1, String var2) {
      Preconditions.checkNotNull(var1, "editor");
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "openEditor");
      this.activate();
      if (!this.ui.isDisplayingEditor(var1)) {
         this.addEditorToWindow(var1);
         this.addEditorToUi(var1);
         this.fireEditorOpened(var1);
      }

      this.ui.selectEditor(var1);
      this.groups.put(var1, var2);
   }

   private void addEditorToWindow(Editor var1) {
      if (var1.getWindow() == null) {
         var1.addToWindow(this.window);
         var1.setOwner(this);
      }
   }

   @Override
   public void openEditor(Editor var1) {
      String var2 = this.groups.get(var1);
      this.openEditor(var1, var2);
   }

   @Override
   public void openEditorDetached(Editor var1) {
      Preconditions.checkNotNull(var1, "editor");
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "openEditorDetached");
      this.activate();
      if (this.ui.isDisplayingEditor(var1)) {
         this.ui.detachEditor(var1);
      } else {
         this.addEditorToWindow(var1);
         this.addDetachedEditorToUi(var1);
         this.fireEditorOpened(var1);
         String var2 = this.groups.get(var1);
         this.groups.put(var1, var2);
      }
   }

   public void switchToEditorSilently(Editor var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "switchToEditorSilently");
      if (this.ui.isDisplayingEditor(var1)) {
         this.blockActiveEditorChangeEvents = true;
         this.ui.selectEditor(var1);
         this.blockActiveEditorChangeEvents = false;
      }
   }

   public EditorPreferencesMemento getEditorMemento() {
      return this.memento;
   }

   private void addEditorToUi(Editor var1) {
      var1.addPropertyChangeListener(this);
      this.ui.addEditor(var1);
      var1.appendState(WindowElementState.OPEN);
      var1.appendState(WindowElementState.ATTACHED);
   }

   private void addDetachedEditorToUi(Editor var1) {
      var1.addPropertyChangeListener(this);
      this.ui.addDetachedEditor(var1);
   }

   @Override
   public boolean closeEditor(Editor var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "closeEditor");
      return this.closeEditorImpl(var1, false);
   }

   boolean closeEditorImpl(Editor var1, boolean var2) {
      if (!var1.removeFromWindow(var2)) {
         return false;
      }

      this.backwardForwardNavigator.setCloseEditorInProgress(true);
      Editor var3 = this.getNextEditorToSelectOnClose(var1);
      var1.revokeState(WindowElementState.ACTIVE);
      this.ui.removeEditor(var1, var3);
      var1.removePropertyChangeListener(this);
      var1.appendState(WindowElementState.CLOSED);
      this.groups.remove(var1);
      this.fireEditorClosed(var1);
      if (this.getNumberOfEditors() == 0) {
         this.lastActiveEditor = null;
      }

      this.backwardForwardNavigator.setCloseEditorInProgress(false);
      this.checkFocus();
      return true;
   }

   private Editor getNextEditorToSelectOnClose(Editor var1) {
      Editor var2 = this.getActiveEditor();
      return var1 == var2 ? this.navigationManager.getEditorToSelectWhenActivePageClosed(var1) : var2;
   }

   private void checkFocus() {
      if (this.ui.getNumberOfOpenEditors() == 0) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               JFrame var1 = DefaultEditorService.this.window.getFrame();
               JMenuBar var2 = var1.getJMenuBar();
               if (var2 != null) {
                  var2.requestFocusInWindow();
               }
            }
         });
      }
   }

   @Override
   public boolean closeOtherEditors(Editor var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "closeOtherEditors");
      if (var1 != null && this.ui.isDisplayingEditor(var1)) {
         return this.getNumberOfEditors() < 2 ? true : this.closeEditorsImpl(new ArrayList<>(this.ui.getOpenEditors()), var1);
      } else {
         return this.closeAllEditors();
      }
   }

   @Override
   public int getNumberOfEditors() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getNumberOfEditors");
      return this.ui.getNumberOfOpenEditors();
   }

   @Override
   public Editor[] getOpenEditors() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getOpenEditors");
      List var1 = this.ui.getOpenEditors();
      return (Editor[]) var1.toArray(new Editor[var1.size()]);
   }

   @Override
   public Editor getEditor(String var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getEditor");

      for (Editor var3 : this.ui.getOpenEditors()) {
         if (var3.getId().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public boolean closeAllEditors() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "closeAllEditors");
      int var1 = this.getNumberOfEditors();
      if (var1 == 0) {
         return true;
      } else if (var1 == 1) {
         this.closeEditor(this.getOpenEditors()[0]);
         return this.getNumberOfEditors() == 0;
      } else {
         return this.closeEditorsImpl(new ArrayList<>(this.ui.getOpenEditors()), null);
      }
   }

   @Override
   public boolean closeEditors(Collection<Editor> var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "closeEditors");
      Preconditions.checkNotNull(var1, "editors");
      return this.closeEditorsImpl(var1, null);
   }

   private boolean closeEditorsImpl(Collection<Editor> var1, Editor var2) {
      int var3 = var1.size();
      EditorCloser var4 = new EditorCloser(this);
      var4.closeEditors(var1, var2);
      return var4.getNumberOfEditorsThatWereClosed() == var3;
   }

   @Override
   public BackwardForwardStack getBackwardForwardStack() {
      return this.backwardForwardNavigator != null ? this.backwardForwardNavigator.getStack() : BackwardForwardStack.EMPTY;
   }

   @Override
   public void openQuickSelector() {
      this.ui.openQuickSelector();
   }

   @Override
   public void propertyChange(final PropertyChangeEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Object var1x = var1.getSource();
            Editor var2 = (Editor)var1x;
            if ("notification".equals(var1.getPropertyName())) {
               if (var1x instanceof Editor) {
                  DefaultEditorService.this.ui.addNotification(var2);
               }
            } else if (var1x instanceof Editor) {
               DefaultEditorService.this.ui.updateTitleAndIcon(var2);
               DefaultEditorService.this.editorListeners.call().dirtyStateChanged(new EditorServiceEvent(DefaultEditorService.this, var2));
            }
         }
      });
   }

   @Override
   public void addEditorServiceListener(EditorServiceListener var1) {
      this.editorListeners.add(var1);
   }

   @Override
   public void removeEditorServiceListener(EditorServiceListener var1) {
      this.editorListeners.remove(var1);
   }

   protected final void fireEditorOpened(Editor var1) {
      EditorServiceEvent var2 = new EditorServiceEvent(this, var1);
      this.editorListeners.call().editorOpened(var2);
      if (this.active) {
         this.fireActiveObjectChange();
      }
   }

   protected final void fireEditorClosed(Editor var1) {
      EditorServiceEvent var2 = new EditorServiceEvent(this, var1);
      this.editorListeners.call().editorClosed(var2);
      if (this.active) {
         this.fireActiveObjectChange();
      }
   }

   protected final void fireActiveEditorChanged() {
      if (!this.blockActiveEditorChangeEvents) {
         Editor var1 = this.getActiveEditor();
         if (var1 != this.lastActiveEditor) {
            this.lastActiveEditor = var1;
            EditorServiceEvent var2 = new EditorServiceEvent(this, var1);
            this.editorListeners.call().activeEditorChanged(var2);
         }

         if (this.active) {
            this.fireActiveObjectChange();
         }
      }
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectListeners.add(var1);
   }

   @Override
   public List<?> getActiveObjects() {
      ArrayList var1 = new ArrayList();
      Editor var2 = this.getActiveEditor();
      if (var2 instanceof ActiveObjectProvider) {
         var1.addAll(((ActiveObjectProvider)var2).getActiveObjects());
      } else if (var2 != null) {
         var1.add(var2.getObject());
      }

      return var1;
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectListeners.remove(var1);
   }

   private void fireActiveObjectChange() {
      ActiveObjectProviderEvent var1 = new ActiveObjectProviderEvent(this, this.getActiveObjects());
      this.activeObjectListeners.call().activeObjectsChanged(var1);
   }

   @Override
   public void activate() {
      if (!this.active) {
         this.active = true;
         this.fireActiveObjectChange();
      }
   }

   @Override
   public void deactivate() {
      if (this.active) {
         this.active = false;
      }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   private final class UiModeListener implements PreferenceListener<EditorServiceUiMode> {
      private UiModeListener() {
      }

      public void valueChanged(Preference<EditorServiceUiMode> var1, EditorServiceUiMode var2, EditorServiceUiMode var3) {
         WaitLock var4 = DefaultEditorService.this.window.startWait();
         this.switchUi(var4);
      }

      private void switchUi(WaitLock var1) {
         boolean var2 = false;

         try {
            Editor var3 = DefaultEditorService.this.getActiveEditor();
            Editor[] var4 = DefaultEditorService.this.getNavigationOrder();
            DefaultEditorService.this.ui.prepareForUiSwitch();
            this.createEmptyUi(var1);
            this.restoreEditors(var4, var3, var1);
            var2 = true;
         } finally {
            if (!var2) {
               var1.release();
            }
         }
      }

      private void createEmptyUi(final WaitLock var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               try {
                  DefaultEditorService.this.ui = DefaultEditorService.this.createUi();
                  DefaultEditorService.this.uiContainer.removeAll();
                  ContainerCanvas.decorate(DefaultEditorService.this.ui, DefaultEditorService.this.uiContainer, "Center");
                  DefaultEditorService.this.window.getFrame().getRootPane().revalidate();
                  DefaultEditorService.this.window.getFrame().getRootPane().repaint();
               } catch (Exception var2) {
                  var1.release();
                  UiModeListener.this.logError(var2);
               }
            }
         });
      }

      private void restoreEditors(final Editor[] var1, final Editor var2, final WaitLock var3) {
         DelayedJob.createAndSchedule(150, new Runnable() {
            @Override
            public void run() {
               try {
                  for (Editor var4 : Arrays2.reverse(var1)) {
                     DefaultEditorService.this.ui.addEditor(var4);
                  }

                  if (var2 != null) {
                     DefaultEditorService.this.ui.selectEditor(var2);
                  }
               } catch (Exception var8) {
                  UiModeListener.this.logError(var8);
               } finally {
                  var3.release();
               }
            }
         });
      }

      private void logError(Exception var1) {
         ErrorLog.attention(DefaultEditorService.class, "An unexpected error occurred while changing the window display mode.", var1);
      }
   }
}
