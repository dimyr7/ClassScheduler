#include "SectionCombo.hpp"
SectionCombo::SectionCombo(){
	return;
}

SectionCombo::~SectionCombo(){
	// Don't delete any sections
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
