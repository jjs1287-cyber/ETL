package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class MDIEditorServiceUi extends AbstractEditorServiceUi implements EditorServiceUi {
   private static final int MIN_WIDTH = 600;
   private static final int MIN_HEIGHT = 600;
   private static final int X_OFFSET = 30;
   private static final int Y_OFFSET = 30;
   private final JDesktopPane desktop = new JDesktopPane();
   private final BiMap<Editor, JInternalFrame> frames = HashBiMap.create();
   private final MDIEditorServiceUi.FrameListener frameListener = new MDIEditorServiceUi.FrameListener();
   private JInternalFrame lastActive;

   public MDIEditorServiceUi(DefaultEditorService var1) {
      super(var1);
   }

   @Override
   public void addEditor(Editor var1) {
      JInternalFrame var2 = this.createFrame(var1);
      this.frames.put(var1, var2);
      this.desktop.add(var2);
      var2.setVisible(true);
      this.selectFrame(var2);
   }

   @Override
   public void addNotification(Editor var1) {
   }

   @Override
   public void addDetachedEditor(Editor var1) {
      this.addEditor(var1);
   }

   @Override
   public void detachEditor(Editor var1) {
   }

   @Override
   public Editor getActiveEditor() {
      JInternalFrame var1 = this.desktop.getSelectedFrame();
      if (var1 == null) {
         var1 = this.lastActive;
      }

      return this.frames.inverse().get(var1);
   }

   @Override
   public int getNumberOfOpenEditors() {
      return this.frames.size();
   }

   @Override
   public List<Editor> getOpenEditors() {
      return Lists.newArrayList(this.frames.keySet());
   }

   @Override
   public boolean isDisplayingEditor(Editor var1) {
      return this.frames.containsKey(var1);
   }

   @Override
   public void removeEditor(Editor var1, Editor var2) {
      JInternalFrame var3 = this.frames.remove(var1);
      if (var3 != null) {
         this.removeInternalFrame(var1, var3);
      }

      if (var2 != null) {
         this.selectEditor(var2);
      }
   }

   private void removeInternalFrame(Editor var1, JInternalFrame var2) {
      var2.removeInternalFrameListener(this.frameListener);
      this.getEditorService().getEditorMemento().store(var1, var2.getLocation(), var2.getSize());
      var2.dispose();
      this.desktop.remove(var2);
      this.selectNextEditor(var1);
   }

   private void selectNextEditor(Editor var1) {
      for (Editor var5 : this.getEditorService().getNavigationOrder()) {
         if (var5 != var1) {
            this.selectEditor(var5);
            break;
         }
      }
   }

   @Override
   public void selectEditor(Editor var1) {
      JInternalFrame var2 = this.frames.get(var1);
      if (var2 != null) {
         this.selectFrame(var2);
      }
   }

   @Override
   public void updateTitleAndIcon(Editor var1) {
      JInternalFrame var2 = this.frames.get(var1);
      if (var2 != null) {
         var2.setTitle(var1.getTitle());
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.desktop);
   }

   @Override
   public void requestFocus() {
      this.desktop.requestFocusInWindow();
   }

   private void selectFrame(JInternalFrame var1) {
      try {
         var1.setIcon(false);
         var1.setSelected(true);
      } catch (PropertyVetoException var3) {
         var3.printStackTrace();
      }

      this.desktop.setSelectedFrame(var1);
   }

   private JInternalFrame createFrame(Editor var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(var1);
      JInternalFrame var3 = new JInternalFrame(var1.getTitle());
      var3.setResizable(true);
      var3.setMaximizable(true);
      var3.setIconifiable(true);
      var3.setClosable(true);
      var3.setDefaultCloseOperation(0);
      var2.asContentPaneFor(var3);
      var3.pack();
      this.setInitialFrameBounds(var1, var3);
      var3.addInternalFrameListener(this.frameListener);
      return var3;
   }

   private void setInitialFrameBounds(Editor var1, JInternalFrame var2) {
      Rectangle var3 = this.calculateFrameBounds(var1, var2);
      this.ensureIsFrameInsideDesktopBounds(var2, var3);
   }

   private Rectangle calculateFrameBounds(Editor var1, JInternalFrame var2) {
      Dimension var3 = DimensionUtils.withMinimumSize(var2.getSize(), 600, 600);
      Point var4 = new Point(30 * this.frames.size(), 30 * this.frames.size());
      return this.getEditorService().getEditorMemento().getBounds(var1, new Rectangle(var4, var3));
   }

   private void ensureIsFrameInsideDesktopBounds(JInternalFrame var1, Rectangle var2) {
      Rectangle var3 = new Rectangle(this.desktop.getSize());
      int var4 = var2.x + var2.width - var3.width;
      if (var4 > 0) {
         var2.x = Math.max(0, var2.x - var4);
      }

      int var5 = var2.y + var2.height - var3.height;
      if (var5 > 0) {
         var2.y = Math.max(0, var2.y - var5);
      }

      var4 = var2.x + var2.width - var3.width;
      if (var4 > 0) {
         var2.width = Math.max(600, var2.width - var4);
      }

      var5 = var2.y + var2.height - var3.height;
      if (var5 > 0) {
         var2.height = Math.max(600, var2.height - var5);
      }

      var1.setBounds(var2);
   }

   private class FrameListener extends InternalFrameAdapter {
      private FrameListener() {
      }

      @Override
      public void internalFrameClosing(InternalFrameEvent var1) {
         Editor var2 = MDIEditorServiceUi.this.frames.inverse().get(var1.getInternalFrame());
         if (var2 != null) {
            MDIEditorServiceUi.this.getEditorService().closeEditor(var2);
         } else {
            var1.getInternalFrame().dispose();
         }
      }

      @Override
      public void internalFrameActivated(InternalFrameEvent var1) {
         JInternalFrame var2 = var1.getInternalFrame();
         Editor var3 = MDIEditorServiceUi.this.frames.inverse().get(var2);
         if (var3 != null) {
            var3.appendState(WindowElementState.ACTIVE);
         }

         MDIEditorServiceUi.this.lastActive = var2;
         MDIEditorServiceUi.this.getEditorService().fireActiveEditorChanged();
      }

      @Override
      public void internalFrameDeactivated(InternalFrameEvent var1) {
         Editor var2 = MDIEditorServiceUi.this.frames.inverse().get(var1.getInternalFrame());
         if (var2 != null) {
            var2.revokeState(WindowElementState.ACTIVE);
         }
      }
   }
}
