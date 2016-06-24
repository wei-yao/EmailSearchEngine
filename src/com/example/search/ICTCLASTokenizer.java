package com.example.search;
import java.io.IOException; 
import java.io.Reader; 
import java.util.Iterator; 
import java.util.List; 
import org.apache.lucene.analysis.Tokenizer; 
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute; 
import org.apache.lucene.analysis.tokenattributes.TypeAttribute; 
 
public class ICTCLASTokenizer extends Tokenizer { 
  private List<String> tokens; 
  private Iterator<String> tokenIter; 
  private CharTermAttribute termAtt; 
  private TypeAttribute typeAtt; 
  public ICTCLASTokenizer(List<String> tokens) { 
    this.tokens = tokens; 
    this.tokenIter = tokens.iterator(); 
    termAtt = addAttribute( CharTermAttribute.class); 
    typeAtt=addAttribute(TypeAttribute.class); 
  } 
  @Override 
  public boolean incrementToken() throws IOException { 
    clearAttributes(); 
    if(tokenIter.hasNext()){ 
      String tokenstring = tokenIter.next(); 
      int pos=tokenstring.lastIndexOf('/'); 
      typeAtt.setType(tokenstring.substring(pos,tokenstring.length())); 
      termAtt.append(tokenstring.substring(0, pos)); 
      termAtt.setLength(pos); 
      return true; 
    } 
    return false; 
  } 
 
  @Override 
  public void reset() throws IOException { 
    tokenIter = tokens.iterator(); 
  } 
   
//  @Override 
  public void reset(Reader input) throws IOException { 
     
  } 
   
  public void reset(List<String> tokens) { 
    this.tokens = tokens; 
    this.tokenIter = tokens.iterator(); 
  } 
} 
