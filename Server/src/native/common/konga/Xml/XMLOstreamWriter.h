/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_KONGA_XML_OSTREAM_WRITER_H
#define ETLFORGE_KONGA_XML_OSTREAM_WRITER_H

#include <ostream>
#include <utility>	       
#include <string>
#include <vector>


#include <boost/noncopyable.hpp>

namespace konga {
namespace xml {

	                                           
	                                                                       
	                                    
	class XmlAttributes {
	public:
		XmlAttributes(void);
		~XmlAttributes(void);

		                                                           
		std::string& operator[](const std::string& name);

		typedef std::vector< std::pair<std::string,std::string> > AttrContainerT;
		typedef AttrContainerT::const_iterator const_iterator;

		                                            
		const_iterator begin() const;
		const_iterator end() const;

		bool empty() const;

		void clear();

	private:
		AttrContainerT m_attrs;

	};

	typedef XmlAttributes AttrT;

	void WriteXmlPI(std::ostream& os, const std::string& encoding = "");

	void WriteStartElement(std::ostream& os, int level, const std::string& name, const AttrT& attributes);

	                                    
	void WriteStartElement(std::ostream& os, int level, const std::string& name);

	                                                                      
	class AutoWriteEndElement : boost::noncopyable {
	public:
		AutoWriteEndElement(std::ostream& os, int level, const std::string& name);
		~AutoWriteEndElement(void);

	private:
		std::ostream& m_os;
		const int m_level;
		const std::string m_sName;
	};

	void WriteEmptyElement(std::ostream& os, int level, const std::string& name, const AttrT& attributes);
	void WriteValueElement(std::ostream& os, int level, const std::string& name, const std::string& value, const AttrT& attributes);

}
}

#endif                                       

