/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef DB_RECORDSET_H
#define DB_RECORDSET_H

#include <string>
#include <vector>

namespace konga
{

class DbRecordSet {

  
                         
                            
                                          
                                          
                                          
                                          
                                                     
   
   

public:
	DbRecordSet() {}
	virtual ~DbRecordSet() {}

	virtual void Select(const std::string& stWhereClause = "") = 0;

    virtual void AddNew() = 0;

                                         
	virtual int Delete() = 0;

                                          
	virtual int Update() = 0;

    virtual void Edit() = 0;

	virtual bool IsEOF() = 0;

	virtual bool Next() = 0;

	virtual void Close() = 0;

	virtual std::string GetLastError() const = 0;
	virtual long GetLastErrorNumber() const = 0;

    virtual std::string GetTableName() const = 0;

    virtual void SetWhereClause(const std::string& stWhereClause) = 0;
    virtual std::string GetWhereClause() const = 0;

                                                                                                            
                                                              
    virtual std::string GetUpdateFilter() = 0;

};

}                       

#endif
