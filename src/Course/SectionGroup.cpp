#include "Course/SectionGroup.hpp"
#include "Course/SectionCombo.hpp"

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
	std::vector<SectionCombo*> combos = this->getCombosHelper(index, 0);
	return combos;
}


std::vector<SectionCombo*> Course::SectionGroup::getCombosHelper(std::valarray<size_t>  index, size_t depth){
	std::vector<SectionCombo*> combosSoFar;	

	// iterate through all sections of a certain type and try to add on to the current running list of sections
	for(size_t i = 0; i < this->_sections[depth].size(); i++){
		
		// If any overlap with any previous section, skip it
		bool thisOverlaps = false;
		Section* thisSection = this->_sections[depth][i];
		for(size_t j  = 0; i < depth; i++){
			size_t indexOfPrevSection = index[j];
			Section* currPrevSection = this->_sections[j][indexOfPrevSection];

			// this section overlaps with a previously chosen section
			if(Section::overlap(thisSection, currPrevSection)){
				thisOverlaps = true;	
				break;
			}
		}
		if(thisOverlaps){
			continue;
		}
		index[depth] = i;

		// if leaf node
		if(depth+1 == this->_numTypes){
			SectionCombo* newCombo = new SectionCombo();

			//std::cout << "=== Creating a new Section Combo ===" << std::endl;
			for(size_t j = 0; j < this->_numTypes; j++){
				size_t sectionIndex = index[j];
				Section* theSection = this->_sections[j][sectionIndex];
				assert(theSection != NULL);
				newCombo->addSection(theSection);	
				//std::cout << theSection->getSectionName() << std::endl;
			}
			combosSoFar.push_back(newCombo);
		}

		// if not leaf node
		else{
			std::vector<SectionCombo*> combosRet = this->getCombosHelper(index, depth+1);
			combosSoFar.insert(combosSoFar.end(), combosRet.begin(), combosRet.end());
		}
	}
	return combosSoFar;
}
