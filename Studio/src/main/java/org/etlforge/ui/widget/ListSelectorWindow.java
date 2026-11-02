package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import org.etlforge.lang.IntRing;
import org.etlforge.system.Disposable;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.DefaultKongaListFactory;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;

public class ListSelectorWindow<T> {
   private final T[] items;
   private final List<ListSelectorWindowListener<T>> listeners = new CopyOnWriteArrayList<>();
   private KongaDialog window;
   private KongaList itemList;
   private ListCellRenderer renderer;
   private ListSelectorWindow<T>.Controller controller;
   private JComponent caption;
   private boolean focusLostMeansCancelled = true;
   private final Set<AWTKeyStroke> moveDownKeys = Sets.newHashSet();
   private final Set<AWTKeyStroke> moveUpKeys = Sets.newHashSet();
   private KongaListFactory listFactory = new DefaultKongaListFactory();

   public ListSelectorWindow(T... var1) {
      this.items = (T[])((Object[])var1.clone());
      this.focusLostMeansCancelled = true;
   }

   public void setListFactory(KongaListFactory var1) {
      this.listFactory = Preconditions.checkNotNull(var1);
   }

   public void setCaption(JComponent var1) {
      this.caption = var1;
   }

   public void setCellRenderer(ListCellRenderer var1) {
      this.renderer = var1;
   }

   public void addMoveDownKey(AWTKeyStroke var1) {
      this.moveDownKeys.add(var1);
   }

   public void addMoveUpKey(AWTKeyStroke var1) {
      this.moveUpKeys.add(var1);
   }

   public void display(ListSelectorWindow.Direction var1) {
      if (this.window == null) {
         this.window = new KongaDialog(UiUtils.getActiveFrame(), "", false);
         this.window.setUndecorated(true);
         this.focusLostMeansCancelled = true;
         this.createList(var1);
         JComponent var2 = this.layoutWindow();
         this.installKeyBindings(var2);
         this.window.add(var2);
         this.window.pack();
         this.window.setLocationRelativeTo(this.window.getOwner());
         this.window.setVisible(true);
         this.controller = new ListSelectorWindow.Controller();
         this.controller.register(this.window);
         this.fireSelectionChanged(0);
      }
   }

   public void updateSelection(ListSelectorWindow.Direction var1) {
      int var2 = this.itemList.getSelection().headIndex();
      IntRing var3 = var1 == ListSelectorWindow.Direction.NEXT ? IntRing.forward(this.items.length) : IntRing.backward(this.items.length);
      int var4 = var3.next(var2);
      this.itemList.setSelectedIndex(var4);
      this.fireSelectionChanged(var4);
   }

   public void addListener(ListSelectorWindowListener<T> var1) {
      this.listeners.add(var1);
   }

   public void removeListener(ListSelectorWindowListener<T> var1) {
      this.listeners.remove(var1);
   }

   private void fireSelectionChanged(int var1) {
      Object var2 = this.items[var1];

      for (ListSelectorWindowListener var4 : this.listeners) {
         var4.selectionChanged(var2);
      }
   }

   private void fireCancelled() {
      for (ListSelectorWindowListener var2 : this.listeners) {
         var2.cancelled();
      }
   }

   private void createList(ListSelectorWindow.Direction var1) {
      ListSelectorWindow.ListFactory var2 = new ListSelectorWindow.ListFactory();
      this.itemList = var2.newList();
      this.updateSelection(var1);
   }

   private void hide() {
      if (this.window != null) {
         this.focusLostMeansCancelled = false;
         this.controller.dispose();
         this.window.setVisible(false);
         this.window.dispose();
         this.window = null;
      }
   }

   private JComponent layoutWindow() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.itemList);
      var1.north(this.caption);
      var1.withBorder(BorderFactory.createLineBorder(Color.BLACK));
      return var1.container();
   }

   private void cancel() {
      this.fireCancelled();
      this.hide();
   }

   private void finalSelectionMade() {
      IndexedSelection var1 = this.itemList.getSelection();
      this.hide();
      int var2 = var1.headIndex();
      Object var3 = var2 >= 0 ? this.items[var2] : null;

      for (ListSelectorWindowListener var5 : this.listeners) {
         var5.done(var3);
      }
   }

   private void installKeyBindings(JComponent var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1, 1);
      var2.addBinding(KeyUtils.ENTER, "enter", new ListSelectorWindow.FinalSelectionAction());
      var2.addBinding(KeyUtils.getCtrlDown(10), "ctrl-enter", new ListSelectorWindow.FinalSelectionAction());
      var2.install();
   }

   private class Controller implements KeyEventDispatcher, WindowFocusListener, Disposable {
      private boolean registered;

      private Controller() {
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         if (var1.getKeyCode() == 27) {
            ListSelectorWindow.this.cancel();
         } else if (var1.getID() == 402 && var1.getKeyCode() == 17) {
            ListSelectorWindow.this.finalSelectionMade();
         } else {
            AWTKeyStroke var2 = AWTKeyStroke.getAWTKeyStrokeForEvent(var1);
            if (ListSelectorWindow.this.moveDownKeys.contains(var2)) {
               ListSelectorWindow.this.updateSelection(ListSelectorWindow.Direction.NEXT);
               return true;
            }

            if (ListSelectorWindow.this.moveUpKeys.contains(var2)) {
               ListSelectorWindow.this.updateSelection(ListSelectorWindow.Direction.PREVIOUS);
               return true;
            }
         }

         return false;
      }

      public void register(Window var1) {
         if (!this.registered) {
            this.registered = true;
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
            var1.addWindowFocusListener(this);
         }
      }

      @Override
      public void dispose() {
         if (this.registered) {
            this.registered = false;
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
         }
      }

      @Override
      public void windowGainedFocus(WindowEvent var1) {
      }

      @Override
      public void windowLostFocus(WindowEvent var1) {
         if (ListSelectorWindow.this.focusLostMeansCancelled) {
            ListSelectorWindow.this.cancel();
            ListSelectorWindow.this.hide();
         }
      }
   }

   public enum Direction {
      NEXT,
      PREVIOUS;
   }

   private class FinalSelectionAction extends AbstractAction {
      private FinalSelectionAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ListSelectorWindow.this.finalSelectionMade();
      }
   }

   private class ListFactory {
      private int maxLength;

      private ListFactory() {
      }

      public KongaList newList() {
         ListModel var1 = this.newModel();
         ListSelectorWindow.this.listFactory.setPrototypeValue(this.getPrototypeCellValue());
         ListSelectorWindow.this.listFactory.setVisibleRowCount(Math.min(ListSelectorWindow.this.items.length, 20));
         ListSelectorWindow.this.listFactory.addMouseListener(new MouseActionListener() {
            @Override
            protected void onSingleClick(MouseEvent var1) {
               ListSelectorWindow.this.finalSelectionMade();
            }
         });
         if (ListSelectorWindow.this.renderer != null) {
            ListSelectorWindow.this.listFactory.setCellRenderer(ListSelectorWindow.this.renderer);
         }

         ListSelectorWindow.this.listFactory.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
         return ListSelectorWindow.this.listFactory.newList(var1);
      }

      private ListModel newModel() {
         this.maxLength = -1;
         DefaultListModel var1 = new DefaultListModel();

         for (Object var5 : ListSelectorWindow.this.items) {
            var1.addElement(var5);
            this.maxLength = Math.max(this.maxLength, var5.toString().length());
         }

         return var1;
      }

      private Object getPrototypeCellValue() {
         StringBuilder var1 = new StringBuilder();

         for (int var2 = 0; var2 < this.maxLength + 6; var2++) {
            var1.append("X");
         }

         return var1;
      }
   }
}
