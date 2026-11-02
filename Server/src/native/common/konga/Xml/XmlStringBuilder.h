/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_KONGA_XMLSTRINGBUILDER_H
#define ETLFORGE_KONGA_XMLSTRINGBUILDER_H

#include <string>
#include <stack>
#include <map>
#include <vector>
#include <ostream>
#include <boost/noncopyable.hpp>

namespace konga {
	namespace xml {

		  
                                                                           
                                                                
    
                    
    
                              
                                  
                                             
                                    
                                                 
                                                                   
                            
                                               
                                                                 
                                
                     
                         
    
                                                                     
     
                                           
                            
                                                                                   
                                                                               
              
    
     
		class XmlStringBuilder : boost::noncopyable {

		public:
			XmlStringBuilder() : 
			  m_processingInstruction("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
			  m_root(0) 
			  {}
			virtual ~XmlStringBuilder();

			  
                                                                          
                                                                          
      
			void SetProcessingInstruction(const std::string& pi) {
				m_processingInstruction = pi;
			}

			  
                                                                                
                                                                             
                                                      
     
                                                                         
                                                                          
                                   
      
                                                                             
                               
      
      
			void AddElement(const std::string& name);

			  
                                                                           
                                                  
      
                                                                    
      
			void SetElementValue(const std::string& value);

			  
                                                                                  
                                                                              
                                                                             
                                                 
     
                                                                               
                               
     
                                                                        
                   
      
			void CloseElement();

			  
                                                                     
     
                                                                         
                    
      
			void CloseAllElements();

			  
                                                                           
                                                                         
                                       
      
                                                                               
      
			void AddAttribute(const std::string& name, const std::string& value);

			  
                                                                         
                                                                          
                                                                         
                             
      
                                                                         
                                                                           
                
      
			void PrintXml(std::ostream& out) const;

			  
                                                                          
                                                               
      
                                                                         
                                                                           
                
      
			void GetXml(std::string& xml) const;

		private:

			typedef std::map<std::string, std::string> Attributes;
			static std::string	INDENT;

			class Element {
			public:
				Element(const std::string& name) : m_name(name), m_hasValue(false) {}
				virtual ~Element();

				void SetValue(const std::string& value) {
					m_hasValue = true;
					m_value = value;
				}
				
				void AddAttribute(const std::string& name, const std::string& value) {
					m_attributes[name] = value;
				}
				
				void AddChild(Element* child) {
					m_children.push_back(child);
				}

				void PrintXml(std::ostream& out, const std::string& indent) const;

			private:
				std::string				m_name;
				std::string				m_value;
				bool					m_hasValue;
				Attributes				m_attributes;
				std::vector<Element*>	m_children;

				bool HasContent() const {
					return m_hasValue || HasChildren();
				}

				bool HasChildren() const {
					return !m_children.empty();
				}

				void PrintAttributes(std::ostream& out) const;
				void PrintValue(std::ostream& out) const;
				void PrintChildren(std::ostream& out, const std::string& indent) const;
			};

			std::string				m_processingInstruction;
			Element*				m_root;
			std::stack<Element*>	m_elements;
		};
	}
}

#endif
