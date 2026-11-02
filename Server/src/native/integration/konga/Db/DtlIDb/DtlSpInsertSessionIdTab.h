/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_DTL_SPINSERTSESSIONIDTAB_H
#define KONGA_DB_IDB_DTL_SPINSERTSESSIONIDTAB_H

#include <string>

#include <dtl/DTL.h>
#include <dtl/nullable.h>

#include <konga/Db/DbUtils/DbNullException.h>
#include <konga/Db/IDb/SpInsertSessionIdTab.h>


namespace dtl_idb
{

class DtlSpInsertSessionIdTab : public IDb::SpInsertSessionIdTab
{
public:

    struct SpReturnVals
    {
		                                              
    };
    
    class SpBCA
    {
    public:
        void operator()(dtl::BoundIOs &cols, SpReturnVals &r)
        {
			                                              
        }
    };
    
    struct SpParameters
    {
		SpParameters(void) : SessionId(-1) {}
                                                                                            
                                                                                  
        dtl::nullable<std::string> Note;
		int	SessionId;
    };
    
    class SpBPA
    {
    public:
         void operator()(dtl::BoundIOs &cols, SpParameters &r)
         {
			 cols[0] >> r.SessionId;
             cols[1] << r.Note;
         }
    };

    DtlSpInsertSessionIdTab(dtl::DBConnection* pConn) : m_pConn(pConn) {}
    virtual ~DtlSpInsertSessionIdTab() {}

    void SetNote(const std::string& stNote) {
        m_params.Note = stNote;
    }
    void SetNoteToNull() {
        m_params.Note.SetNull();
    }

    virtual int GetSessionId() const {
        return m_iSessionId;
    }

    void Close() {
                        
    }

	void Execute();

private:
    dtl::DBConnection* m_pConn;

    SpParameters    m_params;
	int	m_iSessionId;
};

}                         


#endif                                           

