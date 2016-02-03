#include "Course.hpp"
#include "SectionCombo.hpp"
#include "SectionGroup.hpp"
#include <Map>
#include <bitset>
/*
 * ============================== 
 * Object Creation
 * ============================== 
 */
Course::Course(std::string department, std::string courseNumber, std::string name){
	this->_department = department;
	this->_courseNumber = courseNumber;
	this->_courseName = name;
	this->_sync = false;
}

Course::~Course(){
	// Iterates through all the SectionCombos and delets it
	for(std::vector<SectionCombo*>::const_iterator it = this->_combos.begin(); it != this->_combos.end(); it++){
		delete *it;
	}
	// Iterates through all Sections and deletes it
	for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
		delete *it;
	}
}

/*
 * ============================== 
 * Getters
 * ============================== 
 */
std::string Course::getDepartment() const{
	return this->_department;
}
std::string Course::getCourseNumber() const{
	return this->_courseNumber;
}
std::string Course::getCourseName() const{
	return this->_courseName;
}
std::vector<Section*> Course::getSections() const{
	return this->_sections;
}


void Course::addSection(Section* section){
	this->_sections.push_back(section);
}

Course::TypeOfSection Course::getTypeOfSection(const Section* section){
	std::string sectionType = section->getSectionType();
	if(sectionType.compare("Conference") == 0){
		return Course::TypeOfSection::LEC;
	}
	else if(sectionType.compare("Discussion/Recitation") == 0){
		return Course::TypeOfSection::DIS;
	}
	else if(sectionType.compare("Independent Study") == 0){
		return Course::TypeOfSection::IND;
	}
	else if(sectionType.compare("Laboratory") == 0){
		return Course::TypeOfSection::LAB;
	}
	else if(sectionType.compare("Laboratory-Discussion") == 0){
		return Course::TypeOfSection::LBD;
	}
	else if(sectionType.compare("Lecture") == 0){
		return Course::TypeOfSection::LEC;
	}
	else if(sectionType.compare("Lecture-Discussion") == 0){
		return Course::TypeOfSection::LCD;
	}
	else if(sectionType.compare("Online") == 0){
		return Course::TypeOfSection::ONL;
	}
	else if(sectionType.compare("Practice") == 0){
		return Course::TypeOfSection::PR;
	}
	else if(sectionType.compare("Quiz") == 0){
		return Course::TypeOfSection::Q;
	}
	else{
		return Course::TypeOfSection::STA;
	}
}	
bool Course::checkSectionTypeError(std::bitset<Course::NUM_OF_SECTION_TYPES> types){
	// Check for Lecture-Discussion & Lecture
	if(types[(int)Course::TypeOfSection::LCD] == 1 and types[(int)Course::TypeOfSection::LEC] == 1){
		return false;
	}
	// Check for Lecture-Discussion & Discussion
	if(types[(int)Course::TypeOfSection::LCD] == 1 and types[(int)Course::TypeOfSection::DIS] == 1){
		return false;
	}
	if(types[(int)Course::TypeOfSection::LBD] == 1 and types[(int)Course::TypeOfSection::LAB] == 1){
		return false;
	}
	if(types[(int)Course::TypeOfSection::LBD] == 1 and types[(int)Course::TypeOfSection::DIS] == 1){
		return false;
	}
	return true;
}
std::vector<SectionCombo*> Course::getCombos(){
	// Delete the old section groups
	for(std::vector<SectionCombo*>::const_iterator it = this->_combos.begin(); it != this->_combos.end(); it++){
		delete *it;
	}
	// TODO add if its a special topics class
	if(this->getCourseName().find("Special") != std::string::npos){

	}
	// TODO check for honours course
	if(this->getCourseNumber().find("Honor") != std::string::npos){

	}
	// TODO CS233 is a bit weird
	if(this->getDepartment().compare("CS") == 0 and this->getCourseNumber().compare("233") == 0){

	}
	// TODO physics courses are also weird
	if(this->getDepartment().compare("PHYS") == 0){

	}
	// This will map the section Letter  to a vector of Sections
	std::map<std::string, std::vector<Section*>> sectionGroupMap;

	// For each section, assign it to a section letter
	for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
		std::string firstChar = (*it)->getSectionName().substr(0, 1);
		sectionGroupMap[firstChar].push_back((*it));
	}
	
	// For each section letter ...
	for(std::map< std::string, std::vector<Section*> >::iterator it = sectionGroupMap.begin(); it != sectionGroupMap.end(); it++){

		std::bitset< Course::NUM_OF_SECTION_TYPES> typesOfSections;

		// .. Find out all types of sections for that letter
		for(std::vector<Section*>::const_iterator is = it->second.begin(); is != it->second.end(); is++){
			Course::TypeOfSection secType =  Course::getTypeOfSection( *is );
			typesOfSections[ (int)secType] = 1;
		}
		bool sectionError = Course::checkSectionTypeError(typesOfSections);
		if(sectionError){
			//TODO error handle
		}
		else{
			int numOfTypes = typesOfSections.count();

			// Create a section group with a set number of sections
			SectionGroup* newGroup = new SectionGroup(numOfTypes, it->first);
			// Push all sections to that section group
			for(std::vector<Section*>::const_iterator is = it->second.begin(); is != it->second.end(); is++){
				newGroup->addSection(*is);
			}
			std::vector<SectionCombo*> newCombos = newGroup->getCombos();
			this->_combos.insert(this->_combos.end(), newCombos.begin(), newCombos.end());
			delete newGroup;
		}
	}

	return this->_combos;
}
