#include "SGLecDis.hpp"
#include "SectionGroup.hpp"
#include "SectionCombo.hpp"
/*
 * Object Creation
 */
SGLecDis::SGLecDis(std::string id) : SectionGroup(id){
	return;
}

bool 	SGLecDis::addSection(Section* section){
	std::string type = section->getSectionType();
	// If the section is a lecture and has the correct identifier
	if(type.compare("Lecture") == 0){
		this->_lectures.push_back(section);
		return true;
	}
	// If the section is a Discussion and has the correct identifier 
	else if(type.compare("Discussion/Recitation") == 0){
		this->_discussions.push_back(section);
		return true;
	}
	// This section doesnt belong here
	return false;
}


std::vector<SectionCombo*> SGLecDis::getCombos() const{
	std::vector<SectionCombo*> combos;
	
	// For all possilbe lectures
	for(std::vector<Section*>::const_iterator it = this->_lectures.begin(); it != this->_lectures.end(); it++){
		//Section* lecture = *it;
		Section* lecture = *it;
		// For all possible lab-dis 
		for(std::vector<Section*>::const_iterator is = this->_discussions.begin(); is != this->_discussions.end(); is++){
			//Section* labdis = *is;	
			Section* dis = *is;

			// If the lectures & lab-dis overlap, then its not a valid combination
			if(Section::overlap(lecture, dis)){
				continue;
			}
			// Otherwise create a new section combo
			SectionCombo* sectionCombo = new SectionCombo();
			sectionCombo->addSection(lecture);
			sectionCombo->addSection(dis);
			combos.push_back(sectionCombo);
		}
		
	}
	return combos;
}

std::vector<Section*> SGLecDis::getLecSections()const {
	return this->_lectures;
}

std::vector<Section*> SGLecDis::getDisSections() const{
	return this->_discussions;
}


std::ostream& operator<<(std::ostream& os, const SGLecDis& group){
	os << "=== SectioGroup Lec-Dis ===" << std::endl;
	os << "=== Lectures ===" << std::endl;
	/*
	 * Print all lecture section names
	 */
	for(std::vector<Section*>::const_iterator it = group.getLecSections().begin(); it != group.getLecSections().end(); it++){
		os << (*it)->getSectionName() << " - " << (*it)->getCRN() << std::endl;
	}

	/*
	 * Print all Lab-Dis section names
	 */
	os << "=== Lab/Discussion ===" <<std::endl;
	for(std::vector<Section*>::const_iterator it = group.getDisSections().begin(); it != group.getDisSections().end(); it++){
		os << (*it)->getSectionName() << " - " << (*it)->getCRN() << std::endl;
	}
	return os;
}
