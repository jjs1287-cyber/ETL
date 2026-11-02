package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;

public interface HistoryPointFactory {
   EditorHistoryPoint createHistoryPoint(IntegrationEntityPage var1);
}
