/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entities.Subject;
import facades.SubjectFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author tim
 */
@ManagedBean
@SessionScoped
public class KmeansController {
    private Subject subject = new Subject();
    RequestContext rtx;
     ExternalContext ext;
     HttpServletRequest request;
      FacesContext ctx;
     @EJB
    private SubjectFacade subjectFacade = new SubjectFacade();
     private DataModel<Subject> subjectDM = new ListDataModel<>();
     private List resultDM;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public DataModel<Subject> getSubjectDM() {
        subjectDM = new ListDataModel<>(subjectFacade.findAll());
        return subjectDM;
    }

    public void setSubjectDM(DataModel<Subject> subjectDM) {
        this.subjectDM = subjectDM;
    }
     
    public void addAttributes(){
        subjectFacade.create(subject);
        subject = new Subject();
        
    }
    
    public void getClusters() throws IOException{
        System.out.println("Pressed this nigga ");
      ctx = FacesContext.getCurrentInstance();
      rtx = RequestContext.getCurrentInstance();
      //call kmeans class
      Kmeans means = new Kmeans();
      means.Classes();
      List data = new ArrayList();
      BufferedReader br = null;
      FileReader fr = null;
      try {

			fr = new FileReader("/home/tim/Documents/NetBeansProjects/KmeansClustering/result.txt");
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader("/home/tim/Documents/NetBeansProjects/KmeansClustering/result.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
                                //setResultDM(sCurrentLine);
                                data.add(sCurrentLine);
			}
      
      //System.out.println("Final result =====> "+result.get(0));
      rtx.update("edit");
      rtx.execute("PF('dlg3').show()");
      }catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
           setResultDM(data);
    }
    
    public void deleteDetails(){
        //subjectDM.getRowData();
        
         subjectFacade.remove(subjectDM.getRowData());
         subject = new Subject();
        
    }

    public List getResultDM() {
        return resultDM;
    }

    public void setResultDM(List resultDM) {
        this.resultDM = resultDM;
    }

  

    
    
}
