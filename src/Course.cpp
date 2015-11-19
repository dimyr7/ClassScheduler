#include "Course.hpp"
#include "SectionCombo.hpp"
#include "SectionGroup.hpp"
#include <Map>
#include <bitset>

#ifdef DEBUG
#include <cassert>
#endif

/*
 * ============================== 
 * Object Creation
 * ============================== 
 */
Course::Course(std::string department, std::string courseNumber){
	this->_department = department;
	this->_courseNumber = courseNumber;
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

size_t Course::getNumSectionTypes(const std::vector<Section*> sections){
	std::bitset<Course::NUM_OF_SECTION_TYPES> numOfTypes;
	for(std::vector<Section*>::const_iterator it = sections.begin(); it != sections.end(); it++){
		Course::TypeOfSection secType = Course::getTypeOfSection(*it);
		numOfTypes[(int)secType] = 1;
	}
	return numOfTypes.count();
}

std::vector<SectionCombo*> Course::getCombos(){
	if( this->_department.compare("PHYS") == 0){
		// PHYS courses are weird
		// Any combination of sections will work
		int numOfTypes = Course::getNumSectionTypes(this->_sections);
		
		// Add ALL sections to the same section group
		SectionGroup physGroup(numOfTypes, this->_courseNumber);
		for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
			physGroup.addSection(*it);
		}

		this->_combos = physGroup.getCombos();
	}
	else if(this->_department.compare("CS") == 0 and this->_courseNumber.compare("233")){
		// TODO handle CS 233
		std::cout<< "Tryig to work with CS233" << std::endl;
#ifdef DEBUG
		assert(false);
#endif
	}
	// TODO add if its a special topics class
	// TODO check for honours course
	else{
		// This will map the section Letter  to a vector of Sections
		std::map<std::string, std::vector<Section*>> sectionGroupMap;

		// For each section, assign it to a section letter
		for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
			std::string sectionCode = (*it)->getSectionName().substr(0, 1);
			sectionGroupMap[sectionCode].push_back((*it));
		}
		
		// For each section letter ...
		for(std::map< std::string, std::vector<Section*> >::iterator it = sectionGroupMap.begin(); it != sectionGroupMap.end(); it++){
			int numOfTypes = Course::getNumSectionTypes(it->second);

			// Create a section group with a set number of sections
			SectionGroup newGroup(numOfTypes, it->first);
			
			// Push all sections to that section group
			for(std::vector<Section*>::const_iterator is = it->second.begin(); is != it->second.end(); is++){
				newGroup.addSection(*is);
			}
			std::vector<SectionCombo*> newCombos = newGroup.getCombos();
			this->_combos.insert(this->_combos.end(), newCombos.begin(), newCombos.end());
		}
	}

	return this->_combos;
}
