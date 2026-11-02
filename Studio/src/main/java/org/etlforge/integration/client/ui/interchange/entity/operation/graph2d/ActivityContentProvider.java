package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

public interface ActivityContentProvider<C> {
   void selectExisting();

   boolean isCreateNewSupported();

   void createNew();
}
