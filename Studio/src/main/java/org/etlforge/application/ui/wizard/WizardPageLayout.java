package org.etlforge.application.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class WizardPageLayout {
   private final OneColumnPanel layout = new OneColumnPanel().withInsets(0, 0, 10, 0).withBorder(Empty.border(5));
   private boolean empty = true;
   private boolean transparent = true;

   public WizardPageLayout() {
      this(null, null);
   }

   public WizardPageLayout(String var1, String var2) {
      if (StringUtils.isNotBlank(var1) || StringUtils.isNotBlank(var2)) {
         this.layoutCaptions(var1, var2);
      }
   }

   public final JPanel asFlowLayout() {
      JPanel var1 = InvisiblePanel.of(this.layout);
      var1.setOpaque(false);
      return var1;
   }

   public final JPanel asNorthBorderLayout() {
      return this.asBorderLayout("North");
   }

   public final JPanel asCenterBorderLayout() {
      return this.asBorderLayout("Center");
   }

   public final JPanel asWestBorderLayout() {
      return this.asBorderLayout("West");
   }

   private JPanel asBorderLayout(Object var1) {
      JPanel var2 = new JPanel(new BorderLayout());
      this.addToContainer(var2, var1);
      var2.setOpaque(false);
      return var2;
   }

   public final void setBorder(Border var1) {
      this.layout.withBorder(var1);
   }

   public final void setInsets(int var1, int var2, int var3, int var4) {
      this.layout.withInsets(var1, var2, var3, var4);
   }

   public final void addToContainer(Container var1, Object var2) {
      this.layout.setOpaque(false, this.transparent);
      this.layout.addToParent(var1, var2);
   }

   public final void add(Component var1, boolean var2, boolean var3) {
      this.prepareForNewComponent(var2, var3);
      this.layout.add(var1);
      this.empty = false;
   }

   public final void add(UiProvider var1, boolean var2, boolean var3) {
      this.prepareForNewComponent(var2, var3);
      this.layout.add(var1);
      this.empty = false;
   }

   public final void setTransparent(boolean var1) {
      this.transparent = var1;
   }

   private void layoutCaptions(String var1, String var2) {
      this.layout.withInsets(2, 0, 2, 0);
      if (StringUtils.isNotBlank(var1)) {
         this.layout.add(this.getMainCaptionStyle().makeLabel(var1));
      }

      if (StringUtils.isNotBlank(var2)) {
         this.layout.add(this.getSubCaptionStyle().makeLabel(var2));
      }
   }

   protected TextStyle getMainCaptionStyle() {
      return TextStyles.InfoTextHeader;
   }

   protected TextStyle getSubCaptionStyle() {
      return TextStyles.AdminHint;
   }

   private void prepareForNewComponent(boolean var1, boolean var2) {
      Insets var3 = this.empty ? new Insets(8, 0, 2, 0) : new Insets(25, 0, 2, 0);
      this.layout.withInsets(var3);
      if (var2) {
         this.layout.setFill(OneColumnPanel.BOTH);
         this.layout.setWeightY(1.0);
      } else {
         this.layout.setFill(OneColumnPanel.HORIZONTAL);
         this.layout.setWeightY(0.0);
      }

      if (var1) {
         this.layout.setWeightX(1.0);
      } else {
         this.layout.setWeightX(0.0);
      }
   }
}
