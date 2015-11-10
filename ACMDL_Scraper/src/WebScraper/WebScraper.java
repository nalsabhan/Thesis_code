package semantic2;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;

public class WebScraper{
	
	
	
	 public boolean scrap(String id, String folder)
	 { 
		
			
		 boolean b = true;
			 WebClient webClient = new WebClient();
		 	webClient.setCssEnabled(false);
		 	webClient.setJavaScriptEnabled(false);
		 	
		 	String ID = id;
		 	String lnk = "http://dl.acm.org/citation.cfm?id="+ID+"&preflayout=flat";
		    HtmlPage pg = null;
		    List<?> pgT = null;
		    String title = null;
			try {
				pg = webClient.getPage(lnk);  
			    title = pg.getTitleText();
			} catch (FailingHttpStatusCodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			  Writer writer = null;
			String abst ="";
			HtmlDivision div = null;
			pgT = pg.getByXPath("//div[@CLASS='flatbody']");
			div = (HtmlDivision) pgT.get(0);
	        abst =  div.asText();
	      
		     if(abst.length()<=29)
		     { 
		    	 System.out.println(folder+" has no abstract"+id);
		    	 b = false;
		     
		     }
		     else
		     {
		     
			    try {
			      
			
			        File file = new File("data\\"+folder+"\\"+ID+".txt");
			        
			        writer = new BufferedWriter(new FileWriter(file));
			        writer.write(abst+ " "+title);
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			        b = false;
			    } catch (IOException e) {
			        e.printStackTrace();
			        b = false;
			    } finally {
			        try {
			            if (writer != null) {
			                writer.close();
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			            b = false;
			        }  
			    }

   
		     }
    webClient.closeAllWindows();
  
	
    
		 return b;
}

	public static void main(String[] args) {
	
		WebScraper ws = new WebScraper();
		boolean ret=true;
		 String inputFileName  = "acm-proceedings.rdf";
		
		 Model model1 = ModelFactory.createDefaultModel();
		
		 InputStream inp = FileManager.get().open( inputFileName );
		if (inp == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + inputFileName + " not found");
		}

						model1.read(inp, "");
						int i2=0;
						int i21=0;
						int i26=0;
						int i28=0;
						int i3=0;
						int i36=0;
						int i35=0;
						int i37=0;
						int i4=0;
						int i41=0;
						int i46=0;
						int i48=0;
						int i6=0;
						int i63=0;
						int i65=0;
						int i68=0; 
		
				String[] lab = { "I.2", 
							  	 "I.3", 
							  	 "I.4", 
							  	 "I.6", 
							  	 "I.3.5", 
							  	 "I.3.6", 
							  	 "I.3.7", 
							  	 "I.2.1", 
							  	 "I.2.6", 
							  	 "I.2.8", 
							  	 "I.6.3",
							  	 "I.6.5", 
							  	 "I.6.8", 
							  	 "I.4.1", 
							  	 "I.4.6", 
							  	 "I.4.8"};
						
			for (int j=0; j<16; j++)
			{
					    String querySt =
					        "PREFIX akt: <http://www.aktors.org/ontology/portal#>  "+
					        "select ?uri "+
					        "where { "+
					         "?uri akt:addresses-generic-area-of-interest <http://acm.rkbexplorer.com/ontologies/acm#"+lab[j]+">  "+
					        "} \n ";
					    
					    Query query = QueryFactory.create(querySt);

					    QueryExecution q = QueryExecutionFactory.create(query, model1);
					   
					    try{
					    	com.hp.hpl.jena.query.ResultSet results =  q.execSelect();

					    for ( ; results.hasNext() ; )
					    {
					      QuerySolution sol = results.nextSolution() ;
					      
					      RDFNode x = sol.get("uri") ;         
					     ret = ws.scrap(x.toString().substring(30), lab[j]);
					   if(!ret)
					   {    if(j==0)
						     i2++;
					        else if(j==1)
						     i3++; 
						    else if(j==2)
						     i4++; 
						    else if(j==3)
						  	 i6++; 
						  	else if(j==4)
						  	 i35++; 
						  	else if(j==5)
						  	 i36++; 
						  	else if(j==6)
						  	 i37++;
						  	else if(j==7)
						  	 i21++; 
						  	else if(j==8)
						  	 i26++; 
						  	else if(j==9)
						  	 i28++; 
						  	else if(j==10)
						  	 i63++;
						  	else if(j==11)
						  	 i65++;
						  	else if(j==12)
						  	 i68++;
						  	else if(j==13)
						  	 i41++;
						  	else if(j==14)
						  	 i46++;
						  	else if(j==15)
						  	 i48++;
						  	    				
					   }
					    }
					    
					  } finally { q.close() ; }
					    			
				}//end for
			System.out.println("I.2 :"+ i2+
							  	 "\nI.3 :"+ i3+ 
							  	 "\nI.4 :"+ i4+
							  	 "\nI.6 :"+ i6+
							  	 "\nI.3.5 :"+ i35+
							  	 "\nI.3.6 :"+ i36+
							  	 "\nI.3.7 :"+ i37+
							  	 "\nI.2.1 :"+ i21+
							  	 "\nI.2.6 :"+ i26+
							  	 "\nI.2.8 :"+ i28+
							  	 "\nI.6.3 :"+ i63+
							  	 "\nI.6.5 :"+ i65+
							  	 "\nI.6.8 :"+ i68+
							  	 "\nI.4.1 :"+ i41+
							  	 "\nI.4.6 :"+ i46+
							  	 "\nI.4.8 :"+i48);
			
	}
		
	}



