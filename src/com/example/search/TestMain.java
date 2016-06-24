package com.example.search;
import java.io.File; 
import java.io.FileReader; 
 
import org.apache.lucene.analysis.Analyzer; 
import org.apache.lucene.analysis.TokenStream; 
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute; 
import org.apache.lucene.analysis.tokenattributes.TypeAttribute; 
 

 
 
  public        class TestMain 
{   //Ö÷º¯Êý 
    public static void main(String[] args) throws Exception 
    { 
        Analyzer analyzer = new ICTCLASAnalyzer(); 
        TokenStream ts=analyzer.tokenStream("contents", new FileReader(new File("text.txt"))); 
        while(ts.incrementToken()){ 
            System.out.print(ts.getAttribute(CharTermAttribute.class)+""+ts.getAttribute(TypeAttribute.class).type()+" "); 
        } 
        analyzer.close();
        
    } 
} 
