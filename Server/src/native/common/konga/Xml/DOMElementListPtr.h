/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DOM_ELEMENT_LIST_PTR_H
#define KONGA_DOM_ELEMENT_LIST_PTR_H

#include <vector>

#include <boost/shared_ptr.hpp>

#include "DOMNodeListPtr.h"
#include "DOMElementPtr.h"

namespace konga {
	namespace xml {

		   
                                               
     
		class DOMElementListPtr {
		public:
			                                                                         
			DOMElementListPtr(const DOMNodeListPtr& nodeList) : item(nodeList) { }
			~DOMElementListPtr(void) {}

			unsigned long getLength() const {
				return item.size();
			}

			class ListItem {
			private:
				ListItem(const DOMNodeListPtr& nodeList);
				~ListItem(void);

			public:
				DOMElementPtr operator[](unsigned long index) const;

			private:
				unsigned long size() const;
				friend class DOMElementListPtr;	                                                                   
				boost::shared_ptr< std::vector<DOMElementPtr> > m_pList;
			};

			ListItem item;
		};
	}
}
#endif                                

