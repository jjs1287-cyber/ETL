/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBUTILS_DBROWSETDATA_H
#define KONGA_DBUTILS_DBROWSETDATA_H

#include <string>
#include <vector>

namespace konga {

struct DbData {
	DbData() : isNull(true) {}
	DbData(const std::string& sData) : isNull(false), data(sData) {}

	bool isNull;
	std::string data;
};

struct DbRowData {
	virtual ~DbRowData() {
		clear();
	}
	void clear() {
		for(std::vector<DbData*>::iterator it=row.begin(); it!=row.end(); ++it)
			delete *it;
	}

	void push_back(DbData*p) { row.push_back(p); }
	std::vector<DbData*> row;
};

struct DbRowSetData {
	virtual ~DbRowSetData() {
		clear();
	}
	void clear() {
		for(std::vector<DbRowData*>::iterator it=rowset.begin(); it!=rowset.end(); ++it)
			delete *it;
	}

	void push_back(DbRowData*r) { rowset.push_back(r); }
	std::vector<DbRowData*> rowset;
};

}                   

#endif                                
