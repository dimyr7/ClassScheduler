#include "Course.hpp"
#include "Section.hpp"
#include "SectionBuilder.hpp"
#include "Parser.hpp"
void example1(){
	Section::SectionBuilder b;
	
	b.setSectionName("AL1");
	b.setSectionType("Lecture-Discussion");
	b.setDescription("Lecture for a class");
	b.setCRN("11111");

	b.setInstructorName("Alan Turing");

	b.setStartTime(Week::Day::monday, 6, 0);
	b.setEndTime(Week::Day::monday, 8, 0);

	b.setStartTime(Week::Day::tuesday, 7, 0);
	b.setEndTime(Week::Day::tuesday, 9, 0);

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


	if(daysOfWeek.find("M")  != std::string::npos){
		builder.setStartTime(Week::Day::monday, startHour, startMin);
		builder.setEndTime(Week::Day::monday, endHour, endMin);
	}
	if(daysOfWeek.find("T")  != std::string::npos){
		builder.setStartTime(Week::Day::tuesday, startHour, startMin);
		builder.setEndTime(Week::Day::tuesday, endHour, endMin);
	}
	if(daysOfWeek.find("W") != std::string::npos){
		builder.setStartTime(Week::Day::wednesday, startHour, startMin);
		builder.setEndTime(Week::Day::wednesday, endHour, endMin);
	}
	if(daysOfWeek.find("R") != std::string::npos){
		builder.setStartTime(Week::Day::thursday, startHour, startMin);
		builder.setEndTime(Week::Day::thursday, endHour, endMin);
	}
	if(daysOfWeek.find("F") != std::string::npos){
		builder.setStartTime(Week::Day::friday, startHour, startMin);
		builder.setEndTime(Week::Day::friday, endHour, endMin);
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
    Parser parse; 
	Course cs125("CS", "125");
	std::vector<Section*> sections = parse.getAll();

    for (int i = 0; i < (int)sections.size(); i++) {
        //cout << *sections[i] << endl;
		cs125.addSection(sections[i]);
    }
	std::vector<SectionCombo*> combos = cs125.getCombos();	
	std::cout << combos.size() << std::endl;
	for(std::vector<SectionCombo*>::const_iterator it = combos.begin(); it != combos.end(); it++){
		std::cout << "=== New Combo ===" << std::endl;
		std::vector<Section*> sections = (*it)->getSections();
		for(std::vector<Section*>::const_iterator is = sections.begin(); is != sections.end(); is++){
			std::cout << (*is)->getSectionName() <<std::endl;
		}

	}


    return 0;
}
