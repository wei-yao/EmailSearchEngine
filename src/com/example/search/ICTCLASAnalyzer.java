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
    
    // ��ʼ�� 
    if (icta.ICTCLAS_Init(initPath.getBytes("GB2312")) == false) { //initPath.getBytes("GB2312")
      System.out.println("Init Fail!"); 
      return; 
    } 
 
    // ���ô��Ա�ע��(0 ������������ע����1 ������һ����ע����2 ���������ע����3 ����һ����ע��) 
    icta.ICTCLAS_SetPOSmap(2); 
 
    // �����û��ֵ� 
    int nCount = 0; 
    String usrdir = "userdict.txt"; // �û��ֵ�·�� 
    byte[] usrdirb = usrdir.getBytes();// ��stringת��Ϊbyte���� 
    // �����û��ֵ�,���ص����û����������һ������Ϊ�û��ֵ�·�����ڶ�������Ϊ�û��ֵ�ı������� 
    nCount = icta.ICTCLAS_ImportUserDictFile(usrdirb, 0); 
    //System.out.println("�����û��ʸ���" + nCount); 
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
//    System.out.println("�ִʽ���� " + nativeStr); 
        //���д��ôʹ��� 
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
    //�Դʵ�Ҫ�� 
//    if(term.length()>1){ 
//        accept=true; 
//    } 
//    if(!accept)return accept; 
      //�Դ��Ե�Ҫ�� 
      if(type.startsWith("n")   //���� 
            ||type.startsWith("t")  //ʱ��� 
            ||type.startsWith("s")  //������ 
            ||type.startsWith("f")  //��λ�� 
            ||type.startsWith("a")  //���ݴ� 
            ||type.startsWith("v")  //���� 
            ||type.startsWith("b")  //����� 
            ||type.startsWith("z")  //״̬�� 
//          ||type.startsWith("r")  //���� 
//          ||type.startsWith("m")  //���� 
            ||type.startsWith("q")  //���� 
//          ||type.startsWith("d")  //���� 
            ||type.startsWith("p")  //��� 
            ||type.startsWith("c")  //���� 
//          ||type.startsWith("u")  //���� 
//          ||type.startsWith("e")  //̾�� 
//          ||type.startsWith("y")  //������ 
            ||type.startsWith("o")  //������ 
            ||type.startsWith("h")  //ǰ׺ 
            ||type.startsWith("k")  //��׺ 
            ||type.startsWith("x")  //��ַURL 
//          ||type.startsWith("w")  //������ 
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