package org.etlforge.integration.client.ui.interchange.component.toolbar;

import java.util.List;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;

public interface NewEntityToolBarEntry {
   List<? extends ToolBarEntry> getNewEntityToolBarButtons(EntityConfigLookup var1);
}
