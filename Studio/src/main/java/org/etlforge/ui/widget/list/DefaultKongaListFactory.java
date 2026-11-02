package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import org.etlforge.collections.Pair;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;

public class DefaultKongaListFactory implements KongaListFactory {
   private ListCellRenderer renderer;
   private ListSelectionMode selectionMode = ListSelectionMode.MULTIPLE_INTERVAL_SELECTION;
   private int visibleRows = 8;
   private Object prototypeValue;
   private PopupMenuRequestHandler popupMenuRequestHandler;
   private List<KeyBindingInstaller> keyBindings;
   private List<Pair<Action, String>> focusActions;
   private final List<MouseListener> mouseListeners = Lists.newArrayList();
   private boolean useScrolling = true;
   private boolean dragSource;
   private TransferHandler transferHandler;
   private TextStyle style;
   private Border border;
   private JLabel label;
   private Function<Object, String> nextMatchFunction;
   private SelectionConverter selectionConverter = SelectionConverter.TRIVIAL;

   protected JList createEmptyList() {
      return new KongaListBase();
   }

   @Override
   public KongaList newList() {
      JList var1 = this.createEmptyList();
      this.customizeList(var1);
      return this.createKongaList(var1);
   }

   @Override
   public KongaList newList(Collection<?> var1) {
      return this.newList(var1.toArray());
   }

   @Override
   public KongaList newList(Object[] var1) {
      DefaultListModel var2 = ListUtils.createModel(Arrays.asList(var1));
      return this.newList(var2);
   }

   @Override
   public KongaList newList(ListModel var1) {
      JList var2 = this.createEmptyList();
      var2.setModel(var1);
      this.customizeList(var2);
      return this.createKongaList(var2);
   }

   private DefaultKongaList createKongaList(JList var1) {
      DefaultKongaList var2 = new DefaultKongaList(var1);
      var2.setSelectionConverter(this.selectionConverter);
      if (this.label != null) {
         var2.setLabel(this.label);
      }

      if (!this.useScrolling) {
         var2.noScrolling();
      }

      return var2;
   }

   @Override
   public void setCellRenderer(ListCellRenderer var1) {
      this.renderer = var1;
   }

   @Override
   public void setNextMatchFunction(Function<Object, String> var1) {
      this.nextMatchFunction = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setSelectionMode(ListSelectionMode var1) {
      this.selectionMode = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setPrototypeValue(Object var1) {
      this.prototypeValue = var1;
   }

   @Override
   public void setVisibleRowCount(int var1) {
      this.visibleRows = var1;
   }

   @Override
   public void addKeyBindings(KeyBindingInstaller var1) {
      Preconditions.checkNotNull(var1);
      if (this.keyBindings == null) {
         this.keyBindings = Lists.newArrayList();
      }

      this.keyBindings.add(var1);
   }

   @Override
   public void addFocusKeyBinding(Action var1, String var2) {
      if (this.focusActions == null) {
         this.focusActions = Lists.newArrayList();
      }

      this.focusActions.add(Pair.of(var1, var2));
   }

   @Override
   public void setPopupMenuRequestHandler(PopupMenuRequestHandler var1) {
      this.popupMenuRequestHandler = var1;
   }

   @Override
   public void addMouseListener(MouseListener var1) {
      this.mouseListeners.add(var1);
   }

   @Override
   public void noScrolling() {
      this.useScrolling = false;
   }

   @Override
   public void setDragSource(boolean var1) {
      this.dragSource = var1;
   }

   @Override
   public void setTransferHandler(TransferHandler var1) {
      this.transferHandler = var1;
   }

   @Override
   public void setStyle(TextStyle var1) {
      this.style = var1;
   }

   @Override
   public void setBorder(Border var1) {
      this.border = var1;
   }

   @Override
   public void setLabel(JLabel var1) {
      this.label = var1;
   }

   @Override
   public void setSelectionConverter(SelectionConverter var1) {
      this.selectionConverter = Preconditions.checkNotNull(var1);
   }

   private void customizeList(JList var1) {
      this.selectionMode.applyTo(var1);
      this.applyStyle(var1);
      this.installRenderer(var1);
      this.installNextMatchFunction(var1);
      this.customizeHeightAndWidth(var1);
      this.applyBorder(var1);
      this.installKeyBindings(var1);
      this.installController(var1);
      this.installDragAndDrop(var1);
   }

   private void applyBorder(JList var1) {
      if (this.border != null) {
         var1.setBorder(this.border);
      }
   }

   private void installRenderer(JList var1) {
      if (this.renderer != null) {
         var1.setCellRenderer(this.renderer);
      }
   }

   private void installNextMatchFunction(JList var1) {
      if (this.nextMatchFunction != null && var1 instanceof KongaListBase) {
         ((KongaListBase)var1).setNextMatchFunction(this.nextMatchFunction);
      }
   }

   private void customizeHeightAndWidth(JList var1) {
      var1.setPrototypeCellValue(this.prototypeValue);
      var1.setVisibleRowCount(this.visibleRows);
   }

   private void installKeyBindings(JList var1) {
      if (this.focusActions != null) {
         this.addFocusKeyBindings();
      }

      if (this.keyBindings != null) {
         for (KeyBindingInstaller var3 : this.keyBindings) {
            var3.install(var1);
         }
      }
   }

   private void addFocusKeyBindings() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(0);

      for (Pair var3 : this.focusActions) {
         KeyStroke var4 = (KeyStroke)((Action)var3.first).getValue("AcceleratorKey");
         if (var4 != null) {
            var1.addBinding(var4, var3.second, (Action)var3.first);
         } else {
            System.err.println("No keystroke defined for the action " + var3.first.getClass().getSimpleName());
         }
      }

      this.addKeyBindings(var1);
   }

   private void installController(JList var1) {
      if (this.popupMenuRequestHandler != null) {
         PopupMenuRequestHandlers.install(var1, this.popupMenuRequestHandler);
      }

      for (MouseListener var3 : this.mouseListeners) {
         var1.addMouseListener(var3);
      }
   }

   private void installDragAndDrop(JList var1) {
      var1.setDragEnabled(this.dragSource);
      if (this.transferHandler != null) {
         var1.setTransferHandler(this.transferHandler);
      }
   }

   private void applyStyle(JList var1) {
      if (this.style != null) {
         this.style.makeOver(var1);
      }
   }
}
