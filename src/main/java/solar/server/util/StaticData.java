package solar.server.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class StaticData {
	
	public class ProtocolVersion 
	{
		public static final String VER ="V1";
		public static final String NEW ="NEW";
	}
//@Bean (name ="TypeNewsReceive")	
	public class TypeNewsReceive
	{   
		public static final int    TYPE_INDEX = 4;
		public static final int T1 = 1;		
		public static final int T2 = 2;
		public static final int T3 = 3;
		public static final int T4 = 4;
		public static final int T7 = 7;
		public static final int T8 = 8;
		public static final int T9 = 9;
		public static final int T10 = 10;
		public static final int T12 = 12;
		public static final int T13 = 13;
		public static final int T14 = 14;
		public static final int T17 = 17;
		public static final int T18 = 18;
		public static final int T19 = 19;
		public static final int T21 = 21;
		public static final int T25 = 25;


	}
	public class ErrCode
	{
		public static final int noErr = 0;
		public static final int formatErr = 1;
		public static final int dataNull = 2;
	}

}
