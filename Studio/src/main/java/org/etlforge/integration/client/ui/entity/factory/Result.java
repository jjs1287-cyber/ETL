package org.etlforge.integration.client.ui.entity.factory;

import com.google.common.base.Optional;
import org.etlforge.integration.data.entity.Folder;

public final class Result {
   private final Optional<Folder> parent;

   public static Result continueProcess(Folder var0) {
      return new Result(Optional.of(var0));
   }

   public static Result cancel() {
      return new Result(Optional.absent());
   }

   private Result(Optional<Folder> var1) {
      this.parent = var1;
   }

   public boolean isCanceled() {
      return !this.parent.isPresent();
   }

   public Folder getParent() {
      return this.parent.get();
   }

   @Override
   public String toString() {
      return this.isCanceled() ? "Canceled" : "Parent = " + this.parent.get();
   }
}
