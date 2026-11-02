/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_PARSESRCSTRINGTABLE_H
#define ETLFORGE_PARSESRCSTRINGTABLE_H

#include <map>
#include <string>

namespace konga {
    class RCStringTable
    {
    public:
        RCStringTable(void);
        ~RCStringTable(void);

        bool Parse(const std::string& stRCFileName, std::map<int, std::string>& mapStringTable);
        std::string getErrorString(){return m_stErr;}
    
	private:
        char*skipws(char*buf);
        char*fgets_(){
            m_lineNumber++;
            char*s=fgets(line,4096,m_in);
            return s;
        }

		void close_file() {
			if ( m_in ) {
				fclose(m_in);
				m_in = 0;
			}
		}

	private:
        FILE* m_in;
        int m_lineNumber;
        char line[4097];                                           
        std::string m_stErr;
    };
}

#endif                                  
