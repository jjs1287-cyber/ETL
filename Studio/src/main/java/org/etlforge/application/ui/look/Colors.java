package org.etlforge.application.ui.look;

import java.awt.Color;
import org.etlforge.ui.UIManager2;

public final class Colors {
   public static final Color TEXT_FOREGROUND = Color.WHITE;
   public static final Color FIELD_BACKGROUND = new Color(24, 24, 24);
   public static final Color PANEL_BACKGROUND = Color.BLACK;
   public static final Color THEME_TEXT_COLOR = UIManager2.getColor("etlforge.theme.text.color", Color.WHITE);
   public static final Color ETLFORGE_BLUE = Color.BLACK;
   public static final Color ETLFORGE_ORANGE = Color.BLACK;
   public static final Color ERROR_TEXT_COLOR = new Color(255, 125, 125);
   public static final Color VALID_TEXT_COLOR = new Color(108, 209, 144);
   public static final Color LIGHT_BLUE_BACKGROUND = Color.BLACK;
   public static final Color POPUPWINDOW_TITLE_COLOR = LIGHT_BLUE_BACKGROUND;
   public static final Color NEUTRAL_BACKGROUND = PANEL_BACKGROUND;
   public static final Color LIGHT_BLUE_TABLE_HEADER_BACKGROUND = Color.BLACK;
   public static final Color CHARACTER_PAIR_MATCHING_HIGHLIGHT = new Color(44, 91, 61);
   public static final Color CHARACTER_PAIR_MISMATCH_HIGHLIGHT = new Color(107, 47, 47);
   public static final Color DIALOG_TOP_COLOR = PANEL_BACKGROUND;
   public static final Color DIALOG_BOTTOM_COLOR = PANEL_BACKGROUND;
   public static final Color TREE_BACKGROUND = FIELD_BACKGROUND;
   public static final Color TREE_TITLE_FOCUSED_BACKGROUND = LIGHT_BLUE_BACKGROUND;
   public static final Color TREE_TITLE_UNFOCUSED_BACKGROUND = NEUTRAL_BACKGROUND;
   public static final Color UNSELECTED_TAB_BACKGROUND = PANEL_BACKGROUND;
   public static final Color GRADIENT_LEFT = FIELD_BACKGROUND;
   public static final Color GRADIENT_RIGHT = FIELD_BACKGROUND;

   private Colors() {
   }
}


