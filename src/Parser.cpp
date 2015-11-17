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

/*
 * Creates a Parser object that parses the json in the file example.json by default.
 */
Parser::Parser() {
    parseJSON("example.json");
}

/*
 * Creates a Parser object that parses the json from a file.  jsonFileName
 * is the name of the file to read from.
 */
Parser::Parser(std::string fileName) {
    parseJSON(fileName);
}

/*
 * ======================================
 * Getters
 * ======================================
 */

/*
 * Returns a vector of pointers to all Section objects created
 */
std::vector<Section::Section*> Parser::getAll() {
    return _sections;
}

/*
 * Returns a pointer to a single Section object
 */
Section* Parser::getNext() {
    if (this->_index == (int)this->_sizeInit) {
        throw std::out_of_range("No more sections");
    }
    return _sections[_index++];
}

/*
 *  Returns total number of sections in json file
 */
int Parser::getSize() {
    return this->_sizeInit;
}

/*
 * Returns true if there is a section remaining that has 
 * not yet been returned.  Returns false otherwise.
 */
bool Parser::hasNext() {
    return this->_sizeInit - this->_index;
}

/*
 * ===============================================
 * Private Helper Functions
 * ===============================================
 */

/*
 * Builds all sections contained in the json file
 * and stores them in vector _sections
 */
void Parser::buildAllSections() {
    for (int index = 0; index < (int)_sizeInit; index++) {
        const Value &section = _dom["Sections"][index];        // An individual section
        
        Section::SectionBuilder sectBuild;                     // Information per section
        sectBuild.setCRN(section["CRN"].GetString());
        sectBuild.setSectionType(section["Code"].GetString());
        sectBuild.setDescription(_description);

        // Semester start and end dates
        std::string startSemester(section["Start"].GetString());
        int year = stoi(startSemester.substr(0, 4));
        int month = stoi(startSemester.substr(5, 7));
        int day = stoi(startSemester.substr(8, 10));
        sectBuild.setSemesterStart(day, month, year);
        std::string endSemester(section["End"].GetString());
        year = stoi(endSemester.substr(0, 4));
        month = stoi(endSemester.substr(5, 7));
        day = stoi(endSemester.substr(8, 10));
        sectBuild.setSemesterEnd(day, month, year);
        
        // Information we don't have yet
        sectBuild.setSemesterName("Spring Semester");
        sectBuild.setSemsterYear("2016");
        sectBuild.setSemesterSeason("Spring");
        
        // const Value &meeting = section["Meetings"][0];
        for (auto i = 0; i < (int)section["Meetings"].Size(); i++) {
            const Value &meeting = section["Meetings"][i];

            std::string start(meeting["Start"].GetString());    
            std::string end(meeting["End"].GetString());    

            const char *days = meeting["Days"].GetString();

            std::vector<int> startTime = convertTime(start);
            std::vector<int> endTime = convertTime(end);


            for (int i = 0; i < (int)strlen(days); i++) {
                if (days[i] == 'M') {
                    sectBuild.setStartTime(Week::Day::monday, startTime[0], startTime[1]);
                    sectBuild.setEndTime(Week::Day::monday, endTime[0], endTime[1]);
                } else if (days[i] == 'T') {
                    sectBuild.setStartTime(Week::Day::tuesday, startTime[0], startTime[1]);
                    sectBuild.setEndTime(Week::Day::tuesday, endTime[0], endTime[1]);
                } else if (days[i] == 'W') {
                    sectBuild.setStartTime(Week::Day::wednesday, startTime[0], startTime[1]);
                    sectBuild.setEndTime(Week::Day::wednesday, endTime[0], endTime[1]); 
                } else if (days[i] == 'R') {
                    sectBuild.setStartTime(Week::Day::thursday, startTime[0], startTime[1]);
                    sectBuild.setEndTime(Week::Day::thursday, endTime[0], endTime[1]); 
                } else if (days[i] == 'F') {
                    sectBuild.setStartTime(Week::Day::friday, startTime[0], startTime[1]);
                    sectBuild.setEndTime(Week::Day::friday, endTime[0], endTime[1]); 
                }
            }
            sectBuild.setLocationBuilding(meeting["Building"].GetString());
            sectBuild.setSectionName(meeting["Type"]["Name"].GetString());
        
            // Information we don't have yet in the json files
            sectBuild.setLocationLat(40.113803);
            sectBuild.setLocationLon(-88.224904);
            sectBuild.setLocationRoom("3340");
            
            for (auto j = 0; j < (int)meeting["Instructors"].Size(); j++) {
                std::string name = std::string(meeting["Instructors"][j]["LastName"].GetString()) + ", "
                                               + std::string(meeting["Instructors"][j]["FirstName"].GetString());
                sectBuild.setInstructorName(name);
            }
        }

        // Build section and add to vector _sections
        Section::Section *builtSection = sectBuild.buildSection();
        _sections.push_back(builtSection);
    }
}

/*
 *  Converts a string containing a time into two integers,
 *  one for the hour and one for the minutes.  Time is changed
 *  from 12 hour clock to 24 hour clock.
 *  e.g. "3:50 PM" -> [15, 50].
 */
std::vector<int> Parser::convertTime(std::string time) {
    std::vector<int> time_24;
    time_24.push_back(stoi(time.substr(0, 2)));
    time_24.push_back(stoi(time.substr(3, 5)));
    if (time[6] == 'A' && time_24[0] == 12) {
        time_24[0] = 0;
    } else if (time[6] == 'P' && time_24[0] != 12) {
        time_24[0] += 12;
    }
    return time_24;
}

/*
 * Opens file fileName and then reads and parses the JSON
 */
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
    this->_description = this->_dom["description"].GetString();
    buildAllSections();
}
