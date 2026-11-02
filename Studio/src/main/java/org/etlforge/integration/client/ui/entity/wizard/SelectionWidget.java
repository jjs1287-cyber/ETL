package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.undo.UndoRedoInstaller;

interface SelectionWidget<T extends IntegrationEntity> extends UiProvider, Disposable {
   T getSelectedEntity();

   void setSelectedEntity(IntegrationEntityId var1);

   void setNewEntityCallback(UiEntityFactoryCallback var1);

   void addSelectionListener(SelectionListener var1);

   void removeSelectionListener(SelectionListener var1);

   void setFilter(EntityFilter var1);

   boolean accepts(IntegrationEntity var1);

   void setSelectorLabel(String var1);

   void installUndoRedo(UndoRedoInstaller var1);
}
