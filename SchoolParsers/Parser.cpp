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
    parseJSON("example.json");
}

Parser::Parser(std::string fileName) {
    parseJSON(fileName);
}

/*
 * ======================================
 * Getters
 * ======================================
 */

std::vector<Section::Section*> Parser::getAll() {
    std::vector<Section::Section*> sections;
    int temp = _index;
    _index = 0;
    for (int i = 0; i < _sizeInit; i++) {
        Section* sect = getNext();
        sections.push_back(sect);
    }
    _index = temp;
    return sections;
}

Section* Parser::getNext() {
    if (_index == _sizeInit) {
        throw std::out_of_range("No more sections");
    }
    const Value &sect = _dom["Sections"][_index++];
    const Value &meeting = sect["Meetings"][0];

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
    sectBuild.setCRN(sect["CRN"].GetString());
    sectBuild.setSectionName(meeting["Type"]["Name"].GetString());
    sectBuild.setSectionType(sect["Code"].GetString());
    sectBuild.setDescription(_description);
    std::string firstName(meeting["Instructors"][0]["FirstName"].GetString());
    std::string lastName(meeting["Instructors"][0]["LastName"].GetString());
    sectBuild.setInstructorName(lastName + ", " + firstName);
    std::string start(sect["Start"].GetString());
    int year = stoi(start.substr(0, 4));
    int month = stoi(start.substr(5, 7));
    int day = stoi(start.substr(8, 10));
    sectBuild.setSemesterStart(day, month, year);
    std::string end(sect["End"].GetString());
    year = stoi(end.substr(0, 4));
    month = stoi(end.substr(5, 7));
    day = stoi(end.substr(8, 10));
    sectBuild.setSemesterEnd(day, month, year);
    sectBuild.setSemesterName("Spring Semester");  // don't have yet
    sectBuild.setSemsterYear("2016");         // don't have yet
    sectBuild.setSemesterSeason("Spring");    // don't have yet
    sectBuild.setLocationLat(40.113803);      // don't have yet
    sectBuild.setLocationLon(-88.224904);     // don't have yet
    sectBuild.setLocationBuilding(meeting["Building"].GetString());
    sectBuild.setLocationRoom("3340");        // don't have yet
    Section::Section *builtSection = sectBuild.buildSection();
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

// Reads from file and parses JSON
void Parser::parseJSON(std::string fileName) {
    std::ifstream jsonFile;
    jsonFile.open(fileName);
    while(!jsonFile.is_open()) {
        std::cout << "File not found. Enter file name: ";
        std::cin >> fileName;
        jsonFile.open(fileName);
    }
    std::string contents((std::istreambuf_iterator<char>(jsonFile)), std::istreambuf_iterator<char>());
    jsonFile.close();
    _dom.Parse(contents.c_str());
    _index = 0;
    _sizeInit = _dom["Sections"].Size();
    _description = _dom["Description"].GetString();
}
