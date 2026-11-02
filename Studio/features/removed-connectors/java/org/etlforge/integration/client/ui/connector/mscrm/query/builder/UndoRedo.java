package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import org.etlforge.ui.property.UndoableEditProducer;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.undo.UndoRedoControllerBase;

public final class UndoRedo {
   public static UndoRedoController install(QueryBuilderUi var0, RootPaneContainer var1) {
      return install(var0, var1.getRootPane());
   }

   public static UndoRedoController install(QueryBuilderUi var0, JComponent var1) {
      UndoableEditProducer var2 = createEditProducer(var0.getModel());
      return install(var1, var2);
   }

   private static UndoableEditProducer createEditProducer(QueryBuilderModel var0) {
      UndoableEditProducer var1 = new UndoableEditProducer();
      var0.listenToAllChanges(var1);
      return var1;
   }

   private static UndoRedoControllerBase install(JComponent var0, UndoableEditProducer var1) {
      UndoRedoControllerBase var2 = new UndoRedoControllerBase();
      var2.addEditSender(var1);
      var2.installKeyBindings(var0, 1);
      return var2;
   }

   public static UndoRedoController install(LinkEntityBuilderUi var0, JComponent var1) {
      UndoableEditProducer var2 = createEditProducer(var0.getModel());
      var2.ignoreProperty("alias");
      UndoRedoControllerBase var3 = install(var1, var2);
      var0.contributeToUndoRedo(var3);
      return var3;
   }

   private UndoRedo() {
   }
}
