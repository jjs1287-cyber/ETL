package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.ui.window.editor.Editor;

public final class BackwardForwardStack {
   static final BackwardForwardStack EMPTY = new BackwardForwardStack(null, new Editor[0], -1);
   private final BackwardForwardNavigator navigator;
   private final Editor[] editors;
   private int position;

   BackwardForwardStack(BackwardForwardNavigator var1, Editor[] var2, int var3) {
      this.navigator = var1;
      this.editors = (Editor[])var2.clone();
      this.position = var3;
   }

   public int size() {
      return this.editors.length;
   }

   public Editor getEditorAt(int var1) {
      return this.editors[var1];
   }

   public int getPosition() {
      return this.position;
   }

   public Editor getNextEditor() {
      return this.canMoveForward() ? this.editors[this.position - 1] : null;
   }

   public boolean canMoveForward() {
      return this.position > 0;
   }

   public void moveForward() {
      if (this.canMoveForward()) {
         this.moveToPosition(this.position - 1);
      }
   }

   public Editor getPreviousEditor() {
      return this.canMoveBackward() ? this.editors[this.position + 1] : null;
   }

   public boolean canMoveBackward() {
      return this.position < this.size() - 1;
   }

   public void moveBackward() {
      if (this.canMoveBackward()) {
         this.moveToPosition(this.position + 1);
      }
   }

   public void moveToPosition(int var1) {
      int var2 = this.editors.length - var1 - 1;
      this.navigator.moveToPosition(var2);
      this.position = var1;
   }
}
