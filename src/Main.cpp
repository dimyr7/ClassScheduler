#include <fstream>
#include <iostream>
#include <string>
#include "../include/Course.hpp"
#include "../include/SectionBuilder.hpp"
#include "../include/Parser.hpp"

void example1(){
	Section::SectionBuilder b;
	
	b.setSectionName("AL1");
	b.setSectionType("Lecture-Discussion");
	b.setDescription("Lecture for a class");
	b.setCRN("11111");

	b.setInstructorName("Alan Turing");

	b.setStartTime(Section::Week::Day::monday, 6, 0);
	b.setEndTime(Section::Week::Day::monday, 8, 0);

	b.setStartTime(Section::Week::Day::tuesday, 7, 0);
	b.setEndTime(Section::Week::Day::tuesday, 9, 0);

	b.setSemesterStart(1, 1, 2015);
	b.setSemesterEnd(30, 12, 2015);

	b.setSemsterYear("2015");
	b.setSemesterSeason("All Year");
	b.setSemesterName("365 All year");

	b.setLocationLat(2.4);
	b.setLocationLon(-23.6);
	b.setLocationBuilding("Siebel");
	b.setLocationRoom("1204");


	Section* section = b.buildSection();
	std::cout << (*section) << std::endl;

}

void parseTimes(Section::SectionBuilder& builder, std::string daysOfWeek, std::string start, std::string end){
	int startHour = stoi(start.substr(0, 2));
	int startMin  = stoi(start.substr(3, 5));
	bool isStartPM = (start.substr(6, 8).compare("PM") == 0);
	if(isStartPM  and startHour!= 12){
		startHour +=12;
	}
	else if(not isStartPM and startHour == 12){
		startHour = 0;
	}


	int endHour   = stoi(end.substr(0, 2));
	int endMin    = stoi(end.substr(3, 5));
	bool isEndPM= (end.substr(6, 8).compare("PM") == 0);	
	if(isEndPM and endHour != 12){
		endHour += 12;
	}
	else if(not isEndPM and endHour == 12){
		endHour = 0;
	}

	//std::cout << "Start Time: " << startHour << ":" << startMin << std::endl;
	//std::cout << "End Time: " << endHour << ":" << endMin << std::endl << std::endl;

	if(daysOfWeek.find("M")  != std::string::npos){
		builder.setStartTime(Section::Week::Day::monday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::monday, endHour, endMin);
	}
	if(daysOfWeek.find("T")  != std::string::npos){
		builder.setStartTime(Section::Week::Day::tuesday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::tuesday, endHour, endMin);
	}
	if(daysOfWeek.find("W") != std::string::npos){
		builder.setStartTime(Section::Week::Day::wednesday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::wednesday, endHour, endMin);
	}
	if(daysOfWeek.find("R") != std::string::npos){
		builder.setStartTime(Section::Week::Day::thursday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::thursday, endHour, endMin);
	}
	if(daysOfWeek.find("F") != std::string::npos){
		builder.setStartTime(Section::Week::Day::friday, startHour, startMin);
		builder.setEndTime(Section::Week::Day::friday, endHour, endMin);
	}
}

void parseDates(Section::SectionBuilder& builder, std::string start, std::string end){
	
	int startDay = stoi(start.substr(8, 10));
	int startMonth = stoi(start.substr(5, 7));
	int startYear = stoi(start.substr(0, 4));
	builder.setSemesterStart(startDay, startMonth, startYear);

	int endDay = stoi(end.substr(8, 10));
	int endMonth = stoi(end.substr(5,7));
	int endYear = stoi(start.substr(0, 4));
	builder.setSemesterEnd(endDay, endMonth, endYear);
	
}

using std::iostream;
using std::ifstream;
using namespace std;
int main(){
    // Parser::Parser parse("bin/example.json");
    Parser::Parser parse("bin/two_instructors.json"); // file is CS173 json
    
    /* Get sections individually 
    while(parse.hasNext()) { 
        Section::Section* current = parse.getNext(); 
        cout << *current << endl; 
    }*/

    // Get vector of all sections
    std::vector<Section::Section*> sections = parse.getAll();
    for (auto index = sections.begin(); index != sections.end(); index++) {
        cout << **index << endl;
    }
    return 0;
}
