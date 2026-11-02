package org.etlforge.ui.widget.text;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class TextOverlay {
   private final JTextComponent tc;
   private BorderLayoutBuilder layout;
   private JLabel textLabel;
   private JLabel iconLabel;

   public TextOverlay(JTextComponent var1) {
      Preconditions.checkNotNull(var1, "tc");
      this.tc = var1;
      var1.addFocusListener(new TextOverlay.TextAppearanceHandler());
      this.layout = BorderLayoutBuilder.from(var1);
   }

   public void setText(String var1) {
      if (StringUtils.isEmpty(var1)) {
         this.removeText();
      } else {
         this.showText(var1);
      }
   }

   private void removeText() {
      if (this.textLabel != null) {
         this.textLabel = null;
         this.rebuild();
      }
   }

   private void showText(String var1) {
      boolean var2 = false;
      if (this.textLabel == null) {
         this.textLabel = this.createTextLabel();
         var2 = true;
      }

      this.textLabel.setText(var1);
      if (var2) {
         this.rebuild();
      }
   }

   private JLabel createTextLabel() {
      JLabel var1 = this.createLabel();
      Color var2 = this.tc.getForeground();
      var1.setForeground(new Color(var2.getRed(), var2.getGreen(), var2.getBlue(), 100));
      return var1;
   }

   public Icon getIcon() {
      return this.iconLabel != null ? this.iconLabel.getIcon() : null;
   }

   public void setIcon(Icon var1) {
      if (var1 == null) {
         this.removeIcon();
      } else {
         this.showIcon(var1);
      }
   }

   private void removeIcon() {
      if (this.iconLabel != null) {
         this.iconLabel = null;
         this.rebuild();
      }
   }

   private void showIcon(Icon var1) {
      boolean var2 = false;
      if (this.iconLabel == null) {
         this.iconLabel = this.createLabel();
         var2 = true;
      }

      this.iconLabel.setIcon(var1);
      if (var2) {
         this.rebuild();
      }
   }

   private JLabel createLabel() {
      JLabel var1 = new JLabel();
      var1.setBorder(Empty.border(this.tc.getInsets()));
      return var1;
   }

   private void rebuild() {
      this.tc.removeAll();
      if (this.iconLabel != null) {
         this.layout.east(this.iconLabel);
      }

      if (this.textLabel != null && !this.tc.isFocusOwner()) {
         this.layout.center(this.textLabel);
      }

      this.tc.revalidate();
      this.tc.repaint();
   }

   private class TextAppearanceHandler implements FocusListener {
      private TextAppearanceHandler() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         this.rebuildIfNeeded();
      }

      private void rebuildIfNeeded() {
         if (TextOverlay.this.textLabel != null) {
            TextOverlay.this.rebuild();
         }
      }

      @Override
      public void focusLost(FocusEvent var1) {
         if (TextOverlay.this.textLabel != null) {
            TextOverlay.this.rebuild();
         }
      }
   }
}
