#include "Course/SectionGroup.hpp"

#include <cassert>
Course::SectionGroup::SectionGroup(size_t numTypes, std::string id){
	this->_id = id;
	this->_numTypes = numTypes;
	this->_sections     = std::valarray< std::vector<Section*> >(numTypes);
	this->_sectionTypes = std::valarray<std::string>(numTypes);
	for(auto it = std::begin(this->_sectionTypes); it != std::end(this->_sectionTypes); it++){
		(*it) = "";
	}
}


Course::SectionGroup::SectionGroup(const SectionGroup& copy){
	this->_id = copy._id;
	this->_numTypes = copy._numTypes;
	this->_sections = copy._sections;
	this->_sectionTypes = copy._sectionTypes;
}

Course::SectionGroup& Course::Course::SectionGroup::operator=(const SectionGroup& copy){
	this->_id = copy._id;
	this->_numTypes = copy._numTypes;
	this->_sections = copy._sections;
	this->_sectionTypes = copy._sectionTypes;

	return *this;
}

Course::SectionGroup::~SectionGroup(){
	// don't delete any sections
	return;
}

bool Course::SectionGroup::validID(const std::string id){
	return (this->_id.compare(id) == 0);
}

bool Course::SectionGroup::addSection(Section* section){
	std::string type = section->getSectionType();
	if(type.compare("") == 0){
		// section must have a type, cannot be empty
		return false;
	}
	// look through all section-types
	for(size_t i = 0; i < this->_numTypes; i++){
		std::string typeCompare = this->_sectionTypes[i];
		// if this section types already exists...
		if(typeCompare.compare(type) == 0){
			this->_sections[i].push_back(section);	
			return true;
		}
		//  if an empty one is found
		else if(typeCompare.compare("") == 0){
			this->_sections[i].push_back(section);	
			this->_sectionTypes[i] = type;
			return true;
		}
	}
	// no place to put it
	return false;
}


std::vector<SectionCombo*> Course::SectionGroup::getCombos(){
	std::valarray<size_t> index = std::valarray<size_t>(this->_numTypes);
	std::vector<SectionCombo*> combos;
 
	for(auto it = std::begin(index); it != std::end(index); it++){
		*it = 0;	
	}
	do{
		std::vector<Section*> combinations;
		for(size_t i = 0; i < index.size(); i++){
			combinations.push_back(this->_sections[i][index[i]]);
		}
		bool overlap = Course::SectionGroup::overlap(combinations);
		if(not overlap){
			SectionCombo* newCombo = new SectionCombo(combinations);
			combos.push_back(newCombo);
		}
	}
	while(this->nextIteration(index));
	return combos;
}

bool Course::SectionGroup::nextIteration(std::valarray<size_t> &index){
	for(size_t i = 0; i < index.size(); i++){
		size_t indexVal = index[i];
		if(indexVal+1 == this->_sections[i].size()){
			index[i] = 0;
		}
		else{
			index[i]++;
			return true;
		}
	}
	return false;
	
}

bool Course::SectionGroup::overlap(std::vector<Section*> potCombo){
	for(size_t i = 0; i < potCombo.size()-1 ; i++){
		for(size_t j = 1; j < potCombo.size(); j++){
			Section* sectionA = potCombo[i];
			Section* sectionB = potCombo[j];
			bool overlap = Section::overlap(sectionA, sectionB);	
			if(overlap){
				return true;
			}
		}	
	}
	return false;
}
