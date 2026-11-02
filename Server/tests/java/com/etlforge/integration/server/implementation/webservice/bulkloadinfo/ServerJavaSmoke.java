package com.etlforge.integration.server.implementation.webservice.bulkloadinfo;
import java.util.*;
import com.google.common.base.Function;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.ws.WsUtils;
public final class ServerJavaSmoke extends Task<String> {
 private static void checkSoapBean(Class<?> type) throws Exception {
  Set<String> names=new HashSet<>();
  for(java.beans.PropertyDescriptor property:java.beans.Introspector.getBeanInfo(type).getPropertyDescriptors())names.add(property.getName());
  org.apache.axis.description.TypeDesc descriptor=(org.apache.axis.description.TypeDesc)type.getMethod("getTypeDesc").invoke(null);
  for(org.apache.axis.description.FieldDesc field:descriptor.getFields())if(field.getFieldName()!=null && !names.contains(field.getFieldName()))throw new AssertionError(type.getName()+": SOAP field has no JavaBean property: "+field.getFieldName());
 }
 private ServerJavaSmoke(){super("smoke","unused","unused");}
 protected String[] runTask(ExternalApiSupport ignored){return new String[0];}
 public static void main(String[] args) throws Exception {
  if(!"64".equals(System.getProperty("sun.arch.data.model")))throw new AssertionError("x64 JVM required");
  for(String name:Arrays.asList("org.etlforge.integration.server.backend.webservice.json.WsJsonSessionId","org.etlforge.integration.server.backend.webservice.jdbc.WsJdbcSessionId","com.etlforge.integration.server.implementation.webservice.bulkload.WsBulkLoadSessionId","org.etlforge.integration.server.backend.webservice.function.WsFunctionSessionId"))checkSoapBean(Class.forName(name));
  ServerJavaSmoke task=new ServerJavaSmoke();String[] result=new String[3];
  task.populateResponse(result,Arrays.asList(3,1,2),new Function<Integer,String>(){public String apply(Integer n){return "value-"+n;}});
  if(!Arrays.equals(result,new String[]{"value-3","value-1","value-2"}))throw new AssertionError("bulk response ordering/conversion");
  task.populateResponse(new String[0],Collections.<Integer>emptyList(),new Function<Integer,String>(){public String apply(Integer n){throw new AssertionError("empty input");}});
  String error=WsUtils.escapeExceptionMessageAndGetCause(new Exception("outer",new Exception("inner")));
  if(!error.contains("outer")||!error.contains("inner"))throw new AssertionError("SOAP error cause preserved");
  for(String name:Arrays.asList("org.sqlite.JDBC","org.etlforge.integration.server.db.sqlite.SQLiteDriver","org.etlforge.util.persist.sqlite.SQLitePersistor","org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters","org.etlforge.integration.server.engine.connector.util.XmlUtils","org.etlforge.integration.server.backend.webservice.netsuite.ConnectorNetSuiteDesignEngineAdapter")){
   try{Class.forName(name);throw new AssertionError("Excluded class present: "+name);}catch(ClassNotFoundException expected){}
  }
  String origin=Task.class.getProtectionDomain().getCodeSource().getLocation().toString().replace('\\','/');
  if(!origin.contains("/Server/dist/lib/"))throw new AssertionError("Original product fallback: "+origin);
  System.out.println("PASS: x64 JVM, rebuilt server origin, bulk response conversion/order, empty input, SOAP error cause, SQLite/vendor-connector absence and SOAP/JavaBean property contracts");
 }
}
