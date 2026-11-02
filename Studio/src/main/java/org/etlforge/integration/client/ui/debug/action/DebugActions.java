package org.etlforge.integration.client.ui.debug.action;

import com.google.common.collect.ImmutableList;
import javax.swing.Action;

public final class DebugActions {
   private final Action stop;
   private final Action resume;
   private final Action step;

   public DebugActions(Action var1, Action var2, Action var3) {
      this.stop = var1;
      this.resume = var2;
      this.step = var3;
   }

   public Action getStopAction() {
      return this.stop;
   }

   public Action getResumeAction() {
      return this.resume;
   }

   public Action getStepAction() {
      return this.step;
   }

   public Iterable<Action> all() {
      return ImmutableList.of(this.resume, this.step, this.stop);
   }
}
