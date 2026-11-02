#pragma once
namespace konga {
class SourceDataLocation; class TargetDataLocation; class ProcessedFiles;
void expandEndpointArchives(const SourceDataLocation&,ProcessedFiles&);
void encryptEndpointFiles(const TargetDataLocation&,ProcessedFiles&);
void compressEndpointFiles(const TargetDataLocation&,ProcessedFiles&);
}
