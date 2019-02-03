/*
 * This is a stateless bean used in jsf to retrieve the Student Entity. 
 * Stores data from forms.
 */
package rgz.rrstudent;

import java.io.Serializable;
import java.util.List;
import javax.ejb.*;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.faces.application.*;
import javax.faces.context.*;
import org.primefaces.event.*;

/**
 *
 * @author rod_g
 */
@Stateless
@Named
public class StudentFacade extends AbstractFacade<Student> implements Serializable{

    @PersistenceContext(unitName = "rrpu")
    private EntityManager em;
    private String firstName;
    private String lastName;
    
     public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        System.out.println("updating firstname " + firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    static boolean init = false;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentFacade() {
        super(Student.class);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Student> getStudents() {
        if (init == false) {
            init = true;

            System.out.println("persisting student");
            em.persist(new Student(null, "Test", "LTest"));

        }
        return em.createNamedQuery("Student.findAll").getResultList();

    }

    public void onRowEdit(RowEditEvent event) {
        final Student student = (Student) event.getObject();
//        Student merged = em.merge(student);
//
//        em.flush();
        FacesMessage msg = new FacesMessage("Student Edited", student.getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        final Student name = (Student) event.getObject();

        FacesMessage msg = new FacesMessage("Student Edit Cancelled", name.getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onAddNew() {
        
       em.persist(new Student(firstName, lastName));
        FacesMessage msg = new FacesMessage("New Student Record added");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Student student) {

        Student merged = em.merge(student);
        
        em.flush();
    }
    
    
    

}
