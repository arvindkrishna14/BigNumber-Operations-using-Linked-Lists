import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * 
 * @author kumaran_rajendiran, arvind_krishna_parthasarathy
 * 
 * kxr131430, axp133230
 *
 *Class for all methods (add, subtract, multiply, power, strToNum, numToStr)
 *a and b are the two linked lists that take in values for these operations.
 *
 */
public class AXP133230_Project5 {
	public int commandLineNum = 1;

	public LinkedList<String> input = new LinkedList<String>();
	public Map<String, LinkedList<Integer>> resultMap = new HashMap<String, LinkedList<Integer>>();



	/**
	 * Method to convert a string representing a number to a linked list of integers. 
	 * 
	 * @param numString - is a string of that is the input number.
	 * @return - returns a linked list object which has a single character from the numString in each of its node.
	 */
	public LinkedList<Integer> strToNum(String numString) {
		LinkedList<Integer> number = new LinkedList<Integer>();
		boolean sign = false;
		if (numString.charAt(0) == '-') {
			sign = true;
			numString = numString.substring(1);
		}
		numString = numString.replaceFirst("^0+", "");

		for (int i = 0; i < numString.length(); i++) {
			number.addFirst(Integer.parseInt(numString.charAt(i) + ""));
		}
		if (sign == true) {
			number.set(number.size() - 1, number.getLast() * -1);
		}
		return number;
	}


	/**
	 * Method to convert a linked list of integers into a string representing an number.
	 * 
	 * @param number - is a Linked list object that has a number at each node.
	 * @return - returns the String corresponding to the Linked list object.
	 * 
	 * The function removes any 0 values that might occur at the beginning of the string.
	 */
	public static String numToString(LinkedList<Integer> number) {
		if(number==null|| number.size()==0){
			return "0";
		}
		Iterator<Integer> iteartor = number.iterator();
		StringBuffer stringBuffer = new StringBuffer();
		while (iteartor.hasNext()) {
			int num = iteartor.next();
			if (num < 0) {
				stringBuffer.append(Math.abs(num) + "-");
			} else {
				stringBuffer.append(num);
			}

		}

		int start = 0;
		int end = stringBuffer.length() - 1;
		while (start < end) {
			char tmp = stringBuffer.charAt(start);
			stringBuffer.setCharAt(start, stringBuffer.charAt(end));
			stringBuffer.setCharAt(end, tmp);
			start++;
			end--;
		}
		String numString = stringBuffer.toString();

		return numString.replaceFirst("^0+(?!$)", "");
	}

	/**
	 * Method to add two linked lists of integers and return the resulting linked list.
	 * 
	 * @param a - A linked list object which has been populated by strToNum method.
	 * @param b - A linked list object which has been populated by strToNum method.
	 * @return - Node values are added synchronously starting from head until both objects are exhausted. The 
	 * 			 method returns the result linked list object.
	 */
	public LinkedList<Integer> add(LinkedList<Integer> number1, LinkedList<Integer> number2) {
		LinkedList<Integer> a = new LinkedList<Integer>(number1);
		LinkedList<Integer> b = new LinkedList<Integer>(number2);
		boolean sign = false;
		if (a == null && b == null || a.size() == 0 && b.size() == 0) {
			return strToNum("0");
		} else if (a == null || a.size() == 0) {
			return b;
		} else if (b == null || b.size() == 0) {
			return a;
		} else if (a.getLast() < 0 && b.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			b.set(b.size() - 1, b.getLast() * -1);
			sign = true;
		} else if (a.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			return subtract(b, a);
		} else if (b.getLast() < 0) {
			b.set(b.size() - 1, b.getLast() * -1);
			return subtract(a, b);
		}
		Iterator<Integer> desIterator = a.descendingIterator();
		while (desIterator.hasNext()) {
			if (desIterator.next() == 0) {
				desIterator.remove();
			} else
				break;
		}
		desIterator = b.descendingIterator();
		while (desIterator.hasNext()) {
			if (desIterator.next() == 0) {
				desIterator.remove();
			} else
				break;
		}
		Iterator<Integer> iterator1 = a.iterator();
		Iterator<Integer> iterator2 = b.iterator();
		LinkedList<Integer> c = new LinkedList<Integer>();
		int carry = 0;
		while (iterator1.hasNext() && iterator2.hasNext()) {
			int sum = iterator1.next() + iterator2.next();
			sum = sum + carry;
			carry = sum / 10;
			c.add(sum % 10);
		}
		while (iterator1.hasNext()) {
			int sum = iterator1.next();
			sum = sum + carry;
			carry = sum / 10;
			c.add(sum % 10);
		}
		while (iterator2.hasNext()) {
			int sum = iterator2.next();
			sum = sum + carry;
			carry = sum / 10;
			c.add(sum % 10);
		}
		if (carry != 0) {
			c.add(carry);
		}
		if (sign == true) {
			c.set(c.size() - 1, c.getLast() * -1);
		}
		if(c.size()==0){
			c.add(0);
		}
		return c;
	}
	
	
	/**
	 * 
	 * Implements karatsuba multiplication algorithm. References wiki.
	 * 
	 * http://en.wikipedia.org/wiki/Karatsuba_algorithm
	 * 
	 * @param number1 Linked list representing number 1.
	 * @param number2 Linked list representing number 2.
	 * @return linked list representing the product.
	 */
	public LinkedList<Integer> perform_karatsuba_multiplication(LinkedList<Integer> number1,
			LinkedList<Integer> number2) {
		LinkedList<Integer> a = new LinkedList<Integer>(number1);
		LinkedList<Integer> b = new LinkedList<Integer>(number2);
		LinkedList<Integer> res = new LinkedList<Integer>();
		if(a.size()==0||b.size()==0){
			return strToNum("0");
		}
		if (a.size() == 1 || b.size() == 1) {
			return multiply(a, b);
		}
		int m = Math.max(a.size(), b.size());
		int m2 = (int) Math.ceil((double) m / 2);
		ArrayList<LinkedList<Integer>> aSplitResult = splitList(a, m2);
		ArrayList<LinkedList<Integer>> bSplitResult = splitList(b, m2);

		LinkedList<Integer> a0 = aSplitResult.get(1);
		LinkedList<Integer> a1 = aSplitResult.get(0);
		LinkedList<Integer> b0 = bSplitResult.get(1);
		LinkedList<Integer> b1 = bSplitResult.get(0);
		LinkedList<Integer> temp0 = karatsuba_multiplication(a0, b0);
		LinkedList<Integer> temp1 = karatsuba_multiplication(add(a0, a1),
				add(b0, b1));
		LinkedList<Integer> temp2 = karatsuba_multiplication(a1, b1);
		LinkedList<Integer> temp3 = subtract(subtract(temp1, temp2), temp0);
		temp2 = appendZeroes(temp2, 2 * m2);
		temp3 = appendZeroes(temp3, m2);
		res = add(temp0, add(temp2, temp3));
		return res;
	}
	/**
	 * Wrapper to karatsuba algorithm.
	 * @param number1
	 * @param number2
	 * @return
	 */
	public LinkedList<Integer> karatsuba_multiplication(LinkedList<Integer> number1,
			LinkedList<Integer> number2) {
		LinkedList<Integer> a = new LinkedList<Integer>(number1);
		LinkedList<Integer> b = new LinkedList<Integer>(number2);
		LinkedList<Integer> c = new LinkedList<Integer>();
		boolean sign = false;
		if (a == null || b == null || a.size() == 0 || b.size() == 0) {
			return strToNum("0");
		} else if (a.getLast() < 0 && b.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			b.set(b.size() - 1, b.getLast() * -1);
		} else if (a.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			sign = true;
		} else if (b.getLast() < 0) {
			b.set(b.size() - 1, b.getLast() * -1);
			sign = true;
		}
		 c= perform_karatsuba_multiplication(a, b);
		if (sign == true) {
			c.set(c.size() - 1, c.getLast() * -1);
		}
		return c;	
	}
	
	/**
	 * 
	 * Divides the list into two parts, (0 to m) and (m to end). Returns the linked lists.
	 * 
	 * @param list Linked list
	 * @param m integer
	 * @return
	 */
	private ArrayList<LinkedList<Integer>> splitList(LinkedList<Integer> list,
			int m) {
		ArrayList<LinkedList<Integer>> splitResult = new ArrayList<LinkedList<Integer>>();
		LinkedList<Integer> l1 = new LinkedList<Integer>();
		LinkedList<Integer> l2 = new LinkedList<Integer>();
		for (Integer number : list) {
			if (m > 0) {
				l2.add(number);
			} else {
				l1.add(number);
			}
			m--;
		}
		splitResult.add(l1);
		splitResult.add(l2);
		return splitResult;
	}
	/**
	 * Method to subtract the second linked list from the first linked list and return the resulting linked list.
	 * 
	 * @param a - A linked list object which has been populated by strToNum method.
	 * @param b - A linked list object which has been populated by strToNum method.
	 * @return -  Node values are subtracted (b from a is subtracted each time) synchronously starting from head until both objects are exhausted. The 
	 * 			 method returns the result linked list object.
	 * 				
	 * 	Also handles negative outputs.
	 */
	public LinkedList<Integer> subtract(LinkedList<Integer> number1,
			LinkedList<Integer> number2) {
		LinkedList<Integer> a = new LinkedList<Integer>(number1);
		LinkedList<Integer> b = new LinkedList<Integer>(number2);
		LinkedList<Integer> c = null;
		if (a == null && b == null || a.size() == 0 && b.size() == 0) {
			return strToNum("0");
		} else if (a == null || a.size() == 0) {
			c = new LinkedList<Integer>(b);
			c.set(c.size() - 1, c.getLast() * -1);
			return c;
		} else if (b == null || b.size() == 0) {
			c = new LinkedList<Integer>(a);
			return c;
		} else if (a.getLast() < 0 && b.getLast() < 0) {
			LinkedList<Integer> temp = a;
			a = b;
			b = temp;
			a.set(a.size() - 1, a.getLast() * -1);
			b.set(b.size() - 1, b.getLast() * -1);
		} else if (a.getLast() < 0) {
			b.set(b.size() - 1, b.getLast() * -1);
			return add(a, b);
		} else if (b.getLast() < 0) {
			b.set(b.size() - 1, b.getLast() * -1);
			return add(a, b);
		}
		c = new LinkedList<Integer>();
		Iterator<Integer> firstListIterator = a.iterator();
		Iterator<Integer> secondListIterator = b.iterator();
		int carry = 0;
		while (firstListIterator.hasNext() && secondListIterator.hasNext()) {
			int num1 = firstListIterator.next() - carry;
			int num2 = secondListIterator.next();
			if (num1 < num2) {
				num1 = (num1 + 10);
				carry = 1;
			} else {
				carry = 0;
			}
			c.add(num1 - num2);
		}
		while (firstListIterator.hasNext()) {
			int num = firstListIterator.next();
			if (carry == 1) {
				if (num == 0) {
					num = 9;
					carry = 1;
				} else {
					num = num - 1;
					carry = 0;
				}
			}
			c.add(num);
		}
		while (secondListIterator.hasNext()) {
			int num1 = 10;
			int num2 = secondListIterator.next();
			if (carry == 1) {
				num1 = 9;
			}
			carry = 1;
			c.add(num1 - num2);
		}
		if (carry == 1) {
			carry = 0;
			Iterator<Integer> thirdListForwardIterator = c.iterator();
			for (int i = 0; i < c.size(); i++) {
				int num1 = 0 - carry;
				int num2 = thirdListForwardIterator.next();
				if (num1 < num2) {
					num1 = (num1 + 10);
					carry = 1;
				} else {
					carry = 0;
				}
				c.set(i, num1 - num2);
			}
			for (int i = c.size() - 1; i >= 0; i--) {
				if (c.get(i) > 0) {
					c.set(i, c.get(i) * -1);
					break;
				} else {
					c.remove(i);
				}
			}
			// return strToNum("0");

		} else {
			Iterator<Integer> desIterator = c.descendingIterator();
			while (desIterator.hasNext()) {
				if (desIterator.next() == 0) {
					desIterator.remove();
				} else
					break;
			}
		}
		if(c.size()==0){
			c.add(0);
		}
		return c;
	}
	/**
	 * Method to multiply two linked lists of integers and return the resulting linked list.
	 * 
	 * @param a - A linked list object which has been populated by strToNum method.
	 * @param b - A linked list object which has been populated by strToNum method.
	 * @return - In place addition of results in c. Only one linked list stores the result of multiplying each node in
	 * 			 b by all other node in a.
	 */
	public LinkedList<Integer> multiply(LinkedList<Integer> number1,
			LinkedList<Integer> number2) {
		LinkedList<Integer> a = new LinkedList<Integer>(number1);
		LinkedList<Integer> b = new LinkedList<Integer>(number2);
		LinkedList<Integer> c = new LinkedList<Integer>();
		c.add(0);
		boolean sign = false;
		if (a == null || b == null || a.size() == 0 || b.size() == 0) {
			return strToNum("0");
		} else if (a.getLast() < 0 && b.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			b.set(b.size() - 1, b.getLast() * -1);
		} else if (a.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			sign = true;
		} else if (b.getLast() < 0) {
			b.set(b.size() - 1, b.getLast() * -1);
			sign = true;
		}
			Iterator<Integer> firstListIterator = a.iterator();

			long zeroCount = 0;
			while (firstListIterator.hasNext()) {
				LinkedList<Integer> tempResult = new LinkedList<>();
				int multiplier = firstListIterator.next();
				int carry = 0;

				for (int i = 0; i < zeroCount; i++) {
					tempResult.add(0);
				}

				Iterator<Integer> secondListIterator = b.iterator();
				while (secondListIterator.hasNext()) {
					int result = (secondListIterator.next() * multiplier)
							+ carry;
					carry = result / 10;
					tempResult.add(result % 10);
				}
				if (carry > 0) {
					tempResult.addLast(carry);
				}
				c = add(c, tempResult);
				zeroCount++;
			}
		if (sign == true) {
			c.set(c.size() - 1, c.getLast() * -1);
		}
		return c;
	}


	private boolean isEven(LinkedList<Integer> num2) {
		if(num2.getFirst()%2==0){
			return true;
		}else
			return false;
	}

	
	/**
	 * Add zeros to make the divisor and dividend equal in length. then add 1 to the most significant
	 * bit and so on until we exceed the divisor. Then we move on to the next digit until we
	 * run out of digits. At each step we update quotient depending on the number added.
	 * 
	 * @param number1 dividend
	 * @param number2 divisor
	 * @param calculateModulus Boolean value. When true, mod is returned. Else quotient is returned.
	 * @return
	 */
	public LinkedList<Integer> divide(LinkedList<Integer> number1,
			LinkedList<Integer> number2, boolean calculateModulus)  {
		LinkedList<Integer> a = new LinkedList<Integer>(number1);
		LinkedList<Integer> b = new LinkedList<Integer>(number2);
		boolean sign = false;
		if (b == null || (b.size() == 1 && b.get(0) == 0)) {
			System.out.println("Divide by zero");
			return strToNum("0");

		} else if (a == null || (a.size() == 1 && a.get(0) == 0)
				|| (a.size() < b.size()) || compareLists(a, b) < 0) {
			LinkedList<Integer> res = new LinkedList<Integer>();
			res.add(0);
			return res;
		}else if (a.getLast() < 0 && b.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			b.set(b.size() - 1, b.getLast() * -1);
		} else if (a.getLast() < 0) {
			a.set(a.size() - 1, a.getLast() * -1);
			sign = true;
		} else if (b.getLast() < 0) {
			b.set(b.size() - 1, b.getLast() * -1);
			sign = true;
		}
		
			long n = a.size() - b.size();
			LinkedList<Integer> temp = new LinkedList<Integer>();
			LinkedList<Integer> quotient = new LinkedList<Integer>();
			LinkedList<Integer> resTemp = new LinkedList<Integer>();
			LinkedList<Integer> qTemp = new LinkedList<Integer>();
			quotient.add(1);
			resTemp.add(0);
			qTemp.add(0);
			temp = appendZeroes(b, n);
			quotient = appendZeroes(quotient, n);
			while (true) {
				resTemp = add(resTemp, temp);
				qTemp = add(qTemp, quotient);
				while (compareLists(resTemp, a) < 0) {
					resTemp = add(resTemp, temp);
					qTemp = add(qTemp, quotient);
				}
				if (compareLists(resTemp, a) > 0) {
					resTemp = subtract(resTemp, temp);
					qTemp = subtract(qTemp, quotient);
				}
				n -= 1;
				if (n < 0) {
					break;
				}
				temp = appendZeroes(b, n);
				quotient = appendZeroes(strToNum("1"), n);
			}
			if (calculateModulus) {
				LinkedList<Integer> modulo = subtract(a, resTemp);
				if(sign==true&&modulo.size()>0){
					modulo.set(modulo.size()-1, modulo.getLast()*-1);
				}
				return modulo;
			} else {
				if(sign==true&&qTemp.size()>0){
					qTemp.set(qTemp.size()-1, qTemp.getLast()*-1);
				}
				return qTemp;
			}

	}

	
	/**
	 * 
	 * Appends the specified number of zeros to the beginning of the list. Used in divide and
	 * square root.
	 * 
	 * @param a Linked list input
	 * @param n number of zeros to be appended to the list.
	 * @return
	 */
	public LinkedList<Integer> appendZeroes(LinkedList<Integer> a, long n){
		LinkedList<Integer> result = new LinkedList<Integer>(a);
		for(long i = 0 ; i<n;i++){
			result.addFirst(0);
		}
		return result;
	}

	/**
	 * 
	 * Returns value < 0 if value in a is less than value in b.
	 * Returns value = 0 if value in b is less than value in a.
	 * else returns value > 0.
	 * 
	 * @param a Linked list 1
	 * @param b Linked list 2
	 * @return
	 */
	public int compareLists(LinkedList<Integer>a , LinkedList<Integer>b){
		String listA = numToString(a);
		String listB = numToString(b);
		if(listA.length() >listB.length()){
			return 1;
		}
		else if(listA.length() <listB.length()){
			return -1;
		}
		else{
			return listA.compareTo(listB);
		}
	}


	/**
	 * Method to compute power of a number represented by a linked list.
	 * 
	 * @param a Linked list representing the number
	 * @param b Power value.
	 * @return Linked list representing a^b.
	 */

	public LinkedList<Integer> computePower(LinkedList<Integer> a,
			LinkedList<Integer> b) {
		LinkedList<Integer> res = new LinkedList<Integer>();
		LinkedList<Integer> num = new LinkedList<Integer>();
		LinkedList<Integer> power = new LinkedList<Integer>(b);
		num.add(1);

		if(b.getLast()<1){
			res.add(0);
			return res;
		}else if (a.size() == 1 && a.get(0) == 0) {
			res.add(0);
			return res;
		} else if (a.size() == 1 && a.get(0) == 1) {
			res.add(1);
			return res;
		} else if (b.size() == 1 && b.get(0) == 0) {
			res.add(1);		
			return res;
		} else if (b.size() == 1 && b.get(0) == 1) {
			return a;
		} else {
			num = new LinkedList<Integer>(a);
			power = new LinkedList<Integer>(b);
			return power(num,power);
		}

	}
	
	/**
	 * Uses karatsuba algorithm for multiplication, while exponentially decreasing the power in 
	 * each step. 
	 * 
	 * Returns a linked list of integers with num1^num2 as its value.
	 * 
	 * 
	 * @param num1 Base value
	 * @param num2 Exponent value
	 * @return
	 */
	public LinkedList<Integer> power(LinkedList<Integer> num1, LinkedList<Integer> num2) {
		if ((checkIfListRepresentsZeroOrLesser(num2)))  
			return strToNum("1") ;
	    if (num2.size() == 1 && num2.get(0) == 1)        
	    	return num1;
	    if (isEven( num2))   
	    	return power(karatsuba_multiplication(num1,num1), divide(num2,strToNum("2"), false));
	    else                
	    	return multiply(num1, power(karatsuba_multiplication(num1,num1), divide(num2,strToNum("2"), false))); 
	}

	/**
	 * Method to iterate through the linked list representing the input and evaluating them as we iterate through it.
	 * @throws Exception 
	 */
	public void evaluateInput() throws Exception {
		for(commandLineNum =1; commandLineNum<input.size(); commandLineNum++){
			//System.out.println(commandLineNum);
			String command = input.get(commandLineNum);
			evaluateCommand(commandLineNum, command);
		}
	}


	/**
	 * Method to evaluate the command depending on the operator present, or print value if length is 1. 
	 * 
	 * @param commandLineNum - Line number in case we encounter a loop.
	 * @param command - String representing the command.
	 * @throws Exception 
	 */
	private void evaluateCommand(int commandLineNum, String command) throws Exception {
		if (command.contains("+")) {
			char resultId = command.charAt(0);
			char num1Id = command.charAt(2);
			char num2Id = command.charAt(4);
			LinkedList<Integer> result = add(resultMap.get(num1Id + ""),
					resultMap.get(num2Id + ""));
			resultMap.put(resultId + "", result);
		} else if (command.contains("-")) {
			char resultId = command.charAt(0);
			char num1Id = command.charAt(2);
			char num2Id = command.charAt(4);
			LinkedList<Integer> result = subtract(resultMap.get(num1Id + ""),
					resultMap.get(num2Id + ""));
			resultMap.put(resultId + "", result);
		} else if (command.contains("*")) {
			char resultId = command.charAt(0);
			char num1Id = command.charAt(2);
			char num2Id = command.charAt(4);
			LinkedList<Integer> result = multiply(resultMap.get(num1Id + ""),
					resultMap.get(num2Id + ""));
			resultMap.put(resultId + "", result);
		}else if (command.contains("%")) {
			char resultId = command.charAt(0);
			char num1Id = command.charAt(2);
			char num2Id = command.charAt(4);
			LinkedList<Integer> result = divide(resultMap.get(num1Id + ""),
					resultMap.get(num2Id + ""),true);
			resultMap.put(resultId + "", result);
		}
		else if (command.contains("/")) {
			char resultId = command.charAt(0);
			char num1Id = command.charAt(2);
			char num2Id = command.charAt(4);
			LinkedList<Integer> result = divide(resultMap.get(num1Id + ""),
					resultMap.get(num2Id + ""),false);
			resultMap.put(resultId + "", result);
		}else if (command.contains("^")) {
			char resultId = command.charAt(0);
			char num1Id = command.charAt(2);
			char num2Id = command.charAt(4);
			LinkedList<Integer> result = computePower(
					resultMap.get(num1Id + ""), resultMap.get(num2Id + ""));
			resultMap.put(resultId + "", result);
		} else if(command.contains("~")) {
			char resultId = command.charAt(0);
			char numId = command.charAt(2);
			LinkedList<Integer> result = sqRoot(
					resultMap.get(numId + ""));
			resultMap.put(resultId + "", result);
		}else if(command.contains(")")) {
			char resultId = command.charAt(0);
			char numId = command.charAt(2);
			LinkedList<Integer> result = powerIn15Seconds(
					resultMap.get(numId + ""));
			resultMap.put(resultId + "", result); 
		}else if (command.contains("?")) {
			evaluateGotoCommand(commandLineNum, command);
		} else if (command.length() == 1) {
			System.out.println(numToString(resultMap.get(command)));
		} else {
			String[] input = command.split("=");
			resultMap.put(input[0], strToNum(input[1]));
		}
	}

	/**
	 * Method to handle '?' Go to command, which takes the command line number as input and the command itself.
	 * Calls the evaluateCommand function inside the loop.
	 * 
	 * @param commandLineNum - line number containing the '?' operator.
	 * @param command - String representing the command.
	 */
	private void evaluateGotoCommand(int commandLineNum, String command) {
		String iterationVariable = command.charAt(0) + "";
		int gotoLineNum = Integer.parseInt(command.substring(2));
		if (!checkIfListRepresentsZero(resultMap.get(iterationVariable))) {
			this.commandLineNum = gotoLineNum-1;
		}

	}

	/**
	 * Returns true if the list represents zero.
	 * @param b
	 * @return
	 */
	private boolean checkIfListRepresentsZero(LinkedList<Integer> b) {
		if (b == null || b.size() == 0) {
			return true;
		}
		Iterator<Integer> iterator = b.iterator();
		
		while (iterator.hasNext()) {
			if (iterator.next() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method to check if the linked list represents zero, for early termination.
	 * 
	 * @param b - Linked list on which the check is to be done.
	 * @returns true or false.
	 */
	private boolean checkIfListRepresentsZeroOrLesser(LinkedList<Integer> b) {
		if (b == null || b.size() == 0) {
			return true;
		}
		Iterator<Integer> iterator = b.iterator();
		if (b.getLast() < 0) {
			return true;
		}
		while (iterator.hasNext()) {
			if (iterator.next() != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Follows a similar algorithm to that of division. Starts with a list with values that are
	 * present in the second half the list and keeps finding the maximum value it can add to each
	 * digit starting from the most significant bit and moves down to the least significant bit.
	 * 
	 * Checks if the square of a number is greater than the actual number. 
	 * 
	 * @param a Linked list input
	 * @return
	 */
	public LinkedList<Integer> sqRoot(LinkedList<Integer> a){
		LinkedList<Integer> b = new LinkedList<Integer>();
		LinkedList<Integer> temp = new LinkedList<Integer>();
		LinkedList<Integer> sqb = new LinkedList<Integer>();

		int size = a.size();
		int n =size/2;
		int i = 0;
		Iterator<Integer> listIterator = a.descendingIterator();
		while(listIterator.hasNext() && i<n){
			b.addFirst(listIterator.next());
			i++;
		}
		if(size%2 == 1){
			b.add(1);
			temp=karatsuba_multiplication(b,b);
			if(compareLists(temp,a)>0){
				b.removeFirst();
			}
		}
		temp = new LinkedList<Integer>();
		temp.add(1);
		n = b.size();
		temp = appendZeroes(temp, n-1);
		while(true){
			b = add(b,temp);
			sqb = karatsuba_multiplication(b,b);
			while(compareLists(a, sqb)>0){
				b = add(b,temp);
				sqb = karatsuba_multiplication(b,b);
			}
			if(compareLists(a, sqb)<0){
				b = subtract(b,temp);
			}
			n -= 1;
			if(n<0){
				break;
			}
			temp = appendZeroes(strToNum("1"),n-1);
		}
		return b;
	}
	/**
	 * 
	 * Starts with a given input and squares it. Checks if it ran out of time. If not, calculate 
	 * its square. If we find that we ran out of time, we discard the last found square and return
	 * the previous power.
	 * 
	 * @param a Linked list input.
	 * @return maximum power it can calculate in 15 seconds.
	 */
	public LinkedList<Integer> powerIn15Seconds(LinkedList<Integer> a){
		LinkedList<Integer> b=  new LinkedList<Integer>();
		long start = System.currentTimeMillis();
		long end=0;
		int n=2;
		do{
			b=a;
			a = karatsuba_multiplication(a,a);
			n = n*2;
			end = System.currentTimeMillis();
		}while(end-start<15000);
		return b;
	}
	public static void main(String[] args) throws Exception {
		AXP133230_Project5 obj = new AXP133230_Project5();
		String line;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		obj.input.add("");
		while ((line = stdin.readLine()) != null && line.length() != 0) {
			String[] input = line.split("\\s+");
			obj.input.add(input[1]);
		}
		long start = System.currentTimeMillis();
		obj.evaluateInput();
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
}
