package   com.example.search;
import java.io.IOException; 
import java.io.Reader; 
import java.io.UnsupportedEncodingException; 
import java.util.ArrayList; 
import java.util.List; 
import org.apache.lucene.analysis.Analyzer; 
import org.apache.lucene.analysis.TokenStream; 
import org.apache.lucene.analysis.Tokenizer; 
import ICTCLAS.I3S.AC.ICTCLAS50;
 
public class ICTCLASAnalyzer extends Analyzer { 
  private ICTCLAS50 icta; 
  private volatile boolean initialized = false; 
  public ICTCLASAnalyzer() throws UnsupportedEncodingException { 
    icta = new ICTCLAS50(); 
    String initPath="D:\\Program Files (x86)\\tomcat 7.0.12\\bin"; //.
    
    // 初始化 
    if (icta.ICTCLAS_Init(initPath.getBytes("GB2312")) == false) { //initPath.getBytes("GB2312")
      System.out.println("Init Fail!"); 
      return; 
    } 
 
    // 设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集) 
    icta.ICTCLAS_SetPOSmap(2); 
 
    // 导入用户字典 
    int nCount = 0; 
    String usrdir = "userdict.txt"; // 用户字典路径 
    byte[] usrdirb = usrdir.getBytes();// 将string转化为byte类型 
    // 导入用户字典,返回导入用户词语个数第一个参数为用户字典路径，第二个参数为用户字典的编码类型 
    nCount = icta.ICTCLAS_ImportUserDictFile(usrdirb, 0); 
    //System.out.println("导入用户词个数" + nCount); 
    initialized = true; 
  } 
 
  public List<String> tokenizeReader(Reader reader) { 
    List<String> result = new ArrayList<String>(1000); 
    try { 
      StringBuffer contentbuffer = new StringBuffer(); 
      char[] temp = new char[1024]; 
      int size = 0; 
      while ((size = reader.read(temp, 0, 1024)) != -1) { 
        String tempstr = new String(temp, 0, size); 
        contentbuffer.append(tempstr); 
      } 
      byte nativeBytes[] = icta.ICTCLAS_ParagraphProcess(contentbuffer.toString().getBytes("GB2312"), 2, 1); 
      String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312"); 
//    System.out.println("分词结果： " + nativeStr); 
        //进行词用词过滤 
     String[] terms=nativeStr.split("\\s+"); 
     int pos; 
     String term,type; 
     for (String string : terms) { 
         pos=string.lastIndexOf('/'); 
         if(pos==-1)continue; 
         term=string.substring(0,pos); 
         type=string.substring(pos+1, string.length()); 
         if(accept(term,type)){ 
             result.add(string); 
         } 
     } 
    } catch (Throwable e) { 
      e.printStackTrace(); 
    } 
    return result; 
  } 
  private boolean accept(String term,String type){ 
      boolean accept=false; 
    //对词的要求 
//    if(term.length()>1){ 
//        accept=true; 
//    } 
//    if(!accept)return accept; 
      //对词性的要求 
      if(type.startsWith("n")   //名词 
            ||type.startsWith("t")  //时间词 
            ||type.startsWith("s")  //处所词 
            ||type.startsWith("f")  //方位词 
            ||type.startsWith("a")  //形容词 
            ||type.startsWith("v")  //动词 
            ||type.startsWith("b")  //区别词 
            ||type.startsWith("z")  //状态词 
//          ||type.startsWith("r")  //代词 
//          ||type.startsWith("m")  //数词 
            ||type.startsWith("q")  //量词 
//          ||type.startsWith("d")  //副词 
            ||type.startsWith("p")  //介词 
            ||type.startsWith("c")  //连词 
//          ||type.startsWith("u")  //助词 
//          ||type.startsWith("e")  //叹词 
//          ||type.startsWith("y")  //语气词 
            ||type.startsWith("o")  //拟声词 
            ||type.startsWith("h")  //前缀 
            ||type.startsWith("k")  //后缀 
            ||type.startsWith("x")  //网址URL 
//          ||type.startsWith("w")  //标点符号 
                ){ 
          return true; 
      } 
       
      return accept; 
  } 
@Override 
  public TokenStream tokenStream(String fieldName, Reader reader) { 
    if(!initialized) 
      return null; 
    List<String> tokens = tokenizeReader(reader); 
    return new ICTCLASTokenizer(tokens); 
  } 
 
  @Override 
  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException { 
    Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream(); 
    if (tokenizer == null) { 
      List<String> tokens = tokenizeReader(reader); 
      tokenizer = new ICTCLASTokenizer(tokens); 
      setPreviousTokenStream(tokenizer); 
    } else{ 
      tokenizer.reset(reader); 
      ICTCLASTokenizer t = (ICTCLASTokenizer)tokenizer; 
      List<String> tokens = tokenizeReader(reader); 
      t.reset(tokens); 
    } 
    return tokenizer; 
  } 
   
  @Override 
  public void close() { 
    icta.ICTCLAS_SaveTheUsrDic(); 
    icta.ICTCLAS_Exit(); 
    initialized = false; 
  }


} 