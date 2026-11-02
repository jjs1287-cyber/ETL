/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_ENGINE_OPERATION_HISTORY_WRITER
#define KONGA_OPERATION_ENGINE_OPERATION_HISTORY_WRITER

namespace konga {

	class ProcessedFiles;
	class CrDateTime;
	class DbConnection;

	                     

	   
                                             
                                                                      
                                               
   
                                                                        
                              
    
	class OperationHistoryWriter {
	public:
		OperationHistoryWriter();
		~OperationHistoryWriter(void);

		                                                        
		void Write(long lOperationId, ProcessedFiles& files, DbConnection* pDbConn);

	private:
		CrDateTime* m_pStartTimeUtc;
	};
}

#endif                                                   

