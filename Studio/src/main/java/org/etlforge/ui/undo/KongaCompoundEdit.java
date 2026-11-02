package org.etlforge.ui.undo;

import com.google.common.base.Optional;
import javax.annotation.Nullable;
import javax.swing.undo.CompoundEdit;

public final class KongaCompoundEdit extends CompoundEdit {
   private final Optional<String> presentationName;

   public KongaCompoundEdit() {
      this(null);
   }

   public KongaCompoundEdit(@Nullable String var1) {
      this.presentationName = Optional.fromNullable(var1);
   }

   public boolean isEmpty() {
      return this.edits.isEmpty();
   }

   @Override
   public String getPresentationName() {
      return this.presentationName.or(super.getPresentationName());
   }
}
