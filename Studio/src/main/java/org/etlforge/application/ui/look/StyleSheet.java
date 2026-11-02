package org.etlforge.application.ui.look;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import org.etlforge.ui.Empty;
import org.etlforge.ui.style.ButtonStyle;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.border.BorderUtils;
import org.etlforge.ui.util.border.TitledToolbar;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.ComponentPainters;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.window.PopupWindowTitle;

public final class StyleSheet {
   public static final PanelStyle GenericEditorTitlePanel = new PanelStyle(new Color(204, 204, 204), BorderFactory.createEmptyBorder(2, 5, 5, 2));
   public static final Border GenericEditorOuterBorder = BorderFactory.createEmptyBorder(0, 2, 2, 2);
   public static final Border GenericEditorInnerBorder = new BorderBuilder().etched().empty(6);
   public static final PanelStyle GenericEditorCanvas = new PanelStyle(Colors.LIGHT_BLUE_BACKGROUND, GenericEditorOuterBorder);
   public static final PanelStyle GenericEditorPanelClass = new PanelStyle(Colors.NEUTRAL_BACKGROUND, GenericEditorInnerBorder);
   public static final ButtonStyle LinkButton = new ButtonStyle(
      TextStyles.DefaultText.getFont().getFamily(), TextStyles.DefaultText.getFontSize(), 0, new Color(40, 40, 200), null, null
   );
   public static final ComponentPainter LEFT_TO_RIGHT_GRADIENT = ComponentPainters.horizontalGradient(Colors.GRADIENT_LEFT, Colors.GRADIENT_RIGHT);
   public static final Border TreeTitleBorder = new BorderBuilder().etched().empty(1, 5, 1, 5);
   public static final PanelStyle ComponentTableHeader = new PanelStyle(
      Colors.LIGHT_BLUE_TABLE_HEADER_BACKGROUND, new BorderBuilder().bevel(1, new Color(110, 123, 139), new Color(150, 150, 150, 150)).empty(4)
   );
   public static final PanelStyle ComponentTableEntry = new PanelStyle(Color.WHITE, new BorderBuilder().line(new Color(204, 204, 204), 1).empty(2));
   public static final PanelStyle ComponentTableAlternateEntry = new PanelStyle(
      new Color(221, 221, 221), BorderFactory.createLineBorder(new Color(204, 204, 204))
   );

   public static LabelDecorator createPopupWindowTitle(String var0) {
      return createPopupWindowTitle(var0, TextStyles.DefaultText);
   }

   public static LabelDecorator createPopupWindowTitle(String var0, TextStyle var1) {
      Color var2 = var1.getColor();
      Font var3 = var1.getFont();
      Color var4 = Colors.POPUPWINDOW_TITLE_COLOR;
      return new PopupWindowTitle(var0, var2, var4, var3);
   }

   public static final Border getDefaultTitleBorder(String var0) {
      TitledBorder var1 = new TitledBorder(var0);
      var1.setTitleColor(Colors.THEME_TEXT_COLOR);
      var1.setTitleFont(TextStyles.DefaultTitleBorderText.getFont());
      return new BorderBuilder(var1).empty(5);
   }

   public static final Border getLineTitleBorder(String var0) {
      return BorderUtils.getEmptyPaddedTitledBorder(
         null,
         new Insets(5, 5, 5, 5),
         var0,
         BorderUtils.getEmptyPaddedBorder(BorderFactory.createLineBorder(Color.BLACK), new Insets(5, 5, 5, 5), new Insets(2, 5, 2, 5)),
         Colors.THEME_TEXT_COLOR,
         TextStyles.DefaultTitleBorderText.getFont()
      );
   }

   public static void configureTitledToolbar(TitledToolbar var0) {
      var0.setInsideBorder(Empty.border(5));
      var0.setTitleFont(TextStyles.DefaultTitleBorderText.getFont());
      Color var1 = Colors.THEME_TEXT_COLOR;
      if (var1 instanceof ColorUIResource) {
         var1 = new Color(var1.getRed(), var1.getGreen(), var1.getBlue(), var1.getAlpha());
      }

      var0.setTitleColor(var1);
   }

   private StyleSheet() {
   }
}
