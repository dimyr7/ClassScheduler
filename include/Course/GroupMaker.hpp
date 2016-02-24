template<class S, class T>
/**
 * A GroupMake will be given many S and those will be placed in different catagories
 * When told to, this will get all possible combinations (1 from each category) and exclude the ones that overlap
 */
class GroupMaker{
	public:
		/**
		 * A constructor for a GroupMake with a given number of categories and an id
		 * @param numTypes is the number of catogories of groups
		 * @param id is the string representation of an id
		 */
		GroupMaker(size_t numTypes, std::string id);
	
		/**
		 * Checks the id of this object against the provided id
		 * @param id is the id to check
		 * @return true this id matches the provided id
		 */
		bool validID(std::string id);

		/**
		 * Adds a new S to this group in the valid category
		 * @param insert is a new object to add
		 * @param type is the category to which it will go to
		 * @return true if the object was inserted succefully, false otherwise
		 */
		bool add(S* insert, std::string type);	

		/**
		 * Adds many objects to one category
		 * @param insert is the vector of S pointers to insert
		 * @param type is the category of these new inserted objects
		 * @return true if the insertion is successful, false otherwise
		 */
		bool add(std::vector<S*> insert, std::string type);

		/**
		 * Generatas all valid combinations that don't overlap
		 * @return is a vector output objects
		 */
		std::vector<T*> compute();
	private:
	
		/**
		 * Given the indeceices of the current iteration, this will increment the indeceies to the next possible combination]
		 * @param index is the list of indeces
		 * @return true if the incrementation succesdded succefully, false if the last combo is reached and index has been reset to all 0s
		 */
		bool nextIteration(std::valarray<size_t>  &index);

		/**
		 * Checks if the specifed combination has any overlap
		 * @param potentialCombo is the combination to check for overlap
		 * @return true if any two S's overlap, false otherwise
		 */
		static bool overlap(std::vector<S*> potentialCombo);
		
		/**
		 * Id of the GroupMaker
		 */
		std::string _id;

		/**
		 * Number of types
		 */
		size_t _numTypes;

		/**
		 * The data
		 */
		std::valarray< std::vector<S*> > _data;

		/**
		 * The id of hte data
		 */
		std::valarray< std::string > _dataTypes;
	
};
