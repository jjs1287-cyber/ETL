package etlforge.studio;
import java.util.*;
import java.beans.*;
import javax.swing.JPanel;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.etlforge.collections.*;
import org.etlforge.lang.Arrays2;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.system.*;
import org.etlforge.util.PropertyChangeManager;
import org.etlforge.util.name.*;
import org.etlforge.ui.util.border.TitledToolbar;
import org.etlforge.integration.salesforce.parser.*;
public final class StudioRecoverySmoke {
 private static int count;
 private static void check(boolean ok,String message){++count;if(!ok)throw new AssertionError(message);}
 private enum Id implements EnumWithId<Integer>{ ONE;public Integer getId(){return 1;} }
 private static final class Named implements Nameable {private final String name;Named(String n){name=n;}public String getName(){return name;} }
 public static void main(String[] args) throws Exception {
  boolean rebuilt=Arrays.asList(args).contains("--rebuilt");
  check("64".equals(System.getProperty("sun.arch.data.model")),"64-bit JVM");
  Function<Integer,String> labels=new Function<Integer,String>(){public String apply(Integer n){return "n"+n;}};
  Predicate<String> n2=new Predicate<String>(){public boolean apply(String s){return s.equals("n2");}};
  check(KongaListUtils.transform(Arrays.asList(1,2),labels).equals(Arrays.asList("n1","n2")),"generic transform");
  check(KongaListUtils.transform(Arrays.asList(1,2),labels,n2).equals(Arrays.asList("n2")),"transform/filter");
  check(org.etlforge.collections.Collections2.keep(Arrays.asList("n1","n2"),n2).equals(Arrays.asList("n2")),"generic keep");
  check(KongaListUtils.filter(Arrays.asList("n1","n2"),n2).equals(Arrays.asList("n2")),"generic filter");
  List<String> appended=new ArrayList<>();KongaListUtils.add(appended,"a","b");check(appended.equals(Arrays.asList("a","b")),"generic add");
  String[] array=Arrays2.transform(new Integer[]{1,2},String.class,labels);
  check(array.getClass()==String[].class && Arrays.equals(array,new String[]{"n1","n2"}),"typed array transform");
  check(Arrays.equals(Arrays2.reverse(new String[]{"a","b"}),new String[]{"b","a"}),"typed reverse");
  check(Arrays.equals(Arrays2.toArray(Arrays.asList("a","b"),String.class),new String[]{"a","b"}),"typed array conversion");
  Map<String,Integer> map=new LinkedHashMap<>();map.put("a",1);map.put("b",2);
  check(KongaMaps.findValue(map,new Predicate<String>(){public boolean apply(String s){return s.equals("b");}}).get()==2,"typed map value");
  check(KongaMaps.findKey(map,new Predicate<Integer>(){public boolean apply(Integer n){return n==1;}}).get().equals("a"),"typed map key");
  check(EnumWithIdLookup.lookup(Id.class,1)==Id.ONE && EnumWithIdLookup.lookupOr(2,Id.ONE)==Id.ONE,"enum lookup");
  check(!KongaFunctions.notNull().apply(null) && KongaFunctions.notNull().apply(new Object()),"null function");
  List<PropertyChangeEvent> events=new ArrayList<>();PropertyChangeManager changes=new PropertyChangeManager(new Object());changes.addPropertyChangeListener(events::add);
  changes.firePropertyChange("int",1,2);changes.firePropertyChange("long",3L,4L);changes.firePropertyChange("bool",false,true);changes.firePropertyChange("char",'a','b');
  check(events.size()==4 && events.get(0).getOldValue().equals(1) && events.get(1).getNewValue().equals(4L) && events.get(3).getNewValue().equals('b'),"property overloads");
  check(Nameables.getNames(Arrays.asList(new Named("a"),new Named("b"))).equals(Arrays.asList("a","b")),"named collection");
  check(new TitledToolbar(new JPanel(),"test")!=null,"toolbar constructor");
  check(new CompoundSupportedQueryPolicy(SupportedQueryPolicy.NO_RESTRICTIONS,SupportedQueryPolicy.NO_RESTRICTIONS).isSupported(null),"policy constructor");
  ClassLoader loader=StudioRecoverySmoke.class.getClassLoader();
  check(loader.getResource("Application.Config.properties")!=null && loader.getResource("ApplicationContext.Core.Common.xml")!=null,"packaged configuration");
  Class.forName("org.etlforge.integration.client.main.IntegrationMain",false,loader);
  if(rebuilt){
   for(String removed:new String[]{"org.etlforge.integration.client.ui.interchange.component.start.GettingStartedPage","org.etlforge.application.ui.window.action.OpenGettingStartedPageAction","org.jitterbit.integration.client.main.IntegrationMain"}){
    try{Class.forName(removed,false,loader);throw new AssertionError("Excluded or old product class present: "+removed);}catch(ClassNotFoundException expected){}
   }
   java.io.InputStream context=loader.getResourceAsStream("ApplicationContext.Interchange.Common.xml");
   java.util.Scanner scanner=new java.util.Scanner(context,"UTF-8").useDelimiter("\\A");
   String xml=scanner.hasNext()?scanner.next():"";scanner.close();
   check(!xml.contains("action.ic.GettingStarted") && !xml.contains("openGettingStartedPageAction"),"Getting Started action absent from packaged Spring configuration");
   check(org.etlforge.integration.client.ui.icons.ClientIcons.APPLICATION_32.getIconWidth()==32,"renamed application icon resolves");
   org.etlforge.integration.client.project.XmlStorageSmoke.run();
   check(org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary.DICTIONARY.getName("action.ic.Open")!=null,"menu dictionary initialization order");
   check(!org.etlforge.integration.client.info.ClientInfo.isDataLoader(),"Data Loader disabled");
   try{Class.forName("org.sqlite.JDBC");throw new AssertionError("SQLite driver present");}catch(ClassNotFoundException expected){}
   try{Class.forName("org.etlforge.integration.client.dl.migration.ProjectMigrator");throw new AssertionError("Data Loader migration present");}catch(ClassNotFoundException expected){}
   String origin=Arrays2.class.getProtectionDomain().getCodeSource().getLocation().toString().replace('\\','/');
   check(origin.contains("/Studio/dist/lib/"),"rebuilt product classes only: "+origin);
   try{Class.forName("com.etlforge.integration.client.ui.interchange.core.action.ProfileOperationAction",false,loader);throw new AssertionError("profiling unexpectedly enabled");}catch(ClassNotFoundException expected){}
  }
  System.out.println("PASS: "+count+" checks; x64 runtime, generic collections, arrays, enums, property events, constructors, resources"+(rebuilt?", rebuilt module origin and optional profiling exclusion":""));
 }
}

