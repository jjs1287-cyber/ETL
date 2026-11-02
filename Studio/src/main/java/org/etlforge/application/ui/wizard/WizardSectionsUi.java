package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.GraphicsState;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

public class WizardSectionsUi implements UiProvider {
   private static final Color BACKGROUND = Colors.ETLFORGE_ORANGE;
   private static final Color ACTIVE_TEXT = Color.WHITE;
   private static final Color INACTIVE_TEXT = Color.WHITE;
   private LinkedHashMap<String, WizardSectionsUi.Section> sections;
   private BorderLayoutBuilder container;
   private String activeSection;
   private Border border = Empty.border(0, 2, 0, 2);

   public WizardSectionsUi(String[] var1) {
      this.setSections(var1);
   }

   public void setSections(String[] var1) {
      this.sections = Maps.newLinkedHashMap();

      for (int var2 = 0; var2 < var1.length; var2++) {
         boolean var3 = var2 == var1.length - 1;
         String var4 = var1[var2];
         this.sections.put(var4, new WizardSectionsUi.Section(var4, !var3));
      }

      if (!this.sections.containsKey(this.activeSection)) {
         this.activeSection = null;
      }

      if (this.container != null) {
         this.container.removeAll();
         BoxBuilder var5 = this.layoutLabels();
         this.setSection(this.activeSection);
         this.container.center(var5);
         this.container.refreshLayout();
      }
   }

   public void setSection(String var1) {
      Preconditions.checkArgument(this.sections.containsKey(var1));
      WizardSectionsUi.Section var2 = this.sections.get(var1);

      for (WizardSectionsUi.Section var4 : this.sections.values()) {
         var4.setActive(var4 == var2);
      }

      if (this.container != null) {
         this.container.repaint();
      }

      this.activeSection = var1;
   }

   public String getSection() {
      return this.activeSection;
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (!this.sections.isEmpty()) {
         BoxBuilder var2 = this.layoutLabels();
         this.container = new BorderLayoutBuilder().withBorder(this.border);
         this.container.center(var2);
         this.container.decorate(var1);
      }
   }

   private BoxBuilder layoutLabels() {
      BoxBuilder var1 = BoxBuilder.horizontal();

      for (WizardSectionsUi.Section var3 : this.sections.values()) {
         var1.add(var3.panel);
      }

      return var1;
   }

   public void setBorder(Border var1) {
      if (this.container == null) {
         this.border = var1;
      } else {
         this.container.withBorder(var1);
      }
   }

   @Override
   public void requestFocus() {
   }

   public void setHorizontalAlignment(int var1) {
      for (WizardSectionsUi.Section var3 : this.sections.values()) {
         var3.label.setHorizontalAlignment(var1);
      }
   }

   private static class Arrow implements Icon {
      private final boolean active;

      public Arrow(boolean var1) {
         this.active = var1;
      }

      @Override
      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         Graphics2D var5 = (Graphics2D)var2;
         GraphicsState var6 = new GraphicsState(var5, GraphicsState.COLOR, GraphicsState.STROKE);
         this.prepareGraphics(var1, var5);
         this.drawTriangle(var5, var3, var4);
         var6.restore();
      }

      private void prepareGraphics(Component var1, Graphics2D var2) {
         var2.setStroke(new BasicStroke(1.5F));
         UiUtils.makeAntiAliased(var2);
         var2.setColor(this.active ? WizardSectionsUi.ACTIVE_TEXT : WizardSectionsUi.INACTIVE_TEXT);
      }

      private void drawTriangle(Graphics2D var1, int var2, int var3) {
         var1.fillPolygon(new int[]{var2, var2 + this.getIconWidth(), var2}, new int[]{var3, var3 + this.getIconHeight() / 2, var3 + this.getIconHeight()}, 3);
      }

      @Override
      public int getIconWidth() {
         return 10;
      }

      @Override
      public int getIconHeight() {
         return 18;
      }
   }

   private static class Section {
      public final JComponent panel;
      private final KongaLabel label;
      private final JLabel iconLabel;

      public Section(String var1, boolean var2) {
         this.label = new KongaLabel(var1.toUpperCase(), 0);
         this.label.setAntiAliasingEnabled(true);
         this.label.setHorizontalTextPosition(10);
         WizardStyleSheet.SECTION_STYLE.makeOver(this.label);
         if (var2) {
            this.iconLabel = new JLabel();
            this.iconLabel.setBorder(Empty.border(0, 0, 0, 5));
         } else {
            this.iconLabel = null;
         }

         BorderLayoutBuilder var3 = new BorderLayoutBuilder();
         var3.center(this.label).east(this.iconLabel);
         this.panel = var3.container();
         this.panel.setBackground(WizardSectionsUi.BACKGROUND);
         this.panel.setBorder(Empty.border(5));
         this.setActive(false);
      }

      public void setActive(boolean var1) {
         Color var2 = var1 ? WizardSectionsUi.ACTIVE_TEXT : WizardSectionsUi.INACTIVE_TEXT;
         this.label.setForeground(var2);
         if (this.iconLabel != null) {
            WizardSectionsUi.Arrow var3 = new WizardSectionsUi.Arrow(var1);
            this.iconLabel.setIcon(var3);
         }
      }
   }
}

