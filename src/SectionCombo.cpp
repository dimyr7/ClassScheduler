#include "SectionCombo.hpp"
SectionCombo::SectionCombo(){
	this->_isValid = false;
}

SectionCombo::~SectionCombo(){
	for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
		delete *it;		
	}
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
