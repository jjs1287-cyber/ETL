/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <list>
#include <string>

#include "DatabaseObject.h"

using namespace konga;
using namespace std;

DatabaseObject::~DatabaseObject() {
	for(vector<DatabaseColumn*>::iterator itCol=m_columns.begin(); itCol!=m_columns.end(); ++itCol)
		delete *itCol;
}
