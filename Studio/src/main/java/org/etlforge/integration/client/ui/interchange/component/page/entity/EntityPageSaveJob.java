package org.etlforge.integration.client.ui.interchange.component.page.entity;

public interface EntityPageSaveJob {
   String getId();

   String getDescription();

   void save();

   void postSaveRevert();

   void undoWithoutSaving();

   void redoWithoutSaving();
}
