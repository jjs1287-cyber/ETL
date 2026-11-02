/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef DATABASE_VIEW_H
#define DATABASE_VIEW_H

#include <string>

#include "DatabaseObject.h"

namespace konga {

class DatabaseView : public DatabaseObject {
public:
	DatabaseView() : DatabaseObject("VIEW") {}
	virtual ~DatabaseView() {}

private:
};

}                   

#endif                   
