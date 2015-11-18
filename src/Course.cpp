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
Course::Course(std::string department, std::string courseNumber){
	this->_department = department;
	this->_courseNumber = courseNumber;
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

std::vector<SectionCombo*> Course::getCombos(){
	// Delete the old section groups
	if( this->_department.compare("PHYS") == 0){
		std::bitset< Course::NUM_OF_SECTION_TYPES > listOfSections;
		for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
			Course::TypeOfSection secType = Course::getTypeOfSection((*it));
			listOfSections[ (int)secType] = 1;
		}
		int numOfTypes = listOfSections.count();
		SectionGroup* physGroup = new SectionGroup(numOfTypes, this->_courseNumber);
		for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
			physGroup->addSection((*it));
		}
		this->_combos = physGroup->getCombos();
		delete physGroup;
	}
	// TODO add if its a special topics class
	// TODO check for honours course
	// TODO CS 233 is a bit weird
	else{
		// This will map the section Letter  to a vector of Sections
		std::map<std::string, std::vector<Section*>> sectionGroupMap;

		// For each section, assign it to a section letter
		for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
			std::string firstChar = (*it)->getSectionName().substr(0, 1);
			sectionGroupMap[firstChar].push_back((*it));
		}
		
		// For each section letter ...
		for(std::map< std::string, std::vector<Section*> >::iterator it = sectionGroupMap.begin(); it != sectionGroupMap.end(); it++){

			std::bitset< Course::NUM_OF_SECTION_TYPES> listOfSections;

			// .. Find out all types of sections for that letter
			for(std::vector<Section*>::const_iterator is = it->second.begin(); is != it->second.end(); is++){
				Course::TypeOfSection secType =  Course::getTypeOfSection( *is );
				listOfSections[ (int)secType] = 1;
			}
			int numOfTypes = listOfSections.count();

			// Create a section group with a set number of sections
			SectionGroup* newGroup = new SectionGroup(numOfTypes, it->first);
			// Push all sections to that section group
			for(std::vector<Section*>::const_iterator is = it->second.begin(); is != it->second.end(); is++){
				newGroup->addSection(*is);
			}
			this->_combos = newGroup->getCombos();
			delete newGroup;
		}
	}

	return this->_combos;
}
