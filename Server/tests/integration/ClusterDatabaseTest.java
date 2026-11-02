import java.sql.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public final class ClusterDatabaseTest {
 static int checks;static String password,schema="cluster_test_"+UUID.randomUUID().toString().replace("-","");
 static int schedule=-Math.abs(new Random().nextInt(1000000000))-1;
 static void ok(boolean value,String text){if(!value)throw new AssertionError(text);checks++;System.out.println("PASS "+text);}
 static Connection db(String name)throws Exception{return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/etlforge_dev_"+name,"etlforge_dev",password);}
 static void sql(Connection c,String text)throws Exception{try(Statement s=c.createStatement()){s.execute(text);}}
 static long number(Connection c,String text)throws Exception{try(Statement s=c.createStatement();ResultSet r=s.executeQuery(text)){r.next();return r.getLong(1);}}
 static void readPassword()throws Exception{
  for(String line:Files.readAllLines(Paths.get("C:/ETL/Server/private/pgbouncer-dev.pgpass"),StandardCharsets.UTF_8)){
   if(line.startsWith("#"))continue;List<String> parts=new ArrayList<>();StringBuilder b=new StringBuilder();boolean escape=false;
   for(char ch:line.toCharArray()){if(escape){b.append(ch);escape=false;}else if(ch=='\\')escape=true;else if(ch==':'){parts.add(b.toString());b.setLength(0);}else b.append(ch);}parts.add(b.toString());
   if(parts.size()==5&&parts.get(3).equals("etlforge_dev")){password=parts.get(4);return;}
  }throw new AssertionError("No test database credential");
 }
 static void occurrenceTests()throws Exception{
  ExecutorService pool=Executors.newFixedThreadPool(8);CountDownLatch gate=new CountDownLatch(1);List<Future<Integer>> work=new ArrayList<>();
  long start=System.nanoTime();
  for(int worker=0;worker<8;worker++)work.add(pool.submit(()->{gate.await();int won=0;try(Connection c=db("msg")){c.setAutoCommit(false);
   for(int slot=0;slot<100;slot++){
    sql(c,"SELECT pg_advisory_xact_lock(427043,"+schedule+")");
    try(PreparedStatement p=c.prepareStatement("INSERT INTO etlforge_cluster.schedule_occurrence(schedule_id,operation_id,scheduled_local,outcome) VALUES(?,?,timestamp '2035-01-01'+?*interval '1 second','enqueued') ON CONFLICT DO NOTHING RETURNING schedule_id")){
     p.setInt(1,schedule);p.setInt(2,schedule);p.setInt(3,slot);try(ResultSet r=p.executeQuery()){if(r.next()){sql(c,"INSERT INTO "+schema+".dispatch(slot) VALUES("+slot+")");won++;}}
    }c.commit();
   }}return won;}));gate.countDown();int total=0;try{for(Future<Integer> f:work)total+=f.get(60,TimeUnit.SECONDS);}finally{pool.shutdownNow();}
  ok(total==100,"eight concurrent schedulers generate exactly 100 occurrences");
  try(Connection c=db("msg")){
   ok(number(c,"SELECT count(*) FROM "+schema+".dispatch")==100,"one committed queue entry per occurrence");
   c.setAutoCommit(false);sql(c,"INSERT INTO etlforge_cluster.schedule_occurrence VALUES("+schedule+","+schedule+",'2036-01-01','enqueued',now())");sql(c,"INSERT INTO "+schema+".dispatch VALUES(999)");c.rollback();
   ok(number(c,"SELECT count(*) FROM etlforge_cluster.schedule_occurrence WHERE schedule_id="+schedule+" AND scheduled_local='2036-01-01'")==0,"rollback releases occurrence marker");
   ok(number(c,"SELECT count(*) FROM "+schema+".dispatch WHERE slot=999")==0,"rollback removes matching queue entry");c.commit();
  }
  System.out.println("Scheduler contention elapsed_ms="+(System.nanoTime()-start)/1000000);
 }
 static void claims(boolean skip)throws Exception{
  try(Connection c=db("msg")){sql(c,"TRUNCATE "+schema+".queue");sql(c,"INSERT INTO "+schema+".queue SELECT i,0,'' FROM generate_series(1,1000)i");}
  ExecutorService pool=Executors.newFixedThreadPool(8);CountDownLatch gate=new CountDownLatch(1);Set<Integer> committed=ConcurrentHashMap.newKeySet();AtomicInteger retries=new AtomicInteger(),duplicates=new AtomicInteger();List<Future<Integer>> futures=new ArrayList<>();long start=System.nanoTime();
  for(int worker=0;worker<8;worker++){final int node=worker;futures.add(pool.submit(()->{gate.await();int count=0;try(Connection c=db("msg")){c.setTransactionIsolation(skip?Connection.TRANSACTION_READ_COMMITTED:Connection.TRANSACTION_SERIALIZABLE);c.setAutoCommit(false);
   for(;;){List<Integer> batch=new ArrayList<>();try{
    try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT id FROM "+schema+".queue WHERE state=0 ORDER BY id LIMIT 10"+(skip?" FOR UPDATE SKIP LOCKED":""))){while(r.next())batch.add(r.getInt(1));}
    if(batch.isEmpty()){c.commit();if(number(c,"SELECT count(*) FROM "+schema+".queue WHERE state=0")==0){c.commit();break;}c.commit();Thread.sleep(2);continue;}
    for(int id:batch)sql(c,"UPDATE "+schema+".queue SET state=1,owner='worker"+node+"' WHERE id="+id);
    c.commit();for(int id:batch){if(!committed.add(id))duplicates.incrementAndGet();count++;}
   }catch(SQLException e){c.rollback();if(!"40001".equals(e.getSQLState()))throw e;retries.incrementAndGet();Thread.sleep(2);}}
  }return count;}));}
  gate.countDown();int total=0;try{for(Future<Integer> f:futures)total+=f.get(90,TimeUnit.SECONDS);}finally{pool.shutdownNow();}
  long elapsed=(System.nanoTime()-start)/1000000;
  ok(total==1000&&committed.size()==1000&&duplicates.get()==0,(skip?"SKIP LOCKED":"baseline SERIALIZABLE")+" claims 1000 items exactly once");
  try(Connection c=db("msg")){ok(number(c,"SELECT sum(id) FROM "+schema+".queue WHERE state=1")==500500,"all claimed payload IDs preserved");if(skip)ok(number(c,"SELECT count(DISTINCT owner) FROM "+schema+".queue WHERE state=1")>1,"multiple workers receive work");}
  System.out.println((skip?"SKIP LOCKED":"BASELINE")+" elapsed_ms="+elapsed+" serialization_retries="+retries.get());
 }
 static void revisions()throws Exception{
  try(Connection c=db("tran")){c.setAutoCommit(false);
   for(String table:new String[]{"scheduletab","schedulepropertiestab","operationstab","operationpropertiestab"}){
    long before=number(c,"SELECT revision FROM etlforge_cluster.schedule_revision WHERE singleton");
    sql(c,"DELETE FROM public."+table+" WHERE false");
    ok(number(c,"SELECT revision FROM etlforge_cluster.schedule_revision WHERE singleton")==before+1,"shared schedule invalidation trigger: "+table);
    c.rollback();ok(number(c,"SELECT revision FROM etlforge_cluster.schedule_revision WHERE singleton")==before,"rollback preserves schedule revision: "+table);c.commit();
   }
  }
 }
 static void heartbeats()throws Exception{
  String node=UUID.randomUUID().toString(),inc=UUID.randomUUID().toString();
  try(Connection c=db("tran")){c.setAutoCommit(false);sql(c,"SELECT etlforge_cluster.scheduler_heartbeat('"+node+"','test-host','"+inc+"',123)");c.commit();
   ok(number(c,"SELECT count(*) FROM etlforge_cluster.service_heartbeat WHERE node_id='"+node+"'")==1,"scheduler heartbeat registers a node");c.commit();
   sql(c,"SELECT etlforge_cluster.scheduler_heartbeat('"+node+"','test-host','"+inc+"',123)");c.commit();ok(true,"same scheduler incarnation renews heartbeat");
   boolean denied=false;try{sql(c,"SELECT etlforge_cluster.scheduler_heartbeat('"+node+"','test-host','"+UUID.randomUUID()+"',456)");}catch(SQLException expected){denied=true;c.rollback();}ok(denied,"duplicate live scheduler incarnation rejected");
   denied=false;try{sql(c,"SELECT etlforge_cluster.scheduler_heartbeat('"+node+"','other-host','"+inc+"',123)");}catch(SQLException expected){denied=true;c.rollback();}ok(denied,"copied NodeId on another hostname rejected");
   sql(c,"UPDATE etlforge_cluster.service_heartbeat SET heartbeat_at=now()-interval '1 hour' WHERE node_id='"+node+"'");c.commit();sql(c,"SELECT etlforge_cluster.scheduler_heartbeat('"+node+"','test-host','"+UUID.randomUUID()+"',456)");c.commit();ok(true,"stale scheduler registration can be replaced");
  }finally{try(Connection c=db("tran")){sql(c,"DELETE FROM etlforge_cluster.service_heartbeat WHERE node_id='"+node+"'");sql(c,"DELETE FROM etlforge_cluster.node WHERE node_id='"+node+"'");}}
 }
 public static void main(String[] args)throws Exception{
  readPassword();try(Connection c=db("msg")){sql(c,"CREATE SCHEMA "+schema);sql(c,"CREATE TABLE "+schema+".dispatch(slot integer PRIMARY KEY)");sql(c,"CREATE TABLE "+schema+".queue(id integer PRIMARY KEY,state integer,owner text)");}
  try{occurrenceTests();claims(false);claims(true);heartbeats();revisions();System.out.println("Passed "+checks+" multi-server database checks");}
  finally{try(Connection c=db("msg")){sql(c,"DELETE FROM etlforge_cluster.schedule_occurrence WHERE schedule_id="+schedule);sql(c,"DROP SCHEMA "+schema+" CASCADE");}}
 }
}
