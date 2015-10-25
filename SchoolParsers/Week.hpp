#ifndef WEEK_H
#define WEEK_H
#include <array>
#include "Time.hpp"
namespace CourseInfo{
using std::array;
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

		bool 	setDay(Day day, Time* start, Time* end);
		void  	unsetDay(Day day);
		array<Time*, 2> 	getTimes(Day day);

	private:
		
		array<array<Time*, 2>, 7> _times;

};
}
#endif
