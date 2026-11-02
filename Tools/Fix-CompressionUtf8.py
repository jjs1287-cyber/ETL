from pathlib import Path
p=Path('C:/ETL/Server/src/native/compression/EndpointArchive.cpp');s=p.read_text().replace('#include <stdexcept>','#include <stdexcept>\n#include <clocale>');s=s.replace('constexpr unsigned long long maxBytes', '''struct Utf8Locale {
 int previousMode;std::string previousLocale;
 Utf8Locale(){previousMode=_configthreadlocale(_ENABLE_PER_THREAD_LOCALE);const char* current=setlocale(LC_CTYPE,nullptr);previousLocale=current?current:"C";if(!setlocale(LC_CTYPE,".UTF8")){_configthreadlocale(previousMode);throw std::runtime_error("Cannot activate thread-local UTF-8 archive encoding");}}
 ~Utf8Locale(){setlocale(LC_CTYPE,previousLocale.c_str());_configthreadlocale(previousMode);}
};
constexpr unsigned long long maxBytes''');s=s.replace(' auto destination=path(directory);',' Utf8Locale utf8;\n auto destination=path(directory);').replace(' extension(format);if(files.empty())',' Utf8Locale utf8;\n extension(format);if(files.empty())');p.write_text(s)
p=Path('C:/ETL/Server/src/native/integration/konga/OperationObjects/FtpLocation.cpp');s=p.read_text();b=Path('C:/ETL/Archives/endpoint-compression-20261029/Server/src/native/integration/konga/OperationObjects/FtpLocation.cpp');b.write_text(s);s=s.replace('return m_CommonData.m_lTransferType == 1;','return !m_extendedProperties.GetBool("compression_enabled",false) && m_CommonData.m_lTransferType == 1;');p.write_text(s)
