package com.etlforge.integration.client.ui.sfdc.page;

import java.util.List;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public interface LinkToImplementingObject extends Disposable {
   List<IntegrationEntity> applyToOperation(Operation var1);

   void layoutIn(TwoColumnLayout var1);

   void layoutIn(NColumnPanel var1);

   void registerAsChangeSource(InputPanel var1);

   void validate() throws IntegrationDataPanelException;
}
