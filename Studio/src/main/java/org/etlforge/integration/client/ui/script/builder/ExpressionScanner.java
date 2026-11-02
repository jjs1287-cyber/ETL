package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.debug.ScriptDebugModel;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.data.script.de.DataElementReference;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementFactory;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public final class ExpressionScanner {
   private static final boolean PUBLISH_LOCAL_DATA_ELEMENTS = false;
   private final ExpressionSource source;
   private final ExpressionParser parser;
   private final List<Receiver<ExpressionParser.Result>> resultReceivers = Lists.newArrayList();
   private DataElementFactory dataElementFactory = DataElementFactory.TRIVIAL;
   private final ExpressionScanner.Publisher publisher;
   private ExpressionScanner.Worker worker;
   private int delay = 750;
   private Document document;
   private volatile long scanVersion;
   private static final java.util.concurrent.ExecutorService JAVASCRIPT_VALIDATION=java.util.concurrent.Executors.newSingleThreadExecutor(task->{Thread thread=new Thread(task,"ETLForge-JavaScript-validation");thread.setDaemon(true);return thread;});

   public ExpressionScanner(ExpressionSource var1, GlobalDataElements var2, ExpressionParser var3) {
      this.source = Preconditions.checkNotNull(var1, "source");
      this.parser = Preconditions.checkNotNull(var3, "parser");
      this.publisher = new ExpressionScanner.Publisher(Preconditions.checkNotNull(var2, "globalDataElementStore"));
      this.resultReceivers.add(this.publisher);
   }

   public void setScriptBuilderModel(ScriptBuilderModel var1) {
      this.publisher.setScriptBuilderModel(var1);
   }

   public void setDebugModel(ScriptDebugModel var1) {
      this.publisher.setDebugModel(var1);
   }

   public void setDataElementFactory(DataElementFactory var1) {
      this.dataElementFactory = Preconditions.checkNotNull(var1, "f");
   }

   public void install(JTextComponent var1) {
      if (this.worker == null) {
         this.worker = new ExpressionScanner.Worker();
         this.document = var1.getDocument();
         this.document.addDocumentListener(this.worker);
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               ExpressionScanner.this.doScan();
            }
         });
      }
   }

   public void uninstall() {
      this.scanVersion++;
      if (this.document != null) {
         this.document.removeDocumentListener(this.worker);
         this.document = null;
         this.worker = null;
      }
   }

   public void setDelay(int var1) {
      this.delay = var1;
   }

   public void doScan() {
      final Document current=this.document;if(current==null)return;
      final String expression=this.getExpression();final long version=++this.scanVersion;
      JAVASCRIPT_VALIDATION.execute(()->{
         if(version!=this.scanVersion)return;
         final ExpressionParser.Result result=this.parser.parse(expression);
         UiUtils.runOnEventThread(()->{if(this.document==current&&version==this.scanVersion&&expression.equals(this.getExpression()))for(Receiver receiver:this.resultReceivers)receiver.handle(result);});
      });
   }

   private String getExpression() {
      try {
         return this.document.getText(0, this.document.getLength());
      } catch (BadLocationException var2) {
         return "";
      }
   }

   public void addResultListener(Receiver<ExpressionParser.Result> var1) {
      this.resultReceivers.add(Preconditions.checkNotNull(var1, "lst"));
   }

   public void removeResultListener(Receiver<ExpressionParser.Result> var1) {
      this.resultReceivers.remove(Preconditions.checkNotNull(var1, "lst"));
   }

   private final class Publisher implements Receiver<ExpressionParser.Result> {
      private ScriptBuilderModel scriptBuilderModel;
      private ScriptDebugModel debugModel;
      private final GlobalDataElements globalDataElementStore;
      private List<DataElement> dataElements;

      public Publisher(GlobalDataElements var2) {
         this.globalDataElementStore = var2;
      }

      public void setScriptBuilderModel(ScriptBuilderModel var1) {
         this.scriptBuilderModel = var1;
      }

      public void setDebugModel(ScriptDebugModel var1) {
         this.debugModel = var1;
      }

      public void handle(ExpressionParser.Result var1) {
         if (this.scriptBuilderModel != null) {
            this.scriptBuilderModel.setStatus(var1.getStatus());
         }

         this.dataElements = Lists.newArrayList();
         this.processGlobalDataElements(var1);
         this.processSourceDataElements(var1);
         if (this.debugModel != null) {
            this.debugModel.getDataElementsModel().setDataElementsScannedFromScript(this.dataElements);
         }
      }

      private void processGlobalDataElements(ExpressionParser.Result var1) {
         ImmutableSet var2 = var1.getGlobalDataElementReferences();
         if (var2 != null) {
            this.updateProjectGlobalDEStore(var2);
            this.updateScriptModelWithGlobalDataElements(var2);
         }
      }

      private void updateProjectGlobalDEStore(Set<DataElementReference> var1) {
         this.globalDataElementStore.updateReferences(ExpressionScanner.this.source, var1);
      }

      private void updateScriptModelWithGlobalDataElements(Set<DataElementReference> var1) {
         for (DataElementReference var3 : var1) {
            DataElement var4 = ExpressionScanner.this.dataElementFactory.create(var3.getDataElement(), DataElementScope.GLOBAL);
            if (var4 != null) {
               this.dataElements.add(var4);
            }
         }
      }

      private void processSourceDataElements(ExpressionParser.Result var1) {
         ImmutableSet var2 = var1.getSourceDataElements();
         if (var2 != null) {
            this.processNonGlobalDataElements(DataElementScope.SOURCE, var2);
         }
      }

      private void processNonGlobalDataElements(DataElementScope var1, Iterable<String> var2) {
         for (String var4 : var2) {
            DataElement var5 = ExpressionScanner.this.dataElementFactory.create(var4, var1);
            if (var5 != null) {
               this.dataElements.add(var5);
            }
         }
      }

      private void processLocalDataElements(ExpressionParser.Result var1) {
         ImmutableSet var2 = var1.getLocalDataElements();
         if (var2 != null) {
            this.processNonGlobalDataElements(DataElementScope.LOCAL, var2);
         }
      }
   }

   private final class Worker extends DocumentContentChangeListener implements Runnable {
      private DelayedJob timer;

      private Worker() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         ExpressionScanner.this.scanVersion++;
         this.scheduleNewScan();
      }

      private void scheduleNewScan() {
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = DelayedJob.createAndSchedule(ExpressionScanner.this.delay, this);
      }

      @Override
      public void run() {
         ExpressionScanner.this.doScan();
         this.timer = null;
      }
   }
}
