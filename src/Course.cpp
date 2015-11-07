#include "Course.hpp"
#include "Section.hpp"
#include "SectionCombo.hpp"
#include "SectionGroup.hpp"
#include "SGLecLBD.hpp"
#include <vector>
#include <iostream>
/*
 * ============================== 
 * Object Creation
 * ============================== 
 */
Course::Course(std::string department, std::string courseNumber){
	this->_department = department;
	this->_courseNumber = courseNumber;

	this->_syncd = false;
}

Course::~Course(){
	for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
		delete *it;
	}

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
	//std::cout << this->_groups.size();
	for(std::vector<SectionGroup*>::const_iterator it = this->_groups.begin(); it != this->_groups.end(); it++){
		//std::cout << "generatign new combo " << std::endl;
		std::vector<SectionCombo*>* newCombos = (*it)->getCombos();
		this->_combos.insert(this->_combos.end(), newCombos->begin(), newCombos->end());
	}

	return this->_combos;
}

bool Course::isSyncd() const{
	return this->_syncd;
}

bool Course::generateCombo(){
	return false;
}

void Course::setDepartment(std::string department){
	this->_department = department;
}

void Course::setCourseNumber(std::string number){
	this->_courseNumber = number;
}

void Course::addSection(Section* section){
	//TODO Add section group
	for(std::vector<SectionGroup*>::const_iterator it = this->_groups.begin(); it != this->_groups.end(); it++){
		bool insertSuccess = (*it)->addSection(section);
		if(insertSuccess){
			return;
		}
	}
	/*
	 * TODO Determine what type of section group
	 * Lets assume its always Lec-LBD
	 */
	SGLecLBD* newGroup = new SGLecLBD(section->getSectionName().substr(0, 1));
	newGroup->addSection(section);
	this->_groups.push_back(newGroup);
	this->_sections.push_back(section);
}

