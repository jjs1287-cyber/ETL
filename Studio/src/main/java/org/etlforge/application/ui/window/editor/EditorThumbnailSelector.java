package org.etlforge.application.ui.window.editor;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.lang.IntRing;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;

public final class EditorThumbnailSelector extends AbstractEditorSelector implements UiProvider, Disposable {
   private final Grid grid;
   private final Map<JComponent, EditorThumbnail> thumbnails = Maps.newHashMap();
   private final List<JComponent> components;
   private EditorThumbnailSelector.Coords selectedCoords;
   private EditorThumbnail[][] thumbnailGrid;

   public EditorThumbnailSelector(List<EditorThumbnail> var1, int var2) {
      this(var1, var2, false);
   }

   public EditorThumbnailSelector(List<EditorThumbnail> var1, int var2, boolean var3) {
      this.grid = new Grid(0, var2, 10, 10).withBorder(Empty.border(8)).nonOpaque();
      this.components = this.createLayoutComponents(var1, var2, var3);
      this.layoutGrid(new EditorThumbnailSelector.MouseHandler());
      this.installKeyBindings();
   }

   private List<JComponent> createLayoutComponents(List<EditorThumbnail> var1, int var2, boolean var3) {
      int var4 = var1.size() / var2 + (var1.size() % var2 > 0 ? 1 : 0);
      this.thumbnailGrid = new EditorThumbnail[var4][var2];
      ArrayList var5 = Lists.newArrayList();
      if (var3) {
         for (int var6 = var4 - 1; var6 >= 0; var6--) {
            int var7 = var6 * var2;
            int var8 = var7;

            for (int var9 = 0; var8 < var7 + var2; var9++) {
               if (var8 < var1.size()) {
                  EditorThumbnail var10 = (EditorThumbnail)var1.get(var8);
                  JComponent var11 = var10.getUi();
                  this.thumbnails.put(var11, var10);
                  var5.add(var11);
                  this.thumbnailGrid[var6][var9] = var10;
               } else {
                  var5.add(null);
               }

               var8++;
            }
         }
      } else {
         int var12 = 0;
         int var13 = 0;

         for (EditorThumbnail var15 : var1) {
            JComponent var16 = var15.getUi();
            this.thumbnails.put(var16, var15);
            var5.add(var16);
            this.thumbnailGrid[var12][var13] = var15;
            if (++var13 == var2) {
               var13 = 0;
               var12++;
            }
         }
      }

      return var5;
   }

   private void layoutGrid(MouseListener var1) {
      for (JComponent var3 : this.components) {
         if (var3 == null) {
            this.grid.add(new JLabel());
         } else {
            if (var1 != null) {
               var3.addMouseListener(var1);
            }

            this.grid.add(var3);
         }
      }
   }

   private void installKeyBindings() {
      JPanel var1 = this.grid.container();
      byte var2 = 2;
      ActionKeyBinding.install(new EditorThumbnailSelector.RowNavigationAction(1), var1, var2);
      ActionKeyBinding.install(new EditorThumbnailSelector.RowNavigationAction(-1), var1, var2);
      ActionKeyBinding.install(new EditorThumbnailSelector.ColumnNavigationAction(1), var1, var2);
      ActionKeyBinding.install(new EditorThumbnailSelector.ColumnNavigationAction(-1), var1, var2);
      ActionKeyBinding.install(new EditorThumbnailSelector.FinalSelectionAction(KeyUtils.SPACE), var1, var2);
      ActionKeyBinding.install(new EditorThumbnailSelector.FinalSelectionAction(KeyUtils.ENTER), var1, var2);
      ActionKeyBinding.install(new EditorThumbnailSelector.CloseThumbnailAction(), var1, var2);
   }

   @Override
   public void dispose() {
      for (EditorThumbnail var2 : this.thumbnails.values()) {
         var2.dispose();
      }

      this.clearStatusPanel();
   }

   private void clearStatusPanel() {
      ApplicationMessageEvent.postInformationMessage(this.getClass(), "", new ApplicationMessageEvent.LifeTime(-1L));
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.grid.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   public void setSelectedEditor(final Editor var1) {
      UiUtils.nestedInvokeLater(2, new Runnable() {
         @Override
         public void run() {
            EditorThumbnail var1x = null;

            for (EditorThumbnail var3 : EditorThumbnailSelector.this.thumbnails.values()) {
               if (var1 == var3.getEditor()) {
                  var1x = var3;
                  var3.setActive(true);
                  EditorThumbnailSelector.this.publishTitleToStatusPanel(var3);
               } else {
                  var3.setActive(false);
               }
            }

            if (var1x != null) {
               EditorThumbnailSelector.this.selectedCoords = EditorThumbnailSelector.this.getCoordsFor(var1x);
            }
         }
      });
   }

   private void publishTitleToStatusPanel(EditorThumbnail var1) {
      ApplicationMessageEvent.postInformationMessage(EditorThumbnailSelector.class, var1.getEditor().getTitle());
   }

   private EditorThumbnailSelector.Coords getCoordsFor(EditorThumbnail var1) {
      for (int var2 = 0; var2 < this.thumbnailGrid.length; var2++) {
         EditorThumbnail[] var3 = this.thumbnailGrid[var2];

         for (int var4 = 0; var4 < var3.length; var4++) {
            if (var3[var4] == var1) {
               return new EditorThumbnailSelector.Coords(var2, var4);
            }
         }
      }

      throw new RuntimeException("Unknown thumbnail: " + var1);
   }

   public void setNotifications(Collection<Editor> var1) {
      for (EditorThumbnail var3 : this.thumbnails.values()) {
         Editor var4 = var3.getEditor();
         if (var1.contains(var4)) {
            var3.addNotification();
         }
      }
   }

   public void removeEditor(Editor var1) {
      JComponent var2 = null;
      ArrayList var3 = Lists.newArrayList();

      for (Entry var5 : this.thumbnails.entrySet()) {
         JComponent var6 = (JComponent)var5.getKey();
         EditorThumbnail var7 = (EditorThumbnail)var5.getValue();
         if (var7.getEditor() == var1) {
            var7.dispose();
            EditorThumbnailSelector.Coords var8 = this.getCoordsFor(var7);
            this.thumbnailGrid[var8.row][var8.col] = null;
            if (var8.equals(this.selectedCoords)) {
               this.selectedCoords = null;
            }

            var2 = var6;
            var3.add(null);
         } else {
            var3.add(var6);
         }
      }

      if (var2 != null) {
         this.thumbnails.remove(var2);
         int var9 = this.components.indexOf(var2);
         this.components.set(var9, null);
         this.grid.removeAll();
         this.layoutGrid(null);
         this.grid.refresh();
      }
   }

   public boolean isEmpty() {
      return this.thumbnails.isEmpty();
   }

   private EditorThumbnail getSelectedThumbnail() {
      return this.selectedCoords != null ? this.thumbnailGrid[this.selectedCoords.row][this.selectedCoords.col] : null;
   }

   private class CloseThumbnailAction extends AbstractAction {
      public CloseThumbnailAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(87));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EditorThumbnail var2 = EditorThumbnailSelector.this.getSelectedThumbnail();
         if (var2 != null) {
            Editor var3 = var2.getEditor();
            CloseEditorAction var4 = new CloseEditorAction(var3);
            var4.actionPerformed(var1);
         }
      }
   }

   private class ColumnNavigationAction extends EditorThumbnailSelector.NavigationAction {
      public ColumnNavigationAction(int var2) {
         super(var2);
         KeyStroke var3 = var2 == 1 ? KeyUtils.DOWN : KeyUtils.UP;
         this.putValue("AcceleratorKey", var3);
      }

      @Override
      protected EditorThumbnail getThumbnailToSelect() {
         EditorThumbnailSelector.Coords var1 = this.getStartCoords();

         do {
            EditorThumbnail var2 = EditorThumbnailSelector.this.thumbnailGrid[var1.row][var1.col];
            if (var2 != null) {
               return var2;
            }

            this.alreadyProcessed[var1.row][var1.col] = true;
            var1 = this.next(var1);
         } while (var1 != null);

         EditorThumbnailSelector.this.selectedCoords = null;
         return null;
      }

      private EditorThumbnailSelector.Coords getStartCoords() {
         if (EditorThumbnailSelector.this.selectedCoords == null) {
            int var3 = this.nextRow(-1);
            byte var4 = 0;
            return new EditorThumbnailSelector.Coords(var3, var4);
         } else {
            int var1 = EditorThumbnailSelector.this.selectedCoords.col;
            int var2 = this.nextRow(EditorThumbnailSelector.this.selectedCoords.row);
            return new EditorThumbnailSelector.Coords(var2, var1);
         }
      }

      private EditorThumbnailSelector.Coords next(EditorThumbnailSelector.Coords var1) {
         int var2 = var1.row;
         int var3 = var1.col;
         int var4 = var3;
         int var5 = var2;

         do {
            for (int var6 = this.nextRow(var2); var6 != var5; var6 = this.nextRow(var6)) {
               if (!this.alreadyProcessed[var6][var4]) {
                  return new EditorThumbnailSelector.Coords(var6, var4);
               }
            }

            var4 = this.nextCol(var4);
            var2 = -1;
            var5 = this.lastRow();
         } while (var4 != var3);

         return null;
      }
   }

   private static class Coords {
      public final int row;
      public final int col;

      public Coords(int var1, int var2) {
         this.row = var1;
         this.col = var2;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.row, this.col);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof EditorThumbnailSelector.Coords)) {
            return false;
         }

         EditorThumbnailSelector.Coords var2 = (EditorThumbnailSelector.Coords)var1;
         return this.row == var2.row && this.col == var2.col;
      }
   }

   private class FinalSelectionAction extends AbstractAction {
      public FinalSelectionAction(KeyStroke var2) {
         this.putValue("AcceleratorKey", var2);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EditorThumbnail var2 = EditorThumbnailSelector.this.getSelectedThumbnail();
         if (var2 != null) {
            EditorThumbnailSelector.this.fireFinalSelection(var2.getEditor());
         }
      }
   }

   private class MouseHandler extends MouseActionListener {
      private MouseHandler() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         EditorThumbnail var2 = this.getThumbnail(var1);
         if (var2 != null) {
            Editor var3 = var2.getEditor();
            boolean var4 = false;
            JPopupMenu var5 = EditorThumbnailSelector.this.createContextMenu(var3, var4);
            var5.show(var1.getComponent(), var1.getX(), var1.getY());
         }
      }

      @Override
      protected void onSingleClick(MouseEvent var1) {
         EditorThumbnail var2 = this.getThumbnail(var1);
         if (var2 != null) {
            EditorThumbnailSelector.this.fireFinalSelection(var2.getEditor());
         }
      }

      private EditorThumbnail getThumbnail(MouseEvent var1) {
         return EditorThumbnailSelector.this.thumbnails.get(var1.getSource());
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         EditorThumbnail var2 = this.getThumbnail(var1);

         for (EditorThumbnail var4 : EditorThumbnailSelector.this.thumbnails.values()) {
            var4.setActive(var4 == var2);
         }

         if (var2 != null) {
            EditorThumbnailSelector.this.selectedCoords = EditorThumbnailSelector.this.getCoordsFor(var2);
            EditorThumbnailSelector.this.publishTitleToStatusPanel(var2);
            EditorThumbnailSelector.this.fireTemporarySelection(var2.getEditor());
         } else {
            EditorThumbnailSelector.this.selectedCoords = null;
         }
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         EditorThumbnail var2 = this.getThumbnail(var1);
         if (var2 != null) {
            Component var3 = var1.getComponent();
            Point var4 = var1.getPoint();
            if (!var3.contains(var4)) {
               var2.setActive(false);
               EditorThumbnailSelector.this.clearStatusPanel();
            }
         }
      }
   }

   private abstract class NavigationAction extends AbstractAction {
      private final IntRing.Direction direction;
      private IntRing rows;
      private IntRing cols;
      protected boolean[][] alreadyProcessed;

      public NavigationAction(int var2) {
         this.direction = var2 == 1 ? IntRing.FORWARD : IntRing.BACKWARD;
      }

      @Override
      public final void actionPerformed(ActionEvent var1) {
         this.initializeTracker();
         EditorThumbnail var2 = this.getThumbnailToSelect();
         if (var2 != null) {
            EditorThumbnailSelector.this.selectedCoords = EditorThumbnailSelector.this.getCoordsFor(var2);
            this.setSelectedThumbnail(var2);
         }
      }

      private void initializeTracker() {
         this.rows = new IntRing(this.direction, EditorThumbnailSelector.this.thumbnailGrid.length);
         this.cols = new IntRing(this.direction, EditorThumbnailSelector.this.thumbnailGrid[0].length);
         this.alreadyProcessed = new boolean[this.rows.length()][this.cols.length()];
         if (EditorThumbnailSelector.this.selectedCoords != null) {
            this.alreadyProcessed[EditorThumbnailSelector.this.selectedCoords.row][EditorThumbnailSelector.this.selectedCoords.col] = true;
         }
      }

      protected abstract EditorThumbnail getThumbnailToSelect();

      private void setSelectedThumbnail(EditorThumbnail var1) {
         for (EditorThumbnail var3 : EditorThumbnailSelector.this.thumbnails.values()) {
            var3.setActive(var3 == var1);
         }

         if (var1 != null) {
            EditorThumbnailSelector.this.publishTitleToStatusPanel(var1);
         }
      }

      protected final int nextCol(int var1) {
         return this.cols.next(var1);
      }

      protected final int lastRow() {
         return this.rows.last();
      }

      protected final int nextRow(int var1) {
         return this.rows.next(var1);
      }

      protected final int lastCol() {
         return this.cols.last();
      }
   }

   private class RowNavigationAction extends EditorThumbnailSelector.NavigationAction {
      public RowNavigationAction(int var2) {
         super(var2);
         KeyStroke var3 = var2 == 1 ? KeyUtils.RIGHT : KeyUtils.LEFT;
         this.putValue("AcceleratorKey", var3);
      }

      @Override
      protected EditorThumbnail getThumbnailToSelect() {
         EditorThumbnailSelector.Coords var1 = this.getStartCoords();

         do {
            EditorThumbnail var2 = EditorThumbnailSelector.this.thumbnailGrid[var1.row][var1.col];
            if (var2 != null) {
               return var2;
            }

            this.alreadyProcessed[var1.row][var1.col] = true;
            var1 = this.next(var1);
         } while (var1 != null);

         EditorThumbnailSelector.this.selectedCoords = null;
         return null;
      }

      private EditorThumbnailSelector.Coords getStartCoords() {
         if (EditorThumbnailSelector.this.selectedCoords == null) {
            byte var3 = 0;
            int var4 = this.nextCol(-1);
            return new EditorThumbnailSelector.Coords(var3, var4);
         } else {
            int var1 = EditorThumbnailSelector.this.selectedCoords.row;
            int var2 = this.nextCol(EditorThumbnailSelector.this.selectedCoords.col);
            return new EditorThumbnailSelector.Coords(var1, var2);
         }
      }

      private EditorThumbnailSelector.Coords next(EditorThumbnailSelector.Coords var1) {
         int var2 = var1.row;
         int var3 = var1.col;
         int var4 = var2;
         int var5 = var3;

         do {
            for (int var6 = this.nextCol(var3); var6 != var5; var6 = this.nextCol(var6)) {
               if (!this.alreadyProcessed[var4][var6]) {
                  return new EditorThumbnailSelector.Coords(var4, var6);
               }
            }

            var4 = this.nextRow(var4);
            var3 = -1;
            var5 = this.lastCol();
         } while (var4 != var2);

         return null;
      }
   }
}
