package org.etlforge.integration.client.ui.entity.search;

import javax.swing.JComponent;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.selection.SelectionAction;

interface ResultDisplayer {
   JComponent getDisplayer();

   void requestFocus();

   IntegrationEntity getSelectedEntity();

   void setDefaultAction(SelectionAction var1);
}
