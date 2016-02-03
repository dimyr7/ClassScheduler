#include "Course.hpp"
#include "Section.hpp"
#include "Parser.hpp"
#include "Schedule.hpp"

#include <sys/resource.h>
#include <sys/types.h>
#include <sys/time.h>
#include <iostream>
#include <valarray>
#include <pthread.h>
std::vector<Schedule*> scheduleHelper(std::vector<Course*> courses, std::valarray<size_t> index, size_t depth){
	std::vector<Schedule*> schedulesSoFar;

	std::vector<SectionCombo*> courseCombos = courses[depth]->getCombos();

	// For each combo in this course
	for(size_t i = 0; i < courseCombos.size(); i++){

		bool thisOverlaps = false;
		SectionCombo* thisCombo = courseCombos[i];
	
		// If there are overlaps with any previous combos
		for(size_t j = 0; j < depth; j++){
			size_t indexOfPrevCombo = index[j];
			SectionCombo* currPrevCombo = courses[j]->getCombos()[indexOfPrevCombo];

			// This combo overlaps
			if(SectionCombo::overlap(thisCombo, currPrevCombo)){
				thisOverlaps = true;
				break;
			}
		}
		if(thisOverlaps){
			continue;
		}
		index[depth] = i;

		// this is a leaf node 
		if(depth+1 == courses.size()){
			Schedule* newSchedule = new Schedule();

			// add all the combos so far
			for(size_t j =0; j < courses.size(); j++){
				size_t comboIndex = index[j];
				SectionCombo* combo = courses[j]->getCombos()[comboIndex];

				assert(combo != NULL);
				newSchedule->addCombo(combo);
			}
			schedulesSoFar.push_back(newSchedule);
		}
		// if not a leaf node
		else{
			std::vector<Schedule*> scheduleRet = scheduleHelper(courses, index, depth+1);
			schedulesSoFar.insert(schedulesSoFar.end(), scheduleRet.begin(), scheduleRet.end());
		}
		
	}
	return schedulesSoFar;
}



std::vector<Schedule*> schedule(std::vector<Course*> courses){
	size_t numOfCourses = courses.size();
	std::valarray<size_t> index = std::valarray<size_t>(numOfCourses);
	
	std::vector<Schedule*> schedules = scheduleHelper(courses, index, 0);
	return schedules;
}

int main(){
    Parser parse("phys211.json");
	Course cs125("PHYS", "211");
	std::vector<Section*> sections = parse.getAll();
	
    for (int i = 0; i < (int)sections.size(); i++) {
        //cout << *sections[i] << endl;
		cs125.addSection(sections[i]);
    }
	std::vector<SectionCombo*> combos = cs125.getCombos();	
	for(std::vector<SectionCombo*>::const_iterator it = combos.begin(); it != combos.end(); it++){
		//std::cout << "=== New Combo ===" << std::endl;
		std::vector<Section*> sections = (*it)->getSections();
		for(std::vector<Section*>::const_iterator is = sections.begin(); is != sections.end(); is++){
			//std::cout << (*is)->getSectionName() <<std::endl;
		}
	}


    return 0;
}



