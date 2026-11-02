package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

public interface DataTreeListener {
   void newDataLoaded();

   void dataRemoved();

   void newDataInstanceSelected(String var1);
}
