#include "SGLecLBD.hpp"
#include "SectionGroup.hpp"
#include "SectionCombo.hpp"
/*
 * Object Creation
 */
SGLecLBD::SGLecLBD(std::string id) : SectionGroup(id){
	return;
}





bool 	SGLecLBD::addSection(Section* section){
	std::string type = section->getSectionType();
	// If the section is a lecture and has the correct identifier
	if(type.compare("Lecture") == 0){
		this->_lectures.push_back(section);
		return true;
	}
	// If the section is a LBD and has teh correct identifier 
	else if(type.compare("Laboratory-Discussion") == 0){
		this->_labDiscussions.push_back(section);
		return true;
	}
	// This section doesnt belong here
	return false;
}


std::vector<SectionCombo*> SGLecLBD::getCombos() const{
	std::vector<SectionCombo*> combos;
	/*
	 * For all possilbr lectures
	 */
	//for(std::vector<Section*>::const_iterator it = this->_lectures.begin(); it != this->_lectures.end(); it++){
	for(int i = 0; i < (int)this->_lectures.size(); i++){
		//Section* lecture = *it;
		Section* lecture = this->_lectures[i];
		/*
		 * For all possible lab-dis 
		 */
		//for(std::vector<Section*>::const_iterator is = this->_labDiscussions.begin(); is != this->_labDiscussions.end(); is++){
		for(int j = 0; j < (int)this->_labDiscussions.size(); j++){
			//Section* labdis = *is;	
			Section* labdis = this->_labDiscussions[j];

			/*
			 * If the lectures & lab-dis overlap, then its not a valid combination
			 */
			if(Section::overlap(lecture, labdis)){
				continue;
			}
			/*
			 * Otherwise it is valid
			 */
			SectionCombo* sectionCombo = new SectionCombo();
			sectionCombo->addSection(lecture);
			sectionCombo->addSection(labdis);
			combos.push_back(sectionCombo);
		}
		
	}
	return combos;
}

std::vector<Section*> SGLecLBD::getLecSections()const {
	return this->_lectures;
}

std::vector<Section*> SGLecLBD::getLBDSections() const{
	return this->_labDiscussions;
}


std::ostream& operator<<(std::ostream& os, const SGLecLBD& group){
	os << "=== SectioGroup Lec-LBD ===" << std::endl;
	os << "=== Lectures ===" << std::endl;
	/*
	 * Print all lecture section names
	 */
	for(std::vector<Section*>::const_iterator it = group.getLecSections().begin(); it != group.getLecSections().end(); it++){
		os << (*it)->getSectionName() << std::endl;
	}

	/*
	 * Print all Lab-Dis section names
	 */
	os << "=== Lab/Discussion ===" <<std::endl;
	for(std::vector<Section*>::const_iterator it = group.getLBDSections().begin(); it != group.getLBDSections().end(); it++){
		os << (*it)->getSectionName() << std::endl;
	}
	return os;
}
