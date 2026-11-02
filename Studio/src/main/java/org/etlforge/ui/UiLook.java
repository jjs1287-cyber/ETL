package org.etlforge.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

public final class UiLook {
   private static final boolean COLORED_TITLE_PANELS = false;
   private static Color[] COL_TITLE_PANEL_BACKGROUND;
   private static Color[] COL_TITLE_PANEL_FOREGROUND;
   private static final JLabel DEFAULT_LABEL = new JLabel();
   private static final JTextField DEFAULT_TEXTFIELD = new JTextField();

   public static void setSystemLookAndFeel() throws InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException {
      String var0 = UIManager.getSystemLookAndFeelClassName();
      UIManager.setLookAndFeel(var0);
   }

   public static void setSystemLookAndFeelIgnoreErrors() {
      try {
         String var0 = UIManager.getSystemLookAndFeelClassName();
         UIManager.setLookAndFeel(var0);
      } catch (UnsupportedLookAndFeelException var1) {
         var1.printStackTrace();
      } catch (ClassNotFoundException var2) {
         var2.printStackTrace();
      } catch (InstantiationException var3) {
         var3.printStackTrace();
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
      }
   }

   public static void setMetalLookAndFeel() throws UnsupportedLookAndFeelException {
      UIManager.setLookAndFeel(new MetalLookAndFeel());
   }

   public static void setMetalTheme(MetalTheme var0) throws UnsupportedLookAndFeelException {
      MetalLookAndFeel.setCurrentTheme(var0);
      UIManager.setLookAndFeel(new MetalLookAndFeel());
   }

   public static void setCrossPlatformLookAndFeel() throws InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException {
      String var0 = UIManager.getCrossPlatformLookAndFeelClassName();
      UIManager.setLookAndFeel(var0);
   }

   public static synchronized Color getTitlePanelBackground(boolean var0) {
      if (COL_TITLE_PANEL_BACKGROUND == null) {
         initTitlePanelBackgroundColors();
      }

      return COL_TITLE_PANEL_BACKGROUND[var0 ? 0 : 1];
   }

   public static synchronized Color getTitlePanelForeground(boolean var0) {
      if (COL_TITLE_PANEL_FOREGROUND == null) {
         initTitlePanelForegroundColors();
      }

      return COL_TITLE_PANEL_FOREGROUND[var0 ? 0 : 1];
   }

   public static Font getLabelFont() {
      Object var0 = UIManager.getDefaults().get("Label.font");
      return var0 instanceof FontUIResource ? (FontUIResource)var0 : DEFAULT_LABEL.getFont();
   }

   public static Font getLabelFont(int var0, int var1) {
      Font var2 = getLabelFont();
      return var2.deriveFont(var0, var2.getSize() + var1);
   }

   public static Font getTextFieldFont() {
      Object var0 = UIManager.getDefaults().get("TextField.font");
      return var0 instanceof FontUIResource ? (FontUIResource)var0 : DEFAULT_TEXTFIELD.getFont();
   }

   public static Font getTextFieldFont(int var0, int var1) {
      Font var2 = getTextFieldFont();
      return var2.deriveFont(var0, var2.getSize() + var1);
   }

   public static Color getTreeSelectionBackground(boolean var0) {
      Color var1 = null;
      if (var0) {
         Object var2 = UIManager.getDefaults().get("Tree.selectionBackground");
         if (var2 instanceof ColorUIResource) {
            var1 = (ColorUIResource)var2;
         } else {
            var1 = getTitlePanelBackground(true);
         }
      } else {
         var1 = new JPanel().getBackground();
      }

      return var1;
   }

   public static Color getSplashScreenBackground() {
      Object var0 = UIManager.getDefaults().get("InternalFrame.activeTitleBackground");
      return var0 instanceof ColorUIResource ? (ColorUIResource)var0 : COL_TITLE_PANEL_FOREGROUND[0];
   }

   public static Color getSelectedTabBackground() {
      return getTitlePanelBackground(true);
   }

   public static Color getSelectedTabForeground() {
      return getTitlePanelForeground(true);
   }

   public static Color getUnSelectedTabBackground() {
      return getTitlePanelBackground(false);
   }

   public static Color getUnSelectedTabForeground() {
      return getTitlePanelForeground(false);
   }

   private static void initTitlePanelBackgroundColors() {
      COL_TITLE_PANEL_BACKGROUND = new Color[]{new Color(200, 196, 179), (ColorUIResource)UIManager.getDefaults().get("Panel.background")};
   }

   public static void initTitlePanelForegroundColors() {
      COL_TITLE_PANEL_FOREGROUND = new Color[]{Color.black, Color.black};
   }

   private UiLook() {
   }

   public static final class Theme {
      public static final MetalTheme DEFAULT = new UiLook.Theme.Default();
      public static final MetalTheme AQUA = new UiLook.Theme.Aqua();
      public static final MetalTheme LARGE_FONT = new UiLook.Theme.LargeFont();
      public static final MetalTheme LOW_VISION = new UiLook.Theme.LowVision();
      private static final MetalTheme[] fValues = new MetalTheme[]{DEFAULT, AQUA, LARGE_FONT, LOW_VISION};
      public static final List<MetalTheme> VALUES = Collections.unmodifiableList(Arrays.asList(fValues));
      private static final String fDEFAULT_NAME = "Default";
      private static final String fAQUA_NAME = "Aqua";
      private static final String fLARGE_FONT_NAME = "Large Font";
      private static final String fLOW_VISION_NAME = "Low Vision";

      private Theme() {
      }

      public static MetalTheme valueOf(String var0) {
         if (var0 == null) {
            return null;
         }

         Iterator var1 = VALUES.iterator();
         MetalTheme var2 = null;

         while (var1.hasNext()) {
            var2 = (MetalTheme)var1.next();
            if (var0.equals(var2.getName())) {
               return var2;
            }
         }

         throw new IllegalArgumentException("Cannot parse into Theme object:" + var0);
      }

      static boolean hasLargeFont(MetalTheme var0) {
         return var0 == LARGE_FONT || var0 == LOW_VISION;
      }

      private static final class Aqua extends UiLook.Theme.Default {
         private static final String fName = "Aqua";
         private final ColorUIResource fPrimary1 = new ColorUIResource(102, 153, 153);
         private final ColorUIResource fPrimary2 = new ColorUIResource(128, 192, 192);
         private final ColorUIResource fPrimary3 = new ColorUIResource(159, 235, 235);

         private Aqua() {
         }

         @Override
         public String getName() {
            return "Aqua";
         }

         @Override
         protected ColorUIResource getPrimary1() {
            return this.fPrimary1;
         }

         @Override
         protected ColorUIResource getPrimary2() {
            return this.fPrimary2;
         }

         @Override
         protected ColorUIResource getPrimary3() {
            return this.fPrimary3;
         }
      }

      private static class Default extends DefaultMetalTheme {
         private static final String fName = "Default";

         private Default() {
         }

         @Override
         public String getName() {
            return "Default";
         }

         @Override
         public final String toString() {
            return this.getName();
         }
      }

      private static class LargeFont extends UiLook.Theme.Default {
         private static final String fName = "Large Font";
         private final FontUIResource fControlFont = new FontUIResource("Dialog", 1, 18);
         private final FontUIResource fSystemFont = new FontUIResource("Dialog", 0, 18);
         private final FontUIResource fWindowTitleFont = new FontUIResource("Dialog", 1, 18);
         private final FontUIResource fUserFont = new FontUIResource("SansSerif", 0, 18);
         private final FontUIResource fSmallFont = new FontUIResource("Dialog", 0, 14);

         private LargeFont() {
         }

         @Override
         public String getName() {
            return "Large Font";
         }

         @Override
         public FontUIResource getControlTextFont() {
            return this.fControlFont;
         }

         @Override
         public FontUIResource getSystemTextFont() {
            return this.fSystemFont;
         }

         @Override
         public FontUIResource getUserTextFont() {
            return this.fUserFont;
         }

         @Override
         public FontUIResource getMenuTextFont() {
            return this.fControlFont;
         }

         @Override
         public FontUIResource getWindowTitleFont() {
            return this.fWindowTitleFont;
         }

         @Override
         public FontUIResource getSubTextFont() {
            return this.fSmallFont;
         }
      }

      private static final class LowVision extends UiLook.Theme.LargeFont {
         private static final String fName = "Low Vision";
         private final ColorUIResource fPrimary1 = new ColorUIResource(0, 0, 0);
         private final ColorUIResource fPrimary2 = new ColorUIResource(204, 204, 204);
         private final ColorUIResource fPrimary3 = new ColorUIResource(255, 255, 255);
         private final ColorUIResource fPrimaryHighlight = new ColorUIResource(102, 102, 102);
         private final ColorUIResource fSecondary2 = new ColorUIResource(204, 204, 204);
         private final ColorUIResource fSecondary3 = new ColorUIResource(255, 255, 255);
         private final Border fBlackLineBorder = new BorderUIResource(new LineBorder(this.getBlack()));
         private final Object fTextBorder = new BorderUIResource(new CompoundBorder(this.fBlackLineBorder, new MarginBorder()));
         private static final int fInternalFrameIconSize = 30;
         private static final Integer fScrollBarWidth = 25;

         private LowVision() {
         }

         @Override
         public String getName() {
            return "Low Vision";
         }

         @Override
         public ColorUIResource getPrimaryControlHighlight() {
            return this.fPrimaryHighlight;
         }

         @Override
         public ColorUIResource getControlHighlight() {
            return super.getSecondary3();
         }

         @Override
         public ColorUIResource getFocusColor() {
            return this.getBlack();
         }

         @Override
         public ColorUIResource getTextHighlightColor() {
            return this.getBlack();
         }

         @Override
         public ColorUIResource getHighlightedTextColor() {
            return this.getWhite();
         }

         @Override
         public ColorUIResource getMenuSelectedBackground() {
            return this.getBlack();
         }

         @Override
         public ColorUIResource getMenuSelectedForeground() {
            return this.getWhite();
         }

         @Override
         public ColorUIResource getAcceleratorForeground() {
            return this.getBlack();
         }

         @Override
         public ColorUIResource getAcceleratorSelectedForeground() {
            return this.getWhite();
         }

         @Override
         public void addCustomEntriesToTable(UIDefaults var1) {
            super.addCustomEntriesToTable(var1);
            var1.put("ToolTip.border", this.fBlackLineBorder);
            var1.put("TitledBorder.border", this.fBlackLineBorder);
            var1.put("ScrollPane.border", this.fBlackLineBorder);
            var1.put("TextField.border", this.fTextBorder);
            var1.put("PasswordField.border", this.fTextBorder);
            var1.put("TextArea.border", this.fTextBorder);
            var1.put("TextPane.border", this.fTextBorder);
            var1.put("EditorPane.border", this.fTextBorder);
            var1.put("InternalFrame.closeIcon", MetalIconFactory.getInternalFrameCloseIcon(30));
            var1.put("InternalFrame.maximizeIcon", MetalIconFactory.getInternalFrameMaximizeIcon(30));
            var1.put("InternalFrame.iconifyIcon", MetalIconFactory.getInternalFrameMinimizeIcon(30));
            var1.put("InternalFrame.minimizeIcon", MetalIconFactory.getInternalFrameAltMaximizeIcon(30));
            var1.put("ScrollBar.width", fScrollBarWidth);
         }

         @Override
         protected ColorUIResource getPrimary1() {
            return this.fPrimary1;
         }

         @Override
         protected ColorUIResource getPrimary2() {
            return this.fPrimary2;
         }

         @Override
         protected ColorUIResource getPrimary3() {
            return this.fPrimary3;
         }

         @Override
         protected ColorUIResource getSecondary2() {
            return this.fSecondary2;
         }

         @Override
         protected ColorUIResource getSecondary3() {
            return this.fSecondary3;
         }
      }
   }
}
