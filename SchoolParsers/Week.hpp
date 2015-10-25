#ifndef WEEK_H
#define WEEK_H
#include "Time.hpp"
class Week{
	public:
		enum Day{
			monday,
			tuesday,
			wednesday,
			thursday,
			friday,
			saturday,
			sunday
		};
		Week();
		~Week();

		bool 	setDay(Day day, Time start, Time end);
		void  	unsetDay(Day day);
		Time** 	getTimes();

		bool 	isValid();
	private:
		
		Time* 	_times[7][2];

};
#endif
