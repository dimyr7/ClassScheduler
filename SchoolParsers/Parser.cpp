#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include "rapidjson/document.h"
#include "Section.hpp"
#include "SectionBuilder.hpp"
#include "Week.hpp"
#include "Parser.hpp"
#include <stdexcept>

using namespace rapidjson;

/*
 * =======================================
 * Object Creation
 * =======================================
 */
Parser::Parser() {
    parseJSON();
}

Parser::Parser(const char *json) {
    parseJSON();
}

/*
 * ======================================
 * Getters
 * ======================================
 */
std::vector<Section::Section*> Parser::getAll() {
    std::vector<Section::Section*> sections;
    for (int i = 0; i < _sizeInit; i++) {
        Section* sect = this->getNext();
        sections.push_back(sect);
    }
    return sections;
}

Section* Parser::getNext() {
    if (_index == _sizeInit) {
        throw std::out_of_range("No more sections");
    }
    const Value &sect = _dom["Sections"][_index++];
    std::string crn(sect["CRN"].GetString());
    std::string sectType(sect["Code"].GetString());
    const Value &meeting = sect["Meetings"][0];
    std::string sectName(meeting["Type"]["Name"].GetString());

    const char *startTime = meeting["Start"].GetString();
    const char *endTime = meeting["End"].GetString();
    const char *days = meeting["Days"].GetString();

    Section::SectionBuilder sectBuild;

    int classTime[4];
    getClassTime(startTime, endTime, classTime);

    for (int i = 0; i < strlen(days); i++) {
        if (days[i] == 'M') {
            sectBuild.setStartTime(Section::Week::Day::monday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Section::Week::Day::monday, classTime[2], classTime[3]);
        } else if (days[i] == 'T') {
            sectBuild.setStartTime(Section::Week::Day::tuesday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Section::Week::Day::tuesday, classTime[2], classTime[3]);
        } else if (days[i] == 'W') {
            sectBuild.setStartTime(Section::Week::Day::wednesday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Section::Week::Day::wednesday, classTime[2], classTime[3]); 
        } else if (days[i] == 'R') {
            sectBuild.setStartTime(Section::Week::Day::thursday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Section::Week::Day::thursday, classTime[2], classTime[3]); 
        } else if (days[i] == 'F') {
            sectBuild.setStartTime(Section::Week::Day::friday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Section::Week::Day::friday, classTime[2], classTime[3]); 
        }
    }
    // for now
    sectBuild.setCRN(crn);
    sectBuild.setSectionName(sectName);
    sectBuild.setSectionType(sectType);
    sectBuild.setDescription("Super awesome class");
    sectBuild.setInstructorName("Professor");
    sectBuild.setSemesterStart(20, 1, 2016);
    sectBuild.setSemesterEnd(15, 5, 2016);
    sectBuild.setSemesterName("Spring Semester");
    sectBuild.setSemsterYear("2016");
    sectBuild.setSemesterSeason("Spring");
    sectBuild.setLocationLat(40.113803);      // coordinates of
    sectBuild.setLocationLon(-88.224904);     // Siebel Center
    sectBuild.setLocationBuilding("Siebel");
    sectBuild.setLocationRoom("3340");
    Section::Section *builtSection = sectBuild.buildSection();
    if (!builtSection) {
        throw std::invalid_argument ("Invalid Section");
    }
    return builtSection;
}

// Returns total number of sections
int Parser::getSize() {
    return _sizeInit;
}

// Returns total number of sections left
int Parser::getSizeLeft() {
    return _sizeInit - _index;
}

// Returns if there is a section remaining
bool Parser::hasNext() {
    return _sizeInit - _index;
}

/*
 * ===============================================
 * Private Helper Functions
 * ===============================================
 */

// Gets class start and end times in 24 hour time format
void Parser::getClassTime(const char* start, const char* end, int* times) {
    int startHour, startMinute, endHour, endMinute;
    std::sscanf(start, "%d:%d", &startHour, &startMinute);    
    std::sscanf(end, "%d:%d", &endHour, &endMinute);
    if (start[6] == 'A' && startHour == 12) {
        startHour = 0;
    } else if (start[6] == 'P' && startHour != 12) {
        startHour += 12;
    }
    if (end[6] == 'A' && endHour == 12) {
         endHour = 0;
    } else if (end[6] == 'P' && endHour != 12) {
        endHour += 12;
    }
    times[0] = startHour;
    times[1] = startMinute;
    times[2] = endHour;
    times[3] = endMinute;
}

// Parses the JSON
void Parser::parseJSON() {
    std::ifstream jsonFile;
    jsonFile.open("example.json");
    std::string contents((std::istreambuf_iterator<char>(jsonFile)), std::istreambuf_iterator<char>());
    _dom.Parse(contents.c_str());
    _index = 0;
    _sizeInit = _dom["Sections"].Size();
}
