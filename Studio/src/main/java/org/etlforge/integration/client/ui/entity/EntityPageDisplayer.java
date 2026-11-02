package org.etlforge.integration.client.ui.entity;

import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityPageDisplayer extends EntityPageLocator {
   Editor openPage(IntegrationEntity var1);
}
