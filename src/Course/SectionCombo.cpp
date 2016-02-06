#include "Course/SectionCombo.hpp"
SectionCombo::SectionCombo(){
	this->_isValid = false;
}

SectionCombo::~SectionCombo(){
	return;
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
