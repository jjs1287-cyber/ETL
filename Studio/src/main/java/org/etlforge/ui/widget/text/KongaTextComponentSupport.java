package org.etlforge.ui.widget.text;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.graphics.RectangleUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class KongaTextComponentSupport {
   private final JTextComponent component;
   private KongaTextComponentSupport.PopupHandler popupHandler;
   private List<Object> additionalActions;
   private FocusListener selectAllHandler;
   private JLabel label;
   private UiProvider asUiProvider;

   public KongaTextComponentSupport(JTextComponent var1) {
      this.component = var1;
      this.popupHandler = new KongaTextComponentSupport.PopupHandler();
      var1.addMouseListener(this.popupHandler);
      KongaTextComponentCustomizer.registerKeyBindings(var1);
      if (Boolean.getBoolean("HighlightFocusedTextComponent")) {
         this.installBorderManager();
      }

      var1.addPropertyChangeListener("enabled", new KongaTextComponentSupport.EnabledListener());
   }

   public KongaTextComponentSupport(KongaTextArea var1) {
      this((JTextComponent)var1);
      var1.setWrapStyleWord(true);
      var1.setLineWrap(true);
   }

   public void disableContextMenu() {
      this.component.removeMouseListener(this.popupHandler);
      this.popupHandler = null;
   }

   public void addAdditionalAction(Action var1) {
      this.addAdditionToContextMenu(var1);
   }

   public void addAdditionalMenuItem(JMenuItem var1) {
      this.addAdditionToContextMenu(var1);
   }

   public void scrollToRow(int var1) {
      try {
         int var2 = DocumentUtils.getRowStart(this.component.getDocument(), var1);
         Rectangle var3 = this.component.modelToView(var2);
         RectangleUtils.padMe(var3, 0, 10);
         this.component.scrollRectToVisible(var3);
      } catch (BadLocationException var4) {
         var4.printStackTrace();
      }
   }

   public void setLabel(String var1) {
      if (var1 != null) {
         this.setLabel(KongaLabel.withMnemonic(var1));
      } else {
         this.label = null;
      }
   }

   public void setLabel(JLabel var1) {
      this.label = var1;
      if (var1 != null) {
         var1.setLabelFor(this.component);
      }
   }

   public JLabel getLabel() {
      return this.label;
   }

   public void setEnabled(boolean var1) {
      if (this.label != null) {
         this.label.setEnabled(var1);
      }
   }

   public UiProvider asUiProvider() {
      if (this.asUiProvider == null) {
         this.asUiProvider = this.createUiProvider();
      }

      return this.asUiProvider;
   }

   private UiProvider createUiProvider() {
      if (this.label == null) {
         return new UiProviderAdapter(this.component);
      }

      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 2).nonOpaque();
      var1.north(this.label).center(this.component);
      return var1;
   }

   public void setSelectAllWhenFocused(boolean var1) {
      if (var1) {
         this.installSelectAllHandler();
      } else {
         this.removeSelectAllHandler();
      }
   }

   private void installSelectAllHandler() {
      if (this.selectAllHandler == null) {
         this.selectAllHandler = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent var1) {
               KongaTextComponentSupport.this.component.selectAll();
            }
         };
         this.component.addFocusListener(this.selectAllHandler);
      }
   }

   private void removeSelectAllHandler() {
      if (this.selectAllHandler != null) {
         this.component.removeFocusListener(this.selectAllHandler);
         this.selectAllHandler = null;
      }
   }

   private void installBorderManager() {
      if (this.component instanceof JTextField) {
         this.component.addFocusListener(new KongaTextComponentSupport.BorderManager());
      }
   }

   private void addAdditionToContextMenu(Object var1) {
      if (this.additionalActions == null) {
         this.additionalActions = new ArrayList<>();
      }

      this.additionalActions.add(var1);
   }

   private KongaPopupMenu getContextMenu() {
      KongaPopupMenu var1 = KongaTextComponentCustomizer.getPopupMenu(this.component);
      return this.addAdditionalActionsToContextMenu(var1);
   }

   private KongaPopupMenu addAdditionalActionsToContextMenu(KongaPopupMenu var1) {
      if (this.additionalActions == null) {
         return var1;
      }

      KongaPopupMenu var2 = new KongaPopupMenu();
      var2.addItems(this.additionalActions);

      for (Component var6 : var1.getComponents()) {
         var2.add(var6);
      }

      return var2;
   }

   private final class BorderManager implements FocusListener {
      private final Border normal = KongaTextComponentSupport.this.component.getBorder();
      private final Border focused = new FocusedTextBorder(KongaTextComponentSupport.this.component);

      private BorderManager() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         KongaTextComponentSupport.this.component.setBorder(this.focused);
      }

      @Override
      public void focusLost(FocusEvent var1) {
         KongaTextComponentSupport.this.component.setBorder(this.normal);
      }
   }

   private final class EnabledListener implements PropertyChangeListener {
      private EnabledListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (KongaTextComponentSupport.this.label != null) {
            KongaTextComponentSupport.this.label.setEnabled(KongaTextComponentSupport.this.component.isEnabled());
         }
      }
   }

   private final class PopupHandler extends MouseActionListener {
      private PopupHandler() {
      }

      @Override
      protected void onPopupTrigger(final MouseEvent var1) {
         KongaTextComponentSupport.this.component.requestFocusInWindow();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               KongaTextComponentSupport.this.getContextMenu().show(KongaTextComponentSupport.this.component, var1.getX(), var1.getY());
            }
         });
      }
   }
}
