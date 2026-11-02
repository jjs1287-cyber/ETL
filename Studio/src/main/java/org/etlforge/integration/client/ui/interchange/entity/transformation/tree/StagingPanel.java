package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.ui.UIManager2;

public class StagingPanel extends JPanel {
   private static final ResourceBundle s_resources = ResourceBundle.getBundle(
      "org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.Icons"
   );
   public static final Image m_imgExpand = (Image)s_resources.getObject("Expand");
   public static final Image m_imgPartExpand = (Image)s_resources.getObject("PartExpand");
   public static final Image m_imgCollapse = (Image)s_resources.getObject("Collapse");
   public static final Image m_imgMultiple = (Image)s_resources.getObject("Multiple");
   public static Color m_clrBgdTree = org.etlforge.application.ui.look.Colors.FIELD_BACKGROUND;
   public static Color m_clrLightBgd = org.etlforge.application.ui.look.Colors.PANEL_BACKGROUND;
   public static Color m_clrUnderLine = new Color(70, 105, 150);
   public static Color m_clrHighLightText = org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND;
   public static Color m_clrFolderText = UIManager2.getColor("etlforge.mappingtree.folderColor", Colors.THEME_TEXT_COLOR);
   public static Color m_clrAttributeText = UIManager2.getColor("etlforge.mappingtree.attributeColor", Colors.THEME_TEXT_COLOR);
   public static Color m_clrPreconditionText = UIManager2.getColor("etlforge.mappingtree.preconditionColor", Colors.THEME_TEXT_COLOR);
   public static Color m_clrHighLightRect = org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND;
   public static Color m_clrDefaultValueText = Color.DARK_GRAY;
   public static Color m_clrWarningText = new Color(205, 0, 0);
   public static Color m_clrBatchNode = Color.GREEN.darker();
   public static int m_textSize = 0;
   public static Font m_font12Bold;
   public static Font m_font11Bold;
   public static Font m_font11Plain;
   public static Font dataValueFont = new Font("Sans Serif, Verdana", 0, 14);

   public static void updateFonts(int var0) {
      m_textSize = var0;
      m_font12Bold = new Font("Verdana", 1, m_textSize + 1);
      m_font11Bold = new Font("Verdana", 1, m_textSize);
      m_font11Plain = new Font("Verdana", 0, m_textSize);
      dataValueFont = new Font("Sans Serif, Verdana", 0, m_textSize);
   }

   public StagingPanel() {
      this.setOpaque(false);
   }

   public Window getOwner() {
      return SwingUtilities.getWindowAncestor(this);
   }

   static {
      if (m_textSize == 0) {
         updateFonts(14);
      }
   }
}

