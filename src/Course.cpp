#include "Course.hpp"
#include "SectionCombo.hpp"
#include "SectionGroup.hpp"
#include "SGLecLBD.hpp"
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
	// TODO delete other pointers
	// Iterates through all Sections and deletes it
	for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
		delete *it;
	}

	// Iterates through all the SectionCombos and delets it
	for(std::vector<SectionCombo*>::const_iterator it = this->_combos.begin(); it != this->_combos.end(); it++){
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

std::vector<SectionCombo*> Course::getCombos() {
	this->generateSectionGroup();
	// Iterates through all the section groups
	for(std::vector<SectionGroup*>::const_iterator it = this->_groups.begin(); it != this->_groups.end(); it++){
		// Gests all teh valid combos and adds to the running vector of valid combinations
		std::vector<SectionCombo*> newCombos = (*it)->getCombos();
		this->_combos.insert(this->_combos.end(), newCombos.begin(), newCombos.end());
	}
	return this->_combos;
}


void Course::addSection(Section* section){
	this->_sections.push_back(section);
}

void Course::generateSectionGroup(){
	// Delete the old section groups
	this->_groups.erase(this->_groups.begin(), this->_groups.end());
	
	if( this->_department.compare("PHYS") == 0){
		//Physics is a bit weird
	}

}
