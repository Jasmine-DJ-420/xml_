package xml_seperate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Elks
 * 
 * SAX 解析XML文档
 */
public class _seperate {
	private int item_num = 0;
	private String head = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	private String head_2 = "<items>";
	private String tail = "</items>";
//	private static String dir_path = "/usr/dj/Crawl_data/StackOverFlow_seperate/";
	private static String dir_path = "G:\\seperate\\";
	private static String path;
	private File file ;
	private int query_num = 10;
	
	public static void main(String[] args) {
		_seperate parser = new _seperate();
//		String file = "/usr/dj/Crawl_data/StackOverFlow/";
		String file = "C:\\Users\\DJ\\Desktop\\act\\Crawler\\test demo\\";
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		in.close();
		file = file + s + ".xml";
		File dir = new File(dir_path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		path = dir_path + "_" + s + "_";
		
		parser.parserXml(file);
	}

    public void parserXml(String fileName) {
        SAXParserFactory saxfac = SAXParserFactory.newInstance();

        try {
            SAXParser saxparser = saxfac.newSAXParser();
            InputStream is = new FileInputStream(fileName);
            saxparser.parse(is, new MySAXHandler());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class MySAXHandler extends DefaultHandler {
    	private BufferedOutputStream fos;
    	@Override
        public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
            try {
				fos.write((new String(arg0, arg1, arg2)).replace("<", "&lt;")
						.replace(">", "&gt;")
						.replace("&", "&amp;")
						.replace("'", "&apos;")
						.replace("\"", "&quot;").getBytes("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            super.characters(arg0, arg1, arg2);
        }
     
        @Override
        public void endDocument() throws SAXException {
            System.out.println("Finish!");
            if(item_num%query_num!=0){	
				try {
					fos.write(tail.getBytes("UTF-8"));
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            super.endDocument();
        }
     
        @Override
        public void endElement(String arg0, String arg1, String arg2)
                throws SAXException {
        	try {
        		if (!arg2.equals("items")){
					fos.write("</".getBytes("UTF-8"));
					fos.write(arg2.replace("<", "&lt;")
							.replace(">", "&gt;")
							.replace("&", "&amp;")
							.replace("'", "&apos;")
							.replace("\"", "&quot;")
							.getBytes("UTF-8"));
					fos.write(">".getBytes("UTF-8"));
        		}
				if (arg2.equals("item")){
					if(item_num%query_num ==0){
						fos.write(tail.getBytes("UTF-8"));
						fos.close();
					}
				}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            super.endElement(arg0, arg1, arg2);
        }
     
//        @Override
//        public void startDocument() throws SAXException {
//            System.out.println("开始解析");
//            String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//            System.out.println(s);
//            super.startDocument();
//        }
     
        @Override
        public void startElement(String arg0, String arg1, String arg2,
                Attributes arg3) throws SAXException {
        	if (arg2.equals("item")) {
                item_num++;
                if (item_num%query_num == 1){
                	String filePath = path+item_num/query_num+".xml";
                	file = new File(filePath);
                	try {
						file.createNewFile();
						fos = new BufferedOutputStream(new FileOutputStream(filePath)); 
						fos.write(head.getBytes("UTF-8"));
						fos.write(head_2.getBytes("UTF-8"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        	try{
			fos.write("<".getBytes("UTF-8"));
			fos.write(arg2.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("&", "&amp;")
					.replace("'", "&apos;")
					.replace("\"", "&quot;").getBytes("UTF-8"));
			if (arg3 != null) {
                for (int i = 0; i < arg3.getLength(); i++) {
                	fos.write((" " + arg3.getQName(i) + "=\"" + arg3.getValue(i) + "\"").replace("<", "&lt;")
    						.replace(">", "&gt;")
    						.replace("&", "&amp;")
    						.replace("'", "&apos;")
    						.replace("\"", "&quot;").getBytes("UTF-8"));
                }
            }
			fos.write(">".getBytes("UTF-8"));
        	}catch(Exception e){
        		
        	}
            super.startElement(arg0, arg1, arg2, arg3);
        }
    }
}

