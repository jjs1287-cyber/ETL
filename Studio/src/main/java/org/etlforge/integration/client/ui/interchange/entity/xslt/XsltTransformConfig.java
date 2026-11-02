package org.etlforge.integration.client.ui.interchange.entity.xslt;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class XsltTransformConfig extends AbstractEntityConfig {
    public XsltTransformConfig() { super(EntityType.XsltTransform); }
    @Override protected IntegrationEntityPage createNewPage(IntegrationEntity entity) {
        return new XsltTransformPage((XsltTransform)entity);
    }
    @Override protected PageConfig createPageConfig(ApplicationWindow window, IntegrationEntity entity, ProjectPersistor persistor) {
        return new DefaultPageConfig(window, persistor) {};
    }
}
