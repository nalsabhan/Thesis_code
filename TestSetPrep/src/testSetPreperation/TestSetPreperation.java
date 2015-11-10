package testSetPreperation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;


public class TestSetPreperation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String[] lab = { 
				 "I.2", 
			  	 "I.3", 
			  	 "I.5", 
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
			  	 "I.5.1", 
			  	 "I.5.2", 
			  	 "I.5.4"};
	    File[] files2 = null;
	    IOFileFilter flt = FileFilterUtils.fileFileFilter();
	    String to ="H:/.das/Desktop/NewTData/"; // destination folder

	    String foundn;
		String foundfl;
	    
	    IOFileFilter filt2;	
		files2 = FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(new File("H:/.das/Desktop/NewTData/"), flt, TrueFileFilter.INSTANCE));

	    File tFile ;
        for (int k=0; k<files2.length ;k++)	
	    {
				    	filt2 = FileFilterUtils.nameFileFilter(files2[k].getName());
			        Iterator<File> it = FileUtils.iterateFiles(new File("d:/Users/nma1g11/workspace2/WebScraperFlatNew/data/"), filt2, TrueFileFilter.INSTANCE);
			        boolean isDel;
			        while(it.hasNext())
			        {
			        	
			        	File foundf = (File) it.next();
			        	foundn = foundf.getName();
			        	
			        	foundfl = foundf.getParent().substring(51);
			        	tFile = new File(to+foundfl+"/"+foundn);
			        	try {
							FileUtils.copyFile(foundf, tFile);
							isDel = FileUtils.deleteQuietly(foundf);
							if(!isDel)
							{
								System.out.println("-------"+foundf+"------Not Deleted-----see : "+ foundfl +" ------" );
							}
							
						} catch (IOException e) {
						
							e.printStackTrace();
						}
			            System.out.println(foundn+" In "+foundfl );
			        }
	    }

	}
}
