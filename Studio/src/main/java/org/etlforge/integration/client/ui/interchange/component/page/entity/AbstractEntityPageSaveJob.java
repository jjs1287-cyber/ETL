package org.etlforge.integration.client.ui.interchange.component.page.entity;

import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

public abstract class AbstractEntityPageSaveJob<T> implements EntityPageSaveJob {
   private final String id;
   private final T oldValue;
   private final T newValue;
   private String description;
   private Receiver<T> undoRedoStateReceiver;

   public AbstractEntityPageSaveJob(String var1, T var2, T var3) {
      this.id = var1;
      this.oldValue = (T)var2;
      this.newValue = (T)var3;
      this.description = "";
      this.undoRedoStateReceiver = NoReceiver.create();
   }

   @Override
   public final String getId() {
      return this.id;
   }

   public final void setDescription(String var1) {
      this.description = var1;
   }

   @Override
   public final String getDescription() {
      return this.description;
   }

   @Override
   public final void save() {
      this.apply(this.newValue);
   }

   protected abstract void apply(T var1);

   @Override
   public final void postSaveRevert() {
      this.apply(this.oldValue);
   }

   @Override
   public final void undoWithoutSaving() {
      this.undoRedoStateReceiver.handle(this.oldValue);
   }

   @Override
   public final void redoWithoutSaving() {
      this.undoRedoStateReceiver.handle(this.newValue);
   }

   protected final void setUndoRedoStateReceiver(Receiver<T> var1) {
      this.undoRedoStateReceiver = var1;
   }
}
