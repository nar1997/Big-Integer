package bigint;

public class BigInteger {

	boolean negative;
	int numDigits;
	DigitNode front;
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}

	public static BigInteger parse(String integer)
	throws IllegalArgumentException {
		BigInteger n = new BigInteger(); //creating an instance

		n.front = null;
		integer = integer.trim();

		if(integer.charAt(0)=='0' && integer.length() == 0)
		{
			return n;
		}
		while((integer.charAt(0) == ' ' || integer.charAt(0) == '0' || integer.charAt(0) == '+') && integer.length() > 1) //checks & delete leading space or zero
		{
			integer = integer.substring(1);
		}


		if(integer.charAt(0) == '-') //check for negative -- cannot handle -00 or 00-0123
		{
			n.negative = true;
			integer = integer.substring(1); // can we assume at least 1 number inputed? if not needs to be fixed
		}

		DigitNode ptr = new DigitNode(0,null);
		if( integer.length() == 1 ) //handles case of 1 digit
		{
			if(integer.charAt(0) == '0')
			{
				ptr = new DigitNode(Character.digit(integer.charAt(0), 10),null);
				n.front = null;
				return n;
			}
			ptr = new DigitNode(Character.digit(integer.charAt(0), 10),null);
			n.front = ptr;
			n.numDigits = integer.length();
			return n;
		}

		n.numDigits = integer.length(); // set length of number

		for( int i = integer.length()-1; i > 0; i-- )
		{
			if(Character.isDigit(integer.charAt(i)) && !Character.isDigit(integer.charAt(i-1))) //throw exception for space in mid (might need to throw for more cases
			{
				throw new IllegalArgumentException();
			}
			if(integer.length()-1 == i)
			{
				ptr = new DigitNode( Character.digit(integer.charAt(i),10 ),null );
				n.front = ptr;
			}

			ptr.next = new DigitNode( Character.digit(integer.charAt(i-1), 10 ),null );

			ptr = ptr.next;

		}

		return n;
	}


	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger result = new BigInteger();

			int bigSize = first.numDigits;
			int smallSize = second.numDigits;

			boolean bigNeg = first.negative;
			boolean smallNeg = second.negative;

			DigitNode scanBig = first.front;
			DigitNode scanSmall = second.front;
			DigitNode checkBig = first.front; // used to traverse big & small nums if same size digit
			DigitNode checkSmall = second.front;//also used to find bigger num
			DigitNode ptr1check = first.front;
			DigitNode ptr2check = second.front;


				if(bigSize >= smallSize)
				{
					scanBig = first.front;
					 scanSmall = second.front;

				}
				else
				{
					scanBig = second.front;
					scanSmall = first.front;
					bigSize = second.numDigits;
					smallSize = first.numDigits;
					bigNeg = second.negative;
					smallNeg = first.negative;
				}

				if(bigSize == 1 && 1 == smallSize) //two negatives: fix for adding
				{
					while(checkBig != null)
					{
						if(checkSmall.digit > checkBig.digit)
						{
							scanBig = second.front;
							scanSmall = first.front;
							bigNeg = second.negative;
							smallNeg = first.negative;
							checkSmall = first.front;
							checkBig = second.front;
							break;
						}
						else
							checkBig = checkBig.next;
							checkSmall = checkSmall.next;

					}

				}
				else if (bigSize == smallSize)
				{

					if (first.numDigits==1 && second.numDigits==1)
					{
						if (first.front.digit>second.front.digit)
						{
							scanBig=first.front;
							scanSmall=second.front;
						}
						else
						{
							scanBig=second.front;
							scanSmall=first.front;
						}
					}

					for(int i=first.numDigits-1; i>0; i--)
					{
						for (int j=i; j>0; j--)
						{
							ptr1check=ptr1check.next;
							ptr2check=ptr2check.next;
						}
						if (ptr1check.digit>ptr2check.digit)
						{
							scanBig=first.front;
							scanSmall=second.front;
							bigNeg = first.negative;
							smallNeg = second.negative;
							break;
						}
						else if (ptr1check.digit<ptr2check.digit)
						{
							scanBig=second.front;
							scanSmall=first.front;
							bigNeg = second.negative;
							smallNeg = first.negative;
							break;
						}
						else
						{
								ptr1check=first.front;
								ptr2check=second.front;

							if (i==1)
							{
								if (ptr1check.digit > ptr2check.digit)
								{
									scanBig=first.front;
									scanSmall=second.front;
									break;
								}
								else
								{
									scanSmall=first.front;
									scanBig=second.front;
									break;
								}
							}
						}
					}
				}

			if((bigNeg == false && smallNeg == false) || (bigNeg == true && smallNeg == true)) //both or none neg so: adding
			{
				DigitNode crawl = result.front;

				while(scanBig != null) // this loop copies one of the digits to result BigInt
				{
					if(result.front == null) // sets front = to node first time
					{
						DigitNode n = new DigitNode(scanBig.digit, null);
						result.front = n;
						crawl = n;
					}
					else
					{
						DigitNode node = new DigitNode(scanBig.digit,null);
						crawl.next = node;
						crawl = crawl.next;
					}

					scanBig = scanBig.next;
				}
				crawl = result.front;

				Boolean addOne = false;

							if(bigSize == 1 && smallSize == 1) // handles one digit adding with raminder
							{
								if(crawl.digit + scanSmall.digit > 9)
								{
								crawl.digit = ((crawl.digit + scanSmall.digit)-10);
								crawl.next = new DigitNode(1,null);
								}
								else
								{
									crawl.digit = crawl.digit + scanSmall.digit;
								}
								return result;
							}

					while(scanSmall != null)
					{
						if(addOne && (crawl.digit + scanSmall.digit > 8))//checking if remainder can be added to next digit along with small num
						{
							crawl.digit = (crawl.digit + scanSmall.digit + 1)-10;
							addOne = true;
						}
						else if(crawl.digit + scanSmall.digit > 9)
						{
							crawl.digit = (crawl.digit + scanSmall.digit)-10;
							addOne = true;
						}


						else if(addOne)
						{
							crawl.digit = crawl.digit + scanSmall.digit +1;
							addOne = false;
						}
						else
						{
							crawl.digit = crawl.digit + scanSmall.digit;
						}

						scanSmall = scanSmall.next;
						crawl = crawl.next;
					}
					//crawl = result.front;
					if(bigNeg == true || smallNeg == true)
					{
						result.negative = true;
					}

					if(addOne)
					{
						while(crawl != null)
						{
							if(crawl.digit + 1 > 9)
							{
								crawl.digit = (crawl.digit + 1)-10;
								crawl = crawl.next;
							}
							else
							{
								crawl.digit += 1;
								addOne = false;
								break;
							}
						}
						crawl = result.front;
						if(addOne)
						{
							while(crawl.next != null)
							{
								crawl = crawl.next;
							}
							crawl.next = new DigitNode(1,null);
						}
						return result;
					}

			}
			// this line ends adding two positives or two negs

			if((bigNeg == true && smallNeg == false) || (bigNeg == false && smallNeg == true)) // one number neg so: subtracting
			{
					Boolean changedBase = false;
					DigitNode crawl = result.front;

					while(scanBig != null) // this loop copies one of the digits to result BigInt
					{
						if(result.front == null) // sets front = to node first time
						{
							DigitNode n = new DigitNode(scanBig.digit, null);
							result.front = n;
							crawl = n;
						}
						else
						{
							DigitNode node = new DigitNode(scanBig.digit,null);
							crawl.next = node;
							crawl = crawl.next;
						}

						scanBig = scanBig.next;
					}
					crawl = result.front;



					while(scanSmall != null)
					{
						if(crawl.digit == 0 && scanSmall.digit != crawl.digit)
						{

						}
						if(scanSmall.digit > crawl.digit)
						{
							crawl.digit = ((crawl.digit+10) - scanSmall.digit);
							changedBase = true;
						}
						else
						{
							crawl.digit = ( crawl.digit - scanSmall.digit );
							//changedBase = false;
						}
						crawl = crawl.next;
						scanSmall = scanSmall.next;
						scanBig = crawl;
					//	DigitNode zeroCase = crawl;
							if(changedBase)
							{
								if(scanBig != null)
								{
									if(scanBig.digit > 0)
									{
										scanBig.digit = (scanBig.digit-1);
										scanBig = scanBig.next;
										changedBase = false;
									}
									else
									{
										//scanBig = crawl;
										scanBig.digit = 9;
										scanBig = scanBig.next;
										changedBase = true;

									}

								}

							}
					}
					if(changedBase)
					{
						while(scanBig != null)
						{
							if(scanBig.digit > 0)
							{
								scanBig.digit = (scanBig.digit-1);
								scanBig = scanBig.next;
								changedBase = false;
							}
							else
							{

								scanBig.digit = 9;
								scanBig = scanBig.next;
								changedBase = true;

							}
						}
					}
					result = parseResult(result);
			}

			if(bigNeg == true && smallNeg == false) // traverse result LL and if all 0 then no neg
			{
				if(result != null)
				{
					result.negative = true;
				}
			}

		return result;

	}

	private static BigInteger parseResult(BigInteger result)
	{
		DigitNode crawl = result.front;
		BigInteger newResult = result;
		DigitNode findNum = null;
		int numDig = 0;

		while(crawl != null)
		{
			numDig++;
			crawl = crawl.next;
		}

		if(numDig>2)
		{
			crawl = result.front;

			while(crawl.next != null)
			{
				crawl = crawl.next;
			}
			if(crawl.digit > 0)
			{
				return newResult;
			}
		}

		crawl = result.front;

		if(numDig > 2)//need special case for two digits
		{
			for(int i = 0; i < numDig; i++)
			{
				if(crawl.digit > 0 && crawl.next.digit == 0)
				{
					findNum = crawl;
					crawl = crawl.next;
					if(crawl == null)
					{
						break;
					}
				}
				else
				{
					crawl = crawl.next;
					continue;
				}
				while(crawl != null && crawl.digit == 0)
				{
					crawl = crawl.next;
				}
				if(crawl != null)
				{
					findNum = crawl;
				}
				else
					break;
			}
			if (crawl==null)
			{
				result.negative=false;
				result.numDigits=0;
				result.front.next=null;
				return result;
			}

			findNum.next = null;
		}
		else if(numDig == 2)//handles 2 digit num being passed
		{
			if(crawl.digit > 0 && crawl.next.digit == 0)
			{
				crawl.next = null;
			}
		}

		crawl = result.front;


		return newResult;
	}


	public static BigInteger multiply(BigInteger first, BigInteger second) { //handle negative

		BigInteger result = new BigInteger();
		BigInteger smallLL = new BigInteger();
		BigInteger bigLL = new BigInteger();
		smallLL = second;
		bigLL = first;
		DigitNode scanBig = first.front;
		DigitNode scanSmall = second.front;
		DigitNode crawl = first.front;
		boolean meeseek = true;
		result.numDigits = first.numDigits;

		if(first.front == null || second.front == null)
		{
			return result;
		}

		if(first.numDigits < second.numDigits)
		{
			 scanBig = second.front;
			 scanSmall = first.front;
			 crawl = second.front;
			 smallLL = first;
			 bigLL = second;
			 result.numDigits = second.numDigits;
		}

		while(scanBig != null)//copy bigger node to result LL bc its bigger so i creat less new Nodes
		{
			if(meeseek)
			{
				result.front = new DigitNode(scanBig.digit,null);
				crawl = result.front;
				meeseek = false;
			}
			else
			{
			crawl.next = new DigitNode(scanBig.digit,null);
			crawl = crawl.next;
			}

			scanBig = scanBig.next;
		}

		scanBig = result.front;
		crawl = result.front;


		result = multOne(result,scanSmall);
		if(smallLL.numDigits == 1)
		{
			if(first.negative == true && second.negative == false || second.negative == true && first.negative == false)
			{
				result.negative = true;
			}
			else
			{
				result.negative = false;
			}

			return result;
		}

			scanSmall = scanSmall.next;
			smallLL.front = scanSmall;
			smallLL.numDigits -= 1;


			result = multMany(bigLL,smallLL,result);
				if(first.negative == true && second.negative == false || second.negative == true && first.negative == false)
				{
					result.negative = true;
				}

			return result;

		}


	private static BigInteger multMany(BigInteger first, BigInteger second, BigInteger addResult)
	{
	BigInteger result = new BigInteger();
	result = addResult;
	BigInteger smallLL = new BigInteger();
	BigInteger addLLZero = new BigInteger();
	BigInteger addLL = new BigInteger();
	BigInteger bigLL = new BigInteger();
	BigInteger smallLess = new BigInteger();
	DigitNode scanBig = first.front;
	DigitNode scanSmall = second.front;
	DigitNode crawl = null;
	DigitNode ptrZero = null;
	int zero = 1;
	boolean firsty = true;
	boolean secondT = true;
	boolean firstZ = true;
	int i = 1;

	smallLL = second;

	while(scanSmall != null)
	{
		i = 1;
		scanBig = first.front;
		while(scanBig != null)
		{
			if(firsty)
			{
				bigLL.front = new DigitNode((scanBig.digit),null);
				crawl = bigLL.front;
				firsty = false;
				secondT = true;
				scanBig = scanBig.next;
			}
			else
			{
				crawl.next = new DigitNode((scanBig.digit),null);
				crawl = crawl.next;
				scanBig = scanBig.next;
			}
		}

			scanBig = first.front;
			if(zero > 1)
			{
				smallLL = smallLess;
			}

			addLL = multOne(bigLL,scanSmall);

			firstZ = true;

		while(i <= zero)
		{
			if(firstZ)
			{
				addLLZero.front = new DigitNode(0,null);
				ptrZero = addLLZero.front;
				firstZ = false;
				i++;
			}
			else
			{
				ptrZero.next = new DigitNode(0,null);
				ptrZero = ptrZero.next;
				i++;
			}

		}
			ptrZero.next = addLL.front;

			ptrZero = addLLZero.front;

			bigLL.front = null;
		while(scanBig != null)
		{
			if(secondT)
			{
				bigLL.front = new DigitNode((scanBig.digit),null);
				crawl = bigLL.front;
				firsty = true;
				secondT = false;
				scanBig = scanBig.next;
			}
			else
			{
				crawl.next = new DigitNode((scanBig.digit),null);
				crawl = crawl.next;
				scanBig = scanBig.next;
			}
		}


			while(ptrZero!= null)//gives length of addLLZero
			{
				addLLZero.numDigits++;
				ptrZero = ptrZero.next;
			}

			ptrZero = addLLZero.front;

		result = add(result,addLLZero);

		DigitNode smallPtr = smallLL.front;
		boolean pass1 = true;
		if(smallLL.numDigits == 2)
		{
			smallLess.front = new DigitNode(smallPtr.next.digit,null);
		}
		else
		{
		while(smallPtr != null)
		{
			if(pass1)
			{
				smallLess.front = new DigitNode(smallPtr.digit,null);
				scanBig = smallLess.front;
				pass1 = false;
			}
			else
			{
				scanBig.next = new DigitNode(smallPtr.digit,null);
				scanBig = scanBig.next;
			}
			smallPtr = smallPtr.next;
		}
		}

			smallLL.numDigits -= 1;

			zero++;

		scanSmall = scanSmall.next;
	}

		return result;
	}


	private static BigInteger multOne(BigInteger first, DigitNode second)
	{
		BigInteger result = new BigInteger();
		BigInteger firstCheck = new BigInteger();
		BigInteger secondCheck = new BigInteger();
		DigitNode scanBig = first.front;
		DigitNode scanSmall = second;
		DigitNode crawl = first.front;

		result.front = crawl;

		int remain = 0;
		int temp = 0;
		while(scanBig != null)
		{
			if(remain != 0 && (scanBig.digit * scanSmall.digit + remain < 10))
			{
				crawl.digit = (scanBig.digit * scanSmall.digit + remain );
				remain = 0;
			}
			else if(remain != 0 && (scanBig.digit * scanSmall.digit + remain > 9)) // if a second,third,etc. remain needed
			{
				temp = crawl.digit;
				crawl.digit = ((scanBig.digit * scanSmall.digit + remain) % 10);
				remain = (((temp * scanSmall.digit + remain) - ((temp * scanSmall.digit + remain) % 10))/10);
				temp = 0;
			}
			else if(scanBig.digit * scanSmall.digit > 9)//first time a remain is needed
			{
				temp = crawl.digit;
				crawl.digit = ((scanBig.digit * scanSmall.digit) % 10);
				remain = (((temp * scanSmall.digit) - ((temp * scanSmall.digit) % 10))/10);
				temp = 0;
			}
			else
			{
				crawl.digit = (scanBig.digit * scanSmall.digit);
			}
			scanBig = scanBig.next;
			crawl = crawl.next;
		}
		scanBig = first.front;
		if(remain != 0)
		{
			while(scanBig.next != null)
			{
				scanBig = scanBig.next;
			}
			scanBig.next = new DigitNode(remain,null);
		}
		if(firstCheck.negative == true && secondCheck.negative == false || firstCheck.negative == false && secondCheck.negative == true)
		{
			result.negative = true;
		}

		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}

		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}

}
