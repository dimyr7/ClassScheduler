#ifndef TIME_H
#define TIME_H
/*
 * Represents a time on 24 hour clock
 */
class Time{
	public:
		/*
		 * Default constructor will set the time to 00:00
		 */
		Time();
		/*
		 * Sets the hour and minute to the specified parameters
		 * If either value is invalid (e.g. hour >23, minute >59 ...) the time will be 00:00
		 */
		Time(int hour, int minute);
		~Time();
		int 	getHour();
		int 	getMinute();

		/*
		 * This function will return true on success
		 * Returns false otherwise (i.e. value is invalid) and does not change the time
		 */
		bool 	setHour(int hour);
		bool 	setMinute(int minute);
		static bool validHour(int hour);
		static bool validMinute(int minute);
	private:
		int _hour;
		int _minute;
};
#endif
