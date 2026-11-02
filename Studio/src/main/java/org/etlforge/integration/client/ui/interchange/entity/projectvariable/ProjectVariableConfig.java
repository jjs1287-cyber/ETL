package org.etlforge.integration.client.ui.interchange.entity.projectvariable;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProjectVariable;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ProjectVariableConfig extends AbstractEntityConfig {
    public ProjectVariableConfig() { super(EntityType.ProjectVariable); }
    @Override protected IntegrationEntityPage createNewPage(IntegrationEntity entity) {
        return new ProjectVariablePage((ProjectVariable)entity);
    }
    @Override protected PageConfig createPageConfig(ApplicationWindow window, IntegrationEntity entity, ProjectPersistor persistor) {
        return new DefaultPageConfig(window, persistor) {};
    }
}
