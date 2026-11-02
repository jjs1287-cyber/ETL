package org.etlforge.application.ui.window.menu;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiMode;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiModePreference;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.menu.ToggleActionRadioButtonMenuItem;

public class WindowDisplayModeMenu {
   private final JMenu jmenu = new JMenu("Display Mode");

   public WindowDisplayModeMenu() {
      ButtonGroup var1 = new ButtonGroup();

      for (EditorServiceUiMode var3 : EditorServiceUiMode.getSupportedModes()) {
         ToggleActionRadioButtonMenuItem var4 = new ToggleActionRadioButtonMenuItem(new WindowDisplayModeMenu.SwitchAction(var3));
         var1.add(var4);
         this.jmenu.add(var4);
      }
   }

   public JMenu getMenu() {
      return this.jmenu;
   }

   private static class SwitchAction extends AbstractAction implements ToggleAction, PreferenceListener<EditorServiceUiMode> {
      private final EditorServiceUiMode mode;

      public SwitchAction(EditorServiceUiMode var1) {
         super(var1.toString());
         this.mode = var1;
         this.setSelected(EditorServiceUiModePreference.get() == var1);
         EditorServiceUiModePreference.INSTANCE.addListener(this);
      }

      @Override
      public boolean isSelected() {
         return (Boolean)this.getValue("ToggleAction#ON_OFF");
      }

      @Override
      public void setSelected(boolean var1) {
         this.putValue("ToggleAction#ON_OFF", var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.setSelected(!this.isSelected());
         if (this.isSelected()) {
            EditorServiceUiModePreference.INSTANCE.set(this.mode);
         }
      }

      public void valueChanged(Preference<EditorServiceUiMode> var1, EditorServiceUiMode var2, EditorServiceUiMode var3) {
         this.setSelected(this.mode == var3);
      }
   }
}
