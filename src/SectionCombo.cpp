#include "SectionCombo.hpp"
#include <iostream>
SectionCombo::SectionCombo(){
	return;
}

SectionCombo::~SectionCombo(){
	// Don't delete any sections
	return;
}

SectionCombo::SectionCombo(const SectionCombo& copy){
	this->_sections = copy._sections;
}

SectionCombo& SectionCombo::operator=(const SectionCombo& copy){
	this->_sections = copy._sections;
	return *this;
}

void SectionCombo::addSection(Section* section){
	if(section == NULL){
		return;	
	}
	this->_sections.push_back(section);
}

std::vector<Section*> SectionCombo::getSections(){
	return this->_sections;
}

bool SectionCombo::overlap(SectionCombo* one, SectionCombo* two){
	if(one == NULL or two == NULL){
		return false;
	}
	for(std::vector<Section*>::const_iterator it = one->_sections.begin(); it != one->_sections.end(); it++){
		Section* oneSection = *it;
		
		for(std::vector<Section*>::const_iterator is = two->_sections.begin(); is != two->_sections.end(); is++){
			Section* twoSection = *is;
			if(Section::overlap(oneSection, twoSection)){
				return true;
			}
		}
	}
	return false;
}

std::ostream& operator<<(std::ostream& os, SectionCombo& combo){
	for(std::vector<Section*>::const_iterator it = combo.getSections().begin(); it != combo.getSections().end(); it++){
		os << (*it)->getSectionName() << std::endl;
	}
	return os;
}
