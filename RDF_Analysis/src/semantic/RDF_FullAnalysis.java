package semantic;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;



public class RDF_FullAnalysis {

	public static void main(String[] args) {
		String NSpace = "akt";
		String lclName = "has-title";
		String lclNameC = "addresses-generic-area-of-interest";
		
		String FileName  = "acm-proceedings.rdf";
		
		 Model mIn = ModelFactory.createDefaultModel();

		
		 InputStream inS = FileManager.get().open( FileName );
		 
		if (inS == null) {
		 
			throw new IllegalArgumentException(
					"File: " + FileName + " not found");
		}
		
	
						mIn.read(inS, "");
						String URI = mIn.getNsPrefixURI(NSpace);
						String pre = mIn.getNsURIPrefix(URI);
						Property prp = mIn.getProperty(URI, lclName);
						Property prp2 = mIn.getProperty(URI, lclNameC);

						
						System.out.println(pre + "  " +URI);
						
						System.out.println(prp2.toString());
													
																									   
		int size = 0;
		int cnt0 =0;
		int cnt1 =0;
		int cnt2 =0;
		int cnt3 =0;
		int cnt4 =0;
		int cnt4p =0;
		int c = 0;
		String cr;
		String cr1;
		String cr2;
		String cr3;
		String cr4;
		String cr5;
		String crm;
		
		String temp ="";
		String temp2 ="";
		
	for (int q=0; q<=13; q++) //10
	{
		for (int j=0; j<=13; j++)
		{
			
		ResIterator iter = mIn.listSubjectsWithProperty(prp);
									

		Resource r = mIn.getResource(URI);
		
							
		
		
						
					
					 cr = "H"+temp2+temp;
					 cr1 = "C"+temp2+temp;
					/*String cr10 ="H.1.0";
					String cr11 ="H.1.1";
					String cr12 ="H.1.2";
					String cr1m ="H.1.m";*/
					
					
					 cr2 = "I"+temp2+temp;
					/*String cr20 ="H.2.0";
					String cr21 ="H.2.1";
					String cr22 ="H.2.2";
					String cr23 ="H.2.3";
					String cr24 ="H.2.4";
					String cr25 ="H.2.5";
					String cr26 ="H.2.6";
					String cr27 ="H.2.7";
					String cr28 ="H.2.8";
					String cr2m ="H.2.m";*/
					
					 cr3 = "K"+temp2+temp;
					/*String cr30 ="H.3.0";
					String cr31 ="H.3.1";
					String cr32 ="H.3.2";
					String cr33 ="H.3.3";
					String cr34 ="H.3.4";
					String cr35 ="H.3.5";
					String cr36 ="H.3.6";
					String cr37 ="H.3.7";
					String cr3m ="H.3.m";*/
					
					 cr4 = "F"+temp2+temp;
					/*String cr40 ="H.4.0";
					String cr41 ="H.4.1";
					String cr42 ="H.4.2";
					String cr43 ="H.4.3";
					String cr4m ="H.4.m";*/
					
					 cr5 = "J"+temp2+temp;
					/*String cr50 ="H.5.0";
					String cr51 ="H.5.1";
					String cr52 ="H.5.2";
					String cr53 ="H.5.3";
					String cr54 ="H.5.4";
					String cr55 ="H.5.5";
					String cr5m ="H.5.m";*/
					
					crm = "D"+temp2+temp;
					
					//----------------------
					int cri =0;
					int cri1 = 0;
					/*int cri10 =0;
					int cri11 =0;
					int cri12 =0;
					int cri1m = 0;*/
					
					
					int cri2 = 0;
					/*int cri20 =0;
					int cri21 =0;
					int cri22 =0;
					int cri23 =0;
					int cri24 =0;
					int cri25 =0;
					int cri26 =0;
					int cri27 =0;
					int cri28 =0;
					int cri2m =0;*/
					
					int cri3 = 0;
					/*int cri30 =0;
					int cri31 =0;
					int cri32 =0;
					int cri33 =0;
					int cri34 =0;
					int cri35 =0;
					int cri36 =0;
					int cri37 =0;
					int cri3m =0;*/
					
					int cri4 = 0;
					/*int cri40 =0;
					int cri41 =0;
					int cri42 =0;
					int cri43 =0;
					int cri4m =0;*/
					
					int cri5 = 0;
					/*int cri50 =0;
					int cri51 =0;
					int cri52 =0;
					int cri53 =0;
					int cri54 =0;
					int cri55 =0;
					int cri5m =0;*/
					
					int crim = 0;
					
					//----------------------
					boolean flag;
					boolean flag1;
					boolean flag2;
					boolean flag3;
					boolean flag4;
					boolean flag5;
					boolean flag6;
					String str = "none";
					int i;
					
					java.util.List<Statement> ls;
					//r = iter.nextResource();
					//System.out.println(r.hasURI("http://acm.rkbexplorer.com/id/1007173"));
					while (iter.hasNext())
					{	/////////99 stIter = r.listProperties(prp2);
						r = iter.nextResource();
						
					/*	if(j==0)
						System.out.println("-------------"+c+"-----------");*/
						
						ls = r.listProperties(prp2).toList();
						
						/*if(j==0)
						System.out.println(r.asResource().getURI());*/
						
						c++;
						size = ls.size();
						i = 0;
					
					
						
						for(i=0;i<size;i++)
						{	
							flag  = true;
							flag1 = true;
							flag2 = true;
							flag3 = true;
							flag4 = true;
							flag5 = true;
							flag6 = true;
							
							str = ls.get(i).getResource().getLocalName();
							
							/*if(j==0)
							System.out.println(str);*/
							
							if (str.startsWith(cr) && flag)
							{
								cri++;
								flag = false;
							}
							else if (str.startsWith(cr1) && flag1)
							{
								cri1++;
								flag1 = false;
							}
							else if (str.startsWith(cr2) && flag2)
							{
								cri2++;
								flag2 = false;
							}
							else if (str.startsWith(cr3) && flag3)
							{
								cri3++;
								flag3 = false;
							}
							else if (str.startsWith(cr4) && flag4)
							{
								cri4++;
								flag4 = false;
							}
							else if (str.startsWith(cr5) && flag5)
							{
								cri5++;
								flag5 = false;
							}
							else if (str.startsWith(crm) && flag6)
							{
								crim++;
								flag6 = false;
							}
					
						}
						
							if(i==0 && j==0)
							{	
							   if(size == 1)
								{
									cnt1++;
								}
							   else if (size == 0)
								{
									cnt0++;
								}
								else if (size == 2)
								{
									cnt2++;
								}
								else if (size == 3)
								{
									cnt3++;
								}
								else if (size == 4)
								{
									cnt4++;
								}
								else if (size > 4)
								{
									cnt4p++;
								}
							}
					}
					if(q==0)
					{
						System.out.println("     withNoLabel: "+ cnt0);
						System.out.println("     with1Label: "+ cnt1);
						System.out.println("     with2Labels: "+ cnt2);
						System.out.println("     with3Labels: "+ cnt3);
						System.out.println("     with4Labels: "+ cnt4);
						System.out.println("     with4+Labels: "+ cnt4p);
					}
					//int total = cnt4p+cnt0+cnt1+cnt2+cnt3+cnt4;
					System.out.println("     numOf "+cr+": "+ cri);	
					
					System.out.println("     numOf "+cr1+": "+ cri1);
					
					System.out.println("     numOf "+cr2+": "+ cri2);
					
					System.out.println("     numOf "+cr3+": "+ cri3);
					
					System.out.println("     numOf "+cr4+": "+ cri4);
					
					System.out.println("     numOf "+cr5+": "+ cri5);
					
					System.out.println("     numOf "+crm+": "+ crim);
					
					temp = "."+j;
					
					flag  = true;
					flag1 = true;
					flag2 = true;
					flag3 = true;
					flag4 = true;
					flag5 = true;
					flag6 = true;
					System.out.println(" ---------------------------- ");
				}
		temp2 = "."+q;	
	}
		
	}

}

