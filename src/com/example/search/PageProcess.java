package com.example.search;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageProcess {
	File file;
	String remain="";
	BufferedReader bReader;
	Pattern pattern,labelPat;
	public PageProcess(File file) throws FileNotFoundException {
		this.file = file;
		if (file.exists() && file.isFile())
			bReader = new BufferedReader(new FileReader(this.file));
	 pattern = Pattern
				.compile("<meta\\s+name=\"?([a-zA-Z]*)\"?\\s+content=\"([^\">]*)\">");
	labelPat =Pattern.compile( "<\\s*[^<>]+>");
	}
 /*public  static void main(String[] args){
    	try {
			PageProcess pageProcessor=new PageProcess(new File("web.txt"));
			WebInfo webInfo;
			while(true)
			{
				webInfo=pageProcessor.next();
				if(webInfo==null)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }*/
	public WebInfo next() throws IOException {
		String line="";
		boolean endRight=false;
		StringBuffer doc=new StringBuffer();
		int pos=remain.indexOf("<doc>");
		if(pos==-1)
		{
		while(true){
			line = bReader.readLine();
		if (line == null)			
			{
			bReader.close();	
			return null;
			}
		else
			pos=line.indexOf("<doc>");
		    if(pos!=-1){
		    	remain=line;
		    	break;
		    }
		}//一行一行读 直到读到	“<doc>”
		remain=pos+5>line.length()?"":line.substring(pos+5);
		doc.append(remain);
		}
		WebInfo webInfo =null;
		int position=0;
		if (line.equals("<doc>")) {
			while ((line = bReader.readLine()) != null) {
                    if(line.contains("</doc>"))
                    	{
                    	if((position=line.indexOf("</doc>"))!=0)
                    	      doc.append(line.substring(0, line.indexOf("</doc>")));
                    	if(position+6<line.length())
                    		remain=line.substring(position+6);//下次再处理
                    	endRight=true;	
                    	break;
                    	}
                    doc.append(line);
			}
		}//has not process the condition doc没有出现在行首的情况
		
		if(endRight)
		{
			String name,content;
			try{
				
			Matcher matcher = pattern.matcher(doc);
			webInfo=new WebInfo();
			int start=0;
			int end=0;
			int last=0;
			start=doc.indexOf("<url>", 0);
			if(start!=-1)
			{
				start +=5;
			end=doc.indexOf("</url>",start);
			if(end!=-1)
			webInfo.url=doc.substring(start, end);
			}
			start=doc.indexOf("<title>", 0);
			if(start!=-1)
			{
			start +=7;
			end=doc.indexOf("</title>",start);
			if(end!=-1)
			webInfo.title=doc.substring(start,end);
			}
			while (true) {
				if (matcher.find()) {
//					System.out.println(matcher.group());
					last=matcher.end();
					name=matcher.group(1);
					content=matcher.group(2);
					switch(name)
					{
					case "keywords":
						webInfo.keywords=content;
						break;
					case  "publishid" :
						webInfo.publishid=content;
						break;
					case  "subjectid":
						webInfo.subjectid=content;
						break;
					case    "description":
						webInfo.description=content;						
						default:
							break;
					}
				}
				else{
					content=doc.substring(last);	
					Matcher  labelMatch=labelPat.matcher(content);
					webInfo.content=labelMatch.replaceAll("");
//					webInfo.content=content.replaceFirst("\\b\\s","");
//					webInfo.content=content;
					break;
				}
			}	
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("doc :"+doc);
				System.exit(-1);
			}
		}
		else
			bReader.close();
		return webInfo;

	}

	
}
