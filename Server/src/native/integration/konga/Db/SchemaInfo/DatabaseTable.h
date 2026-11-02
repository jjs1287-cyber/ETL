/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef DATABASE_TABLE_H
#define DATABASE_TABLE_H

#include <string>

#include "DatabaseObject.h"

namespace konga {

class DatabaseTable : public DatabaseObject {
public:
	DatabaseTable() : DatabaseObject("TABLE") {}
	virtual ~DatabaseTable() {}

private:
};

}                   

#endif                    
