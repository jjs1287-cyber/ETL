package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.ui.UiProvider;

public interface WhereClauseEditorUi extends UiProvider {
   void terminateEdit();

   void applyTo(Transformation var1);

   WhereClauseEditorUi.State getMemento();

   void setMemento(WhereClauseEditorUi.State var1);

   interface State {
      @Override
      boolean equals(Object var1);
   }
}
