package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.dnd;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.style.TextStyle;

public final class DropZone {
   private static final TextStyle TITLE_STYLE = TextStyle.adjustSize(TextStyles.BigAdminHintBold, 3);
   private static final TextStyle TEXT_STYLE = TextStyle.adjustSize(TextStyles.AdminHintBold, 1);
   private static final Color INACTIVE_TEXT_COLOR = new Color(160, 160, 160);
   private final String title;
   private final String text;
   private final Color background;
   private boolean supportsDrops;
   private boolean active;

   public DropZone(String var1, String var2, Color var3) {
      this.title = var1;
      this.text = var2;
      this.background = var3;
      this.supportsDrops = true;
   }

   public void setActive(boolean var1) {
      this.active = var1;
   }

   public void render(Graphics2D var1, int var2, int var3, int var4) {
      var1.setColor(this.background);
      var1.fillRect(0, var2, var3, var4);
      Font var5 = TITLE_STYLE.getFont();
      FontMetrics var6 = var1.getFontMetrics(var5);
      Rectangle2D var7 = var6.getStringBounds(this.title, var1);
      Font var8 = TEXT_STYLE.getFont();
      var6 = var1.getFontMetrics(var8);
      Rectangle2D var9 = var6.getStringBounds(this.title, var1);
      byte var10 = 30;
      int var11 = (int)(var7.getHeight() + var10 + var9.getHeight());
      int var12 = var2 + (var4 - var11) / 2;
      var1.setColor(this.active ? Color.BLACK : INACTIVE_TEXT_COLOR);
      var1.setFont(TITLE_STYLE.getFont());
      var1.drawString(this.title, 10, var12);
      var1.setFont(TEXT_STYLE.getFont());
      var1.drawString(this.text, 10, var12 + var10);
   }

   @Override
   public String toString() {
      return this.title;
   }

   public boolean isDropSupported() {
      return this.supportsDrops;
   }

   public void setSupportsDrops(boolean var1) {
      this.supportsDrops = var1;
   }
}
