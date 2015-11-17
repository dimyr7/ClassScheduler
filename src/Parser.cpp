#include "Parser.hpp"
#include "Section.hpp"
#include "SectionBuilder.hpp"
#include "../lib/rapidjson/document.h"
#include <fstream>
#include <cassert>
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

std::vector<Section*> Parser::getAll() {
    std::vector<Section*> sections;
    int temp = this->_index;
    this->_index = 0;
    for (int i = 0; i < this->_sizeInit; i++) {
        Section* sect = getNext();
        sections.push_back(sect);
    }
    this->_index = temp;
    return sections;
}

Section* Parser::getNext() {
    if (this->_index == this->_sizeInit) {
        throw std::out_of_range("No more sections");
    }
	// TODO get rid of nested _index++
    const Value &sect = this->_dom["sections"][this->_index++];
    const Value &meeting = sect["meetings"][0];

    const char *startTime = meeting["start"].GetString();
    const char *endTime = meeting["end"].GetString();
    const char *days = meeting["days"].GetString();

    Section::SectionBuilder sectBuild;

    int classTime[4];
    getClassTime(startTime, endTime, classTime);

    for (int i = 0; i < (int)strlen(days); i++) {
        if (days[i] == 'M') {
            sectBuild.setStartTime(Week::Day::monday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Week::Day::monday, classTime[2], classTime[3]);
        } else if (days[i] == 'T') {
            sectBuild.setStartTime(Week::Day::tuesday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Week::Day::tuesday, classTime[2], classTime[3]);
        } else if (days[i] == 'W') {
            sectBuild.setStartTime(Week::Day::wednesday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Week::Day::wednesday, classTime[2], classTime[3]); 
        } else if (days[i] == 'R') {
            sectBuild.setStartTime(Week::Day::thursday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Week::Day::thursday, classTime[2], classTime[3]); 
        } else if (days[i] == 'F') {
            sectBuild.setStartTime(Week::Day::friday, classTime[0], classTime[1]);
            sectBuild.setEndTime(Week::Day::friday, classTime[2], classTime[3]); 
        }
    }
    sectBuild.setCRN(sect["crn"].GetString());
    sectBuild.setSectionType(meeting["type"]["name"].GetString());
    sectBuild.setSectionName(sect["code"].GetString());
    sectBuild.setDescription(this->_description);
	if(meeting.HasMember("instructors")){
		std::string firstName(meeting["instructors"][0]["first"].GetString());
   		std::string lastName(meeting["instructors"][0]["last"].GetString());
		sectBuild.setInstructorName(lastName + ", " + firstName);
	}
    std::string start(sect["start"].GetString());
    int year = stoi(start.substr(0, 4));
    int month = stoi(start.substr(5, 7));
    int day = stoi(start.substr(8, 10));
    sectBuild.setSemesterStart(day, month, year);
    std::string end(sect["end"].GetString());
    year = stoi(end.substr(0, 4));
    month = stoi(end.substr(5, 7));
    day = stoi(end.substr(8, 10));
    sectBuild.setSemesterEnd(day, month, year);
    sectBuild.setSemesterName("Spring Semester");  // don't have yet
    sectBuild.setSemsterYear("2016");         // don't have yet
    sectBuild.setSemesterSeason("Spring");    // don't have yet
    sectBuild.setLocationLat(40.113803);      // don't have yet
    sectBuild.setLocationLon(-88.224904);     // don't have yet
    sectBuild.setLocationBuilding(meeting["building"].GetString());
    sectBuild.setLocationRoom("3340");        // don't have yet
    Section *builtSection = sectBuild.buildSection();
    return builtSection;
}

// Returns total number of sections
int Parser::getSize() {
    return this->_sizeInit;
}

// Returns total number of sections left
int Parser::getSizeLeft() {
    return this->_sizeInit - this->_index;
}

// Returns if there is a section remaining
bool Parser::hasNext() {
    return this->_sizeInit - this->_index;
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
    this->_dom.Parse(contents.c_str());
    this->_index = 0;
    this->_sizeInit = this->_dom["sections"].Size();
	//std::cout << this->_sizeInit << std::endl;
    this->_description = this->_dom["description"].GetString();
}
