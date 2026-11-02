#include "AuditDatabase.h"
#include "AuditObjectReader.h"
#include <fstream>
#include <iostream>
#include <sstream>
#include <stdexcept>
#include <future>
#include <chrono>

using namespace etlforge::audit;
int checks = 0;
void require(bool ok,const char* message) {++checks;if(!ok)throw std::runtime_error(message);}
struct Connection {
    SQLHENV environment = SQL_NULL_HENV;
    SQLHDBC handle = SQL_NULL_HDBC;
    Connection() {
        if(!SQL_SUCCEEDED(SQLAllocHandle(SQL_HANDLE_ENV,SQL_NULL_HANDLE,&environment)) ||
            !SQL_SUCCEEDED(SQLSetEnvAttr(environment,SQL_ATTR_ODBC_VERSION,reinterpret_cast<void*>(SQL_OV_ODBC3),0)) ||
            !SQL_SUCCEEDED(SQLAllocHandle(SQL_HANDLE_DBC,environment,&handle))) throw std::runtime_error("ODBC initialization failed");
        const char* connection = std::getenv("ETLFORGE_AUDIT_TEST_CONNECTION");
        if(!connection) throw std::runtime_error("Audit test connection not supplied");
        if(!SQL_SUCCEEDED(SQLDriverConnectA(handle,nullptr,reinterpret_cast<SQLCHAR*>(const_cast<char*>(connection)),SQL_NTS,nullptr,0,nullptr,SQL_DRIVER_NOPROMPT)))
            throw std::runtime_error("Audit test database connection failed");
        if(!SQL_SUCCEEDED(SQLSetConnectAttr(handle,SQL_ATTR_AUTOCOMMIT,reinterpret_cast<void*>(SQL_AUTOCOMMIT_OFF),0))) throw std::runtime_error("Cannot begin audit test transaction");
    }
    void commit() {if(!SQL_SUCCEEDED(SQLEndTran(SQL_HANDLE_DBC,handle,SQL_COMMIT)))throw std::runtime_error("Test commit failed");}
    void rollback() {SQLEndTran(SQL_HANDLE_DBC,handle,SQL_ROLLBACK);}
    ~Connection() {if(handle){rollback();SQLDisconnect(handle);SQLFreeHandle(SQL_HANDLE_DBC,handle);}if(environment)SQLFreeHandle(SQL_HANDLE_ENV,environment);}
};
int main() {
    std::string schema = "audit_validation_" + std::to_string(GetCurrentProcessId()) + "_" + std::to_string(GetTickCount64());
    try {
        Connection connection;
        Database db(connection.handle,schema);
        std::ifstream input("C:/ETL/Server/database/migrations/005-audit-log.sql");
        std::string migration((std::istreambuf_iterator<char>(input)),{});
        if(migration.empty())throw std::runtime_error("Migration missing");
        std::istringstream lines(migration);
        std::string line,statement;
        while(std::getline(lines,line)) {
            if(line=="BEGIN;" || line=="COMMIT;")continue;
            std::size_t pos=0;
            while((pos=line.find("audit",pos))!=std::string::npos){line.replace(pos,5,schema);pos+=schema.size();}
            statement+=line+'\n';
            if(line.find(';')!=std::string::npos) {
                try {db.query(statement);}catch(const std::exception& e){throw std::runtime_error(std::string(e.what())+"; migration statement starts: "+statement.substr(0,85));}
                statement.clear();
            }
        }
        connection.commit();
        const std::string unicode=u8"café 世界 😀\n  exact whitespace  ";
        require(*db.query("SELECT CAST(? AS text)",{unicode})[0][0]==unicode,"Unicode audit parameter/result changed");
        require(*db.query(u8"SELECT 'café 世界 😀'::text")[0][0]==u8"café 世界 😀","Unicode SQL statement changed");
        const std::string large=std::string(4094,'A')+u8"😀 世界"+std::string(20000,'Z');
        require(*db.query("SELECT CAST(? AS text)",{large})[0][0]==large,"Chunked Unicode audit result changed");
        bool invalidRejected=false;
        try {db.query("SELECT CAST(? AS text)",{std::string("\xC0\xAF",2)});} catch(const std::runtime_error&) {invalidRejected=true;}
        require(invalidRejected,"Malformed UTF-8 audit parameter accepted");
        Store store(db);
        Deployment deployment{"aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa","bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb","Audit validation","Admin",1};
        std::vector<unsigned char> key(32,0x51);
        Rules rules;
        auto make = [&](const char* directory) {
            Object item{2,"cccccccc-cccc-4ccc-8ccc-cccccccccccc","Source","Source",{}};
            item.snapshot=prepareSnapshot(boost::json::object{{"name","Source"},{"directory",directory},{"password","test-only-secret"}},rules,key,"test-1");
            return Objects{{{item.type,item.guid},item}};
        };
        Objects first=make("C:/temp"),changed=make("C:/temp2");
        store.lockProject(deployment.projectGuid);
        require(store.record(deployment,first,first)==0,"initial baseline generated false events");
        require(*db.query("SELECT count(*)::text FROM audit.event")[0][0]=="0","baseline events persisted");
        connection.commit();
        bool staleLock=false;
        try{store.record(deployment,first,first);}catch(const std::runtime_error&){staleLock=true;}
        require(staleLock,"project lock from committed transaction was reused");
        connection.rollback();
        store.lockProject(deployment.projectGuid);
        require(store.record(deployment,first,first)==0,"unchanged redeploy created event");
        require(store.record(deployment,first,changed)==1,"update event missing");
        auto details=db.query("SELECT previous_value::text,current_value::text,changed_paths::text FROM audit.event_detail");
        require(details.size()==1 && details[0][0]->find("test-only-secret")==std::string::npos && details[0][1]->find("[REDACTED]")!=std::string::npos,"secret exposed in audit details");
        require(*details[0][2]=="[\"/directory\"]","wrong diff persisted");
        connection.rollback();
        require(*db.query("SELECT count(*)::text FROM audit.event")[0][0]=="0","failed deploy left audit entries");
        require(*db.query("SELECT visible_snapshot->>'directory' FROM audit.object_state")[0][0]=="C:/temp","failed deploy changed baseline");
        connection.commit();
        store.lockProject(deployment.projectGuid);
        require(store.record(deployment,first,changed)==1,"committed update missing");
        connection.commit();
        Filter filter;
        auto authorized=std::vector<std::string>{deployment.projectGuid};
        auto page=store.list(authorized,filter);
        require(page.at("entries").as_array().size()==1,"authorized history list missing entry");
        require(store.list({},filter).at("entries").as_array().empty(),"empty permission scope exposed history");
        require(store.list({"ffffffff-ffff-4fff-8fff-ffffffffffff"},filter).at("entries").as_array().empty(),"cross-project history exposed");
        filter.search=" \t\r\n";
        require(store.list(authorized,filter).at("entries").as_array().size()==1,"blank name must return project history");
        require(store.list({"ffffffff-ffff-4fff-8fff-ffffffffffff"},filter).at("entries").as_array().empty(),"blank name escaped project scope");
        filter.search.clear();
        auto entryId=page.at("entries").as_array()[0].at("id").as_int64();
        auto detail=store.detail(authorized,entryId);
        require(detail.at("detailAvailable").as_bool() && canonicalJson(detail).find("$secretHmac")==std::string::npos,"detail API exposed protected state");
        bool denied=false;try{store.detail({},entryId);}catch(const std::runtime_error&){denied=true;}
        require(denied,"detail API ignored permission scope");
        filter.search="' OR 1=1 --";
        require(store.list(authorized,filter).at("entries").as_array().empty(),"search SQL injection");
        filter.search="source";
        require(store.list(authorized,filter).at("entries").as_array().size()==1,"case-insensitive search failed");
        connection.commit();
        store.lockProject(deployment.projectGuid);
        require(store.record(deployment,changed,changed)==0,"second unchanged redeploy created event");
        connection.commit();
        auto invalid=db.query("SELECT count(*)::text FROM audit.settings_event");
        store.saveSettings(1,"Admin",30,365,false);
        require(*db.query("SELECT count(*)::text FROM audit.settings_event")[0][0]==*invalid[0][0],"settings resave creates false event");
        bool rejected=false;
        try{store.saveSettings(1,"Admin",365,90,true);}catch(const std::runtime_error&){rejected=true;}
        require(rejected,"invalid retention accepted");
        store.saveSettings(1,"Admin",1,3,true);
        require(*db.query("SELECT count(*)::text FROM audit.settings_event")[0][0]=="1","settings change not audited");
        db.query("UPDATE audit.event SET deployed_at=now()-interval '2 days'");
        require(store.cleanup(1)==1,"detail retention failed");
        require(*db.query("SELECT count(*)::text FROM audit.event")[0][0]=="1","header removed before retention");
        require(!store.detail(authorized,entryId).at("detailAvailable").as_bool(),"expired details were not marked expired");
        require(*db.query("SELECT count(*)::text FROM audit.object_state")[0][0]=="1","retention removed comparison baseline");
        db.query("UPDATE audit.event SET deployed_at=now()-interval '4 days'");
        require(store.cleanup(1)==1,"header retention failed");
        require(*db.query("SELECT count(*)::text FROM audit.event_detail")[0][0]=="0","orphaned details");
        connection.commit();
        deployment.id="dddddddd-dddd-4ddd-8ddd-dddddddddddd";
        store.lockProject(deployment.projectGuid);
        require(store.record(deployment,changed,changed)==0,"retention expiry caused false redeploy event");
        require(store.record(deployment,changed,{})==1,"delete event missing");
        connection.commit();
        deployment.id="eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee";
        store.lockProject(deployment.projectGuid);
        require(store.record(deployment,{},first)==1,"recreate event missing");
        require(*db.query("SELECT is_deleted::text FROM audit.object_state")[0][0]=="false","recreate left tombstone");
        connection.commit();
        store.lockProject(deployment.projectGuid);
        auto concurrent = std::async(std::launch::async,[&] {
            Connection other;Database otherDb(other.handle,schema);
            otherDb.query("SET lock_timeout='200ms'");
            try{Store(otherDb).lockProject(deployment.projectGuid);return false;}catch(const std::runtime_error&){return true;}
        });
        require(concurrent.get(),"concurrent project deployment was not serialized");
        connection.rollback();
        auto projectRow=db.query("SELECT guid,item_id::text FROM guidsforinterchangeprojectstab ORDER BY item_id LIMIT 1");
        require(!projectRow.empty(),"existing project required for isolated snapshot reader test");
        std::string projectGuid=*projectRow[0][0],projectId=*projectRow[0][1];
        db.query("CREATE TABLE audit.reader_root(id integer PRIMARY KEY,name text,directory text,modtime text,reference_id integer)");
        db.query("CREATE TABLE audit.reader_guid(item_id integer,guid text)");
        db.query("CREATE TABLE audit.reader_member(interchangeproject_id integer,item_id integer)");
        db.query("CREATE TABLE audit.reader_child(id integer,owner_id integer,keyname text,keyvalue text,modtime text)");
        db.query("INSERT INTO audit.reader_root VALUES(1,'Reader Source','C:/temp','old',1)");
        db.query("INSERT INTO audit.reader_guid VALUES(1,'cccccccc-cccc-4ccc-8ccc-cccccccccccc')");
        db.query("INSERT INTO audit.reader_member VALUES(CAST(? AS integer),1)",{projectId});
        db.query("INSERT INTO audit.reader_child VALUES(10,1,'client_secret','fixture-secret','old'),(11,1,'format','xml','old')");
        EntityPlan plan;
        plan.type=2;plan.typeName="Source";plan.nameColumn="name";plan.guidTable="audit.reader_guid";plan.membershipTable="audit.reader_member";
        plan.root.table="audit.reader_root";plan.root.primaryKey="id";
        plan.root.references.push_back({"reference_id","audit.reader_guid"});
        TablePlan child;child.table="audit.reader_child";child.primaryKey="id";child.ownerColumn="owner_id";plan.children.push_back(child);
        ObjectReader reader(db,{plan},rules,key,"test-1");
        auto snapshot1=reader.readProject(projectGuid);
        require(snapshot1.size()==1,"complete object reader failed");
        auto snap1=snapshot1.begin()->second.snapshot;
        require(canonicalJson(snap1.visible).find("fixture-secret")==std::string::npos,"reader exposed property secret");
        db.query("UPDATE audit.reader_root SET id=2,reference_id=2,modtime='new'");
        db.query("UPDATE audit.reader_guid SET item_id=2");
        db.query("UPDATE audit.reader_member SET item_id=2");
        db.query("DELETE FROM audit.reader_child");
        db.query("INSERT INTO audit.reader_child VALUES(101,2,'format','xml','new'),(102,2,'client_secret','fixture-secret','new')");
        auto snapshot2=reader.readProject(projectGuid);
        require(!compareSnapshots(snap1,snapshot2.begin()->second.snapshot),"recreated child rows or generated IDs caused false object change");
        db.query("UPDATE audit.reader_child SET keyvalue='json' WHERE keyname='format'");
        auto snapshot3=reader.readProject(projectGuid);
        require(compareSnapshots(snapshot2.begin()->second.snapshot,snapshot3.begin()->second.snapshot).has_value(),"nested child value edit was missed");
        plan.requiresFileSnapshot=true;
        bool fileRejected=false;
        try{ObjectReader(db,{plan},rules,key,"test-1").readProject(projectGuid);}catch(const std::runtime_error&){fileRejected=true;}
        require(fileRejected,"file-backed object was silently audited without content");
        auto withFiles=ObjectReader(db,{plan},rules,key,"test-1",[](const EntityPlan&,const boost::json::object&){return boost::json::value("file content");}).readProject(projectGuid);
        require(withFiles.begin()->second.snapshot.visible.at("files").as_string()=="file content","file-content snapshot adapter missing");
        connection.rollback();
        auto realProject=db.query("SELECT g.guid FROM guidsforinterchangeprojectstab g JOIN interchangeprojectsourcestab p ON p.interchangeproject_id=g.item_id GROUP BY g.guid ORDER BY count(*) DESC LIMIT 1");
        if(!realProject.empty()) {
            std::vector<EntityPlan> realPlans;
            struct Definition {int type;const char* label;const char* stem;const char* plural;};
            for(const auto& definition:std::vector<Definition>{{2,"Source","source","sources"},{3,"Target","target","targets"},{23,"Script","script","scripts"},{34,"XSLT","xslttransform","xslttransforms"}}) {
                EntityPlan current;current.type=definition.type;current.typeName=definition.label;
                current.nameColumn=std::string(definition.stem)+"_name";
                current.guidTable=std::string("guidsfor")+definition.plural+"tab";
                current.membershipTable=std::string("interchangeproject")+definition.plural+"tab";
                current.root.table=std::string(definition.stem)+"stab";
                current.root.primaryKey=std::string(definition.stem)+"_id";
                TablePlan properties;properties.table=std::string(definition.stem)+"propertiestab";
                properties.primaryKey=std::string(definition.stem)+"properties_id";
                properties.ownerColumn=current.root.primaryKey;current.children.push_back(properties);
                realPlans.push_back(current);
            }
            ObjectReader realReader(db,realPlans,rules,key,"test-1");
            auto realBefore=realReader.readProject(*realProject[0][0]),realAfter=realReader.readProject(*realProject[0][0]);
            require(!realBefore.empty(),"existing project snapshot was empty");
            require(realBefore.size()==realAfter.size(),"read-only live object snapshot count changed");
            for(const auto& item:realBefore)require(!compareSnapshots(item.second.snapshot,realAfter.at(item.first).snapshot),"unchanged existing object snapshot differed");
            std::cout<<"Read-only existing project check: "<<realBefore.size()<<" source/target/script/XSLT object snapshots stable; object values not printed\n";
        }
        connection.rollback();
        db.query("DROP SCHEMA " + schema + " CASCADE");
        connection.commit();
        std::cout << "PASS " << checks << " PostgreSQL audit transaction/retention checks; isolated schema removed\n";
        return 0;
    } catch(const std::exception& error) {
        std::cerr<<"FAIL "<<error.what()<<"; isolated schema (if created): "<<schema<<'\n';
        return 1;
    }
}
