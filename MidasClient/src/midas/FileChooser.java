package midas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser{
	

    private final JFileChooser fileChooser = new JFileChooser();              
    private JFrame frm;
	public FileChooser(JFrame frame)//flag 0이면 오픈 1이면 저장
	{
		fileChooser.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "*.dat file";
			}
			
			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				if(file.isDirectory()) {
					return true;
				}else {
					return file.getName().endsWith(".dat");
				}
			}
		});
		this.frm = frm;
    }
	
	PanelInformation openFile(){
		int returnVal = fileChooser.showOpenDialog(frm);
        if( returnVal == JFileChooser.APPROVE_OPTION)
        {
			try {
				FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile().toString());
				ObjectInputStream ois = new ObjectInputStream(fis);
				
				PanelInformation document = (PanelInformation)ois.readObject();
				// 가져 오고 나서 리스트에 추가 
				return document;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else
        {
            System.out.println("취소합니다");
        }
        return null;
	}
	
	void saveFile(PanelInformation panelInformation){
		int returnVal = fileChooser.showSaveDialog(frm);
        if( returnVal == JFileChooser.APPROVE_OPTION)
        {
        	try {
        		String path = fileChooser.getSelectedFile().toString();
        		String pathsplit[] = fileChooser.getSelectedFile().toString().split("/");
        		String fileName = pathsplit[pathsplit.length-1];
        		System.out.println(fileName);
        		panelInformation.setDocumentName(fileName); 
        		FileOutputStream fos = new FileOutputStream(path + ".dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(panelInformation);
				oos.close();
        	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else
        {
            System.out.println("취소합니다");
        }
	}

    
}