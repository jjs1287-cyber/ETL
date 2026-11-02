package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.undo.UndoRedoInstaller;

public interface WsdlSelector {
   String INPUT_CHANGED_PROPERTY = "IWsdlDefiner#InputChanged";

   boolean isValid();

   void setActive(boolean var1);

   WsdlProcessingJob getProcessingJob();

   UiProvider getUi();

   void addChangeListener(PropertyChangeListener var1);

   void removeChangeListener(PropertyChangeListener var1);

   void addActionListener(ActionListener var1);

   void removeActionListener(ActionListener var1);

   void installUndoRedo(UndoRedoInstaller var1);
}
