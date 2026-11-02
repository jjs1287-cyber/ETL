package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.border.Border;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.Iterables2;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.ui.widget.selection.SelectionWidget;

public class EditorListWidget implements UiProvider, SelectionSource, SelectionWidget {
   private FilteredListModel model;
   private final KongaList list;
   private Editor selectedEditor;
   private final EditorListWidget.Renderer renderer;

   public EditorListWidget(Iterable<Editor> var1) {
      this.model = this.createListModel(var1);
      this.renderer = new EditorListWidget.Renderer();
      this.list = this.createList();
   }

   public EditorListWidget(Editor[] var1) {
      this(Arrays.asList(var1));
   }

   private FilteredListModel createListModel(Iterable<Editor> var1) {
      DefaultListModel var2 = ListUtils.createModel(Iterables2.sortedCopy(var1, new EditorTitleComparator()));
      return new FilteredListModel(var2);
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(10);
      var1.setCellRenderer(this.renderer);
      this.customizeFactory(var1, this.model);
      return var1.newList(this.model);
   }

   protected void customizeFactory(KongaListFactory var1, ListModel var2) {
   }

   public final void setTransient(boolean var1) {
      this.list.setTransient(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.list.requestFocus();
   }

   public final IndexedSelection getSelection() {
      return this.list.getSelection();
   }

   @Override
   public final void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   @Override
   public final void removeSelectionListener(SelectionListener var1) {
      this.list.removeSelectionListener(var1);
   }

   public final void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   public final void setEditors(Editor[] var1) {
      this.setEditors(Arrays.asList(var1));
   }

   public final void setEditors(Iterable<Editor> var1) {
      List var2 = this.model.getFilters();
      FilteredListModel var3 = this.createListModel(var1);

      for (Object __etlItem109 : var2) {
         ListFilter var5 = (ListFilter) __etlItem109;
         var3.addFilter(var5);
      }

      this.list.setModel(var3);
      this.model = var3;
   }

   public final void setSelectedEditor(Editor var1) {
      this.selectedEditor = var1;
      this.list.repaint();
   }

   @Override
   public final void selectFirst() {
      this.list.selectFirst();
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   public final void setFilter(ListFilter var1) {
      this.model.setFilter(var1);
   }

   public final Disposable setNotifications(Collection<Editor> var1) {
      this.renderer.notifications = Sets.newHashSet(var1);
      return new EditorListWidget.NotificationTimer();
   }

   private class NotificationTimer implements ActionListener, Disposable {
      private Timer timer = new Timer(500, this);

      public NotificationTimer() {
         this.timer.setRepeats(true);
         this.timer.start();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EditorListWidget.this.renderer.renderNotifications = !EditorListWidget.this.renderer.renderNotifications;
         EditorListWidget.this.list.repaint();
      }

      @Override
      public void dispose() {
         if (this.timer != null) {
            this.timer.stop();
            this.timer = null;
         }
      }
   }

   private final class Renderer extends DefaultListCellRenderer {
      private final Border border = new BorderBuilder(this.getBorder()).empty(2, 0, 0, 50);
      public boolean renderNotifications;
      public Set<Editor> notifications;

      public Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof Editor) {
            Editor var6 = (Editor)var2;
            this.decorateEditor(var1, var6);
         }

         this.setBorder(this.border);
         return this;
      }

      private void decorateEditor(JList var1, Editor var2) {
         this.setText(var2.getTitle());
         this.setIcon(var2.getSmallIcon());
         this.setToolTipText(var2.getTitle());
         Font var3 = var1.getFont();
         if (var2 == EditorListWidget.this.selectedEditor) {
            var3 = Fonts.bold(var3);
         }

         this.setFont(var3);
         if (this.isNotification(var2)) {
            this.setBackground(Color.BLACK);
            this.setForeground(Color.WHITE);
         }
      }

      private boolean isNotification(Editor var1) {
         return this.renderNotifications && this.notifications != null && this.notifications.contains(var1);
      }
   }
}
