/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef DOCUMENT_H
#define DOCUMENT_H

#include <string>
#include <vector>

#include <konga/MailClient/SmtpPriority.h>		               

#include "WorkflowIdTypes.h"

namespace konga {

	class Process;

	struct ToDocDataElement {
		ToDocDataElement(const std::string& stName, data_element_id_t deId)
			: m_stName(stName), m_DataElementId(deId) {}
		~ToDocDataElement(void) {}
		std::string m_stName;
		data_element_id_t m_DataElementId;
	};

	struct FromDocDataElement {
		FromDocDataElement(const std::string& stName) : m_stName(stName) {}
		~FromDocDataElement(void) { }
		std::string m_stName;
	};

	class Document {
	public:
		Document(Process& process, document_id_t id, task_id_t taskId)
			: m_id(id), m_process(process), m_TaskId(taskId), m_priority(prNormal) {}
		virtual ~Document() {}

		document_id_t GetId() const { return m_id; }
		void SetId(document_id_t id) { m_id = id; }

		task_id_t GetTaskId() const { return m_TaskId; }

		void SetMsgTemplateName(const std::string& stMsgTemplateName) { m_stMsgTemplateName = stMsgTemplateName; }
		std::string GetMsgTemplateName() const { return m_stMsgTemplateName; }

		void SetMsgTemplateLocation(const std::string& stMsgTemplateLocation) { m_stMsgTemplateLocation = stMsgTemplateLocation; }
		std::string GetMsgTemplateLocation() const { return m_stMsgTemplateLocation; }

		virtual std::string GetUrl() const { return std::string(); }
		virtual std::string GetSendFormat() const { return std::string(); }

		void AddToDocDataElement(ToDocDataElement de) { m_vToDocDataElementList.push_back(de); }
		void AddFromDocDataElement(FromDocDataElement de) { m_vFromDocDataElementList.push_back(de); }

		std::string GetToDocDataElementsAsString() const;
		std::string GetFromDocDataElementsAsString() const;

		virtual std::string From() const { return std::string(); }
		virtual std::string To() const { return std::string(); }
		virtual std::string CC() const { return std::string(); }
		virtual std::string BCC() const { return std::string(); }
		virtual std::string Subject() const { return std::string(); }
		virtual std::string ReplyTo() const { return std::string(); }

		void SetPriority(smtpPriority priority) { m_priority = priority; }
		int GetPriority() const {
			return static_cast<int>(m_priority);
		}

	private:
		document_id_t m_id;
		Process& m_process;
		task_id_t m_TaskId;
		smtpPriority m_priority;

		std::string m_stMsgTemplateName, m_stMsgTemplateLocation;

		std::vector<ToDocDataElement> m_vToDocDataElementList;
		std::vector<FromDocDataElement> m_vFromDocDataElementList;
	};

	class EmailDocument : public Document {

	public:
		EmailDocument(Process& process, document_id_t id, task_id_t taskId, bool bResponseRequired) :
		  Document(process, id, taskId), m_bResponseRequired(bResponseRequired) {}
		~EmailDocument() {}

		bool IsResponseRequired() const { return m_bResponseRequired; }

		void SetUrl(const std::string& stUrl) { m_stUrl = stUrl; }
		std::string GetUrl() const { return m_stUrl; }

		void SetSendFormat(const std::string& stSendFormat) { m_stSendFormat = stSendFormat; }
		std::string GetSendFormat() const { return m_stSendFormat; }

		void SetEmailFields(const std::string& stFrom,
							const std::string& stTo,
							const std::string& stCC,
							const std::string& stBCC,
							const std::string& stSubject,
							const std::string& stReplyTo) {
								m_stFrom = stFrom;
								m_stTo = stTo;
								m_stCC = stCC;
								m_stBCC = stBCC;
								m_stSubject = stSubject;
								m_stReplyTo = stReplyTo;
							}

		std::string From() const { return m_stFrom; }
		std::string To() const { return m_stTo; }
		std::string CC() const { return m_stCC; }
		std::string BCC() const { return m_stBCC; }
		std::string Subject() const { return m_stSubject; }
		std::string ReplyTo() const { return m_stReplyTo; }

	private:
		bool m_bResponseRequired;
		std::string m_stUrl;

		std::string m_stSendFormat;

		                
		std::string m_stFrom, m_stTo, m_stCC, m_stBCC, m_stSubject, m_stReplyTo;
	};

}                   

#endif              
