package org.etlforge.integration.client.ui.script.debug;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.data.script.SourceDataElementContainer;
import org.etlforge.integration.debug.client.DebugController;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingException;
import org.etlforge.integration.debug.client.DebuggingParameters;
import org.etlforge.integration.debug.client.DebuggingParametersBuilder;
import org.etlforge.integration.debug.client.DebuggingState;
import org.etlforge.integration.debug.client.SessionRequester;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.event.DebugServiceEvent;
import org.etlforge.integration.debug.client.event.DebugServiceListener;
import org.etlforge.integration.debug.client.event.DebugSessionAdapter;
import org.etlforge.integration.debug.client.event.DebugSessionEvent;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;
import org.etlforge.system.Disposable;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ScriptDebugModel extends AbstractPropertyBasedModel implements Disposable {
   public static final String TEST_PROVIDER_ID = "testProviderId";
   public static final String DEBUG_MESSAGE = "debugMessage";
   public static final String IS_START_ENABLED = "isStartEnabled";
   public static final String DISABLE_BREAKPOINTS = "disableBreakpoints";
   public static final String LINE_NUMBER = "lineNumber";
   private final DebugService debugService;
   private final IntegrationEntityId debuggeeId;
   private DebugSession session;
   private ExpressionArea expressionArea;
   private ExpressionTranslator expressionTranslator;
   private final Property<String> testProviderId;
   private final Property<String> debugMessage;
   private final Property<Boolean> startEnabled;
   private final Property<Boolean> disableBreakpoints;
   private final Property<Integer> lineNumber;
   private final ScriptDataElementsModel dataElementsModel;
   private TargetNode targetNode;
   private ServerTestCallback callback;
   private final ScriptDebugModel.Listener listener;
   private final Object lock;
   private SourceDataElementContainer sourceDeContainer;
   private org.etlforge.integration.data.project.IntegrationProject project;
   public void setProject(org.etlforge.integration.data.project.IntegrationProject value) { this.project = value; }

   public ScriptDebugModel(DebugService var1, IntegrationEntityId var2) {
      this.debugService = var1;
      this.debuggeeId = var2;
      this.dataElementsModel = new ScriptDataElementsModel();
      this.testProviderId = this.newProperty("testProviderId", null);
      this.debugMessage = this.newProperty("debugMessage", "");
      this.callback = new ServerTestCallbackAdapter() {};
      this.startEnabled = this.newProperty("isStartEnabled", Boolean.FALSE);
      this.disableBreakpoints = this.newProperty("disableBreakpoints", Boolean.FALSE);
      this.lineNumber = this.newProperty("lineNumber", -1);
      this.sourceDeContainer = SourceDataElementContainer.EMPTY;
      this.listener = new ScriptDebugModel.Listener();
      this.lock = new Object();
      this.setInitialState();
   }

   @Override
   public void dispose() {
      this.listener.dispose();
   }

   public DebugService getDebugService() {
      return this.debugService;
   }

   public IntegrationEntityId getDebuggeeId() {
      return this.debuggeeId;
   }

   public ScriptDataElementsModel getDataElementsModel() {
      return this.dataElementsModel;
   }

   public boolean areBreakpointsSupported() {
      return this.debugService.areBreakpointsSupported();
   }

   public void setExpressionArea(ExpressionArea var1) {
      Preconditions.checkState(this.expressionArea == null, "The ExpressionArea has already been set");
      Preconditions.checkNotNull(var1, "area");
      this.expressionArea = var1;
      this.listener.install();
   }

   public void setExpressionTranslator(ExpressionTranslator var1) {
      this.expressionTranslator = var1;
   }

   public String getExpressionToTest() {
      Preconditions.checkState(this.expressionArea != null, "The expression area has not been set");
      String var1 = this.expressionArea.getText();
      return this.expressionTranslator != null ? this.expressionTranslator.fromDisplay(var1) : var1;
   }

   public void resetDataElements() {
      this.dataElementsModel.resetDataElements();
   }

   public void setTargetNode(TargetNode var1) {
      if (!Objects.equal(targetNodePath(var1), targetNodePath(this.targetNode))) {
         this.debugMessage.set("");
      }

      this.targetNode = var1;
   }

   private static String targetNodePath(TargetNode var0) {
      return var0 == null ? null : var0.getPath();
   }

   public String getTestProviderId() {
      return this.testProviderId.get();
   }

   public void setStartTestEnabled(boolean var1) {
      this.startEnabled.set(var1);
   }

   public boolean isStartTestEnabled() {
      return this.startEnabled.get();
   }

   public boolean areSourceDataElementValuesEditable() {
      String var1 = this.testProviderId.get();
      return var1 == null || var1 == TestScriptProvider.ID;
   }

   public void setDisableBreakpoints(boolean var1) {
      this.disableBreakpoints.set(var1);
   }

   public boolean getDisableBreakpoints() {
      return this.disableBreakpoints.get();
   }

   public String getDebugMessage() {
      return this.debugMessage.get();
   }

   public void setDebugMessage(String var1) {
      this.debugMessage.set(var1 != null ? var1 : "");
   }

   public void setLineNumber(int var1) {
      this.lineNumber.set(var1);
   }

   public int getLineNumber() {
      return this.lineNumber.get();
   }

   public ServerTestCallback getCallback() {
      return this.callback;
   }

   public void setCallback(ServerTestCallback var1) {
      this.callback = var1 != null ? var1 : new ServerTestCallbackAdapter() {};
   }

   public SourceDataElementContainer getSourceDataElementContainer() {
      return this.sourceDeContainer;
   }

   public void setSourceDataElementContainer(SourceDataElementContainer var1) {
      this.sourceDeContainer = var1;
   }

   public void startNewSession(String var1, DebugController var2, final String var3, final SessionRequester var4) throws DebuggingException {
      this.debugService.startNewDebuggingSession(var1, this.getDebuggeeId(), var2, new SessionRequester() {
         @Override
         public void requestDenied() {
            var4.requestDenied();
         }

         @Override
         public void newSessionStarted(DebugSession var1) {
            var1.setTargetNode(ScriptDebugModel.this.targetNode);
            DebuggingParameters var2x = ScriptDebugModel.this.createDebuggingParametersForSession(var1, var3);
            var1.setParams(var2x);
            ScriptDebugModel.this.setSession(var1);
            var4.newSessionStarted(var1);
         }
      });
   }

   private DebuggingParameters createDebuggingParametersForSession(DebugSession var1, String var2) {
      DebuggingParametersBuilder var3 = new DebuggingParametersBuilder();
      var3.setSessionId(var1.getId())
         .setCommand(var2)
         .setObserveBreakpoints(!this.disableBreakpoints.get())
         .setDataElements(projectDefaults(this.dataElementsModel.getDataElements()));
      return var3.build();
   }

   private java.util.Collection<org.etlforge.integration.debug.client.de.DataElement> projectDefaults(
         java.util.Collection<org.etlforge.integration.debug.client.de.DataElement> existing) {
      java.util.List<org.etlforge.integration.debug.client.de.DataElement> result = new java.util.ArrayList<>(existing);
      if (this.project == null) return result;
      for (org.etlforge.integration.data.entity.ProjectVariable variable : this.project.getAllEntitiesOfClass(org.etlforge.integration.data.entity.ProjectVariable.class)) {
         boolean present = false;
         for (org.etlforge.integration.debug.client.de.DataElement element : existing)
            if (element.getScope() == org.etlforge.integration.debug.client.de.DataElementScope.GLOBAL &&
                element.getName().replaceFirst("^\\$", "").equals(variable.getName()) && element.getValue() != null) present = true;
         if (present) continue;
         result.removeIf(element -> element.getScope() == org.etlforge.integration.debug.client.de.DataElementScope.GLOBAL &&
             element.getName().replaceFirst("^\\$", "").equals(variable.getName()));
         org.etlforge.integration.debug.client.de.DataElement element = new org.etlforge.integration.debug.client.de.DataElement(
             variable.getName(), org.etlforge.integration.debug.client.de.DataElementScope.GLOBAL, "string");
         element.setValue(variable.getDefaultValue()); result.add(element);
      }
      return result;
   }

   private DebugSession getSession() {
      synchronized (this.lock) {
         return this.session;
      }
   }

   private void setSession(DebugSession var1) {
      synchronized (this.lock) {
         this.session = var1;
         this.updateTestProviderId(var1);
      }
   }

   private void updateTestProviderId(DebugSession var1) {
      String var2 = var1 != null ? var1.getTestProviderId() : null;
      this.testProviderId.set(var2);
   }

   private void setInitialState() {
      synchronized (this.debugService) {
         DebugSession var2 = this.debugService.getCurrentSession();
         if (var2 != null) {
            this.setDisableBreakpoints(!var2.getParams().isObservingBreakpoints());
            this.updateTestProviderId(var2);
         }
      }
   }

   private final class Listener implements Disposable {
      private final DocumentListener docListener = new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            Listener.this.updateStartActionState();
         }
      };
      private final DebugServiceListener debugSvcListener;
      private final DebugSessionListener debugSessionListener = new DebugSessionAdapter() {
         @Override
         public void sessionStateChanged(DebugSessionEvent var1) {
            synchronized (Listener.this) {
               Listener.this.debugState = var1.getState();
               Listener.this.update();
            }
         }
      };
      private final PropertyChangeListener deListener;
      private DebuggingState debugState;

      public Listener() {
         this.debugSvcListener = new DebugServiceListener() {
            @Override
            public void newDebugSessionStarted(DebugServiceEvent var1) {
               DebugSession var2 = var1.getSession();
               ScriptDebugModel.this.setSession(var2);
               var2.addListener(Listener.this.debugSessionListener);
               ScriptDebugModel.this.setStartTestEnabled(false);
            }

            @Override
            public void debugSessionEnded(DebugServiceEvent var1) {
               ScriptDebugModel.this.setSession(null);
               ScriptDebugModel.this.setLineNumber(-1);
               ScriptDebugModel.this.dataElementsModel.stopTest();
            }
         };
         this.deListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent var1) {
               this.updateDebuggingParamsDataElements();
            }

            private void updateDebuggingParamsDataElements() {
               DebugSession var1x = ScriptDebugModel.this.getSession();
               if (var1x != null) {
                  DebuggingParametersBuilder var2 = new DebuggingParametersBuilder(var1x.getParams());
                  var2.setDataElements(ScriptDebugModel.this.projectDefaults(ScriptDebugModel.this.dataElementsModel.getDataElements()));
                  var1x.setParams(var2.build());
               }
            }
         };
      }

      public synchronized void install() {
         this.debugState = ScriptDebugModel.this.debugService.getState();
         DebugSession var1 = ScriptDebugModel.this.debugService.getCurrentSession();
         ScriptDebugModel.this.setSession(var1);
         if (var1 != null) {
            var1.addListener(this.debugSessionListener);
         }

         this.update();
         ScriptDebugModel.this.expressionArea.getDocument().addDocumentListener(this.docListener);
         ScriptDebugModel.this.debugService.addListener(this.debugSvcListener);
         ScriptDebugModel.this.dataElementsModel.addPropertyChangeListener("allDataElements", this.deListener);
      }

      @Override
      public void dispose() {
         if (ScriptDebugModel.this.expressionArea != null) {
            ScriptDebugModel.this.expressionArea.getDocument().removeDocumentListener(this.docListener);
         }

         ScriptDebugModel.this.debugService.removeListener(this.debugSvcListener);
         ScriptDebugModel.this.dataElementsModel.removePropertyChangeListener("allDataElements", this.deListener);
      }

      private synchronized void update() {
         this.updateLineNumber();
         this.updateDebugMessage();
         this.updateStartActionState();
         this.updateDataElements();
      }

      private void updateDataElements() {
         DebugSession var1 = ScriptDebugModel.this.getSession();
         if (var1 != null) {
            TestResult var2 = var1.getResult();
            if (var2 != null) {
               ScriptDebugModel.this.dataElementsModel.setDataElementsReturnedFromServer(var2.getDataElements());
            }
         }
      }

      private void updateLineNumber() {
         if (this.debugState != DebuggingState.PAUSED) {
            ScriptDebugModel.this.setLineNumber(-1);
         }
      }

      private void updateDebugMessage() {
         if (this.debugState == DebuggingState.RUNNING) {
            ScriptDebugModel.this.setDebugMessage("");
         }
      }

      private void updateStartActionState() {
         boolean var1 = false;
         if (ScriptDebugModel.this.expressionArea != null) {
            var1 = (this.debugState == DebuggingState.IDLE || this.debugState == DebuggingState.DONE)
               && ScriptDebugModel.this.expressionArea.getDocument().getLength() > 0;
         }

         ScriptDebugModel.this.startEnabled.set(var1);
      }
   }
}
