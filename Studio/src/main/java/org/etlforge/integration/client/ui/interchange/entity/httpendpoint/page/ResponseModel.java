package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.httpendpoint.OperationRunMode;
import org.etlforge.integration.data.entity.httpendpoint.ResponseType;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperations;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.ui.event.UndoableEditListeners;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ResponseModel extends AbstractPropertyBasedModel {
   public static final String OPERATIONS = "operations";
   public static final String RESPONSE_GENERATOR = "response_generator";
   public static final String ASYNCHRONOUS = "asynchronous";
   public static final String SYNCHRONOUS_OPERATIONS = "synchronous_operations";
   public static final String RESPONSE_TYPE = "response_type";
   public static final String RUN_MODE = "run_mode";
   public static final String VARIABLE_NAME = "variable_name";
   private final Property<Set<OperationId>> operationIds = this.newProperty("operations");
   private final Map<OperationId, OperationRunMode> runModes;
   private final Property<Boolean> asynchronous;
   private final Property<OperationId> responseGenerator;
   private final Property<Set<OperationId>> synchronousOperations;
   private final Property<ResponseType> responseType;
   private final Property<String> variableName;
   private final UndoableEditListeners undoRedoListeners;
   private CompoundEdit currentEdit;
   private boolean blockUndoRedo;

   public ResponseModel(HttpEndpoint var1) {
      this.operationIds.set(new LinkedHashSet<>());
      this.operationIds.setAllowsNull(false);
      this.responseGenerator = this.newProperty("response_generator");
      this.runModes = Maps.newHashMap();
      this.asynchronous = this.newProperty("asynchronous");
      this.synchronousOperations = this.newProperty("synchronous_operations");
      this.synchronousOperations.set(new LinkedHashSet<>());
      this.synchronousOperations.setAllowsNull(false);
      this.responseType = this.newProperty("response_type", ResponseType.NONE);
      this.responseType.setAllowsNull(false);
      this.variableName = this.newProperty("variable_name");
      this.undoRedoListeners = new UndoableEditListeners();
      this.reset(var1);
   }

   public void applyTo(HttpEndpoint var1) {
      var1.setResponseType(this.getResponseType());
      var1.setTriggeredOperations(this.asTriggeredOperations());
      var1.setResponseVariable(this.getVariableName());
   }

   public void reset(HttpEndpoint var1) {
      this.responseGenerator.setSilently(null);
      this.runModes.clear();
      this.operationIds.get().clear();
      this.synchronousOperations.get().clear();
      TriggeredOperations var2 = var1.getTriggeredOperations();

      for (TriggeredOperation var4 : var2.getOperationsForVerb(null)) {
         this.operationIds.get().add(var4.getOperationId());
         this.runModes.put(var4.getOperationId(), var4.getRunMode());
         if (var4.getRunMode() == OperationRunMode.SYNCHRONOUS) {
            this.synchronousOperations.get().add(var4.getOperationId());
         }

         if (var4.isResponseGenerator()) {
            this.responseGenerator.setSilently(var4.getOperationId());
         }
      }

      this.setResponseTypeImpl(var1.getResponseType(), true);
      this.setVariableNameSilently(var1.getResponseVariable());
      this.updateAsynchronousFlag(true);
      this.sendChange(null, null, null);
   }

   public ResponseType getResponseType() {
      return this.responseType.get();
   }

   public void setResponseType(ResponseType var1) {
      ResponseType var2 = this.getResponseType();
      if (var1 != var2) {
         boolean var3 = false;
         if (this.currentEdit == null) {
            this.currentEdit = new CompoundEdit();
            var3 = true;
         }

         OperationId var4 = this.getResponseGenerator();
         this.setResponseTypeImpl(var1, false);
         OperationId var5 = this.getResponseGenerator();
         this.currentEdit.addEdit(new ResponseModel.ResponseTypeEdit(var2, var1, var5 != var4 ? var5 : null));
         if (var3) {
            this.sendUndoRedoEvent();
         }
      }
   }

   public String getVariableName() {
      return this.variableName.get();
   }

   public void setVariableName(String var1) {
      this.variableName.set(var1);
   }

   public void setVariableNameSilently(String var1) {
      this.variableName.setSilently(var1);
   }

   public OperationId getResponseGenerator() {
      return this.responseGenerator.get();
   }

   public void setResponseGenerator(OperationId var1) {
      this.checkOperationId(var1);
      OperationId var2 = this.getResponseGenerator();
      if (!Objects.equal(var1, var2)) {
         boolean var3 = false;
         if (this.currentEdit == null) {
            this.currentEdit = new CompoundEdit();
            var3 = true;
         }

         this.responseGenerator.set(var1);
         this.currentEdit.addEdit(new ResponseModel.ResponseGeneratorEdit(var2, var1));
         if (var3) {
            this.sendUndoRedoEvent();
         }
      }
   }

   public List<OperationId> getOperationIds() {
      return Lists.newArrayList(this.operationIds.get());
   }

   public TriggeredOperations asTriggeredOperations() {
      TriggeredOperations var1 = new TriggeredOperations();
      OperationId var2 = this.getResponseGenerator();

      for (OperationId var4 : this.operationIds.get()) {
         OperationRunMode var5 = this.getRunMode(var4);
         boolean var6 = var5 == OperationRunMode.SYNCHRONOUS ? var4 == var2 : false;
         var1.addOperation(new TriggeredOperation(var4, null, var5, var6));
      }

      return var1;
   }

   public void addOperation(Operation var1) {
      boolean var2 = false;
      if (this.currentEdit == null) {
         this.currentEdit = new CompoundEdit();
         var2 = true;
      }

      this.addOperation(var1.getID());
      this.currentEdit.addEdit(new ResponseModel.AddOperationEdit(var1.getID()));
      if (var2) {
         this.sendUndoRedoEvent();
      }
   }

   public boolean contains(OperationId var1) {
      return this.operationIds.get().contains(var1);
   }

   public void removeOperations(List<TriggeredOperation> var1) {
      boolean var2 = false;
      if (this.currentEdit == null) {
         this.currentEdit = new ResponseModel.NamedCompoundEdit("Remove Operation(s)");
         var2 = true;
      }

      for (TriggeredOperation var4 : var1) {
         this.removeOperation(var4.getOperationId());
      }

      this.updateAsynchronousFlag(false);
      if (var2) {
         this.sendUndoRedoEvent();
      }
   }

   public void operationWasDeleted(OperationId var1) {
      this.blockUndoRedo = true;

      try {
         this.removeOperation(var1);
         this.updateAsynchronousFlag(false);
      } finally {
         this.blockUndoRedo = false;
      }
   }

   public void setRunMode(OperationId var1, OperationRunMode var2) {
      this.checkOperationId(var1);
      OperationRunMode var3 = this.getRunMode(var1);
      if (var2 != var3) {
         ResponseType var4 = this.getResponseType();
         boolean var5 = false;
         if (this.currentEdit == null) {
            this.currentEdit = new CompoundEdit();
            var5 = true;
         }

         this.runModes.put(var1, var2);
         this.sendChange("run_mode", var3, var2);
         this.updateAsynchronousFlag(false);
         this.currentEdit.addEdit(new ResponseModel.RunModeEdit(var1, var3, var4, var2, this.getResponseType()));
         if (var5) {
            this.sendUndoRedoEvent();
         }
      }
   }

   public OperationRunMode getRunMode(OperationId var1) {
      OperationRunMode var2 = this.runModes.get(var1);
      if (var2 == null) {
         var2 = OperationRunMode.ASYNCHRONOUS;
      }

      return var2;
   }

   public boolean isAsynchronous() {
      return this.asynchronous.get();
   }

   public void addUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListeners.add(var1);
   }

   public void removeUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListeners.remove(var1);
   }

   private void sendUndoRedoEvent() {
      this.currentEdit.end();
      if (!this.blockUndoRedo) {
         this.undoRedoListeners.call().undoableEditHappened(new UndoableEditEvent(this, this.currentEdit));
      }

      this.currentEdit = null;
   }

   private void addOperation(OperationId var1) {
      LinkedHashSet var2 = Sets.newLinkedHashSet(this.operationIds.get());
      if (!var2.contains(var1)) {
         var2.add(var1);
         this.runModes.put(var1, OperationRunMode.SYNCHRONOUS);
         this.operationIds.set(Sets.newLinkedHashSet(var2));
         this.updateAsynchronousFlag(false);
      }
   }

   private void removeOperation(OperationId var1) {
      if (var1.equals(this.responseGenerator.get())) {
         this.setResponseGenerator(null);
      }

      if (this.currentEdit != null) {
         this.currentEdit.addEdit(new ResponseModel.RemoveOperationEdit(var1));
      }

      LinkedHashSet var2 = Sets.newLinkedHashSet(this.operationIds.get());
      var2.remove(var1);
      this.operationIds.set(Sets.newLinkedHashSet(var2));
      this.runModes.remove(var1);
   }

   private void setResponseTypeImpl(ResponseType var1, boolean var2) {
      ResponseType var3 = this.getResponseType();
      if (var2) {
         this.responseType.setSilently(var1);
      } else {
         this.responseType.set(var1);
      }

      if (var1 == ResponseType.NONE && var1 != var3) {
         this.updateAsynchronousFlag(var2);
      } else if (var1 == ResponseType.OPERATION && this.operationIds.get().size() == 1) {
         OperationId var4 = this.getOperationIds().get(0);
         if (this.getRunMode(var4) == OperationRunMode.SYNCHRONOUS) {
            this.setResponseGenerator(this.getOperationIds().get(0));
         }
      }
   }

   private void updateAsynchronousFlag(boolean var1) {
      boolean var2 = true;

      for (OperationRunMode var4 : this.runModes.values()) {
         if (var4 == OperationRunMode.SYNCHRONOUS) {
            var2 = false;
            break;
         }
      }

      if (var1) {
         this.asynchronous.setSilently(var2);
      } else {
         this.asynchronous.set(var2);
      }

      if (var2) {
         if (var1) {
            this.responseType.setSilently(ResponseType.NONE);
         } else {
            this.responseType.set(ResponseType.NONE);
         }
      }
   }

   private void checkOperationId(OperationId var1) {
      if (var1 != null && !this.operationIds.get().contains(var1)) {
         throw new IllegalArgumentException("Unknown operation ID: " + var1);
      }
   }

   private class AddOperationEdit extends ResponseModel.EditBase {
      private final OperationId opId;

      public AddOperationEdit(OperationId var2) {
         super("Add Operation");
         this.opId = var2;
      }

      @Override
      protected void doRedo() {
         ResponseModel.this.addOperation(this.opId);
      }

      @Override
      protected void doUndo() {
         ResponseModel.this.removeOperation(this.opId);
      }
   }

   private abstract class EditBase extends AbstractUndoableEdit {
      private final String name;

      public EditBase(String var2) {
         this.name = var2;
      }

      @Override
      public final void redo() throws CannotRedoException {
         ResponseModel.this.blockUndoRedo = true;

         try {
            super.redo();
            this.doRedo();
         } finally {
            ResponseModel.this.blockUndoRedo = false;
         }
      }

      @Override
      public final void undo() throws CannotUndoException {
         ResponseModel.this.blockUndoRedo = true;

         try {
            super.undo();
            this.doUndo();
         } finally {
            ResponseModel.this.blockUndoRedo = false;
         }
      }

      protected abstract void doRedo();

      protected abstract void doUndo();

      @Override
      public final String getPresentationName() {
         return this.name;
      }
   }

   private static class NamedCompoundEdit extends CompoundEdit {
      private final String name;

      public NamedCompoundEdit(String var1) {
         this.name = var1;
      }

      @Override
      public String getPresentationName() {
         return this.name;
      }
   }

   private class RemoveOperationEdit extends ResponseModel.EditBase {
      private final OperationId opId;
      private final OperationRunMode runMode;
      private final boolean responseGenerator;
      private final boolean asynchronous;
      private final ResponseType responseType;

      public RemoveOperationEdit(OperationId var2) {
         super("Remove Operation(s)");
         this.opId = var2;
         this.runMode = ResponseModel.this.getRunMode(var2);
         this.responseGenerator = ResponseModel.this.getResponseGenerator() == this.opId;
         this.responseType = ResponseModel.this.getResponseType();
         this.asynchronous = ResponseModel.this.isAsynchronous();
      }

      @Override
      protected void doRedo() {
         ResponseModel.this.removeOperation(this.opId);
      }

      @Override
      protected void doUndo() {
         ResponseModel.this.addOperation(this.opId);
         ResponseModel.this.setResponseType(this.responseType);
         ResponseModel.this.setRunMode(this.opId, this.runMode);
         if (this.responseGenerator) {
            ResponseModel.this.setResponseGenerator(this.opId);
         }

         if (this.asynchronous) {
            ResponseModel.this.asynchronous.set(this.asynchronous);
         }
      }
   }

   private class ResponseGeneratorEdit extends ResponseModel.EditBase {
      private final OperationId oldGenerator;
      private final OperationId newGenerator;

      public ResponseGeneratorEdit(OperationId var2, OperationId var3) {
         super("Response Generator Change");
         this.oldGenerator = var2;
         this.newGenerator = var3;
      }

      @Override
      protected void doRedo() {
         ResponseModel.this.setResponseGenerator(this.newGenerator);
      }

      @Override
      protected void doUndo() {
         ResponseModel.this.setResponseGenerator(this.oldGenerator);
      }
   }

   private class ResponseTypeEdit extends ResponseModel.EditBase {
      private final ResponseType oldType;
      private final ResponseType newType;
      private final OperationId newResponseId;

      public ResponseTypeEdit(ResponseType var2, ResponseType var3, OperationId var4) {
         super("Response Type Change");
         this.oldType = var2;
         this.newType = var3;
         this.newResponseId = var4;
      }

      @Override
      protected void doRedo() {
         ResponseModel.this.setResponseTypeImpl(this.newType, false);
         if (this.newResponseId != null) {
            ResponseModel.this.setResponseGenerator(this.newResponseId);
         }
      }

      @Override
      protected void doUndo() {
         ResponseModel.this.setResponseTypeImpl(this.oldType, false);
         if (this.newResponseId != null) {
            ResponseModel.this.setResponseGenerator(null);
         }
      }
   }

   private class RunModeEdit extends ResponseModel.EditBase {
      private final OperationId opId;
      private final OperationRunMode oldMode;
      private final OperationRunMode newMode;
      private final ResponseType oldResponseType;
      private final ResponseType newResponseType;

      public RunModeEdit(OperationId var2, OperationRunMode var3, ResponseType var4, OperationRunMode var5, ResponseType var6) {
         super("Run Mode Change");
         this.opId = var2;
         this.oldMode = var3;
         this.oldResponseType = var4;
         this.newMode = var5;
         this.newResponseType = var6;
      }

      @Override
      protected void doRedo() {
         ResponseModel.this.setRunMode(this.opId, this.newMode);
         ResponseModel.this.setResponseType(this.newResponseType);
      }

      @Override
      protected void doUndo() {
         ResponseModel.this.setRunMode(this.opId, this.oldMode);
         ResponseModel.this.setResponseType(this.oldResponseType);
      }
   }
}
