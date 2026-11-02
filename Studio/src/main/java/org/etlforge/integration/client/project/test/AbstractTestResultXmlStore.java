package org.etlforge.integration.client.project.test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.test.DetailedTestResultFilter;
import org.etlforge.integration.data.test.PersistTestResultsProperty;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.integration.data.test.TestResultFilter;
import org.etlforge.integration.data.test.TestResultHistory;
import org.etlforge.integration.data.test.TestResultHistoryListener;
import org.etlforge.integration.data.test.TestResultUtils;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.NonFunctioningPersistorStore;
import org.etlforge.util.persist.PersistableFactory;
import org.etlforge.util.persist.PersistorStore;
import org.etlforge.util.persist.XmlPersistorStore;
import org.etlforge.util.time.DateUtils;

abstract class AbstractTestResultXmlStore<T extends TestResult> implements TestResultHistory<T> {
   private final DateFormat dateFolderFormat = new SimpleDateFormat("yyyy-MM-dd");
   private final Folder root;
   private final String type;
   private final CopyOnWriteArrayList<TestResultHistoryListener> listeners;
   private final PersistableFactory<T> factory;
   private final Object lock;

   protected AbstractTestResultXmlStore(Folder var1, String var2, PersistableFactory<T> var3) {
      Preconditions.checkNotNull(var2, "type");
      Preconditions.checkNotNull(var3, "factory");
      this.type = var2;
      this.factory = var3;
      this.lock = new Object();
      this.listeners = new CopyOnWriteArrayList<>();
      this.root = var1.subFolder(var2);
   }

   @Override
   public final void add(T var1) {
      synchronized (this.lock) {
         PersistorStore var3 = this.getStoreForResult((T)var1);
         var3.store(var1, this.type);
      }

      this.notifyListenersOnAdd((T)var1);
   }

   private PersistorStore<T> getStoreForResult(T var1) {
      if (PersistTestResultsProperty.ENABLED) {
         Folder var2 = this.getSubjectFolderForResult((T)var1);
         File var3 = var2.getFile(this.dateFolderFormat.format(new Date(var1.getTimestamp())));
         if (this.ensureDirExists(var3)) {
            return new XmlPersistorStore<>(var3);
         }
      }

      return new NonFunctioningPersistorStore<>();
   }

   private Folder getSubjectFolderForResult(T var1) {
      Object var2 = var1.getSubjectId();
      return this.getSubjectFolder(var2);
   }

   private Folder getSubjectFolder(Object var1) {
      return this.root.subFolder(var1.toString());
   }

   private boolean ensureDirExists(File var1) {
      return var1.exists() ? true : var1.mkdirs();
   }

   protected final void notifyListenersOnAdd(T var1) {
      for (TestResultHistoryListener var3 : this.listeners) {
         var3.onResultAdded(var1);
      }
   }

   @Override
   public final List<T> getStoredResults(TestResultFilter var1) {
      ArrayList var2 = new ArrayList();
      synchronized (this.lock) {
         if (PersistTestResultsProperty.ENABLED) {
            this.getStoredResultsImpl(var1, var2);
         }

         return var2;
      }
   }

   private void getStoredResultsImpl(TestResultFilter var1, List<T> var2) {
      Folder[] var3 = this.getAcceptedSubjectFolders(var1);

      for (Folder var7 : var3) {
         Folder[] var8 = var7.listSubFolders();

         for (Folder var12 : var8) {
            this.getResultsForDate(var12, var2, var1);
         }
      }
   }

   private Folder[] getAcceptedSubjectFolders(TestResultFilter var1) {
      Folder[] var2 = this.root.listSubFolders();
      return var1 instanceof DetailedTestResultFilter ? this.getAcceptedSubjectFolders(var2, (DetailedTestResultFilter)var1) : var2;
   }

   private Folder[] getAcceptedSubjectFolders(Folder[] var1, DetailedTestResultFilter var2) {
      ArrayList var3 = Lists.newArrayList();

      for (Folder var7 : var1) {
         String var8 = var7.getName();
         if (var2.acceptsSubjectId(var8)) {
            var3.add(var7);
         }
      }

      return (Folder[]) var3.toArray(new Folder[var3.size()]);
   }

   private void getResultsForDate(Folder var1, List<T> var2, TestResultFilter var3) {
      if (!(var3 instanceof DetailedTestResultFilter) || this.isValidDate(var1, (DetailedTestResultFilter)var3)) {
         XmlPersistorStore var4 = new XmlPersistorStore(var1.asJavaFile());

         for (Object __etlItem133 : var4.load(this.factory)) {
            T var6 = (T) __etlItem133;
            if (var3 == null || var3.accepts(var6)) {
               var2.add(var6);
            }
         }
      }
   }

   private boolean isValidDate(Folder var1, DetailedTestResultFilter var2) {
      try {
         Date var3 = this.dateFolderFormat.parse(var1.getName());
         return TestResultUtils.isValidDate(var3, var2);
      } catch (ParseException var4) {
         var1.delete();
         return false;
      }
   }

   @Override
   public final void remove(T var1) {
      synchronized (this.lock) {
         PersistorStore var3 = this.getStoreForResult((T)var1);
         var3.remove(var1);
      }

      this.notifyListenersOnRemove((T)var1);
   }

   protected final void notifyListenersOnRemove(T var1) {
      for (TestResultHistoryListener var3 : this.listeners) {
         var3.onResultRemoved(var1);
      }
   }

   @Override
   public final void addTestResultStoreListener(TestResultHistoryListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public final void removeTestResultStoreListener(TestResultHistoryListener var1) {
      this.listeners.remove(var1);
   }

   protected final void deleteAllResultsForSubject(Object var1) {
      boolean var2;
      synchronized (this.lock) {
         Folder var4 = this.getSubjectFolder(var1);
         if (!var4.exists()) {
            return;
         }

         var2 = var4.delete();
      }

      this.processFolderDeletionResult(var1, var2);
   }

   private void processFolderDeletionResult(Object var1, boolean var2) {
      if (var2) {
         this.notifyListenersOnHistoryRebuilt();
      } else {
         String var3 = "Failed to delete test history for the object with ID: " + var1;
         ErrorLog.log(this.getClass(), var3, new Exception(var3));
      }
   }

   private void notifyListenersOnHistoryRebuilt() {
      for (TestResultHistoryListener var2 : this.listeners) {
         var2.onHistoryRebuilt();
      }
   }

   @Override
   public final void setMaximumResultAge(int var1) {
      boolean var2 = this.applyNewAgeLimit(var1);
      if (var2) {
         this.notifyListenersOnHistoryRebuilt();
      }
   }

   private boolean applyNewAgeLimit(int var1) {
      boolean var2 = false;
      synchronized (this.lock) {
         for (Folder var7 : this.root.listSubFolders()) {
            for (Folder var11 : var7.listSubFolders()) {
               if (this.deleteDateFolderIfTooOld(var11, var1)) {
                  var2 = true;
               }
            }

            if (var7.isEmpty()) {
               var7.delete();
            }
         }

         return var2;
      }
   }

   private boolean deleteDateFolderIfTooOld(Folder var1, int var2) {
      return this.isTooOld(var1, var2) ? var1.delete() : false;
   }

   private boolean isTooOld(Folder var1, int var2) {
      if (var2 <= 0) {
         return true;
      }

      try {
         Date var3 = this.dateFolderFormat.parse(var1.getName());
         return DateUtils.getDaysAgo(var3) > var2;
      } catch (ParseException var4) {
         var4.printStackTrace();
         return true;
      }
   }
}
