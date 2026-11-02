package org.etlforge.application.ui.look;

import javax.swing.Icon;
import javax.swing.UIManager;

public final class AlertIcons {
   public static final Icon INFO = UIManager.getIcon("OptionPane.informationIcon");
   public static final Icon WARNING = UIManager.getIcon("OptionPane.warningIcon");
   public static final Icon ERROR = UIManager.getIcon("OptionPane.errorIcon");
   public static final Icon QUESTION = UIManager.getIcon("OptionPane.questionIcon");

   private AlertIcons() {
   }
}
