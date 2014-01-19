package Gson;

public class Period {
	
	public String id_user, CPeriodStart, CPeriodEnd, CDurationCycle, CFertileStart1,
	CFertileEnd1,CMostFertile,CFertileStart2, CFertileEnd2, CLessFertileStart,
	CLessFertileEnd;
	
	public String NPeriodStart, NPeriodEnd, NDurationCycle, NFertileStart1,
	NFertileEnd1, NMostFertile, NFertileStart2, NFertileEnd2, NLessFertileStart,
	NLessFertileEnd, PeriodDays;
	
	public Period(String id_user, String CPeriodStart, String CPeriodEnd,
			String CDurationCycle, String CFertileStart1, String CFertileEnd1,
			String CMostFertile, String CFertileStart2, String CFertileEnd2,
			String CLessFertileStart, String CLessFertileEnd, String NPeriodStart,
			String NPeriodEnd, String NDurationCycle, String NFertileStart1,
			String NFertileEnd1, String NMostFertile, String NFertileStart2,
			String NFertileEnd2, String NLessFertileStart, String NLessFertileEnd, 
			String PeriodDays){
		
		this.id_user = id_user;
		this.CPeriodStart = CPeriodStart;
		this.CPeriodEnd = CPeriodEnd;
		this.CDurationCycle = CDurationCycle;
		this.CFertileStart1 = CFertileStart1;
		this.CFertileEnd1 = CFertileEnd1;
		this.CMostFertile = CMostFertile;
		this.CFertileStart2 = CFertileStart2;
		this.CFertileEnd2 = CFertileEnd2;
		this.CLessFertileStart = CLessFertileStart;
		this.CLessFertileEnd = CLessFertileEnd;
		
		
		this.NPeriodStart = NPeriodStart;
		this.NPeriodEnd = NPeriodEnd;
		this.NDurationCycle = NDurationCycle;
		this.NFertileStart1 = NFertileStart1;
		this.NFertileEnd1 = NFertileEnd1;
		this.NMostFertile = NMostFertile;
		this.NFertileStart2 = NFertileStart2;
		this.NFertileEnd2 = NFertileEnd2;
		this.NLessFertileStart = NLessFertileStart;
		this.NLessFertileEnd = NLessFertileEnd;
		
		this.PeriodDays = PeriodDays;
		
	}

}
