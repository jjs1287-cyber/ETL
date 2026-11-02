package org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;
import org.etlforge.util.Property;

public interface EntityDependenciesModel extends Disposable {
   boolean isEmpty();

   Property<List<IntegrationEntity>> getDependants();
}
