package data.scheedule.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayUtils {

	/**
	 * Casts all elements in an array from Type <T2> to <T1>.
	 * @param <T1> Desired type to cast all elements in collection to, must extend <T1>
	 * @param <T2> Base type that of the items in the collection
	 * @param lst List of elements to cast
	 * @param castTo Class to cast to
	 * @return List of elements casted to <T2>
	 */
	public static <T1, T2 extends T1> List<T1> castAll(Collection<T2> lst, Class<T1> castTo) {
		List<T1> casted = new ArrayList<T1>();
		for (T2 obj : lst) {
			casted.add(castTo.cast(obj));
		}
		return casted;
	}

}
