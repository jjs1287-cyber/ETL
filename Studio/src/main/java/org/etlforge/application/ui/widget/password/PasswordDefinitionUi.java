package org.etlforge.application.ui.widget.password;

import javax.swing.JComponent;
import org.etlforge.ui.UiProvider;

public interface PasswordDefinitionUi extends UiProvider {
   boolean validate();

   char[] getPassword();

   void addCapsLockListener(JComponent var1);

   void removeCapsLockListener(JComponent var1);

   void dispose();

   void setIncludeReTypeField(boolean var1);
}
