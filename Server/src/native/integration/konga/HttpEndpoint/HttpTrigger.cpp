/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>		            

#include "HttpTrigger.h"

using namespace konga;
using std::string;

konga::HttpTriggerType konga::getHttpTriggerType(int itype) {
	switch(itype) {
		case 1:
			return HttpTriggerType_HostedWebService;
		case 2:
			return HttpTriggerType_HttpEndpoint;
		default:
			return HttpTriggerType_Unknown;
	};
}

int konga::toIntegerValue(const HttpTriggerType type) {
	return static_cast<int>(type);
}

string konga::getHttpTriggerTypeName(HttpTriggerType type) {
	switch(type) {
		case HttpTriggerType_HostedWebService:
			return string("Hosted Web Service");
		case HttpTriggerType_HttpEndpoint:
			return string("HTTP Endpoint");
		default:
			return string("Unknown");
	};
}

bool konga::operator==(const konga::HttpTrigger& lhs, const konga::HttpTrigger& rhs) {
	return lhs.Type == rhs.Type && lhs.ItemId == rhs.ItemId;
}

bool konga::operator!=(const konga::HttpTrigger& lhs, const konga::HttpTrigger& rhs) {
	return !(lhs==rhs);
}

konga::HttpTrigger konga::selectHttpTrigger(const std::string& url, DbConnection& dbConn) {
	HttpTrigger retVal;

	string sql("SELECT url, httptriggertype, item_id FROM httptriggerstab WHERE Url = '" + SqlEscape(url) + "'");
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
	rs->Open();
	if ( !rs->Next() )
		return retVal;

	retVal.Url = rs->GetFieldValue(0);
	int itype = atoi(rs->GetFieldValue(1).c_str());
	retVal.Type = getHttpTriggerType(itype);
	retVal.ItemId = atol(rs->GetFieldValue(2).c_str());

	return retVal;
}

void konga::addHttpTrigger(const HttpTrigger& trigger, DbConnection& dbConn) {
	const string& url = trigger.Url;
	const int iTriggerType = toIntegerValue(trigger.Type);
	const long lItemId = trigger.ItemId;
	string sql("SELECT httptrigger_id, url FROM httptriggerstab WHERE httptriggertype = "
		+ toString(iTriggerType)
		+ " AND item_id = " + toString(lItemId));

	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
	rs->Open();

	if ( rs->Next() ) {
		                               
		const long httptrigger_id = atol(rs->GetFieldValue(0).c_str());
		const string& oldUrl = rs->GetFieldValue(1);
		if ( oldUrl != url ) {
		sql = "UPDATE httptriggerstab SET url = '" + SqlEscape(url)
			+ "' WHERE httptrigger_id = " + toString(httptrigger_id);
		}
		else {
			sql.clear();
		}
	}
	else {
		                        
		sql = "INSERT INTO httptriggerstab (url, httptriggertype, item_id) VALUES ('"
			+ url + "'," + toString(iTriggerType) + "," + toString(lItemId) + ")";
	}

	if ( !sql.empty() ) {
		dbConn.ExecuteSql(sql);
	}
}

void konga::removeHttpTrigger(const HttpTrigger& trigger, DbConnection& dbConn) {
	const string sql("DELETE FROM httptriggerstab WHERE httptriggertype = " + toString(toIntegerValue(trigger.Type))
		+ " AND item_id = " + toString(trigger.ItemId));
	dbConn.ExecuteSql(sql);
}
