package pdfBoxToText;


import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper; 

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import com.hp.hpl.jena.datatypes.xsd.impl.XSDDateType;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.itextpdf.text.log.SysoLogger;
import com.mendeley.oapi.common.PagedList;
import com.mendeley.oapi.schema.Document;
import com.mendeley.oapi.schema.Person;
import com.mendeley.oapi.services.MendeleyServiceFactory;
import com.mendeley.oapi.services.SearchService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class tool {

	private static final String CONSUMER_KEY = "1dfb8f5202da307ad29d7f2df81b92fb05043e953";
	private static final String acm = "http://acm.rkbexplorer.com/ontologies/acm#";
	/** The Constant CONSUMER_SECRET. */
	private static final String CONSUMER_SECRET = "0956a6502eee8df4087d2add997f85bc";

	PDFParser parser;
	String NewText;
	PDFTextStripper pdfStrp;
	PDDocument pdDoc;
	COSDocument csDoc;
	PDDocumentInformation DocInfo;
	private int[] Olab;
	private String[] strOlab;
	private int[] Plab;
	private String[] strPlab;
	List<String> strOlab2 = new ArrayList<String>();
	
	public List<String> getstrOlab2()
	{
		return this.strOlab2;
	}
	
	public void setstrOlab2(List<String> strl)
	{
		 this.strOlab2= strl;
	}
	
	public int[] getOlab() {
		return Olab;
	}

	public String[] getOlabStr() {
		return strOlab;
	}
	public void setOlab(int[] lb) {
		this.Olab = lb;
		
	}

	public void setOlabStr(String[] str) {
		this.strOlab = str;
		
	}
	public int[] getPlab() {
		return Plab;
	}

	public String[] getPlabStr() {
		return strPlab;
	}

 
	public tool() {
	}

	String pdf2Text(File fll) {

		String fileName =fll.getName();  

		System.out.println("Parsing PDF file " + fileName+"...");
		File fl = fll;

		if (!fl.isFile()) {
			System.out.println("The File : " + fileName + " does not exist!");
			return null;
		}

		try {
			parser = new PDFParser(new FileInputStream(fl));
		} catch (Exception e) {
			System.out.println("Could not open PDF Parser.");
			return null;
		}

		try {
			parser.parse();
			csDoc = parser.getDocument();

			pdfStrp = new PDFTextStripper();
			pdDoc = new PDDocument(csDoc);
			NewText = pdfStrp.getText(pdDoc);
		
		} catch (Exception e) {
			System.out.println("Error while parsing PDF file!");
			e.printStackTrace();
			try {
				if (csDoc != null) 
					csDoc.close();
				if (pdDoc != null) 
					pdDoc.close();
			} catch (Exception e1) {
				e.printStackTrace();
			}
			return null;
		}
		System.out.println("Done.");
		return NewText;
	}

	
	void writeTexttoFile(String pdfText,List<String> Olab, String fileName) {
		
		String s;
		
		pdfText = pdfText.replaceAll("(\\r|\\n|\')", " ");
		
		System.out.println("\nWriting PDF text to output text file " + fileName + "....");
		try {
			PrintWriter pw = new PrintWriter(fileName);
			
				s= "@relation H__.das_Desktop_NewTData"+"\n"+"@attribute text string"+"\n" +
					"@attribute @@class@@ "+"{I.2, I.3, I.5, I.6, I.2.1, I.2.6, I.2.8, I.3.5, I.3.6, I.3.7, I.5.1, I.5.2, I.5.4, I.6.3, I.6.5, I.6.8, other}"+"\n\n"+"@data"+"\n";
			
			for (int i=0; i < Olab.size();i++) 
			{
				s = s + "'"+pdfText+"'"+",";				
				s = s.concat(Olab.get(i)+"\n");
			}
			
			pw.print(s);
			pw.close();  
		} catch (Exception e) {
			System.out.println("An exception occured in writing the pdf text to file.");
			e.printStackTrace();
		}
		System.out.println("Done.");
	}

	public int[] strToIntLab( String[]  re)
	{	
		int[] z = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		String[] lab = { 
				"I.2", 
				"I.3", 
				"I.5", 
				"I.6", 
				"I.2.1", 
				"I.2.6", 
				"I.2.8", 
				"I.3.5", 
				"I.3.6", 
				"I.3.7", 
				"I.5.1",
				"I.5.2",
				"I.5.4",
				"I.6.3",
				"I.6.5", 
				"I.6.8", };
		
		for(int i=1; i<re.length;i++)
		{
			if (re[i].equals(lab[i]))
			{
				z[i]=1;	

			}
			else
				z[i]=0;
		}
		return z;
	}
	public List<String> IntToStrLab( double[]  d)
	{	
		List<String> z = new ArrayList<String>();
		z.clear();
		int k = 0;
		String[] lab = { 
				"I.2", 
				"I.3", 
				"I.5", 
				"I.6", 
				"I.2.1", 
				"I.2.6", 
				"I.2.8", 
				"I.3.5", 
				"I.3.6", 
				"I.3.7", 
				"I.5.1",
				"I.5.2",
				"I.5.4",
				"I.6.3",
				"I.6.5", 
				"I.6.8", };
		
		for(int i=1; i<d.length;i++)
		{
			if (d[i]==1)
			{
				z.add(lab[i]);
				

			}
			
		}
		return z;
	}
		
	public int[] setLabelsO( String  re)
	{	
		int[] z = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		String[] lb = new String[50];
		List<String> l = new ArrayList<String>();
		
		String[] lab = { 
				"I.2", 
				"I.3", 
				"I.5", 
				"I.6", 
				"I.2.1", 
				"I.2.6", 
				"I.2.8", 
				"I.3.5", 
				"I.3.6", 
				"I.3.7", 
				"I.5.1",
				"I.5.2",
				"I.5.4",
				"I.6.3",
				"I.6.5", 
				"I.6.8", 
		};
		int starti = re.indexOf("Categories and Subject Descriptors");
		int endi = re.indexOf("General Terms");

		if(endi==-1)
			endi = re.indexOf("Keywords");

		if(endi==-1)
			endi = re.indexOf("INTRODUCTION");

		if (endi != -1 || starti != -1)
			re = re.substring(starti, endi);

		for(int i=0; i<lab.length;i++)
		{
			if (re.contains(lab[i]))
			{
				z[i]=1;
				lb[i]=lab[i];
				l.add(lab[i]);
				System.out.print(z[i]);

			}
		}
		this.setOlabStr(lb);
		this.setstrOlab2(l);
		this.setOlab(z);
		this.strOlab = lb;
		this.Olab = z;
		return z;

	}

	public static String getTitle(String fTxt)
	{
			int ind = fTxt.indexOf('\n')-1;
			System.out.println(ind);
		String s = fTxt.substring(0, ind).replace(" ", "+").replace("\n", "+").replace("\t", "+");


		return s;

	}
	void writeRDF( String date, String title, String id,  List<String> cls, String DURI,List<Person> auth, String abst,String doi, String file) {

		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("acm","http://acm.rkbexplorer.com/ontologies/acm#");
		model.setNsPrefix("foaf",FOAF.NS);
		model.setNsPrefix("dcterms",DCTerms.NS);
		Property  Class = model.createProperty(acm + "class");

		Resource doc1 
		= model.createResource(DURI);
		if(id != null)
		doc1.addProperty(DCTerms.identifier, id);
		if(title != null)
		doc1.addProperty(DCTerms.title, title);
		if(abst != null)
		doc1.addProperty(DCTerms.abstract_, abst);
		if(date != null)
		doc1.addProperty(DCTerms.date, date, XSDDateType.XSDdate);
		
			for(int i=0; i<auth.size();i++)
			{	
				doc1.addProperty(DCTerms.creator, model.createResource(FOAF.Person)
						.addProperty(FOAF.firstName, auth.get(i).getForename())
						.addProperty(FOAF.family_name, auth.get(i).getSurname()));
			}
		
		if (cls != null)
		{
			for(int i=0; i<cls.size();i++)
			{
				doc1.addProperty(Class, cls.get(i));
			}
		}	
			try{
				  FileOutputStream out=new FileOutputStream(file);
					  model.write(out,"RDF/XML-ABBREV");
				  }catch(IOException e)
				  {
					  System.out.println("Exception caught"+e.getMessage());
				  }	
		

}

	public double[] measures (int[] org, double[] prd, double[] Hmes)
	{
		//double[] Hmes = null;
		int Cp = (int) Hmes[3];
		int Ct = (int) Hmes[4];
		int CpCt = (int) Hmes[5];
		for( int i = 1; i < org.length; i++ ) // skip first element global
		{		if (org[i]== 1)
					Ct++;
		
				if (prd[i]== 1)
					Cp++; 
				
				if( org[i] == prd[i] && org[i]==1 )
				{
					CpCt++;
					//System.out.println("Intersection at: " + array1[i] );
				}			
		}
		double HP = CpCt/ (float) Cp;
		double HR = CpCt/(float) Ct;
		double HF = (2*HP*HR)/(HP+HR); 
		Hmes[0] = HF;
		Hmes[1] = HP;
		Hmes[2] = HR;
		Hmes[3] = Cp;
		Hmes[4] = Ct;
		Hmes[5] = CpCt;
		return Hmes;
	
	}

	private double[] classifyBU(double[] pred) {

		
		  double[] pred2 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		System.arraycopy(pred, 0, pred2, 1, 16);
		pred = pred2;
		double segma = 0;
		double thresh = 0;
		double node0 = 0;
		double cp = 0;
		int N = 17;
		int M = 12;
		int k;
		int j;
		int predLength =pred.length;
		double[] pv = {0,0,0,0,0,0,0,0,0,0,0,0};
	
		for(int i=5;i < predLength; i++)
		{
			if(pred[i] >= 0.5)
			{   pv[i-5] = 1.0-pred[i];
				pred[i]=1;
			}
			else
			{
				pv[i-5] = pred[i];
				pred[i]=0;
			}
		}

		for(int i=1;i <=4; i++)
		{
	
			k = 3*i-3;
			j = 3*i+2;
			segma = pv[k] + pv[k+1] + pv[k+2];
			thresh = 1/(2-segma);
	
			System.out.print(" sigma : "+segma);
			System.out.println(" thresh : "+thresh);
			
			if(pred[i] > 0.5)
			{cp = 1-pred[i];}
			else
			{cp = pred[i];}
			
			
			if (pred[i] >= thresh)
			{	
				pred[i] = 1;
			}
			else
			{	
				pred[i] = 0;
				
				pred[j] = 0;
				pred[j+1] = 0;
				pred[j+2] = 0;
			}
			
			node0 = node0 + cp + segma;
			
		}
		pred[0] = node0;
		System.out.println(node0);



		return pred;
		
	}
private double[] classifyTD(String test) {

	String[] lab = { "I.2","I.3",	"I.5", 	"I.6", 	"I.2.1","I.2.6","I.2.8","I.3.5","I.3.6","I.3.7","I.5.1", "I.5.2", "I.5.4", "I.6.3", "I.6.5","I.6.8", 
	};
	
	int NSel = 600;    /////////////       Number of selection
	Filter[] filters = new Filter[2];
	double[] x=new double[16];
	double[] prd = new double[16];
	double clsLabel;
	Ranker rank = new Ranker();
	Evaluation eval = null ;
    
    
	StringToWordVector stwv = new StringToWordVector();
	weka.filters.supervised.attribute.AttributeSelection featSel = new weka.filters.supervised.attribute.AttributeSelection();

	
	WordTokenizer wtok = new WordTokenizer();
	String delim = " \r\n\t.,;:'\"()?!$*-&[]+/|\\";
	
	InfoGainAttributeEval ig = new InfoGainAttributeEval();
	
	String[] stwvOpts;
	wtok.setDelimiters(delim);

	Instances[] dataRaw =  new Instances[10000];
	 
	 DataSource[] source =  new DataSource[16];
	 
	 String str;
	
	Instances testset = null;
	DataSource testsrc = null;
	try {
		 testsrc = new DataSource(test);
		testset = testsrc.getDataSet();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
		
	 for(int j=0; j<16; j++) //16 element 0-15
	 {
		 try {
			str = lab[j];

				source[j] = new DataSource("D:/Users/nma1g11/workspace2/WebScraperFlatNew/dataPernode/new/"+str+".arff");
				dataRaw[j] = source[j].getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}	
				
			System.out.println(lab[j]);	
				if (dataRaw[j].classIndex() == -1)
					dataRaw[j].setClassIndex(dataRaw[j].numAttributes() - 1);
		
	}
   if (testset.classIndex() == -1)
					testset.setClassIndex(testset.numAttributes() - 1);
	
	try {			
		stwvOpts = weka.core.Utils.splitOptions("-R first-last -W 1000000 -prune-rate -1.0 -C -T -I -N 1 -L -S -stemmer weka.core.stemmers.LovinsStemmer -M 2 ");
		stwv.setOptions(stwvOpts);
		stwv.setTokenizer(wtok);
		
		rank.setOptions(weka.core.Utils.splitOptions("-T -1.7976931348623157E308 -N 100"));
		rank.setNumToSelect(NSel);
		featSel.setEvaluator(ig);
		featSel.setSearch(rank);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	filters[0] = stwv;
	filters[1] = featSel;
	
	System.out.println("Loading is Done!");
	

	MultiFilter mfilter = new MultiFilter();
	
	mfilter.setFilters(filters);
	
	FilteredClassifier classify = new FilteredClassifier();
	classify.setClassifier(new NaiveBayesMultinomial());     ///////// Algorithm of The Classification  /////////
	classify.setFilter(mfilter);
	
	String ss2= "";
	
		try {			
			Classifier[] clsArr = new  Classifier[16];
			clsArr = Classifier.makeCopies(classify, 16);
			String strcls = "";
			
			
			
			List<String> clsList = new ArrayList<String>();   
			String s = null;
			String newcls = null;
			String lb = "";
			String prev = "";
			boolean flag = false;
			String Ocls = null;
			int q=0;
			
			for(int i=0; i<4; i++)   // top-level only first
			{  
				
				for (int k=0; k<testset.numInstances();k++)
				{
					flag = false;
					
					 
							s = testset.instance(k).stringValue(1);
								clsList.add(s); 
					if(lab[i].equals(s))
					{
						flag = true;
						newcls = s;
					}
				}
				
				
					clsArr[i].buildClassifier(dataRaw[i]);
					eval = new Evaluation(dataRaw[i]);


				for (int j=0; j<testset.numInstances();j++)
				{			
							Ocls = testset.instance(j).stringValue(1);
						
						if (flag && !s.equals(null))
							testset.instance(j).setClassValue(lab[i]);
						
						//-----------------------------------------
						strcls = testset.instance(j).stringValue(1);
						
							if (strcls.substring(0, 3).equals(lab[i]))
								testset.instance(j).setClassValue(lab[i]);
							
						//---------------------------------------------	
				System.out.println(dataRaw[i].classAttribute().value(i)+" --- > Correct%:"+ eval.pctCorrect()+ "  F-measure:"+ eval.fMeasure(i));
						
					    clsLabel = clsArr[i].classifyInstance(testset.instance(j));
					    x = clsArr[i].distributionForInstance(testset.instance(j));
					  
					   prd[i]= x[i];
					   System.out.println(" --- > prob: "+ clsLabel);
					   System.out.println(" --- > x :"+ x[i]);
					   System.out.println(clsLabel + " --> " + testset.classAttribute().value((int) clsLabel));
					
						 testset.instance(j).setClassValue(Ocls);
						 
						 prev = testset.instance(j).stringValue(0);
						 lb = lab[i];
			}
			  
			    System.out.println("Done with "+lab[i]+" !!!");
			}
	//--------------------------------		
			for(int i=4; i<16; i++)   // starting next level 
			{  
				
				for (int k=0; k<testset.numInstances();k++)
				{
					flag = false;
					
					 
							s = testset.instance(k).stringValue(1);
								clsList.add(s); 
					if(lab[i].equals(s))
					{
						flag = true;
						newcls = s;
					}
				}
				
				
				if(x[(i-2)/3]>0.5)
				{
				clsArr[i].buildClassifier(dataRaw[i]);
				eval = new Evaluation(dataRaw[i]);
				}
				else
					clsArr[i] = null;
								
				if(clsArr[i] != null)
				{
					for (int j=0; j<testset.numInstances();j++)
					{			
								Ocls = testset.instance(j).stringValue(1);
							
							if (flag && !s.equals(null))
								testset.instance(j).setClassValue(lab[i]);
						
						//-----------------------------------------
						strcls = testset.instance(j).stringValue(1);
						
							if (strcls.substring(0, 3).equals(lab[i]))
								testset.instance(j).setClassValue(lab[i]);
							
						//---------------------------------------------	
				System.out.println(dataRaw[i].classAttribute().value(i)+" --- > Correct%:"+ eval.pctCorrect()+ "  F-measure:"+ eval.fMeasure(i));
						
					    clsLabel = clsArr[i].classifyInstance(testset.instance(j));
					    x = clsArr[i].distributionForInstance(testset.instance(j));
					  
					   prd[i]= x[i];
					   System.out.println(" --- > prob: "+ clsLabel);
					   System.out.println(" --- > x :"+ x[i]);
					   System.out.println(clsLabel + " --> " + testset.classAttribute().value((int) clsLabel));
					
						 testset.instance(j).setClassValue(Ocls);
						 
						 prev = testset.instance(j).stringValue(0);
						 lb = lab[i];
					}
				} 
			    System.out.println("Done with "+lab[i]+" !!!!!!!!!!!");
			    
			    if(clsArr[i] != null)
			    {
			    	 prd[i]= 0;
			    }
			}
		
			 System.out.println(eval.correct());
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}

	return prd;

	}
private double[] classify(String test) {

	String[] lab = { 
			"I.2",
			"I.3",
			"I.5", 
			"I.6", 
			"I.2.1", 
			"I.2.6", 
			"I.2.8", 
			"I.3.5", 
			"I.3.6", 
			"I.3.7", 
			"I.5.1",
			"I.5.2",
			"I.5.4",
			"I.6.3",
			"I.6.5", 
			"I.6.8", 
	};
		
	int NSel = 1000;    //       Number of selection
	Filter[] filters = new Filter[2];
	double[] x=new double[16];
	double[] prd = new double[16];
	double clsLabel;
	Ranker rank = new Ranker();
	Evaluation eval = null ;
    
    
	StringToWordVector stwv = new StringToWordVector();
	weka.filters.supervised.attribute.AttributeSelection featSel = new weka.filters.supervised.attribute.AttributeSelection();

	WordTokenizer wtok = new WordTokenizer();
	String delim = " \r\n\t.,;:'\"()?!$*-&[]+/|\\";
	
	InfoGainAttributeEval ig = new InfoGainAttributeEval();
	
	String[] stwvOpts;
	wtok.setDelimiters(delim);
	
	Instances[] dataRaw =  new Instances[10000];
		
	 DataSource[] source =  new DataSource[16];
		 
	 String str;
	
	Instances testset = null;
	DataSource testsrc = null;
	try {
		 testsrc = new DataSource(test);
		testset = testsrc.getDataSet();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
		
	 for(int j=0; j<16; j++) //16 element 0-15
	 {
		 try {
			str = lab[j];
				source[j] = new DataSource("D:/Users/nma1g11/workspace2/WebScraperFlatNew/dataPernode/new/"+str+".arff");
				dataRaw[j] = source[j].getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}	
				
			System.out.println(lab[j]);	
				if (dataRaw[j].classIndex() == -1)
					dataRaw[j].setClassIndex(dataRaw[j].numAttributes() - 1);
		
	}
   if (testset.classIndex() == -1)
					testset.setClassIndex(testset.numAttributes() - 1);
	
	try {			
		stwvOpts = weka.core.Utils.splitOptions("-R first-last -W 1000000 -prune-rate -1.0 -C -T -I -N 1 -L -S -stemmer weka.core.stemmers.LovinsStemmer -M 2 ");
		stwv.setOptions(stwvOpts);
		stwv.setTokenizer(wtok);
		
		rank.setOptions(weka.core.Utils.splitOptions("-T -1.7976931348623157E308 -N 100"));
		rank.setNumToSelect(NSel);
		featSel.setEvaluator(ig);
		featSel.setSearch(rank);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	filters[0] = stwv;
	filters[1] = featSel;
	
	System.out.println("Loading is Done!");
	

	MultiFilter mfilter = new MultiFilter();
	
	mfilter.setFilters(filters);
	
	FilteredClassifier classify = new FilteredClassifier();
	classify.setClassifier(new NaiveBayesMultinomial());     ///////// Algorithm of The Classification  /////////
	classify.setFilter(mfilter);
	
	String ss2= "";
	
		try {			
			Classifier[] clsArr = new  Classifier[16];
			clsArr = Classifier.makeCopies(classify, 16);
			String strcls = "";
			
			
			
			List<String> clsList = new ArrayList<String>();  
			String s = null;
			String newcls = null;
			String lb = "";
			String prev = "";
			boolean flag = false;
			String Ocls = null;
			int q=0;
			
			for(int i=0; i<16; i++)
			{  
				
				for (int k=0; k<testset.numInstances();k++)
				{
					flag = false;
					
					 
							s = testset.instance(k).stringValue(1);
								clsList.add(s); 
					if(lab[i].equals(s))
					{
						flag = true;
						newcls = s;
					}
				}
				
				clsArr[i].buildClassifier(dataRaw[i]);
				eval = new Evaluation(dataRaw[i]);
				for (int j=0; j<testset.numInstances();j++)
				{			
							Ocls = testset.instance(j).stringValue(1);
						
						if (flag && !s.equals(null))
							testset.instance(j).setClassValue(lab[i]);
						
						//-----------------------------------------
						strcls = testset.instance(j).stringValue(1);
						if (i<4)
							{if (strcls.substring(0, 3).equals(lab[i]))
								testset.instance(j).setClassValue(lab[i]);
							}
						else if (lab[i].substring(0, 3).equals(strcls))
								testset.instance(j).setClassValue(lab[i]);
						//------------------------------------------------
					    System.out.println(dataRaw[i].classAttribute().value(i)+" --- > Correct%:"+ eval.pctCorrect()+ "  F-measure:"+ eval.fMeasure(i));
					if(!prev.equals(testset.instance(j).stringValue(0)) || !lab[i].equals(lb))
					{   
						
					    clsLabel = clsArr[i].classifyInstance(testset.instance(j));
					    x = clsArr[i].distributionForInstance(testset.instance(j));
					  
					   prd[i]= x[i];
					   System.out.println(" --- > prob: "+ clsLabel);
					   System.out.println(" --- > x :"+ x[i]);
					   System.out.println(clsLabel + " --> " + testset.classAttribute().value((int) clsLabel));
					} 
						 testset.instance(j).setClassValue(Ocls);
						 
						 prev = testset.instance(j).stringValue(0);
						 lb = lab[i];
			}
			  
			    System.out.println("Done with "+lab[i].replace("99", "")+" !!!!!!!!!!!");
			}
			 System.out.println(eval.correct());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	return prd;
	
}
	public static void main(String args[]) {

		final JFileChooser fc = new JFileChooser();
		File dirPath = null, file = null;
		String date = "", title2= "", id= "", DURI= "", givenName = "", familyName= "", abst = "";		
		String[] cls= null;
		List<Person> authors;
		File currentD= new File("H:/.das/Desktop/New folder/");  ///////
		fc.setCurrentDirectory(currentD);
		int state = fc.showOpenDialog(null); 
		if(state == JFileChooser.APPROVE_OPTION)
		{
			dirPath = fc.getCurrentDirectory();
			file = fc.getSelectedFile();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Cancled ");
			System.out.println("Usage  java PDFTextParser ");
			System.exit(1);
		}


		tool pdfParserObj = new tool();
		String pdf2Text = pdfParserObj.pdf2Text(file);

		if (pdf2Text == null) {
			System.out.println("PDF to Text Conversion failed.");
		}
		else {
			String title = getTitle(pdf2Text);
			String STitle = ((title.replace("\n", " ")).trim()).replace(' ', '+');
			
			//	 --------------------------------------------
			MendeleyServiceFactory factory = MendeleyServiceFactory.newInstance(CONSUMER_KEY, CONSUMER_SECRET);
			SearchService service = factory.createSearchService();
			PagedList<Document> documents = service.search(STitle);



			date = documents.get(0).getYear()+"";
			title = documents.get(0).getTitle();
			id = documents.get(0).getUuid();
			String doi = documents.get(0).getDoi(); 
			DURI    = documents.get(0).getMendeleyUrl();
			abst	= documents.get(0).getAbstract();
			authors = documents.get(0).getAuthors();

			
		  pdfParserObj.setLabelsO(pdf2Text);

			//	 ---------------------------------------------
			double[] prb;
			double[] mes = {0,0,0,0,0,0};//0 HF; 1 HP; 2 HR; 3 Cp (retrieved); 4 Ct (relevant); 5 CpCt;
			double[] buRes;
			List<String> list = new ArrayList<String>();
			pdfParserObj.writeTexttoFile(pdf2Text, pdfParserObj.getstrOlab2(), "H:/.das/Desktop/New folder/"+title+".arff");
			prb = pdfParserObj.classify("H:/.das/Desktop/New folder/"+title+".arff");
			buRes = pdfParserObj.classifyBU(prb);
			List<String> clsstr= pdfParserObj.IntToStrLab(buRes);
			mes = pdfParserObj.measures(pdfParserObj.getOlab(), buRes, mes);
			pdfParserObj.writeRDF ( date,  title,  id,   clsstr,  DURI,  authors,  abst, doi,  "H:/.das/Desktop/New folder/rdf/"+title+".rdf");

			System.out.println("\nThe parsed text from the PDF:\n" + pdf2Text);


		}
	}
	


}