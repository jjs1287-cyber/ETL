package org.etlforge.application.ui.action;

import java.awt.event.ActionEvent;
import org.etlforge.ui.action.Action2;

public interface ApplicationAction extends Action2 {
   String getID();

   void execute(ActionEvent var1);

   void dispose();

   boolean isSupported();
}
