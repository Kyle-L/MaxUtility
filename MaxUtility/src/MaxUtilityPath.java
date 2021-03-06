import java.util.*; 

/**
 * An implementation the Find Max Utility Path algorithm.
 * @author Kyle Lierer
 *
 */
public class MaxUtilityPath{

	/**
	 * Returns the path of decisions that can be made by an agent
	 * that would maximize utility.
	 * @param root - The root of the decision tree.
	 * @return 	An ordered list of states that indicate the decisions
	 * 			that should be made by an agent to maximize utility.
	 */
	public static List<State> findMaxUtilityPath (State root) {
		Set<State> allNodes = root.toSet();
		Set<State> visitedSet = new HashSet<State>();
		
		// Initialization.
		initialize(root, allNodes);
		
		PriorityQueue<State> queue = new PriorityQueue<>(allNodes);
			
		// Iterate through queue until empty.
		while (!queue.isEmpty()) {
			// Poll m from the q.
			State m = queue.poll();
			
			// Union m into the visitedSet.
			visitedSet.add(m);
			
			// Relax the children states based on m
			for (State child : m.getChildren()) {
				// Remove the child from the queue.
				queue.remove(child);
				
				// Relax the child.
				relax(m, child);
				
				// Add the child back into the queue.
				queue.add(child);
			}
		}
		
		// Return max path.
		return getMaxPath(visitedSet);
	}
	
	/**
	 * Initializes the tree so that the max utility decision path
	 * can be found. 
	 * @param root - The root of the tree.
	 * @param set - The set of all nodes in the tree.
	 */
	private static void initialize(State root, Set<State> set) {
		for (State state : set) {
			state.setExpectedUtility(Integer.MIN_VALUE);
			state.setParent(null);
		}
		root.setExpectedUtility(0);
	}
	
	/**
	 * Relaxes a state s based upon state m.
	 * @param m
	 * @param s
	 */
	private static void relax (State m, State s) {
		if (s.getExpectedUtility() < m.getExpectedUtility() + s.getUtilitySum()) {
			s.setExpectedUtility(m.getExpectedUtility() + s.getUtilitySum());
			s.setParent(m);
		}
	}
	
	/**
	 * Backtracks through the tree from the state with the highest
	 * expected utility to find the path of maximum expected utility.
	 * @param states - All states within the tree.
	 * @return The path of maximum utility.
	 */
	private static List<State> getMaxPath (Set<State> states) {
		List<State> list = new ArrayList<State>();
		
		// An empty max state so that first leaf state iterated through replaces it.		
		State maxState = new State("", null, new int[] {});
		
		// Iterates through comparing all leaf state to the current max leaf state.
		for (State state : states) {
			if (state.isLeaf()) {
				/* If the current state's expected utility is greater than current max, set the max
				 * to the current state. */
				if ((state.getExpectedUtility() > maxState.getExpectedUtility())) {
					maxState = state;
				}
			}
		}
		/* Now the the max state is found, backtrack up the tree through parent states
		 * until we are at the root node. */
		while (maxState != null) {
			// The states are added to the beginning of the list so the list order is not backward.
			list.add(0, maxState);
			maxState = maxState.getParent();
		}

		return list;
	}
	
}
